<template>
  <div class="admin-box">
    <!--    导航区域    -->
    <admin-nav-bar></admin-nav-bar>
    <!--  下方区域开始 -->
    <div class="box-bottom">
      <!--  菜单区域 -->
      <left-menu></left-menu>
      <!--  数据渲染区域 -->
      <div class="admin-content" >
        <RouterView @updateUser="updateUser"/>
      </div>
    </div>
  </div>

</template>

<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";
import { useUserDataStore } from '@/store/user';
import AdminNavBar from "@/components/AdminNavBar/AdminNavBar.vue";
import LeftMenu from "@/views/admin/components/LeftMenu.vue";


const { userDataStore } = useUserDataStore();
const userData = computed(() => userDataStore);
const data = reactive({
  user: JSON.parse(localStorage.getItem('user_data') || "{}")
})

const logout = () => {
  localStorage.removeItem('user_data')
  location.href = '/login'
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem("user_data") || '{}')
}

</script>

<style lang="scss">

.admin-box{
  height: 100vh;
  width: 100vw;
  box-sizing: border-box;
  .box-bottom{
    width: 100%;
    height: 100%;
    display: flex;
    .admin-content{
      flex: 1;
    }
  }
}

.el-dropdown {
  cursor: pointer;
}
.el-tooltip__trigger {
  outline: none;
}
.el-menu--inline .el-menu-item {
  padding-left: 58px !important;
}
</style>