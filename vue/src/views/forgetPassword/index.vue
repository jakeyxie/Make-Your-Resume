<template>
  <nav-bar></nav-bar>
  <div class="forget-password-box">
    <div v-if="!isSendSuccess" class="icon-box">
      <svg-icon icon-name="icon-xianshi_jinggao" color="red" size="100px"></svg-icon>
    </div>
    <div v-else class="icon-box success-box">
      <svg-icon icon-name="icon-anquanbaozhang" color="#00c091" size="100px"></svg-icon>
      <p>邮件发送成功,请前往您的邮箱重置密码!</p>
    </div>
    <!-- 邮箱填写 -->
    <div class="email-box">
      <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        label-width="160px"
        class="demo-ruleForm"
        status-icon
        size="normal"
        label-position="top"
      >
        <el-form-item label="请填写您的邮箱地址" prop="email">
          <el-input v-model="ruleForm.email" type="email" />
        </el-form-item>
        <div class="form-button">
          <el-button :disabled="isSending" type="primary" :loading="isLoading" @click="submitForm(ruleFormRef)"
            >{{ isSending ? `${countdown}秒后重试` : '发送重置密码链接' }}</el-button
          >
        </div>
      </el-form>
    </div>
  </div>
</template>
<script setup>
  import { forgetPasswordAsync } from '@/http/api/user';
  import NavBar from "@/components/NavBar/NavBar.vue";
  import {startCountdown} from "@/utils/common.js";

  const isSendSuccess = ref(false);
  const isLoading = ref(false);
  const isSending = ref(false)
  const countdown = ref(60)
  const ruleForm = reactive({
    email: ''
  });
  const ruleFormRef = ref();
  const rules = reactive({
    email: [{ required: true, message: '请正确填写你的邮箱地址', trigger: 'blur' }]
  });

  const submitForm = async (formEl) => {
    if (!formEl) return;
    await formEl.validate(async (valid, fields) => {
      if (valid) {
        isLoading.value = true;
        const data = await forgetPasswordAsync(ruleForm);
        if (data.code === '200') {
          isSendSuccess.value = true;
          ElMessage({
            message: '重置密码链接发送成功',
            type: 'success'
          });
          isLoading.value = false;
          startCountdown(60, isSending, countdown);
        } else {
          isSendSuccess.value = false;
          isLoading.value = false;
        }
      } else {
        console.log('error submit!', fields);
      }
    });
  };
</script>
<style lang="scss" scoped>
  .forget-password-box {
    margin-top: 100px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding-top: 30px;
    .success-box {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      color: #00c091;
      p {
        margin-top: 15px;
      }
    }
    .email-box {
      margin-top: 30px;
      .form-button{
        display: flex;
        justify-content: center;
      }
      .demo-ruleForm{
        :deep(.el-input__wrapper) {
          background-color: #f3f3f3;
          border: none;
        }
      }
    }
  }
</style>
