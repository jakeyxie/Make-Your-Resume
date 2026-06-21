<template>
  <div class="person-detail-box">
    <el-form
        class="person-form"
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      label-width="100px"
      size="default"
      label-position="left"
    >
      <el-form-item label="昵称:" prop="username">
        <el-input v-if="isEdit" v-model="ruleForm.username" />
        <p v-else>{{ data.user.username }}</p>
      </el-form-item>
      <el-form-item label="个性签名:" prop="surname">
        <el-input v-if="isEdit" v-model="ruleForm.surname" />
        <p v-else>{{ data.user.surname }}</p>
      </el-form-item>
    </el-form>
    <div class="button-box">
      <el-button v-if="!isEdit" type="primary" @click="edit">编辑</el-button>
      <div v-else>
        <el-button @click="cancle">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submit(ruleFormRef)"
          >保存更改</el-button
        >
      </div>
    </div>
  </div>
</template>
<script setup>
  import {ElMessage} from 'element-plus';
  import { reactive, nextTick } from "vue";
  import request from "@/utils/request.js";
  import { useUserDataStore } from '@/store/user';
  const { userDataStore } = useUserDataStore();
  const store = useUserDataStore();
  // 查询用户信息
  const data = reactive({
    user: userDataStore
  })
  // 表单填写数据
  const ruleForm = reactive({
    username: '',
    surname: ''
  });
  const rules = reactive({
    username: [{ required: true, message: '昵称不能为空', trigger: 'blur' }],
    surname: [{ required: true, message: '个性签名不能为空', trigger: 'blur' }]
  });


  // 点击编辑
  const isEdit = ref(false);
  const edit = () => {
    ruleForm.username = data.user.username;
    ruleForm.surname = data.user.surname;
    isEdit.value = true;
  };

  // 取消更改
  const ruleFormRef = ref();
  const cancle = () => {
    isEdit.value = false;
    ruleFormRef.value.resetFields();
  };

  const update = () => {
    let url
    if (data.user.role === 'admin') {
      url = '/admin/update'
    }
    if (data.user.role === 'user') {
      url = '/user/update'
    }
    data.user.username = ruleForm.username
    data.user.surname = ruleForm.surname
    request.put(url, data.user).then(res => {
      if (res.code === '200') {
        ElMessage.success('更新成功')
        localStorage.setItem("user_data", JSON.stringify(data.user))
      }
    })
  }
  // 保存更改
  const submitLoading = ref(false);
  const submit = async (formEl) => {
    if (!formEl) return;
    update();
    isEdit.value = false;
    store.updateUserData(data.user);
    await nextTick();
    location.reload();
  };
</script>
<style lang="scss" scoped>
  .person-detail-box {
    width: 90%;
    :deep(.el-form-item__label){
      align-self: center;
      font-size: 16px;
      font-family: ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", Segoe UI Symbol, "Noto Color Emoji";;
    }
    .button-box{
      display: flex;
      justify-content: end;
    }
  }
</style>
