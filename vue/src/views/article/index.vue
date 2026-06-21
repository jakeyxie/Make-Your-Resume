<template>
  <nav-bar></nav-bar>
  <div class="article-box">
    <div class="article-carousel">
<!--      轮播图   -->
      <el-carousel>
        <el-carousel-item v-for="item in data.introductionData.slice(0,4)" :key="item">
          <img :src="item.img" alt="" style="cursor: pointer; height: 350px; width: 100%" @click="navTo('/article/articleDetail?id=' + item.id)">
        </el-carousel-item>
      </el-carousel>
    </div>

    <div class="article-body" >
      <div class="template-title">
        <div><span>———</span>优质<span style="color:#5b3df4;">求职攻略,</span>随时一键查看<span>———</span></div>
      </div>
      <!--  模板导航  -->
      <div class="template-select">
        <div class="template-style">
          <div style="margin-right: 20px; margin-left: 20px; font-size: 18px; font-weight: bold">
            攻略分类:
          </div>
          <el-button style="text-align: center; border-radius: 15px; height: 30px; " :class="{ 'highlight-button': data.activeSort === item.sort }"
                     v-for="(item,index) in data.categoryData" :key="index" @click="styleSort(item)"> {{item.title}} </el-button>
        </div>
        <!--  搜索框    -->
        <div class="search" >
          <el-icon style="margin-left: 10px; margin-right: 0"><Search /></el-icon>
          <input v-model="data.title" @keyup.enter="load" class="search-input">
        </div>
      </div>

      <div style="margin-top: 20px; display: flex; grid-gap: 20px" v-for="item in data.introductionData">
        <div style="flex: 1">
          <img @click="navTo('/article/articleDetail?id=' + item.id)" :src="item.img" alt="" style="width: 100%; height: 220px; display: block; border-radius: 5px; cursor: pointer">
        </div>
        <div style="flex: 3">
          <div style="font-size: 20px; font-weight: bold; cursor: pointer" @click="navTo('/article/articleDetail?id=' + item.id)">{{ item.title }}</div>
          <div style="margin-top: 10px; font-size: 15px; color: #666666; line-height: 25px; height: 125px; text-align: justify" class="line5">{{ item.description }}</div>
          <div class="introduction-box" style="display: flex; align-items: center; margin-top: 10px; grid-gap: 10px">
            <img :src="item.userAvatar" alt="" style="width: 30px; height: 30px; border-radius: 50%">
            <div style="font-size: 15px">{{ item.username }}</div>
            <div style="color: #666666">{{ item.time }}</div>
            <div ><span style=" text-align: center; margin: 0 5px 0 5px;padding: 0 5px 0 5px;color: #446cff !important;font-size: 12px !important;width: 40px;line-height: 20px;background-color: #dce5ff;border: 1px solid;border-radius: 4px;">{{ item.categoryTitle }}</span></div>
          </div>
        </div>
      </div>
      <el-pagination
          style="justify-self: center"
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5, 10, 15, 20 ]"
          :total="data.total"
          @update:current-page="load"
          @update:page-size="load"
      />
    </div>
  </div>
  <footer-com></footer-com>
</template>
<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import NavBar from "@/components/NavBar/NavBar.vue";
import FooterCom from "@/components/FooterCom/FooterCom.vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('user_data') || "{}"),
  introductionData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  title: "",
  categoryData: [],
  activeSort: '', // 存储当前高亮的sort值
  style: 10,
})

const navTo = (url) => {
  location.href = url
}

const load = () => {
  request.get('/introduction/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
      categoryId: data.style
    }
  }).then(res => {
    console.log("res",res)
    if (res.code === '200') {
      data.introductionData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const loadCategory = () => {
  request.get('/category/selectAll').then(res => {
    if (res.code === '200') {
      data.categoryData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadCategory()

const styleSort = (value) =>{
  data.activeSort = value.sort;
  data.style = value.id
  load()
  console.log(data.style)
}
</script>
<style lang="scss">
.line5 {
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 5; /* 超出几行省略 */
  overflow: hidden;
}

.article-box {
  margin-top: 70px;
  background-color: #f6f9fc;
  min-height: 1000px;
  .article-carousel {
    width: 50%;
    justify-self: center;
  }
  .article-body {
    width: 70%;
    margin: 30px auto;

    .template-select{
      margin-top: 20px;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .template-style{
        white-space: nowrap;
        width: 70%;
        display: flex;
        align-items: center;
        justify-content: flex-start;
      }
    }

    .template-title{
      justify-self: center;
      color: #221081;
      font-family: ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", Segoe UI Symbol, "Noto Color Emoji";
      font-weight: bold;
      font-size: 40px;
      span{
        color: black;
      }
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
  }
}



</style>