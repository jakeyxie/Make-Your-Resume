<template>
  <div class="interview-page">
    <nav-bar></nav-bar>
    <div class="interview-layout">
      <!-- 左侧：面试历史 -->
      <div class="left-panel">
        <div class="panel-header">
          <h3>面试历史</h3>
          <el-button size="small" type="primary" @click="showNewDialog = true">+ 新建面试</el-button>
        </div>
        <div class="session-list" v-loading="sessionsLoading">
          <div
            v-for="s in sessions"
            :key="s.session_id"
            :class="['session-item', { active: s.session_id === activeSessionId }]"
            @click="switchSession(s.session_id)"
          >
            <div class="session-title">{{ s.job_title }}</div>
            <div class="session-meta">
              <el-tag :type="s.interview_mode === 'pressure' ? 'danger' : 'success'" size="small" class="mode-tag">
                {{ s.interview_mode === 'pressure' ? '压力' : '轻松' }}
              </el-tag>
              <span class="session-date">{{ formatTime(s.created_at) }}</span>
              <span v-if="s.message_count" class="msg-count">{{ s.message_count }}条</span>
            </div>
            <div v-if="s.last_message" class="session-preview">{{ s.last_message }}</div>
          </div>
          <el-empty v-if="!sessionsLoading && sessions.length === 0" description="暂无面试记录" :image-size="80" />
        </div>
      </div>

      <!-- 中间：聊天区域 -->
      <div class="center-panel">
        <template v-if="!activeSessionId">
          <div class="empty-chat">
            <el-empty description="选择左侧面试记录或新建一个面试" :image-size="120" />
          </div>
        </template>
        <template v-else>
          <!-- 聊天头 -->
          <div class="chat-header">
            <div class="chat-info">
              <h2>{{ currentSession?.job_title || '面试中' }}</h2>
              <el-tag v-if="currentSession" :type="currentSession.interview_mode === 'pressure' ? 'danger' : 'success'" size="small">
                {{ currentSession.interview_mode === 'pressure' ? '压力模式' : '轻松模式' }}
              </el-tag>
              <span v-if="currentRound" class="round-badge">第 {{ currentRound }} 轮</span>
              <span v-if="chatStatus === 'disconnected'" class="status-offline">· 已断开</span>
              <span v-else-if="interviewEnded" class="status-ended">· 已结束</span>
            </div>
            <div class="chat-actions">
              <el-button
                v-if="!connectionStarted && !interviewEnded"
                type="primary" size="small"
                @click="startInterview"
              >开始面试</el-button>
              <el-button
                v-if="connectionStarted && !interviewEnded"
                type="danger" plain size="small"
                :disabled="chatStatus !== 'waiting_user'"
                @click="handleEndInterview"
              >结束面试</el-button>
              <el-button
                v-if="interviewEnded"
                type="primary" size="small"
                @click="router.push(`/interview/report/${activeSessionId}`)"
              >查看报告</el-button>
            </div>
          </div>

          <!-- 消息列表 -->
          <div class="chat-messages" ref="messagesRef">
            <div v-if="chatStatus === 'connecting'" class="status-msg">正在连接...</div>
            <div v-if="chatStatus === 'thinking'" class="status-msg thinking">
              <span class="dot-pulse"></span> AI 正在思考...
            </div>

            <div
              v-for="(msg, idx) in chatMessages"
              :key="idx"
              :class="['msg-item', msg.role === 'user' ? 'msg-right' : 'msg-left']"
            >
              <div v-if="msg.role === 'interviewer'" class="msg-interviewer">
                <div class="msg-avatar ia">面</div>
                <div class="msg-body">
                  <div class="msg-label">面试官</div>
                  <div class="msg-bubble interviewer-bubble">{{ msg.content }}</div>
                </div>
              </div>
              <div v-else-if="msg.role === 'user'" class="msg-user">
                <div class="msg-body">
                  <div class="msg-label user-label">我的回答</div>
                  <div class="msg-bubble user-bubble">{{ msg.content }}</div>
                </div>
                <div class="msg-avatar ua">我</div>
              </div>
            </div>
          </div>

          <!-- 输入框 -->
          <div v-if="!interviewEnded" class="chat-input-row">
            <el-input
              v-model="userInput"
              type="textarea"
              :rows="2"
              :disabled="!connectionStarted || chatStatus !== 'waiting_user'"
              :placeholder="chatStatus === 'waiting_user' ? '输入你的回答... (Ctrl+Enter发送)' : '等待 AI 回复...'"
              @keydown.ctrl.enter="handleSendMessage"
            />
            <el-button
              type="primary"
              :disabled="!connectionStarted || !userInput.trim() || chatStatus !== 'waiting_user'"
              @click="handleSendMessage"
            >发送</el-button>
          </div>
        </template>
      </div>

      <!-- 右侧：潜台词分析 + 策略建议 -->
      <div class="right-panel" v-if="activeSessionId">
        <el-tabs v-model="rightTab" class="right-tabs">
          <el-tab-pane label="潜台词分析" name="analyzer">
            <div class="analyzer-workspace">
              <div v-if="analyzerHistory.length === 0" class="empty-tip">
                等待面试官提问后，这里将展示潜台词分析
              </div>
              <div
                v-for="(item, idx) in analyzerHistory"
                :key="idx"
                class="analyze-card"
              >
                <div class="analyze-q">{{ item.question }}</div>
                <div class="analyze-intent">
                  <span class="label">面试官意图：</span>{{ item.intent }}
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="策略建议" name="strategies">
            <div class="strategy-workspace">
              <div v-if="strategyHistory.length === 0" class="empty-tip">
                提交回答后，这里将生成三种应对策略
              </div>
              <div
                v-for="(group, idx) in strategyHistory"
                :key="idx"
                class="strategy-group"
              >
                <div class="strategy-question">{{ group.question }}</div>
                <div
                  v-for="(card, cIdx) in group.strategies"
                  :key="cIdx"
                  class="strategy-card"
                  :class="`card-${cIdx + 1}`"
                >
                  <div class="card-header">{{ card.title }}</div>
                  <div class="card-stance">{{ card.stance }}</div>
                  <div class="card-content">{{ card.content }}</div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 新建面试 Dialog -->
    <el-dialog v-model="showNewDialog" title="新建面试" width="580px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="目标岗位" prop="jobTitle">
          <el-input v-model="form.jobTitle" placeholder="例：高级前端工程师" maxlength="160" />
        </el-form-item>
        <el-form-item label="简历内容" prop="resume">
          <el-input v-model="form.resume" type="textarea" :rows="3" placeholder="粘贴简历内容" maxlength="20000" show-word-limit />
        </el-form-item>
        <el-form-item label="上传简历(PDF)">
          <el-upload :auto-upload="false" :show-file-list="false" :on-change="handleResumeUpload" accept=".pdf">
            <el-button size="small" type="info">选择 PDF</el-button>
            <span v-if="uploadFileName" class="upload-ok">已上传：{{ uploadFileName }}</span>
          </el-upload>
        </el-form-item>
        <el-form-item label="岗位描述">
          <el-input v-model="form.jdText" type="textarea" :rows="2" placeholder="粘贴 JD 原文（可选）" />
        </el-form-item>
        <el-form-item label="面试模式">
          <el-radio-group v-model="form.interviewMode">
            <el-radio value="easy">轻松模式</el-radio>
            <el-radio value="pressure">压力模式</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showNewDialog = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="handleCreate">开始面试</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { createSession, uploadResumePDF, listSessions } from '@/http/api/interview.js';
import NavBar from '@/components/NavBar/NavBar.vue';

const router = useRouter();

const WS_BASE = (import.meta.env.VITE_INTERVIEW_API_URL || 'http://localhost:8000')
  .replace(/^http/, 'ws');

// === 左侧会话列表 ===
const sessions = ref([]);
const sessionsLoading = ref(false);
const activeSessionId = ref('');
const currentSession = computed(() =>
  sessions.value.find(s => s.session_id === activeSessionId.value) || null
);

const loadSessions = async () => {
  sessionsLoading.value = true;
  try {
    const res = await listSessions();
    sessions.value = res.data || [];
  } catch {
    // FastAPI may not be running
  } finally {
    sessionsLoading.value = false;
  }
};

const formatTime = (t) => {
  if (!t) return '';
  const d = new Date(t);
  return `${d.getMonth() + 1}/${d.getDate()} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
};

const switchSession = async (id) => {
  if (ws) { ws.close(); ws = null; }
  activeSessionId.value = id;
  chatMessages.value = [];
  analyzerHistory.value = [];
  strategyHistory.value = [];
  currentRound.value = 0;
  interviewEnded.value = false;
  connectionStarted.value = false;
  chatStatus.value = 'idle';
  rightTab.value = 'analyzer';
  userInput.value = '';

  // 加载历史消息
  try {
    const { getMessages } = await import('@/http/api/interview.js');
    const res = await getMessages(id);
    const msgs = res.data || [];
    chatMessages.value = msgs.filter(m => m.role === 'interviewer' || m.role === 'user');
    // 提取分析器和策略
    const analyzers = msgs.filter(m => m.role === 'analyzer');
    const strategists = msgs.filter(m => m.role === 'strategist');
    analyzerHistory.value = analyzers.map(a => ({ question: '', intent: a.content }));
    strategyHistory.value = strategists.map(s => ({ question: '', strategies: [{ title: '建议', stance: '', content: s.content }] }));
    currentRound.value = chatMessages.value.filter(m => m.role === 'interviewer').length;
    const ended = msgs.some(m => m.role === 'system' && m.content?.includes('ended'));
    if (ended || msgs.some(m => m.role === 'interview_ended')) {
      interviewEnded.value = true;
    }
    // 检查session状态
    const ses = sessions.value.find(s => s.session_id === id);
    if (ses && (ses.status === 'completed' || ses.status === 'abandoned')) {
      interviewEnded.value = true;
    }
  } catch {
    // silent
  }
  await nextTick();
  scrollToBottom();
};

// === 新建面试 ===
const showNewDialog = ref(false);
const formRef = ref(null);
const form = reactive({
  jobTitle: '',
  resume: '',
  jdText: '',
  jdUrl: '',
  interviewMode: 'easy'
});
const rules = { jobTitle: [{ required: true, message: '请输入目标岗位', trigger: 'blur' }] };
const creating = ref(false);
const uploadFileName = ref('');

const handleResumeUpload = async (uploadFile) => {
  try {
    const res = await uploadResumePDF(uploadFile.raw);
    if (form.resume && !form.resume.endsWith('\n')) form.resume += '\n';
    form.resume += res.data.text;
    uploadFileName.value = uploadFile.name;
    ElMessage.success('简历内容已提取');
  } catch {
    ElMessage.error('PDF 解析失败，请手动粘贴');
  }
};

const handleCreate = async () => {
  const valid = await formRef.value.validate().catch(() => false);
  if (!valid) return;
  creating.value = true;
  try {
    const res = await createSession({
      job_title: form.jobTitle,
      resume: form.resume,
      jd_text: form.jdText,
      jd_url: form.jdUrl,
      interview_mode: form.interviewMode
    });
    showNewDialog.value = false;
    // 刷新列表
    await loadSessions();
    // 自动切换到新会话
    await switchSession(res.data.session_id);
    // 自动连接并开始
    connectWebSocket();
    ws.onopen = () => {
      ws.send(JSON.stringify({ type: 'start' }));
    };
  } catch {
    ElMessage.error('创建会话失败，请检查 FastAPI 服务');
  } finally {
    creating.value = false;
  }
};

// === WebSocket 和聊天 ===
let ws = null;
const chatMessages = ref([]);
const analyzerHistory = ref([]);
const strategyHistory = ref([]);
const chatStatus = ref('idle');
const currentRound = ref(0);
const interviewEnded = ref(false);
const connectionStarted = ref(false);
const userInput = ref('');
const messagesRef = ref(null);
const rightTab = ref('analyzer');

const connectWebSocket = () => {
  if (ws) ws.close();
  const url = `${WS_BASE}/ws/chat/${activeSessionId.value}`;
  ws = new WebSocket(url);
  chatStatus.value = 'connecting';
  connectionStarted.value = true;

  ws.onmessage = (event) => {
    const data = JSON.parse(event.data);
    handleWSMessage(data);
  };
  ws.onclose = () => {
    if (!interviewEnded.value) chatStatus.value = 'disconnected';
  };
  ws.onerror = () => {
    ElMessage.error('连接失败');
    chatStatus.value = 'error';
  };
};

const startInterview = () => {
  connectWebSocket();
  ws.onopen = () => {
    ws.send(JSON.stringify({ type: 'start' }));
  };
};

const handleWSMessage = (data) => {
  switch (data.type) {
    case 'connected':
      chatStatus.value = 'ready';
      break;
    case 'agent_status':
      chatStatus.value = data.status === 'thinking' ? 'thinking' : 'waiting_user';
      break;
    case 'agent_message':
      chatStatus.value = 'thinking';
      if (data.role === 'interviewer') {
        currentRound.value++;
        chatMessages.value.push({ role: 'interviewer', content: data.content });
      } else if (data.role === 'analyzer') {
        analyzerHistory.value.push({
          question: chatMessages.value.filter(m => m.role === 'interviewer').slice(-1)[0]?.content || '',
          intent: data.content
        });
        rightTab.value = 'analyzer';
      } else if (data.role === 'strategist') {
        strategyHistory.value.push({
          question: chatMessages.value.filter(m => m.role === 'interviewer').slice(-1)[0]?.content || '',
          strategies: data.strategies || [{ title: '建议', stance: '', content: data.content }]
        });
        rightTab.value = 'strategies';
      }
      if (data.ended) {
        interviewEnded.value = true;
        chatStatus.value = 'ended';
      }
      scrollToBottom();
      break;
    case 'user_message':
      if (data.role === 'user') {
        chatMessages.value.push({ role: 'user', content: data.content });
      }
      scrollToBottom();
      break;
    case 'interview_ended':
      interviewEnded.value = true;
      chatStatus.value = 'ended';
      ElMessage.info('面试已结束');
      break;
    case 'error':
      ElMessage.error(data.message);
      break;
  }
};

const handleSendMessage = () => {
  const text = userInput.value.trim();
  if (!text || chatStatus.value !== 'waiting_user' || !ws) return;
  ws.send(JSON.stringify({ type: 'user_message', content: text }));
  // 本地回显
  chatMessages.value.push({ role: 'user', content: text });
  userInput.value = '';
  chatStatus.value = 'thinking';
  scrollToBottom();
};

const handleEndInterview = async () => {
  try {
    await ElMessageBox.confirm('确定结束本次面试？', '确认', { type: 'warning' });
    if (ws) ws.send(JSON.stringify({ type: 'end_interview' }));
    interviewEnded.value = true;
    chatStatus.value = 'ended';
  } catch { /* cancelled */ }
};

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) messagesRef.value.scrollTop = messagesRef.value.scrollHeight;
  });
};

onMounted(() => { loadSessions(); });
onBeforeUnmount(() => { if (ws) ws.close(); });
</script>

<style scoped>
.interview-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8f9fc 0%, #eef2ff 100%);
}
.interview-layout {
  display: flex;
  height: calc(100vh - 70px);
  padding-top: 70px;
  gap: 0;
}

/* ===== 左侧面板 ===== */
.left-panel {
  width: 280px;
  min-width: 280px;
  background: #fff;
  border-right: 1px solid rgba(0,0,0,0.06);
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 12px rgba(0,0,0,0.03);
}
.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px 16px;
  border-bottom: 1px solid #f0f0f0;
}
.panel-header h3 {
  margin: 0;
  font-size: 15px;
  font-weight: 700;
  color: #1a1a2e;
  letter-spacing: 0.5px;
}
.session-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}
.session-list::-webkit-scrollbar { width: 4px; }
.session-list::-webkit-scrollbar-thumb { background: #e5e7eb; border-radius: 2px; }

.session-item {
  padding: 14px 12px;
  border-radius: 10px;
  cursor: pointer;
  margin-bottom: 4px;
  transition: all 0.2s ease;
  border: 1px solid transparent;
}
.session-item:hover {
  background: #f8f7ff;
  border-color: rgba(91,61,244,0.08);
}
.session-item.active {
  background: linear-gradient(135deg, #eef2ff 0%, #f8f7ff 100%);
  border-color: rgba(91,61,244,0.15);
  box-shadow: 0 2px 8px rgba(91,61,244,0.06);
}
.session-title {
  font-weight: 600;
  font-size: 14px;
  color: #1a1a2e;
  margin-bottom: 6px;
}
.session-item.active .session-title { color: #5b3df4; }
.session-meta {
  display: flex;
  gap: 6px;
  align-items: center;
  font-size: 12px;
  color: #9ca3af;
  margin-bottom: 4px;
  flex-wrap: wrap;
}
.mode-tag { font-size: 11px !important; padding: 0 6px !important; height: 20px !important; line-height: 20px !important; }
.session-date { font-size: 11px; color: #b0b7c3; }
.msg-count { font-size: 11px; margin-left: auto; color: #b0b7c3; }
.session-preview {
  font-size: 12px;
  color: #b0b7c3;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-top: 4px;
}

/* ===== 中间聊天 ===== */
.center-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  background: #fff;
  margin: 0;
  box-shadow: 0 0 20px rgba(0,0,0,0.03);
}
.empty-chat {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafbfc;
}
.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 24px;
  border-bottom: 1px solid #f0f0f0;
  background: #fff;
}
.chat-info { display: flex; align-items: center; gap: 12px; }
.chat-info h2 { font-size: 16px; font-weight: 700; margin: 0; color: #1a1a2e; }
.round-badge {
  font-size: 12px;
  color: #6b7280;
  background: #f3f4f6;
  padding: 2px 10px;
  border-radius: 10px;
  font-weight: 500;
}
.status-offline { color: #ef4444; font-size: 13px; font-weight: 500; }
.status-ended { color: #9ca3af; font-size: 13px; }

.chat-actions :deep(.el-button--small) {
  border-radius: 8px;
  font-weight: 500;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: #fafbfc;
}
.chat-messages::-webkit-scrollbar { width: 4px; }
.chat-messages::-webkit-scrollbar-thumb { background: #e5e7eb; border-radius: 2px; }

.status-msg {
  text-align: center;
  color: #9ca3af;
  font-size: 13px;
  padding: 12px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.status-msg.thinking {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.msg-item { display: flex; gap: 12px; align-items: flex-start; }
.msg-right { flex-direction: row-reverse; }

.msg-avatar {
  width: 34px; height: 34px;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 13px; font-weight: 700; color: #fff;
  flex-shrink: 0;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}
.ia { background: linear-gradient(135deg, #ef4444, #dc2626); }
.ua { background: linear-gradient(135deg, #5b3df4, #7c63f5); }

.msg-body { max-width: 72%; }
.msg-label {
  font-size: 11px;
  color: #9ca3af;
  margin-bottom: 6px;
  font-weight: 500;
  letter-spacing: 0.3px;
}
.user-label { text-align: right; }

.msg-bubble {
  padding: 12px 16px;
  border-radius: 14px;
  font-size: 14px;
  line-height: 1.6;
  white-space: pre-wrap;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.interviewer-bubble {
  background: linear-gradient(135deg, #fef2f2, #fff5f5);
  color: #991b1b;
  border-bottom-left-radius: 4px;
  border: 1px solid rgba(239,68,68,0.08);
}
.user-bubble {
  background: linear-gradient(135deg, #eef2ff, #f8f7ff);
  color: #1a1a2e;
  border-bottom-right-radius: 4px;
  border: 1px solid rgba(91,61,244,0.08);
}

.chat-input-row {
  display: flex;
  gap: 12px;
  padding: 14px 24px;
  border-top: 1px solid #f0f0f0;
  background: #fff;
  align-items: flex-end;
}
.chat-input-row .el-button {
  height: 56px;
  width: 80px;
  border-radius: 10px;
  font-weight: 600;
}
.chat-input-row :deep(.el-textarea__inner) {
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  transition: border-color 0.2s;
  padding: 10px 14px;
  font-size: 14px;
}
.chat-input-row :deep(.el-textarea__inner:focus) {
  border-color: #5b3df4;
  box-shadow: 0 0 0 3px rgba(91,61,244,0.08);
}

/* 点脉冲 */
.dot-pulse {
  width: 8px; height: 8px;
  border-radius: 50%;
  background: linear-gradient(135deg, #5b3df4, #7c63f5);
  animation: pulse 1.2s ease-in-out infinite;
}
@keyframes pulse {
  0%, 100% { opacity: 0.3; transform: scale(0.8); }
  50% { opacity: 1; transform: scale(1.2); }
}

/* ===== 右侧面板 ===== */
.right-panel {
  width: 360px;
  min-width: 360px;
  background: #fff;
  border-left: 1px solid rgba(0,0,0,0.06);
  display: flex;
  flex-direction: column;
  box-shadow: -2px 0 12px rgba(0,0,0,0.03);
}
.right-tabs { height: 100%; display: flex; flex-direction: column; }
.right-tabs :deep(.el-tabs__header) {
  margin: 0;
  padding: 0 20px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
}
.right-tabs :deep(.el-tabs__item) {
  font-size: 14px;
  font-weight: 600;
  color: #6b7280;
  padding: 0 16px;
  height: 44px;
  line-height: 44px;
}
.right-tabs :deep(.el-tabs__item.is-active) {
  color: #5b3df4;
}
.right-tabs :deep(.el-tabs__active-bar) {
  background: linear-gradient(135deg, #5b3df4, #7c63f5);
  height: 3px;
  border-radius: 2px;
}
.right-tabs :deep(.el-tabs__content) {
  flex: 1;
  overflow-y: auto;
  padding: 16px 20px;
}
.right-tabs :deep(.el-tabs__content::-webkit-scrollbar) { width: 4px; }
.right-tabs :deep(.el-tabs__content::-webkit-scrollbar-thumb) { background: #e5e7eb; border-radius: 2px; }

.analyzer-workspace, .strategy-workspace {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.empty-tip {
  text-align: center;
  color: #b0b7c3;
  font-size: 13px;
  padding: 60px 20px;
  line-height: 1.6;
}
.analyze-card {
  background: linear-gradient(135deg, #fffbeb, #fff8e1);
  border: 1px solid rgba(251,191,36,0.2);
  border-radius: 12px;
  padding: 14px;
  box-shadow: 0 2px 8px rgba(251,191,36,0.06);
  transition: transform 0.2s;
}
.analyze-card:hover { transform: translateY(-1px); }
.analyze-q {
  font-size: 12px;
  color: #92400e;
  margin-bottom: 8px;
  font-weight: 600;
  padding-bottom: 8px;
  border-bottom: 1px dashed rgba(251,191,36,0.25);
}
.analyze-intent {
  font-size: 13px;
  color: #78350f;
  line-height: 1.7;
}
.analyze-intent .label {
  font-weight: 700;
  color: #d97706;
  display: inline-block;
  margin-right: 4px;
}

.strategy-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-bottom: 12px;
  border-bottom: 1px dashed #e5e7eb;
}
.strategy-group:last-child { border-bottom: none; }
.strategy-question {
  font-size: 12px;
  color: #6b7280;
  padding: 8px 12px;
  background: #f9fafb;
  border-radius: 8px;
  font-weight: 500;
  border-left: 3px solid #d1d5db;
}
.strategy-card {
  border-radius: 10px;
  padding: 12px 14px;
  border: 1px solid;
  transition: transform 0.2s, box-shadow 0.2s;
}
.strategy-card:hover { transform: translateY(-1px); box-shadow: 0 4px 12px rgba(0,0,0,0.06); }
.card-1 {
  background: linear-gradient(135deg, #f0fdf4, #ecfdf5);
  border-color: rgba(74,222,128,0.25);
}
.card-2 {
  background: linear-gradient(135deg, #fefce8, #fef9c3);
  border-color: rgba(250,204,21,0.25);
}
.card-3 {
  background: linear-gradient(135deg, #fef2f2, #fee2e2);
  border-color: rgba(248,113,113,0.2);
}
.card-header { font-weight: 700; font-size: 13px; margin-bottom: 2px; letter-spacing: 0.3px; }
.card-1 .card-header { color: #166534; }
.card-2 .card-header { color: #92400e; }
.card-3 .card-header { color: #991b1b; }
.card-stance { font-size: 11px; color: #9ca3af; margin-bottom: 6px; }
.card-content { font-size: 12px; line-height: 1.6; }
.card-1 .card-content { color: #166534; }
.card-2 .card-content { color: #78350f; }
.card-3 .card-content { color: #991b1b; }

/* dialog */
.upload-ok { margin-left: 8px; font-size: 12px; color: #5b3df4; }
</style>