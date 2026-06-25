<template>
  <person-view>
    <template #default>
      <div class="records-page">
        <div class="page-header">
          <h2>面试记录</h2>
          <el-button type="primary" @click="router.push('/interview')">新建面试</el-button>
        </div>

        <div v-loading="loading" class="records-list">
          <div v-for="s in sessions" :key="s.session_id" class="record-card">
            <div class="record-left">
              <div class="record-title">{{ s.job_title }}</div>
              <div class="record-meta">
                <el-tag :type="s.interview_mode === 'pressure' ? 'danger' : 'success'" size="small">
                  {{ s.interview_mode === 'pressure' ? '压力' : '轻松' }}
                </el-tag>
                <span class="record-date">{{ formatDate(s.created_at) }}</span>
                <span class="record-count">{{ s.message_count }} 条消息</span>
                <el-tag v-if="s.status === 'completed'" type="success" size="small">已完成</el-tag>
                <el-tag v-else-if="s.status === 'abandoned'" type="info" size="small">已放弃</el-tag>
                <el-tag v-else size="small" type="warning">进行中</el-tag>
              </div>
              <div v-if="s.last_message" class="record-preview">{{ s.last_message }}</div>
            </div>
            <div class="record-actions">
              <el-button size="small" @click="router.push('/interview?session=' + s.session_id)">继续</el-button>
              <el-button size="small" type="primary" @click="router.push('/interview/report/' + s.session_id)">报告</el-button>
            </div>
          </div>
          <el-empty v-if="!loading && sessions.length === 0" description="暂无面试记录" />
        </div>
      </div>
    </template>
  </person-view>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { listSessions } from '@/http/api/interview.js';
import PersonView from '@/views/person/index.vue';

const router = useRouter();
const sessions = ref([]);
const loading = ref(true);

const formatDate = (t) => {
  if (!t) return '';
  const d = new Date(t);
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
};

onMounted(async () => {
  try {
    const res = await listSessions();
    sessions.value = res.data || [];
  } catch {
    // silent
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.records-page {
  padding: 24px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.page-header h2 { margin: 0; font-size: 20px; font-weight: 600; }
.records-list { display: flex; flex-direction: column; gap: 12px; }
.record-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-radius: 12px;
  padding: 16px 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  transition: box-shadow 0.2s;
}
.record-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.08); }
.record-left { flex: 1; min-width: 0; }
.record-title { font-weight: 600; font-size: 16px; margin-bottom: 6px; }
.record-meta { display: flex; gap: 8px; align-items: center; font-size: 12px; color: #9ca3af; margin-bottom: 6px; flex-wrap: wrap; }
.record-preview { font-size: 13px; color: #6b7280; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.record-actions { display: flex; gap: 8px; margin-left: 16px; flex-shrink: 0; }
</style>