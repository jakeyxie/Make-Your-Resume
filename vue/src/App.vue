//项目入口
<template>
  <router-view></router-view>
<!--   全局等待层   -->
  <loading-com-vue v-show="isLoading"></loading-com-vue>
<!--   返回顶部  -->
  <el-backtop style="right: 100px; width: 50px; height: 50px;"></el-backtop>
</template>

<script setup>
import { storeToRefs } from 'pinia';
import { useLoadingStore } from "@/store/loading.js";
import { openAndCloseLoadingByTime } from "@/utils/common.js";
import LoadingComVue from "@/components/Loading/LoadingComVue.vue";

const { isLoading } = storeToRefs(useLoadingStore());
openAndCloseLoadingByTime(10); // 等待动画层

</script>

<style>
#app {
  min-width: 1500px;
}
</style>