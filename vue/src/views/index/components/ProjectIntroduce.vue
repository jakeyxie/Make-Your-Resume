<template>
  <div class="project-introduce-box">
    <div class="top" style="height: 100%; width: 80%; background-color: #ffffff;">
      <div class="head" >
        <a>智能简历助你打造</a>
        <a style="color: #5b3df4">满分简历</a>
        <hr><a>心动offer拿到手软</a>
      </div>
      <div style="margin-top: 10px; font-size: 30px; text-align: center">模版套用/优秀范例/一键排版/专业指导/评分诊断</div>

      <div class="el-but">
        <el-button type="primary" @click="handleCreateResume()">在线制作简历</el-button>
        <el-button >导入简历</el-button>
<!--        <ElButton> 导入简历</ElButton>-->
      </div>
    </div>
<!--    npm i element-ui@2.15.1 -S-->
  </div>
</template>

<script setup>
import router from "@/router/index.js";
// import {reactive, ref} from 'vue'

const handleCreateResume = () => {
  router.push({ path: '/Maker', query: { id: 123 } });
}

</script>

<style scoped>
*{
  margin: 0;
  padding: 0;
}

.project-introduce-box{
  margin-top: 70px;
  width: 100%;
  height: 420px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  background-color: #ffffff;
  z-index: 0;
}
hr{
  border: none;
}
.head{
  font-family: ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", Segoe UI Symbol, "Noto Color Emoji";
  font-weight: bold;
  color: #221081;
  font-size: 70px;
  text-align: center;
  margin-top: 40px;
}
.el-but{
  display: flex;
  justify-content: center;
  margin-top: 30px;
  text-align: center;
  font-size: 30px;
}
.el-but .el-button{
  min-height: 50px !important;
  font-size: 26px;
  margin: 0 80px 0 80px;
  min-width: 200px ;
}
</style>