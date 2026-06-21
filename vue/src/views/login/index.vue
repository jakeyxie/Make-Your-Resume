<template>
    <div class="login-box">
      <div class="login-dialog-form-box">
        <div :class="['container', { 'right-panel-active': isSignUp }]">
          <div class="form-container sign-up-container">
            <h1>创建账号</h1>
            <div class="social-container"> </div>
            <el-form
                ref="registerRuleFormRef"
                class="forms_form"
                :model="RegisterForm.form"
                :rules="RegisterForm.rules"
            >
              <!-- 昵称 -->
              <el-form-item prop="username">
                <el-input
                    v-model="RegisterForm.form.username"
                    type="text"
                    maxlength="16"
                    class="forms_field-input"
                    placeholder="用户名"
                    @keyup.enter="register(registerRuleFormRef)"
                />
              </el-form-item>
              <!-- 邮箱 -->
              <el-form-item prop="email">
                <el-input
                    v-model="RegisterForm.form.email"
                    type="email"
                    class="forms_field-input"
                    maxlength="40"
                    placeholder="电子邮箱"
                    @keyup.enter="register(registerRuleFormRef)"
                />
              </el-form-item>
              <!-- 密码 -->
              <el-form-item prop="password">
                <el-input
                    v-model="RegisterForm.form.password"
                    type="password"
                    class="forms_field-input"
                    placeholder="密码"
                    @keyup.enter="register(registerRuleFormRef)"
                />
              </el-form-item>
              <!-- 确认密码 -->
              <el-form-item prop="surePassword">
                <el-input
                    v-model="RegisterForm.form.surePassword"
                    type="password"
                    class="forms_field-input"
                    placeholder="确认密码"
                    @keyup.enter="register(registerRuleFormRef)"
                />
              </el-form-item>
            </el-form>
            <el-button
                class="forms_buttons-action"
                :loading="isRegisterLoading"
                @click="register(registerRuleFormRef)"
            >注 册</el-button
            >
          </div>

          <!-- 登录界面 -->
          <div class="form-container sign-in-container">
            <h1>登录</h1>
            <div class="social-container"> </div>
            <el-form ref="loginRuleFormRef" class="forms_form" :model="LoginForm.form" :rules="LoginForm.rules">
              <el-form-item prop="role">
                <el-select size="large" style="width: 100%" v-model="LoginForm.form.role">
                  <el-option label="管理员" value="admin"></el-option>
                  <el-option label="普通用户" value="user"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="email">
                <el-input
                    v-model="LoginForm.form.email"
                    type="email"
                    class="forms_field-input"
                    maxlength="40"
                    placeholder="电子邮箱"
                    @keyup.enter="login(loginRuleFormRef)"
                />
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                    v-model="LoginForm.form.password"
                    type="password"
                    class="forms_field-input"
                    placeholder="密码"
                    @keyup.enter="login(loginRuleFormRef)"
                />
              </el-form-item>
            </el-form>
            <a href="#" @click.prevent="forgetPassword">忘记密码？</a>
            <el-button
                class="forms_buttons-action"
                :loading="isLoginLoading"
                @click="login(loginRuleFormRef)"
            >登 录</el-button
            >
          </div>

          <!-- 注册登录浮窗切换 -->
          <div class="overlay-container">
            <div class="overlay">
              <div class="overlay-panel overlay-left">
                <h1>欢迎回来！</h1>
                <p>已有帐号，立即登录！</p>
                <button class="ghost" @click="signIn">立即登录</button>
              </div>

              <div class="overlay-panel overlay-right">
                <h1>你好, 简友！</h1>
                <p>还没有账号？快来注册吧！</p>
                <button class="ghost" @click="signUp">立即注册</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import router from "@/router/index.js";
import {loginAsync, registerAsync} from "@/http/api/user.js";

const loginRuleFormRef = ref()
const registerRuleFormRef = ref()
//是否在注册页面
const isSignUp = ref(false)

const LoginForm = reactive({
  form: { role: 'user' },
  rules: {
    email: [
      { required: true, message: '请输入账号', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ],
  }
})

// 密码校验规则
const passwordValidator = (rule, value, callback) => {
  let pwdRegex = new RegExp('(?=.*[0-9])(?=.*[a-zA-Z]).{8,30}');
  if (!value) {
    callback(new Error('密码不能为空'));
  } else if (!pwdRegex.test(value)) {
    callback(new Error('密码中必须包含字母、数字,至少8个字符'));
  } else {
    callback();
  }
};
// 确认密码的自定义规则
const surePassValidator = (rule, value, callback) => {
  // value 表示用户输入的确认密码
  if (value !== RegisterForm.form.password) {
    callback(new Error("两次输入的密码不匹配！"))
  } else {
    callback()
  }
};
const RegisterForm = reactive({
  form: { role: 'user' },
  rules: {
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
    ],
    email: [
      { required: true, message: '请输入账号', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { validator: passwordValidator, trigger: 'blur'}
    ],
    surePassword: [
      { required: true, message: '请再一次输入密码', trigger: 'blur' },
      { validator: surePassValidator, trigger: 'blur'}
    ],
  }
})
const login = () => {
  loginRuleFormRef.value.validate((valid) => {
    if (valid) {
      loginAsync(LoginForm)
    }
  })
}
const register = () => {
  registerRuleFormRef.value.validate((valid) => {
    if (valid) {
      let sign;
      sign = registerAsync(RegisterForm)
      if(sign) isSignUp.value = false
    }
  })
}
const signIn = () =>{
  isSignUp.value = false
}
const signUp = () =>{
  isSignUp.value = true
}
const forgetPassword = () => {
  router.push('/forgetPassword')
}
</script>

<style lang="scss" scoped>
.login-box{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100%;
}
.login-dialog-form-box {
  position: relative;
  .close-loding-dialog-box {
    position: absolute;
    top: 20px;
    right: 20px;
    z-index: 999;
    cursor: pointer;
  }
  h1 {
    font-weight: bold;
    margin: 0;
  }

  h2 {
    text-align: center;
  }

  p {
    font-size: 14px;
    font-weight: 100;
    line-height: 20px;
    letter-spacing: 0.5px;
    margin: 20px 0 30px;
  }

  span {
    font-size: 12px;
  }

  a {
    color: #333;
    font-size: 14px;
    text-decoration: none;
    margin: 0 0 15px 0;
  }

  button {
    border-radius: 20px;
    border: 1px solid #00c091;
    background-color: #00c091;
    color: #ffffff;
    font-size: 12px;
    font-weight: bold;
    padding: 18px 45px;
    letter-spacing: 1px;
    text-transform: uppercase;
    transition: transform 80ms ease-in;
    cursor: pointer;
  }

  button:active {
    transform: scale(0.95);
  }

  button:focus {
    outline: none;
  }

  button.ghost {
    background-color: transparent;
    border-color: #ffffff;
    cursor: pointer;
    padding: 12px 45px;
  }

  .container {
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
    position: relative;
    overflow: hidden;
    width: 768px;
    max-width: 100%;
    min-height: 480px;
  }

  .form-container {
    position: absolute;
    top: 0;
    height: 100%;
    transition: all 0.6s ease-in-out;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 0 50px;
    :deep(.forms_form) {
      width: 100%;
      input {
        border: none;
        padding: 10px 12px;
        margin: 8px 0;
        width: 100%;
        font-size: 14px;
        color: #333;
      }
      input:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px #ffffff inset !important;
      }
      .el-form-item {
        margin-bottom: 30px;
      }
      .el-input__inner {
        background-color: none !important;
      }
    }
  }

  .sign-in-container {
    left: 0;
    width: 50%;
    z-index: 2;
  }

  .container.right-panel-active .sign-in-container {
    transform: translateX(100%);
  }

  .sign-up-container {
    left: 0;
    width: 50%;
    opacity: 0;
    z-index: 1;
  }

  .container.right-panel-active .sign-up-container {
    transform: translateX(100%);
    opacity: 1;
    z-index: 5;
    animation: show 0.6s;
    background-color: #fff;
  }

  @keyframes show {
    0%,
    49.99% {
      opacity: 0;
      z-index: 1;
    }

    50%,
    100% {
      opacity: 1;
      z-index: 5;
    }
  }

  .overlay-container {
    position: absolute;
    top: 0;
    left: 50%;
    width: 50%;
    height: 100%;
    overflow: hidden;
    transition: transform 0.6s ease-in-out;
    z-index: 100;
  }

  .container.right-panel-active .overlay-container {
    transform: translateX(-100%);
  }

  .overlay {
    background: #b0db7d;
    background: linear-gradient(to bottom right, #73b722 40%, #9dc6ae 100%);
    background-repeat: no-repeat;
    background-size: cover;
    background-position: 0 0;
    color: #ffffff;
    position: relative;
    left: -100%;
    height: 100%;
    width: 200%;
    transform: translateX(0);
    transition: transform 0.6s ease-in-out;
  }

  .container.right-panel-active .overlay {
    transform: translateX(50%);
  }

  .overlay-panel {
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    padding: 0 40px;
    text-align: center;
    top: 0;
    height: 100%;
    width: 50%;
    transform: translateX(0);
    transition: transform 0.6s ease-in-out;
  }

  .overlay-left {
    transform: translateX(-20%);
  }

  .container.right-panel-active .overlay-left {
    transform: translateX(0);
  }

  .overlay-right {
    right: 0;
    transform: translateX(0);
  }

  .container.right-panel-active .overlay-right {
    transform: translateX(20%);
  }

  .social-container {
    margin: 20px 0;
  }

  .social-container a {
    border: 1px solid #dddddd;
    border-radius: 50%;
    display: inline-flex;
    justify-content: center;
    align-items: center;
    margin: 0 5px;
    height: 40px;
    width: 40px;
    cursor: pointer;
    &:hover {
      color: #ff4b2b;
    }
  }
}
</style>
