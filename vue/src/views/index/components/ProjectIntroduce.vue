<template>
  <div class="project-introduce-box">
    <div class="hero-bg"></div>
    <div class="hero-content">
      <div class="head animate-fade-in-up">
        <h1>智能简历助你打造<br><span class="highlight">满分简历</span></h1>
        <p class="subtitle">心动offer拿到手软</p>
      </div>
      <div class="feature-tags">
        <span class="tag">模版套用</span>
        <span class="tag">优秀范例</span>
        <span class="tag">一键排版</span>
        <span class="tag">专业指导</span>
        <span class="tag">评分诊断</span>
      </div>
      <div class="el-but animate-fade-in-up" style="animation-delay: 0.3s">
        <el-button class="btn-primary" type="primary" @click="handleCreateResume()">在线制作简历</el-button>
        <el-button class="btn-secondary" @click="handleImportResume()">导入简历</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";

const handleCreateResume = () => {
  router.push({ path: '/templates' });
};

const handleImportResume = () => {
  router.push({ path: '/templates' });
};
</script>

<style scoped>
.project-introduce-box {
  margin-top: 70px;
  width: 100%;
  min-height: 520px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.hero-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 20% 50%, rgba(255,255,255,0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(255,255,255,0.08) 0%, transparent 40%),
    radial-gradient(circle at 50% 80%, rgba(0,201,167,0.15) 0%, transparent 50%);
  z-index: 1;
}

.hero-bg::before {
  content: '';
  position: absolute;
  width: 600px;
  height: 600px;
  border-radius: 50%;
  border: 2px solid rgba(255,255,255,0.06);
  top: -200px;
  right: -100px;
}

.hero-bg::after {
  content: '';
  position: absolute;
  width: 400px;
  height: 400px;
  border-radius: 50%;
  border: 2px solid rgba(255,255,255,0.04);
  bottom: -150px;
  left: 10%;
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 60px 20px;
}

.head {
  color: #ffffff;
  font-weight: 800;
  text-align: center;
}

.head h1 {
  font-size: 64px;
  line-height: 1.3;
  letter-spacing: -1px;
  margin: 0;
}

.head .highlight {
  background: linear-gradient(135deg, #00c9a7, #ffd700);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.head .subtitle {
  font-size: 28px;
  font-weight: 500;
  margin-top: 16px;
  color: rgba(255,255,255,0.85);
  letter-spacing: 2px;
}

.feature-tags {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 28px;
  flex-wrap: wrap;
}

.tag {
  padding: 6px 20px;
  border-radius: 20px;
  font-size: 15px;
  font-weight: 500;
  color: rgba(255,255,255,0.9);
  background: rgba(255,255,255,0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255,255,255,0.2);
  transition: all 0.3s ease;
}

.tag:hover {
  background: rgba(255,255,255,0.25);
  transform: translateY(-2px);
}

.el-but {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 40px;
}

.btn-primary {
  height: 54px !important;
  font-size: 20px !important;
  padding: 0 48px !important;
  border-radius: 27px !important;
  background: #ffffff !important;
  color: #5b3df4 !important;
  border: none !important;
  font-weight: 600 !important;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
  transition: all 0.3s ease !important;
}

.btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 32px rgba(0,0,0,0.25) !important;
  background: #f8f7ff !important;
}

.btn-secondary {
  height: 54px !important;
  font-size: 20px !important;
  padding: 0 48px !important;
  border-radius: 27px !important;
  background: rgba(255,255,255,0.15) !important;
  color: #ffffff !important;
  border: 2px solid rgba(255,255,255,0.4) !important;
  font-weight: 600 !important;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease !important;
}

.btn-secondary:hover {
  background: rgba(255,255,255,0.25) !important;
  border-color: rgba(255,255,255,0.7) !important;
  transform: translateY(-3px);
}
</style>