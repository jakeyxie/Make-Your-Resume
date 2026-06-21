<template>
  <el-form-item label="字体颜色:">
    <color-picker v-model:rgba="modelItem.style.textColor"></color-picker>
  </el-form-item>
  <el-form-item label="字体大小:">
    <el-select v-model="modelItem.style.textFontSize" class="m-2" placeholder="Select">
      <el-option v-for="(item, index) in fontSizeList" :key="index" :label="item" :value="item" />
    </el-select>
  </el-form-item>
  <el-form-item label="字体粗细:">
    <el-select v-model="modelItem.style.textFontWeight" class="m-2" placeholder="Select">
      <el-option v-for="(item, index) in fontWeightList" :key="index" :label="item" :value="item" />
    </el-select>
  </el-form-item>
  <el-form-item label="上外边距:">
    <el-input-number v-model="mTop" :min="-100" :max="100" @change="handleChange" />
  </el-form-item>
  <el-form-item label="下外边距:">
    <el-input-number v-model="mBottom" :min="-100" :max="100" @change="handleChangeMBottom" />
  </el-form-item>
</template>
<script setup>
  import { useResumeJsonStore } from '@/store/resume';
  import { reactive, ref } from 'vue';
  import { pxTonumber } from '@/utils/common';
  import { useModelIndex } from '@/hooks/useModelIndex';
  import { useFontSizeList } from '@/hooks/useFontSizeList';
  // store
  const { resumeJsonStore } = useResumeJsonStore();
  // 选中的模块
  const index = useModelIndex(); // 选中的索引
  const modelItem = reactive(resumeJsonStore.LIST[index]);
  // 字体大小
  const fontSizeList = useFontSizeList();
  // 字体粗细
  const fontWeightList = reactive([100, 200, 300, 400, 500, 600, 700, 800, 900]);

  // 上外边距
  const defaultMtop = pxTonumber(modelItem.style.mTop);
  const mTop = ref(defaultMtop);
  const handleChange = (value) => {
    modelItem.style.mTop = value + 'px';
  };

  // 下外边距
  const defaultMbottom = pxTonumber(modelItem.style.mBottom);
  const mBottom = ref(defaultMbottom);
  const handleChangeMBottom = (value) => {
    modelItem.style.mBottom = value + 'px';
  };
</script>
