<template>
  <nav-bar></nav-bar>
  <div class="reset-password-box">
    <div class="form-box">
      <el-alert
        v-if="isShowError"
        title="设置密码失败,请尝试重新发送重置邮件"
        :closable="false"
        type="error"
        effect="dark"
        style="margin-bottom: 40px"
      />
      <el-form
        ref="resetRuleFormRef"
        label-width="100px"
        label-position="left"
        :model="formLabelAlign"
        :rules="registerRules"
      >
        <el-form-item label="新密码:" prop="password">
          <el-input v-model="formLabelAlign.password" type="password" />
        </el-form-item>
        <el-form-item label="确认密码:" prop="confirmPassword">
          <el-input v-model="formLabelAlign.confirmPassword" type="password" />
        </el-form-item>
      </el-form>
      <div class="form-button">
        <el-button
            v-if="!showCountdown"
            type="success"
            class="forms_buttons-action"
            :loading="isLoading"
            @click="reset(resetRuleFormRef)"
        >确认重置</el-button
        >
        <el-button
            v-else
            type="primary"
            class="forms_buttons-action"
            :loading="isLoading"
            @click="toLogin"
        >
          立即跳转({{ countdown }}s自动跳转)
        </el-button>
      </div>
    </div>

  </div>
</template>
<script  setup>
  import NavBar from "@/components/NavBar/NavBar.vue";
  import {resetPasswordAsync} from "@/http/api/user.js";

  const route = useRoute();
  const router = useRouter();
  const isLoading = ref(false);
  const formLabelAlign = reactive({
    token:route.query.token,
    password: '',
    confirmPassword: ''
  });
  // 确认密码的自定义规则
  const surePassValidator = (rule, value, callback) => {
    if (!value) {
      callback(new Error('请再次确认密码'));
    } else if (formLabelAlign.password !== value) {
      callback(new Error('两次输入密码不一致'));
    } else {
      callback();
    }
  };
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
  const registerRules = reactive({
    password: [
      {
        required: true,
        validator: passwordValidator,
        trigger: 'blur'
      }
    ],
    confirmPassword: [{ required: true, validator: surePassValidator, trigger: 'blur' }]
  });

  // 重置密码
  let timer = ref(null);
  const countdown = ref(15);
  const showCountdown = ref(false);
  const isShowError = ref(false);
  const resetRuleFormRef = ref();
  const reset = async (formEl) => {
    if (!formEl) return;
    await formEl.validate(async (valid, fields) => {
      if (valid) {
        isShowError.value = false;
        isLoading.value = true;
        const data = await resetPasswordAsync(formLabelAlign);
        console.log("data:",typeof data.code)
        if (data.code === '200') {
          isLoading.value = false;
          isShowError.value = false;
          showCountdown.value = true;
          // 开启自动跳转定时器
          timer = setInterval(() => {
            if (countdown.value > 0) {
              countdown.value--;
            } else {
              clearInterval(timer);
              router.push('/login');
            }
          }, 1000);
        }
        else {
          isLoading.value = false;
          isShowError.value = true;
        }
      } else {
        console.log('error submit!', fields);
      }
    });
  };
  // 跳转至登录
  const toLogin = () => {
    router.push('/login');
  };

  // 销毁前
  onUnmounted(() => {
    if (timer) {
      clearInterval(timer);
    }
  });
</script>
<style lang="scss" scoped>
  .reset-password-box {
    display: flex;
    margin-top: 100px;
    justify-content: center;
    align-items: center;
    padding-top: 20px;
    flex-direction: column;
    .form-box {
      width: 350px;
      border-radius: 5px;
      background-color: #fff;
      box-shadow: rgba(99, 100, 99, 0.2) 0px 0px 10px;
      padding: 40px 30px 10px 30px;
      box-sizing: border-box;
      justify-self: center;
      .form-button{
        display: flex;
        justify-content: center;
      }
      .forms_buttons-action{
        margin-top: 20px;
      }
      :deep(.el-input__wrapper) {
        background-color: #f3f3f3;
        border: none;
      }
    }
    :deep(.el-alert__content) {
      display: flex;
      align-items: center;
      height: 20px;
      .el-alert__title {
        line-height: 0;
      }
    }
    .auto-jump {
      display: flex;
      font-size: 16px;
    }
  }
</style>
