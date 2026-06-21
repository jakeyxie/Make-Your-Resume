<template>
<div class="template-card-box">
  <div  class="template-card" >
    <el-space wrap class="card-space" :size="20">
      <div class="cards" @mouseenter="mouseenter(index)" @mouseleave="mouseleave(index)" @click="handleGotoTemplate(item)"
           v-for="(item, index) in cardData.tableData" :key="index">
        <img :src="getAssetsFile(item.preview)" alt="">
        <span style="color: rgb(38, 38, 38); font-size: 17px">{{item.name}}</span>
        <p >{{item.style}}</p>

        <!-- 遮罩层 -->
        <div class="mask-layer" v-show="data.hoveredCards[index]">
          <div class="preview-icon" title="预览" @click.stop="previewImg(item.preview)">
            <el-icon><View className="preview"/></el-icon>
          </div>
          <div class="design-button" @click="toDesign">立即免费制作</div>
        </div>
      </div>
    </el-space>

    <preview-image v-show="data.dialogVisible" @close="close">
      <img class="previewImg" :src="getAssetsFile(data.preview)" alt="" srcset="" />
    </preview-image>
    <el-dialog title="智能简历" v-model="localData.cardData.formVisible" width="500">
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handletoDesign(cardData.items)">确 认</el-button>
          <el-button @click="localData.cardData.formVisible = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>


    <div class="template-card" style="justify-self: center; margin-top: 10px">
      <el-pagination
          v-model:current-page="localData.cardData.pageNum"
          v-model:page-size="localData.cardData.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[12, 24, 36, 48 ]"
          :total="cardData.total"
          @update:current-page="handleLoad"
          @update:page-size="handleLoad"
      />
    </div>
  </div>
</div>

</template>

<script setup>
import { reactive } from 'vue';
// import { ITempList } from '@/template/type';
import { getAssetsFile } from '@/utils/common';
// import templateList from "@/template/index.js";
import PreviewImage from "@/components/PreviewImage/PreviewImage.vue";
import {throttle} from "lodash";

const data = reactive({
  hoveredCards: [],
  dialogVisible: false,
  preview:""
})

// 定义 props
const props = defineProps({
  cardData: {
    type: Object, // 这里的类型根据实际情况进行定义
    required: true
  },
  load: {  // 新增 load 函数声明
    type: Function,
    required: true
  },
  GotoTemplate: {
    type: Function,
    required: true
  },
  toDesign: {
    type: Function,
    required: true
  }
});
const localData = reactive({
  cardData:props.cardData
})
// 定义 emit 事件
const emit = defineEmits(['toDesign','GotoTemplate','load']);
const handleGotoTemplate = (value) =>{
  emit('GotoTemplate',value)
}
const handletoDesign = (value) =>{
  emit('toDesign',value)
}
const handleLoad = () =>{
  emit('load')
}
const previewImg = (item) =>{
  data.dialogVisible = true
  data.preview = item
}
const close = () => {
  data.dialogVisible = false
  data.hoveredCards = []
}
// eslint-disable-next-line vue/no-dupe-keys
const mouseenter = throttle((index) => {
  console.log(data.dialogVisible)
  if (data.dialogVisible) return;
  data.hoveredCards[index] = true;
}, 100); // 设置为 100 毫秒执行一次

const mouseleave = (index) => {
  console.log(data.dialogVisible)
  console.log(data.preview)
  if (data.dialogVisible) return;
  data.hoveredCards[index] = false;
};


</script>

<style lang="scss" scoped>
.template-card-box{
  width: 90%;
  position: relative;
}
.template-card{
  justify-items: center;
}
.card-space{
  display: grid;
  grid-template-columns: repeat(4, 260px); /* 四列 */
  gap: 20px; /* 卡片间距 */
  justify-content: start;
  img{
    width: 100%;
    height: 80%;
  }
}
.cards{
  width: 100%; /* 自适应网格列宽 */
  height: 400px;
  transition: 0.3s ease;
  text-align: center;
  position: relative;
  z-index: 1;
  p {
    margin: 0 5px 0 5px;
    padding: 0 5px 0 5px;
    color: #446cff !important;
    font-size: 12px !important;
    width: 60px;
    line-height: 20px;
    background-color: #dce5ff;
    border-width: 1px;
    border-style: solid;
    border-radius: 4px;
  }
}

.cards:hover {
  cursor: pointer;
  box-shadow: rgba(0, 0, 0, 0.2) 0 0 20px;
  transform: translateY(-10px);
  transition: all 0.3s ease;
}

/* 在 img 或 p 上悬浮时，触发 .cards 的悬浮效果 */
.cards img:hover,
.cards p:hover {
  cursor: pointer;
}


.mask-layer {
  height: 100%;
  width: 100%;
  position: absolute;
  left: 0;
  top: 0;
  background-color: rgba(0, 0, 0, 0.2);
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
  .design-button {
    width: 100px;
    height: 30px;
    font-size: 13px;
    background-color: #2cbd99;
    border-radius: 6px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #ffffff;
    transition: all 0.3s;
    &:hover {
      background-color: rgba(#42aa90, 0.7);
      transform: scale(1.2);
    }
  }

  .preview-icon {
    position: absolute;
    right: 15px;
    top: 15px;
    z-index: 15;
    width: 30px;
    height: 30px;
    font-size: 20px;
    color: white;;
    background-color: rgba(0, 0, 0, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;

    .preview {
      cursor: pointer;
      font-size: 20px;
    }
  }
}
.previewImg{
  height: 600px;
}
</style>