<template>
  <router-view></router-view>
  <!-- 全局等待层 -->
  <loading-com v-show="isLoading"></loading-com>
  <!-- 返回顶部 -->
  <el-backtop style="right: 100px; width: 50px; height: 50px;"></el-backtop>
</template>

<script setup>
import { storeToRefs } from 'pinia';
import { useLoadingStore } from "@/store/loading.js";
import { openAndCloseLoadingByTime } from "@/utils/common.js";

const { isLoading } = storeToRefs(useLoadingStore());
openAndCloseLoadingByTime(10);
</script>

<style>
#app {
  min-width: 1200px;
  background: var(--bg);
}
</style>
