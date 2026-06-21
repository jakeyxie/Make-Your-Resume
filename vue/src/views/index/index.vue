<template>
  <div class="index-box">
    <!-- 导航栏 -->
<!--  动态导航栏  <nav-bar :navPositions="data.positions" @scroll="handleScroll"></nav-bar>-->
    <nav-bar></nav-bar>
<!--  项目介绍栏  -->
    <div ref="introduceRef">
      <project-introduce></project-introduce>
    </div>
<!--  模板选择栏  -->
    <template-select></template-select>
<!--  页脚  -->
    <footer-com></footer-com>
  </div>
</template>

<script setup>
import NavBar from '@/components/NavBar/NavBar.vue';
import ProjectIntroduce from '@/views/index/components/ProjectIntroduce.vue';
import {reactive} from 'vue'
// import router from "@/router/index.js";
import TemplateSelect from "@/views/index/components/TemplateSelect.vue";
import FooterCom from "@/components/FooterCom/FooterCom.vue";
// import { throttle } from 'lodash';
// 防止高频触发函数

// const handleSelect = (key, keyPath) => {
//   console.log(key, keyPath);
// }
const data = reactive({
  activeIndex: 1,
  activeIndex2: 1,
  positions: ""
})
// const scrollPosition = ref(0);
// onMounted(async () => {
//   // 添加滚动事件监听
//   window.addEventListener('scroll', throttleHandle);
// });
//
// onBeforeUnmount(() => {
//   // 移除滚动事件监听
//   window.removeEventListener('scroll', throttleHandle);
// });
//
// // 页面滚动改变导航栏样式
// const throttleHandle = throttle(() => {
//   handleScroll();
// }, 300);
//
// const handleScroll = () => {
//   if (document.documentElement.scrollTop > 100) {
//     data.positions = 'fixed';
//   }
//   else{
//     data.positions = 'relative';
//   }
//   console.log("scroll",document.documentElement.scrollTop)
// };

</script>

<style  scoped>
.index-box {
  position: relative;
  z-index: 0;
}
</style>