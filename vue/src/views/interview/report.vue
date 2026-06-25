<template>
  <div class="report-page" v-loading="loading">
    <nav-bar></nav-bar>
    <div v-if="!loading && !error" class="report-container">
      <!-- 头部 -->
      <div class="report-header">
        <h1>面试复盘报告</h1>
        <div class="meta">
          <el-tag :type="overallTagType" size="small">{{ overallLabel }}</el-tag>
          <span class="meta-item">会话 ID：{{ route.params.id?.slice(0, 8) }}</span>
        </div>
      </div>

      <!-- 评分概览 -->
      <div class="score-overview">
        <div class="score-card">
          <div class="score-ring" :style="{ '--score': technical.score / 100 }">
            <svg viewBox="0 0 120 120">
              <circle cx="60" cy="60" r="52" class="ring-bg"/>
              <circle cx="60" cy="60" r="52" class="ring-fg"
                :style="{ strokeDashoffset: 326 - 326 * technical.score / 100 }"/>
            </svg>
            <span class="score-value">{{ technical.score }}</span>
          </div>
          <div class="score-label">技术契合度</div>
        </div>
        <div class="score-card">
          <div class="score-ring" :style="{ '--score': communication.score / 100 }">
            <svg viewBox="0 0 120 120">
              <circle cx="60" cy="60" r="52" class="ring-bg"/>
              <circle cx="60" cy="60" r="52" class="ring-fg"
                :style="{ strokeDashoffset: 326 - 326 * communication.score / 100 }"/>
            </svg>
            <span class="score-value">{{ communication.score }}</span>
          </div>
          <div class="score-label">沟通结构性</div>
        </div>
        <div class="score-card">
          <div class="score-ring" :style="{ '--score': emotional.score / 100 }">
            <svg viewBox="0 0 120 120">
              <circle cx="60" cy="60" r="52" class="ring-bg"/>
              <circle cx="60" cy="60" r="52" class="ring-fg"
                :style="{ strokeDashoffset: 326 - 326 * emotional.score / 100 }"/>
            </svg>
            <span class="score-value">{{ emotional.score }}</span>
          </div>
          <div class="score-label">情绪稳定性</div>
        </div>
      </div>

      <!-- 详细分析 -->
      <div class="report-section">
        <h2>技术契合度</h2>
        <p class="section-summary">{{ technical.summary }}</p>
        <el-alert
          v-if="technical.factRisks?.length"
          :title="`风险点（${technical.factRisks.length}）`"
          type="warning"
          :closable="false"
          show-icon
        >
          <template #default>
            <ul>
              <li v-for="(risk, i) in technical.factRisks" :key="i">{{ risk }}</li>
            </ul>
          </template>
        </el-alert>
      </div>

      <div class="report-section">
        <h2>沟通结构性</h2>
        <p class="section-summary">{{ communication.summary }}</p>
        <div v-if="communication.starObservations?.length" class="observation-list">
          <div v-for="(obs, i) in communication.starObservations" :key="i" class="obs-item">
            <el-icon><StarFilled /></el-icon>
            <span>{{ obs }}</span>
          </div>
        </div>
      </div>

      <div class="report-section">
        <h2>情绪稳定性</h2>
        <p class="section-summary">{{ emotional.summary }}</p>
        <div v-if="emotional.pressureMoments?.length" class="observation-list">
          <div v-for="(m, i) in emotional.pressureMoments" :key="i" class="obs-item">
            <el-icon><WarningFilled /></el-icon>
            <span>{{ m }}</span>
          </div>
        </div>
      </div>

      <!-- 改进计划 -->
      <div class="report-section improvement">
        <h2>改进计划</h2>
        <div class="improve-grid">
          <div class="improve-card">
            <h3>优先补强</h3>
            <ul>
              <li v-for="(p, i) in improvement.priorities" :key="i">{{ p }}</li>
            </ul>
          </div>
          <div class="improve-card">
            <h3>学习方向</h3>
            <ul>
              <li v-for="(d, i) in improvement.studyDirections" :key="i">{{ d }}</li>
            </ul>
          </div>
          <div class="improve-card">
            <h3>下次练习</h3>
            <ul>
              <li v-for="(n, i) in improvement.nextPractice" :key="i">{{ n }}</li>
            </ul>
          </div>
        </div>
      </div>

      <!-- 操作 -->
      <div class="report-actions">
        <el-button @click="router.push('/interview')">返回面试</el-button>
        <el-button type="primary" @click="router.push('/person/MyResume')">我的简历</el-button>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-if="error" class="error-state">
      <el-result icon="error" title="获取报告失败" :sub-title="error">
        <template #extra>
          <el-button type="primary" @click="router.push('/interview')">返回面试</el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { StarFilled, WarningFilled } from '@element-plus/icons-vue';
import { getReport } from '@/http/api/interview.js';
import NavBar from '@/components/NavBar/NavBar.vue';

const route = useRoute();
const router = useRouter();

const loading = ref(true);
const error = ref('');
const report = ref(null);

const technical = computed(() => report.value?.technical_fit || { score: 0, summary: '', factRisks: [] });
const communication = computed(() => report.value?.communication_structure || { score: 0, summary: '', starObservations: [] });
const emotional = computed(() => report.value?.emotional_stability || { score: 0, summary: '', pressureMoments: [] });
const improvement = computed(() => report.value?.improvement_plan || { priorities: [], studyDirections: [], nextPractice: [] });
const overallResult = computed(() => report.value?.overall_result || '');

const overallTagType = computed(() => {
  switch (overallResult.value) {
    case 'pass': return 'success';
    case 'needs_practice': return 'warning';
    case 'risky': return 'danger';
    default: return 'info';
  }
});

const overallLabel = computed(() => {
  switch (overallResult.value) {
    case 'pass': return '通过';
    case 'needs_practice': return '需要练习';
    case 'risky': return '风险较高';
    default: return '待评估';
  }
});

onMounted(async () => {
  const sessionId = route.params.id;
  if (!sessionId) {
    error.value = '缺少面试会话 ID';
    loading.value = false;
    return;
  }
  try {
    const res = await getReport(sessionId);
    report.value = res.data.report;
  } catch (e) {
    error.value = '无法获取面试报告，请确认面试已完成';
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.report-page {
  min-height: calc(100vh - 70px);
  padding: 110px 24px 40px;
  background: var(--bg, #f8f9fc);
}

.report-container {
  max-width: 800px;
  margin: 0 auto;
}

.report-header {
  text-align: center;
  margin-bottom: 40px;
}

.report-header h1 {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 12px;
}

.meta {
  display: flex;
  justify-content: center;
  gap: 12px;
  align-items: center;
}

.meta-item {
  color: var(--text-secondary, #6b7280);
  font-size: 14px;
}

/* 评分环 */
.score-overview {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-bottom: 40px;
}

.score-card {
  text-align: center;
}

.score-ring {
  position: relative;
  width: 100px;
  height: 100px;
  margin: 0 auto 8px;
}

.score-ring svg {
  width: 100%;
  height: 100%;
  transform: rotate(-90deg);
}

.ring-bg {
  fill: none;
  stroke: #e5e7eb;
  stroke-width: 8;
}

.ring-fg {
  fill: none;
  stroke: #5b3df4;
  stroke-width: 8;
  stroke-linecap: round;
  stroke-dasharray: 326;
  stroke-dashoffset: 326;
  transition: stroke-dashoffset 1s ease;
}

.score-ring:nth-child(1) .ring-fg { stroke: #5b3df4; }

.score-value {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary, #1a1a2e);
}

.score-label {
  font-size: 14px;
  color: var(--text-secondary, #6b7280);
  font-weight: 500;
}

/* 报告章节 */
.report-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
}

.report-section h2 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--text-primary, #1a1a2e);
}

.section-summary {
  color: var(--text-secondary, #6b7280);
  line-height: 1.6;
  margin-bottom: 12px;
}

.report-section ul {
  margin: 8px 0 0;
  padding-left: 20px;
}

.report-section ul li {
  margin-bottom: 6px;
  line-height: 1.5;
}

.observation-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.obs-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary, #6b7280);
  line-height: 1.5;
}

.obs-item .el-icon {
  margin-top: 2px;
  color: #f59e0b;
}

/* 改进计划 */
.improve-grid {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 16px;
}

.improve-card {
  background: #f9fafb;
  border-radius: 10px;
  padding: 16px;
}

.improve-card h3 {
  font-size: 14px;
  font-weight: 600;
  color: var(--primary, #5b3df4);
  margin-bottom: 8px;
}

.improve-card ul {
  margin: 0;
  padding-left: 16px;
}

.improve-card ul li {
  font-size: 13px;
  color: var(--text-secondary, #6b7280);
  margin-bottom: 4px;
  line-height: 1.5;
}

.report-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 32px;
}

.error-state {
  max-width: 500px;
  margin: 80px auto;
}
</style>