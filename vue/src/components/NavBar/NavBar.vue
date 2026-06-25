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
        <el-menu-item index="/interview" >模拟面试</el-menu-item>
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
.nav-bar-box {
  display: flex;
  height: 70px;
  width: 100%;
  box-sizing: border-box;
  position: fixed;
  padding: 0 40px;
  top: 0;
  align-items: center;
  justify-content: space-between;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.06);
  z-index: 100;
  transition: all 0.3s ease;

  .nav-logo {
    display: flex;
    align-items: center;
    cursor: pointer;

    .logo {
      width: 48px;
      height: 48px;
      margin-left: 50px;
      background-image: url('@/assets/images/logo.png');
      background-size: 250%;
      background-position: center;
      border-radius: 12px;
      cursor: pointer;
      transition: transform 0.3s ease;
    }

    .nav-text {
      margin-left: 10px;
      letter-spacing: 4px;
      font-size: 22px;
      font-weight: 700;
      background: linear-gradient(135deg, #5b3df4, #00c9a7);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      cursor: pointer;
    }
  }

  .nav-logo:hover .logo {
    transform: rotate(-5deg) scale(1.05);
  }

  .midnavi {
    flex: 1;
  }

  .el-menu {
    border: none !important;
    background: transparent !important;
  }

  .user {
    width: fit-content;
    padding-left: 20px;
    padding-right: 40px;
    display: flex;
    align-items: center;
  }
}

.el-menu .is-active {
  background-color: transparent !important;
  color: var(--primary) !important;
  position: relative;
}

.el-menu .is-active::after {
  content: '';
  position: absolute;
  bottom: 8px;
  left: 50%;
  transform: translateX(-50%);
  width: 24px;
  height: 3px;
  background: linear-gradient(135deg, #5b3df4, #00c9a7);
  border-radius: 2px;
}

.el-menu .el-menu-item {
  font-size: 16px;
  padding: 0 28px 0 28px;
  color: var(--text-secondary);
  transition: all 0.3s ease;
  font-weight: 500;
}

.el-menu .el-menu-item:hover {
  color: var(--primary) !important;
  background-color: rgba(91, 61, 244, 0.04) !important;
}

.el-dropdown {
  cursor: pointer;
}

.el-tooltip__trigger {
  outline: none;
}

.logon-register-box {
  display: flex;
  gap: 12px;

  .register-btn {
    border-radius: 8px;
    border-color: var(--border);
    font-weight: 500;
    transition: all 0.3s ease;

    &:hover {
      border-color: var(--primary);
      color: var(--primary);
    }
  }

  .login-btn {
    border-radius: 8px;
    background: linear-gradient(135deg, #5b3df4, #7c63f5) !important;
    border: none !important;
    font-weight: 500;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(91, 61, 244, 0.4) !important;
    }
  }
}
</style>