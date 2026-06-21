<template>
  <div class="nav-box">
    <div class="nav-left">
      <div class="leftArrow" @click="toHome"><el-icon><ArrowLeft /></el-icon></div>
      <div class="logo" title="logo" ></div>
      <span @click="toHome">简才网</span>
    </div>

    <div class="nav-center">
      <p v-show="!data.isShowIpt">
        {{resumeJsonStore.TITLE}}
        <el-icon :size="20" color="#409eff" @click="changeTitle">
          <Edit />
        </el-icon>
      </p>
      <el-input
        class="title-input"
        ref="titleInput"
        v-show="data.isShowIpt"
        v-model="resumeJsonStore.TITLE"
        autofocus
        placeholder="请输入标题"
        @blur="blurTitle"
      />
    </div>
    <div class="nav-right">
      <el-tooltip class="box-item" effect="dark" content="保存到个人中心" placement="bottom">
        <div class="icon-box" @click="handleEdit">
          <svg-icon iconName="icon-xiangmu" color="#fff" size="17px"></svg-icon>
        </div>
      </el-tooltip>
      <el-tooltip class="box-item" effect="dark" content="保存草稿" placement="bottom">
        <div class="icon-box" @click="saveDraft">
          <svg-icon iconName="icon-mti-wodecaogaoxiang" color="#fff" size="17px"></svg-icon>
        </div>
      </el-tooltip>
      <el-tooltip class="box-item" effect="dark" content="导出为PDF" placement="bottom">
        <div class="icon-box" @click="generateReport">
          <svg-icon iconName="icon-pdf" color="#fff" size="17px"></svg-icon>
        </div>
      </el-tooltip>
      <el-tooltip class="box-item" effect="dark" content="导出为JSON数据" placement="bottom">
        <div class="icon-box" @click="exportJSON">
          <svg-icon iconName="icon-yunduanxiazai" color="#fff" size="17px"></svg-icon>
        </div>
      </el-tooltip>
      <el-tooltip class="box-item" effect="dark" content="导入JSON数据" placement="bottom">
        <div class="icon-box" @click="importJson">
          <svg-icon iconName="icon-yunduanshangchuan" color="#fff" size="19px"></svg-icon>
        </div>
      </el-tooltip>
      <el-tooltip class="box-item" effect="dark" content="重置所有设置" placement="bottom">
        <div class="icon-box" @click="reset">
          <svg-icon iconName="icon-zhongzhi" color="#fff" size="17px"></svg-icon>
        </div>
      </el-tooltip>
    </div>
  </div>


  <TemplateImgDiglog
  :formVisible="data.formVisible"
  @close="closeTemplateDialog"
  >
  </TemplateImgDiglog>
  <!-- 上传json代码编辑器 -->
  <import-json-dialog
    :dialogVisible="dialogVisible"
    @close="closeJsonDialog"
  ></import-json-dialog>

</template>
<script  setup>
  import { ref, reactive, nextTick, watch  } from 'vue';
  import { useRouter } from "vue-router";
  import { storeToRefs } from 'pinia';
  import { useResumeJsonStore } from '@/store/resume';
  import { ElMessage, ElMessageBox } from "element-plus";
  import 'element-plus/es/components/message-box/style/index';
  import FileSaver from 'file-saver';
  import ImportJsonDialog from '@/components/ImportJsonDialog/ImportJsonDialog.vue';
  import { debounce } from 'lodash';
  import request from "@/utils/request.js";
  import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
  import templateList from "@/template/index.js";
  import TemplateImgDiglog from "@/views/designer/components/TemplateImgDiglog.vue";
  let { resumeJsonStore } = storeToRefs(useResumeJsonStore()); // store里的模板数据

  const emit = defineEmits(['generateReport', 'reset', 'saveDataToLocal']);
  const router = useRouter();
  //跳转到首页
  const toHome = () => {
    router.push({
      path: '/'
    });
  };
  //
  const data = reactive({
    isShowIpt: false,
    formVisible: false,
    form: {},
  })
  const Template = reactive({
    img: "",
    content: "",
    userId: null,
    resumeId: null,
  })
  const titleInput = ref(null)
  // 更改标题
  const changeTitle = () => {
    data.isShowIpt = true;
    nextTick(() => {
      titleInput.value.focus();
    });
  };
  // 失焦不显示
  const blurTitle = () => {
    data.isShowIpt = false;
  };
  // 保存数据到本地
  let draftTips = ref();
  const saveDataToLocal = () => {
    let key = resumeJsonStore.value.ID; // 当前模板的id
    let saveData = {}; // 需要保存的数据
    let localData = localStorage.getItem('resumeDraft'); // 本地缓存数据
    if (localData) {
      saveData = JSON.parse(localData);
      saveData[key] = resumeJsonStore.value;
    } else {
      saveData[key] = resumeJsonStore.value;
    }
    localStorage.setItem('resumeDraft', JSON.stringify(saveData));
    let date = new Date();
    let formattedDate = date.toLocaleString('sv-SE', { hour12: false }); // "YYYY-MM-DD HH:MM:SS" 格式
    draftTips.value = `已自动保存草稿  ${formattedDate}`;
    console.log(draftTips);
  };
  const handleFileSuccess = (res) => {
    templateList.img = res.data
  }
  //导入图标
  const handleEdit = () => {
    data.formVisible = true
    console.log(data.formVisible)
  }
  // 取消导入
  const closeTemplateDialog = () => {
    data.formVisible = false;
  };
  //保存到个人中心
  const saveToPerson = () => {
    Template.content = JSON.stringify(resumeJsonStore.value);
    let user = JSON.parse(localStorage.getItem('user_data') || '{}')
    Template.userId = user.id;
    request.post('/userTemplate/add', Template).then(res => {
      if (res.code === '200') {
        ElMessage.success('保存成功')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  };

  // 保存草稿
  const saveDraft = () => {
    saveDataToLocal();
    ElMessage({
      message: '保存草稿成功!',
      type: 'success',
      center: true
    });
  };

  // 自动保存草稿 debounce延迟操作
  const debounced = debounce(() => {
    saveDataToLocal();
  }, 1000);
  // 监听模版是否改变 自动保存
  watch(
    resumeJsonStore.value, // JSON数据发生变化，则保存草稿
    () => {
      debounced();
    },
    {
      deep: true
    }
  );

  // 导出JSON
  const exportJSON = () => {
    const data = JSON.stringify(resumeJsonStore.value);           //转为json字符串
    const blob = new Blob([data], { type: '' });   //blob是浏览器提供的文件处理对象
    FileSaver.saveAs(blob, resumeJsonStore.value.TITLE + '.json'); //FileSaver.saveAs 是 FileSaver.js 库中的一个方法，用于让浏览器触发文件下载。
  };

  // 导出pdf
  const generateReport = () => {
    emit('generateReport');
  };

  // 重置
  const reset = () => {
    ElMessageBox.confirm('此操作会重置简历至初始状态，是否继续?', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
      .then(() => {
        emit('reset');
        draftTips.value = ``;
      })
      .catch(() => {});
  };

  // 上传JSON弹窗
  const dialogVisible = ref(false);
  const importJson = () => {
    dialogVisible.value = true;
  };
  // 取消导入JSON
  const closeJsonDialog = () => {
    dialogVisible.value = false;
  };


</script>
<style lang="scss" scoped>
  .nav-box {
    height: 70px;
    width: 100%;
    background-color: #ffffff;
    position: sticky;
    top: 0;
    display: flex;
    box-shadow: rgba(0, 0, 0, 0.2) 0 0 10px;
    z-index: 20;
    .nav-left {
      width: 300px;
      display: flex;
      align-items: center;
      margin-left: 50px;
      .leftArrow{
        cursor: pointer;
        font-size: 20px;
      }
      .logo {
        width: 70px;
        height: 70px;
        background-image: url('@/assets/images/logo.png');
        background-size: 250%;
        background-position: center;
        z-index: 2;   /* 根据内容自动调整高度 */
        cursor: pointer;
        margin-left: 50px;
      }
      span {
        letter-spacing: 4px;
        font-size: 22px;
        font-weight: 600;
        font-family: ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", Segoe UI Symbol, "Noto Color Emoji";
        cursor: pointer;
      }
    }
    .nav-center {
      flex: 1;
      display: flex;
      justify-content: center;
      align-items: center;
      position: relative;
      .title-input :deep(.el-input__inner){
        padding-left: 18px;
        background: #f6f6f6;
        width: 20%;
        border-radius: 20px;
        max-width: 140px;  /* 最大宽度 140px */
        min-width: 100px;  /* 最小宽度 100px */
      }
      .template-title {
        position: absolute;
        top: 50%;
        left: 0;
        transform: translate(0, -50%);
        font-size: 10px;
        color: #999999;
      }
      p {
        display: flex;
        align-items: center;
        font-size: 16px;
        .el-icon {
          margin-left: 10px;
          cursor: pointer;
        }
      }
    }
    .nav-right {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      padding-right: 10px;
      .icon-box {
        width: 45px;
        height: 45px;
        background-color: #409EFF;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        margin-right: 15px;
        transition: all 0.3s;
        &:hover {
          transform: translateY(-5px) scale(1.2);
          background-color: rgb(121.3, 187.1, 255);
        }
      }
    }
  }
</style>
