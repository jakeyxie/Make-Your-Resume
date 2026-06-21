<template>
  <el-dialog
      :model-value="formVisible"
      width="60%"
      :show-close="false"
      :close-on-click-modal="false"
      append-to-body
      class="import-json"
      title="模板封面"
      destroy-on-close
  >
    <el-form ref="formRef" label-width="80px" style="padding: 20px 30px 10px 0">
      <el-form-item prop="img" label="攻略主图">
        <el-upload
            action="http://localhost:8080/files/upload"
            :headers="{ token: data.user.token }"
            :on-success="handleFileSuccess"
            list-type="picture"
        >
          <el-button type="primary">上传图片</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="saveToPerson">保 存</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, nextTick, watch  } from 'vue';
import { useRouter } from "vue-router";
import { storeToRefs } from 'pinia';
import { useResumeJsonStore } from '@/store/resume';
import { ElMessage, ElMessageBox } from "element-plus";
import 'element-plus/es/components/message-box/style/index';
import request from "@/utils/request.js";
import templateList from "@/template/index.js";
let { resumeJsonStore } = storeToRefs(useResumeJsonStore()); // store里的模板数据

const data = reactive({
  user: JSON.parse(localStorage.getItem('user_data') || '{}'),
})

const Template = reactive({
  title: "",
  img: "",
  content: "",
  userId: null,
  resumeId: null,
})
const props = defineProps({
  formVisible: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(['close']);
const router = useRouter();

//导入图标
const handleFileSuccess = (res) => {
  Template.img = res.data
}
// 取消
const close = () => {
  emit('close');
};
//保存到个人中心
const saveToPerson = () => {
  Template.content = JSON.stringify(resumeJsonStore.value);
  let user = JSON.parse(localStorage.getItem('user_data') || '{}')
  Template.userId = user.id;
  request.post('/userTemplate/add', Template).then(res => {
    if (res.code === '200') {
      ElMessage.success('保存成功')
      emit("close")
    } else {
      ElMessage.error(res.msg)
    }
  })
};
</script>
<style lang="scss">
.import-json {
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    .header-left {
      display: flex;
      flex-direction: column;
      height: 48px;
      justify-content: center;
      .title {
        font-size: 20px;
      }
      span {
        font-size: 12px;
        color: red;
        display: flex;
        align-items: center;
        .svg-icon {
          margin-right: 5px;
        }
      }
    }
    .header-right {
      display: flex;
      .icon-box {
        width: 35px;
        height: 35px;
        background-color: #74a274;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        margin-left: 15px;
        transition: all 0.3s;
        &:hover {
          background-color: rgba(0, 192, 145, 0.8);
        }
      }
    }
  }
  .el-dialog__header {
    padding: 20px;
  }
  .el-dialog__body {
    padding: 0;
  }
  .el-dialog__footer {
    padding: 20px;
  }
}

.cm-editor {
  height: 70vh;
}
.el-overlay-dialog {
  display: flex;
  justify-content: center;
  align-items: center;
  .el-dialog {
    padding: 0;
    margin: 0;
  }
}
</style>