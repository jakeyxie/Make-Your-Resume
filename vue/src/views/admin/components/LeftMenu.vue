<template>
  <c-scrollbar
      style="width: 240px;"
      trigger="hover"
  >
    <div class="left-menu">
      <el-menu router :default-openeds="['1', '2']" :default-active="router.currentRoute.value.path" style="min-height: calc(100vh - 70px)">
        <el-menu-item index="/admin/home"><el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-sub-menu index="1">
          <template #title>
            <el-icon><Monitor /></el-icon>
            <span>信息管理</span>
          </template>
          <el-menu-item index="/admin/notice" v-if="userDataStore.role === 'admin'">系统公告</el-menu-item>
          <el-menu-item index="/admin/notice" v-else>公告信息</el-menu-item>
          <el-menu-item index="/admin/category">攻略分类</el-menu-item>
          <el-menu-item index="/admin/introduction">求职攻略</el-menu-item>
          <el-menu-item index="/admin/resumeSort">简历分类</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="2" v-if="userDataStore.role === 'admin'">
          <template #title>
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/admin/adminList">管理员信息</el-menu-item>
          <el-menu-item index="/admin/userList">普通用户信息</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>
  </c-scrollbar>

</template>
<script setup>
import router from "@/router/index.js";
import { useUserDataStore } from '@/store/user';
import {CScrollbar} from "c-scrollbar";
const { userDataStore } = useUserDataStore();

</script>
<style>
c-scrollbar {
  overflow: auto; /* 保持滚动功能 */
}

c-scrollbar :deep(.-webkit-scrollbar) {
  display: none; /* 隐藏滚动条 */
}
  .left-menu{
    z-index: 10;
    overflow: auto;
    margin-top: 70px;
    width: 240px;
    position: fixed;
    span{
      font-size: 16px;
    }
    li{
      font-size: 16px;
    }
    .el-menu {
      background-color: #3a456b;
      border: none;
    }
    .el-sub-menu__title {
      background-color: #3a456b;
      color: #ddd;
    }
    .el-menu-item {
      height: 65px;
      color: #ddd;
    }
    .el-menu .is-active {
      background-color: #537bee;
      color: #fff;
    }
    .el-sub-menu__title:hover {
      background-color: #3a456b;
    }
    .el-menu-item:not(.is-active):hover {
      background-color: #7a9fff;
      color: #333;
    }
  }


</style>