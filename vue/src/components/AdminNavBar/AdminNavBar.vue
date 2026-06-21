<template>
  <div class="nav-box">
    <div class="nav-box-left">
      <div class="logo" title="logo" ></div>
      <span>简才网</span>
    </div>
    <div class="nav-box-right">
      <div><span style="cursor: pointer" @click="router.push('/manager/home')">首页/ {{ router.currentRoute.value.meta.name }}</span></div>
      <div class="user" >
        <el-dropdown>
          <div v-if="!userDataStore.id" class="logon-register-box">
            <el-button class="register-btn" @click="toLogin">注册</el-button>
            <el-button class="login-btn" type="primary" @click="toLogin">登录</el-button>
          </div>
          <div v-else style="display: flex; align-items: center">
            <img v-if="userData.avatar" style="width: 40px; height: 40px; border-radius: 50%" :src="userDataStore.avatar" alt="">
            <img v-else style="width: 40px; height: 40px; border-radius: 50%" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="">
            <span style="margin-left: 5px">{{userData.username || 'user'}}</span>
          </div>
          <template v-if="userDataStore.id" #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="toPerson">个人主页</el-dropdown-item>
              <el-dropdown-item @click="toPassword">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>
<script setup>
import router from "@/router/index.js";
import { useUserDataStore } from '@/store/user';


const { userDataStore } = useUserDataStore();
const userData = computed(() => userDataStore);

const logout = () =>{
  localStorage.removeItem('user_data')
  location.href = '/login'
}
const toLogin = () => {
  router.push('/login')
}

const toPerson = () =>{
  router.push('/person/personDetail');
}
const toPassword = () =>{
  router.push('/person/accountSetting');
}
const emit = defineEmits(['toHome']);
const toHome = () =>{
  router.push('/');
}
</script>

<style lang="scss">
.nav-box {
  height: 70px;
  width: 100%;
  display: flex;
  box-sizing: border-box;
  position: fixed;
  top: 0;
  align-items: center;
  justify-content: space-between;

  background-color: rgb(255, 255, 255);
  z-index: 10;
  .nav-box-left{
    width: 240px;
    display: flex;
    align-items: center;
    background-color: #3a456b;
    span{
      font-size: 26px;
      font-weight: bold;
      color: #f1f1f1;
    }
    .logo{
      width: 70px;
      height: 70px;
      margin-left:30px;
      background-image: url('@/assets/images/logo.png');
      background-size: 250%;
      background-position: center;
      z-index: 2;   /* 根据内容自动调整高度 */
      cursor: pointer;
    }
  }
  .nav-box-right{
    height: 100%;
    flex: 1;
    display: flex;
    align-items: center;
    border-bottom: 1px solid #ddd;
    padding-left: 20px;
    justify-content: space-between;
    box-shadow: rgba(0, 0, 0, 0.2) 0 0 10px;
    .user{
      margin-right: 20px;
    }
  }
}
</style>