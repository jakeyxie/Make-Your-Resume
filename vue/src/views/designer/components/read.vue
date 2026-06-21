<template>
  <div class="design-box">
    <!-- 导航栏 -->
    <design-nav @generateReport="generateReport" @reset="reset"></design-nav>
    <!-- 内容区域 -->
    <div class="bottom">
      <!-- 左侧添加模块区域 -->
      <div class="left" ref="leftRef">
        <c-scrollbar
            trigger="hover"
            :hThumbStyle="{
            'background-color': 'rgba(0,0,0,0.4)'
          }"
        >
          <model-list :leftShowStatus="leftShowStatus" :key="UuidStore.refreshUuid"></model-list>
        </c-scrollbar>
      </div>

      <!--      key唯一标识符 通过uuid获取-->
      <!-- 预览区域 -->
      <div class="center" :key="UuidStore.refreshUuid">
        <div class="design" ref="html2Pdf">
          <!--      ref用于访问该元素-->
          <div class="design-content" ref="htmlContentPdf">
            <component :is="componentName" @contentHeightChange="contentHeightChange" />
          </div>

          <!-- 分页线 -->
          <template v-if="linesNumber > 0">
            <div
                class="lines"
                v-for="(item, index) in linesNumber"
                :key="index"
                :ref="(el) => setLinesRef(el, index)"
                :style="{ top: `${1128 + 1132 * index}px` }"
            >
              <p class="tips">如果分割线遮挡内容，请通过调整模块上下边距以显示内容！</p>
              <p class="page">{{ index + 1 }}/{{ linesNumber }}</p>
            </div>
          </template>
        </div>
      </div>
      <!--       属性设置面板 -->
      <div class="config" :key="UuidStore.refreshUuid">
        <Title :title="title"></Title>
        <component :is="useModel.optionsName" v-if="useModel.model" :key="useModel.id" />
        <!--         全局主题样式设置 -->
        <resume-theme v-else></resume-theme>
      </div>
    </div>
  </div>
</template>

<script setup>
import { nextTick, onBeforeUnmount, onBeforeUpdate, onMounted, ref} from "vue";
import DesignNav from './components/DesignNav.vue';
import Title from './components/Title.vue'
import ModelList from "@/views/designer/components/ModelList.vue";
import ResumeTheme from '@/components/ResumeTheme/ResumeTheme.vue';

import { CScrollbar } from 'c-scrollbar'; // 滚动条
import {useUuidStore} from "@/store/uuid.js";
import { storeToRefs } from 'pinia';
import {useResumeModelStore ,useResumeJsonStore} from "@/store/resume.js";
import {useRoute} from "vue-router";
import TEMPLATE_JSON from '@/schema/model';
import useAddStyle from "@/hooks/useAddStyle.js";
import {ElMessage} from "element-plus";
import downloadPDF from "@/utils/html2pdf.js";

const { title } = storeToRefs(useResumeModelStore());
const store = useResumeJsonStore();
const UuidStore = useUuidStore();

let { resumeJsonStore } = storeToRefs(useResumeJsonStore()); // store里的模板数据


// 重置数据方法
const resetStoreAndLocal = () => {
  TEMPLATE_JSON.ID = id;
  TEMPLATE_JSON.NAME = name;
  let resetObj = useAddStyle(TEMPLATE_JSON); // 初始数据
  store.changeResumeJsonData(resetObj); // 更改store的数据
};

// 获取本地数据,初始化store里面的简历数据
const localData = localStorage.getItem('resumeDraft');
const route = useRoute();

const { id, name } = route.query; // 模板id和模板名称
resumeJsonStore.value.ID = id;
resumeJsonStore.value.NAME = name;
const componentName = ref(name); // 模板名称,即渲染哪个模板

//  RefImpl Vue 3 的响应式引用类型

if (localData) {
  let localObj = JSON.parse(localData)[id];
  if (localObj) {
    store.changeResumeJsonData(localObj);
  } else {
    resetStoreAndLocal();
  }
} else {
  resetStoreAndLocal();
}

// 过滤掉模板2不需要的模块 resume_title
if (Number(id) === 2) {
  let List = [];
  resumeJsonStore.value.LIST.forEach((item) => {
    if (item.model === 'RESUME_TITLE') {
      item.show = false;
    }
    List.push(item);
  });
  resumeJsonStore.value.LIST = List;
}

// 生命周期函数
onMounted(async () => {
  resizeDOM();
  initClickListen();
});
onBeforeUnmount(() => {
  observer?.disconnect();
});
onBeforeUpdate(() => {
  lineRefs = [];
});

// 属性设置
const useModel = useResumeModelStore();
// 全局样式设置
const globalStyleSetting = () => {
  // 重置store选中模块
  useModel.$reset();
};

// 导出pdf
const html2Pdf = ref(null); // 获取元素节点
let lineRefs = []; // 分割线的ref
const setLinesRef = (el, index) => {
  if (el) {
    if (linesNumber.value === index + 1) {
      el.style.top = linesNumber.value * 1160 + 'px'; // 最后一条分割线出现在底部
    }
    lineRefs.push(el);
  }
};

// 重置数据
const reset = async () => {
  resetStoreAndLocal(); // 重置store数据
  globalStyleSetting(); // 重置选中模块
  // 删除本地该条数据
  let localData = localStorage.getItem('resumeDraft'); // 本地缓存数据
  if (localData) {
    let allData = JSON.parse(localData);
    if (Object.keys(allData).length > 1) {
      if (allData[id]) {
        delete allData[id]; // 删除该条数据
        localStorage.setItem('resumeDraft', JSON.stringify(allData));
      }
    } else {
      localStorage.removeItem('resumeDraft');
    }
  }
  ElMessage({
    message: '重置成功!',
    type: 'success',
    center: true
  });
  UuidStore.setUuid(); // 重新渲染左侧列表和右侧属性面板设置
  await nextTick();
  resizeDOM();
};

// 生成pdf方法
const generateReport = async () => {
  let temp = linesNumber.value;
  linesNumber.value = 0;
  useModel.$reset(); // 重置选中模块
  await nextTick();
  downloadPDF(html2Pdf.value, resumeJsonStore.value.TITLE, false, () => {
    linesNumber.value = temp;
  });
};

// 监听内容元素高度变化，绘制分割线
const htmlContentPdf = ref(null);
let observer = null;
let height = 0;
let linesNumber = ref(0);

// 监听htmlContentPdf的大小 调整整体预览大小
const resizeDOM = () => {
  observer = new ResizeObserver(async (entries) => {
    for (let entry of entries) {
      height = (entry.target).offsetHeight;
      linesNumber.value = Math.ceil(height / 1160); // 有几条分割线
      html2Pdf.value.style.height = 1160 * linesNumber.value + 'px'; // 整个简历的高度
      htmlContentPdf.value.style.height = html2Pdf.value.style.height;
    }
  });
  observer.observe(htmlContentPdf.value); // 监听元素
};

// 子组件内容高度发生变化---需要重新计算高度，触发resizeDOM
const contentHeightChange = (height) => {
  htmlContentPdf.value.style.height = height + 'px';
};

// 点击其它区域，取消模块选择，即取消模块选中效果
const initClickListen = () => {
  // 当监听到点击时，触发dealClick
  window.addEventListener('click', dealClick);
};
const dealClick = (e) => {
  const bool = getTargetNode(htmlContentPdf.value, e.target);
  if (bool) {
    globalStyleSetting();
  }
};
// 检查target是否处在ele中
const getTargetNode = (ele, target)=> {
  if (!ele || ele === document) return false;
  return ele === target ? true : getTargetNode(ele.parentNode, target);
};

// 展开或收起左侧栏
const leftRef = ref(null);
const leftShowStatus = ref(true);
const unfoldOrCollapse = (status) => {
  if (status) {
    setTimeout(() => {
      leftShowStatus.value = status;
    }, 100);
    leftRef.value.style.width = '300px';
  } else {
    setTimeout(() => {
      leftShowStatus.value = status;
    }, 100);
    leftRef.value.style.width = '70px';
  }
};
console.log("lineRefs",lineRefs)
</script>

