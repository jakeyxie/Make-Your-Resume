<template>
  <div class="template-select-box" ref="templateRef">
<!--  模版标题  -->
    <div class="template-title">
      <div style="font-size: 30px">———专业模板一键套用———</div>
    </div>
<!--  模板导航  -->
    <div class="template-select">
      <div class="template-style">
        <p style="margin-right: 30px">
          <label style="margin-left: 30px; font-size: 18px; font-weight: bold">模板风格:</label>
        </p>
        <el-button style="text-align: center; border-radius: 15px; height: 30px; " :class="{ 'highlight-button': templateData.activeSort === item.sort }"
                   v-for="(item,index) in templateData.styleList" :key="index" @click="styleSort(item)"> {{item.sort}} </el-button>
      </div>
<!--  搜索框    -->
      <div class="search" >
        <el-icon style="margin-left: 10px; margin-right: 0"><Search /></el-icon>
        <input v-model="templateData.templateKey" @keyup.enter="load" class="search-input">
      </div>
    </div>

<!--模版渲染表-->
    <div class="template-list">
      <template-card :cardData="templateData" @GotoTemplate="GotoTemplate" @toDesign="toDesign" @load="load"> </template-card>
    </div>
  </div>

</template>

<script setup>
import {reactive, onMounted} from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import templateList from "@/template/index.js";
import TemplateCard from "@/views/index/components/TemplateCard.vue";
import {openAndCloseLoadingByTime} from "@/utils/common.js";

const templateData = reactive({
  pageSize: 12,
  pageNum: 1,
  total: 0,
  templateLists: templateList,
  templateKey: "",
  styleList: [],
  tableData: "",
  formVisible: false,
  items:{},
  templateStyle: "",
  activeSort: '', // 存储当前高亮的sort值
})

// const handleCreateResume = () => {
//   router.push({ path: '/Maker', query: { id: 123 } });
// }

const styleSort = (value) =>{
  templateData.activeSort = value.sort;
  templateData.templateStyle = value.sort
  load()
}

const load = async () =>{
  request.get('/template/selectPage', {
    params: {
      pageNum: templateData.pageNum,
      pageSize: templateData.pageSize,
      name: templateData.templateKey,
      style: templateData.templateStyle
    }
  }).then(res => {
    if (res.code === '200') {
      templateData.tableData = res.data.list
      templateData.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const GotoTemplate = (value) =>{
  templateData.formVisible = true
  templateData.items = value
}
const toDesign = (value) =>{
  openAndCloseLoadingByTime();
  const ans = JSON.parse(JSON.stringify(value))
  router.push({
    path: '/designer',
    query: {
      id: ans.templateListId,
      name: ans.name
    }
  })
}

onMounted(() => {
  request('/resumesort/selectAllSorts').then(res => {
    if(res.code === '200') {
      templateData.styleList = res.data
      console.log("成功")
      console.log(templateData.styleList.length)
      if (templateData.styleList.length > 0) {
        templateData.activeSort = templateData.styleList[0].sort;
      }
    } else {
      ElMessage.error(res.msg)
    }
  })

  load()
});
</script>

<style scoped>
.template-select-box {
  position: relative;
  justify-items: center;
  background-color: #ffffff;
  padding: 0 90px 0 90px;
}

.template-select{
  margin-top: 20px;
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.template-style{

  width: 70%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

/* 高亮样式 */
.highlight-button {
  background: #409EFF !important;
  border-color: #409EFF !important;
  color: white !important;
}

.search{
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin-left: 100px;
  width: 100px;
  background-color: #f6f6f6;
  border-radius: 30px;
  transition: all 0.3s ease;
}
.search:hover{
  margin-left: 0;
  width: 200px !important;
  background-color: #f6f6f6;
  transform: scale(1.2);
  transition: all 0.3s ease;
}
.search-input{
  height: 28px;
  width: 80px;
  background-color: #f6f6f6;
  border: 0;
  border-radius: 30px;
  outline:none;
  flex: 1;
}


.template-list{
  width: 100%;
  padding: 20px;
  display: flex;
  justify-content: center;
}

</style>