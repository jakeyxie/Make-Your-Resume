<template>
  <div class="template-select-box" ref="templateRef">
    <!--模版渲染表-->
    <div class="template-list">
      <my-template-card :cardData="templateData" @GotoTemplate="GotoTemplate" @toDesign="toDesign" @load="load"> </my-template-card>
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

const data = reactive({
  user: JSON.parse(localStorage.getItem('user_data') || "{}")
})
const templateData = reactive({
  pageSize: 12,
  pageNum: 1,
  total: 0,
  templateLists: templateList,
  templateKey: "",
  styleList: [],
  tableData: "",
  formVisible: false,
  items: {},
  templateStyle: "",
  activeSort: '', // 存储当前高亮的sort值
})


const load = () => {
  request.get('/userTemplate/selectPage', {
    params: {
      pageNum: templateData.pageNum,
      pageSize: templateData.pageSize,
      userId: data.user.id
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
load()
const GotoTemplate = (value) => {
  templateData.formVisible = true
  templateData.items = value
}
const toDesign = (value) => {
  // 将长文本存储在 localStorage 中
  localStorage.setItem('user_content', value.content);
  router.push({
    path: '/designer',
    query: {
      id: 1,
      name: "template1",
      sign: 1,
    }
  })
}

</script>

<style scoped>
.template-select-box {
  position: relative;
  justify-items: center;
  background-color: #ffffff;
  padding: 0 90px 0 90px;
}

.template-select {
  margin-top: 20px;
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.template-style {

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

.search {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin-left: 100px;
  width: 100px;
  background-color: #f6f6f6;
  border-radius: 30px;
  transition: all 0.3s ease;
}

.search:hover {
  margin-left: 0;
  width: 200px !important;
  background-color: #f6f6f6;
  transform: scale(1.2);
  transition: all 0.3s ease;
}

.search-input {
  height: 28px;
  width: 80px;
  background-color: #f6f6f6;
  border: 0;
  border-radius: 30px;
  outline: none;
  flex: 1;
}


.template-list {
  width: 100%;
  padding: 20px;
  display: flex;
  justify-content: center;
}

</style>