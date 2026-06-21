<template>
  <nav-bar></nav-bar>
  <!-- 在template-select-box下方新增AI问答模块 -->
  <div class="ai-chat-container">
    <el-card class="chat-box">
      <div v-for="(msg, index) in chatMessages" :key="index"
           :class="['chat-message', msg.sender]">
        <div class="message-header">
          <el-avatar :icon="msg.sender === 'user' ? User : Comment" />
          <span class="sender-name">{{ msg.sender === 'user' ? '您' : 'AI助手' }}</span>
        </div>
        <div class="message-content" v-html="marked(msg.content)"></div>
      </div>
      <div v-if="isLoading" class="loading-indicator">
        <el-icon class="is-loading"><Loading /></el-icon> 思考中...
      </div>
    </el-card>

    <div class="input-box">
      <el-input
          v-model="userInput"
          placeholder="请输入简历制作相关问题..."
          @keyup.enter="sendQuestion"
          :disabled="isLoading">
        <template #append>
          <el-button
              type="primary"
              @click="sendQuestion"
              :loading="isLoading">
            发送
          </el-button>
        </template>
      </el-input>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue';
import { marked } from 'marked';
import { getAIResponse } from '@/http/api/express.js'; // 核心导入语句[1,6](@ref)
import DOMPurify from 'dompurify';

// 响应式数据
const chatMessages = ref([]);
const userInput = ref('');
const isLoading = ref(false);


// 发送消息处理
const sendQuestion = async () => {
  if (!userInput.value.trim() || isLoading.value) return;

  try {
    isLoading.value = true;

    // 添加用户消息
    chatMessages.value.push({
      sender: 'user',
      content: userInput.value,
      timestamp: new Date().toISOString()
    });

    const question = userInput.value;
    userInput.value = ''; // 清空输入框
    // 调用外部模块[6](@ref)
    const aiResponse = await getAIResponse(question);

    // 添加AI回复
    chatMessages.value.push({
      sender: 'ai',
      content: aiResponse,
      timestamp: new Date().toISOString()
    });

  } catch (error) {
    console.error('对话异常:', error);
    ElMessage.error(error.message || '服务暂时不可用');
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.ai-chat-container {
  max-width: 1200px;
  margin: 70px auto;
  padding: 20px;
}

.chat-box {
  height: 500px;
  overflow-y: auto;
  margin-bottom: 20px;
  background: #f8f9fa;
}

.chat-message {
  margin: 15px;
  padding: 15px;
  border-radius: 8px;
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);

  &.ai {
    border-left: 4px solid #409EFF;
  }

  &.user {
    border-left: 4px solid #67C23A;
  }
}

.message-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;

  .sender-name {
    margin-left: 10px;
    font-weight: 500;
    color: #606266;
  }
}

.message-content {
  line-height: 1.6;
  color: #303133;

  :deep(pre) { /* Markdown代码块样式 */
    background: #f5f7fa;
    padding: 10px;
    border-radius: 4px;
  }
}

.loading-indicator {
  text-align: center;
  padding: 15px;
  color: #909399;
}
</style>