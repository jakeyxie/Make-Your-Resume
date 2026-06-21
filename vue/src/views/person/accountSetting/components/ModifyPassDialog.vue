<template>
  <el-dialog
    :model-value="dialogVisible"
    title="修改密码"
    width="400px"
    :show-close="false"
    :close-on-click-modal="false"
    append-to-body
  >
    <el-form
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      label-width="100px"
      size="default"
      label-position="left"
    >
      <el-form-item label="邮箱:" prop="email">
        <el-input v-model="ruleForm.email" type="email" />
      </el-form-item>
      <el-form-item label="旧密码:" prop="oldPassword">
        <el-input v-model="ruleForm.oldPassword" />
      </el-form-item>
      <el-form-item label="新密码:" prop="newPassword">
        <el-input v-model="ruleForm.newPassword" type="password" />
      </el-form-item>
      <el-form-item label="确认密码:" prop="sureNewPassword">
        <el-input v-model="ruleForm.sureNewPassword" type="password" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancle">取消</el-button>
        <el-button type="primary" :loading="sureLoading" @click="submit(ruleFormRef)"
          >确认修改</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
  import {ElMessage} from 'element-plus';
  import request from "@/utils/request.js";
  import {nextTick} from "vue";
  import { useUserDataStore } from '@/store/user';
  const { userDataStore } = useUserDataStore();
  const store = useUserDataStore();
  // 查询用户信息
  const data = reactive({
    user: userDataStore
  })
  const emit = defineEmits(['cancle']);

  const props = defineProps({
    dialogVisible: {
      type: Boolean,
      default: false
    }
  });

  // 表单填写数据
  const ruleForm = reactive({
    email: '',
    oldPassword: '',
    newPassword: '',
    sureNewPassword: ''
  });

  // 密码校验规则
  const passwordValidator = (rule, value, callback) => {
    let pwdRegex = new RegExp('(?=.*[0-9])(?=.*[a-zA-Z]).{8,30}');
    if (!value) {
      callback(new Error('新密码不能为空'));
    } else if (!pwdRegex.test(value)) {
      callback(new Error('密码中必须包含字母、数字,至少8个字符'));
    } else {
      callback();
    }
  };
  // 确认密码的自定义规则
  const surePassValidator = (rule, value, callback) => {
    if (!value) {
      callback(new Error('请再次确认密码'));
    } else if (ruleForm.newPassword !== value) {
      callback(new Error('两次输入密码不一致'));
    } else {
      callback();
    }
  };
  const rules = reactive({
    email: [{ required: true, message: '邮箱不能为空', trigger: 'change' }],
    oldPassword: [{ required: true, message: '旧密码不能为空', trigger: 'change' }],
    newPassword: [
      {
        required: true,
        validator: passwordValidator,
        trigger: 'change'
      }
    ],
    sureNewPassword: [{ required: true, validator: surePassValidator, trigger: 'change' }]
  });

  // 取消
  const cancle = () => {
    emit('cancle');
  };

  // 确认修改
  const update = () => {
    let url
    if (data.user.role === 'admin') {
      url = '/admin/update'
    }
    if (data.user.role === 'user') {
      url = '/user/update'
    }
    data.user.email = ruleForm.email
    data.user.password = ruleForm.newPassword
    request.put(url, data.user).then(res => {
      if (res.code === '200') {
        ElMessage.success('修改成功，请重新登录!')
        localStorage.setItem("user_data", JSON.stringify(data.user))
      }
    })
  }
  const sureLoading = ref(false);
  const ruleFormRef = ref();
  const router = useRouter();
  const submit = async (formEl) => {
    if (!formEl) return;
    if (ruleForm.oldPassword !== userDataStore.password ) {
      ElMessage.error('密码错误！');
      return ;
    }
    sureLoading.value = true;
    update();
    sureLoading.value = false;
    store.updateUserData(data.user);
    router.push('/login');
    emit('cancle');
    await nextTick();
    localStorage.removeItem('token');
    localStorage.removeItem('user_data');
  };

</script>
<style lang="scss" scoped>
  .dialog-footer button:first-child {
    margin-right: 10px;
  }
</style>
