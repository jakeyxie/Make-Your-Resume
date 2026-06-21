<template>
<!--导航栏-->
  <div class="nav-bar-box" >
    <!--logo-->
    <div class="nav-logo" @click="toHome" >
      <div class="logo" title="logo" ></div>
      <p class="nav-text">简才网</p>
    </div>

    <div class="midnavi" >
      <el-menu
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
          router :default-openeds="['1']"
          :default-active="router.currentRoute.value.path"
      >
        <el-menu-item style="margin-left: 20%" index="/">首页</el-menu-item>
        <el-menu-item index="/AiResume">AI简历制作</el-menu-item>

        <el-menu-item index="/Templates" >简历模版</el-menu-item>
        <el-menu-item index="/article" >求职攻略</el-menu-item>
        <el-menu-item index="/person/MyResume" >我的简历</el-menu-item>
      </el-menu>
    </div>
<!--用户栏-->
    <div class="user-box">
<!--      <div v-if="data.userInfo" class="login-register">-->
<!--        <el-button class="register-btn" @click="openRegisterDialog">注册</el-button>-->
<!--        <el-button class="login-btn" type="primary" @click="openLoginDialog">登录</el-button>-->
<!--      </div>-->
      <div  class="user" >
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
import { reactive, computed } from 'vue'
import { useUserDataStore } from '@/store/user';


const { userDataStore } = useUserDataStore();
const userData = computed(() => userDataStore);

const handleSelect = (key, keyPath) => {
  console.log(key, keyPath);
}
const data = reactive({
  activeIndex: 1,
  activeIndex2: 1,
  userInfo: true
})
//传递nav位置
// const props = defineProps({
//   navPositions: {
//     type: String,
//     default: 'relative'
//   }
// });
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

<style lang="scss" scoped>
.nav-bar-box{
  display: flex;
  height: 70px;
  width: 100%;
  box-sizing: border-box;
  //position: v-bind(navPositions);
  position: fixed;
  transition: 0.5s ease !important; /* 平滑过渡效果 */
  padding: 0 40px;
  top: 0;
  align-items: center;
  justify-content: space-between;
  box-shadow: rgba(0, 0, 0, 0.2) 0 0 10px;
  background-color: rgb(255, 255, 255);
  z-index: 10;
  .nav-logo{
    display: flex;
    align-items: center;
    .nav-text {
      letter-spacing: 4px;
      font-size: 22px;
      font-weight: 600;
      font-family: ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", Segoe UI Symbol, "Noto Color Emoji";
      cursor: pointer;
    }
    .logo {
      width: 70px;
      height: 70px;
      margin-left:50px;
      background-image: url('@/assets/images/logo.png');
      background-size: 250%;
      background-position: center;
      z-index: 2;   /* 根据内容自动调整高度 */
      cursor: pointer;
    }
  }
  .midnavi{
    flex: 1;
  }
  .el-menu {
    border: none !important;
  }
  .user{
    width: fit-content;
    padding-left: 20px;
    padding-right: 40px;
    display: flex;
    align-items: center;
  }
}
.el-menu .is-active {
  background-color: #ecf5ff;
  color: #fff;
}
.el-menu .el-menu-item{
  font-size: 18px;
  padding: 0 32px 0 32px;
}
.el-dropdown {
  cursor: pointer;
}

.el-tooltip__trigger {
  outline: none;
}



</style>