import axios from 'axios';

const BASE_URL = import.meta.env.VITE_INTERVIEW_API_URL || 'http://localhost:8000';

const interviewApi = axios.create({
  baseURL: BASE_URL,
  timeout: 30000
});

/**
 * 创建面试会话
 */
export const createSession = (data) => {
  return interviewApi.post('/api/session/create', data);
};

/**
 * 上传PDF简历（提取文字）
 */
export const uploadResumePDF = (file) => {
  const formData = new FormData();
  formData.append('file', file);
  return interviewApi.post('/api/session/resume/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 60000
  });
};

/**
 * 获取面试报告
 */
export const getReport = (sessionId) => {
  return interviewApi.get(`/api/session/${sessionId}/report`);
};

/**
 * 获取历史消息
 */
export const getMessages = (sessionId) => {
  return interviewApi.get(`/api/session/${sessionId}/messages`);
};

/**
 * 获取会话列表
 */
export const listSessions = () => {
  return interviewApi.get('/api/session/list');
};

export default interviewApi;