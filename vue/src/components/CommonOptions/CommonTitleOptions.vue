<template>
  <el-form-item :label="`${colorLabel}:`">
    <color-picker v-model:rgba="modelItem.style.titleColor"></color-picker>
  </el-form-item>
  <el-form-item :label="`${fontSizeLabel}:`">
    <el-select v-model="modelItem.style.titleFontSize" class="m-2" placeholder="Select">
      <el-option v-for="(item, index) in fontSizeList" :key="index" :label="item" :value="item" />
    </el-select>
  </el-form-item>
  <el-form-item :label="`${fontWeightLabel}:`">
    <el-select v-model="modelItem.style.titleFontWeight" class="m-2" placeholder="Select">
      <el-option v-for="(item, index) in fontWeightList" :key="index" :label="item" :value="item" />
    </el-select>
  </el-form-item>
</template>
<script setup >
  import { useFontSizeList } from '@/hooks/useFontSizeList';
  import { useModelIndex } from '@/hooks/useModelIndex';
  import { useResumeJsonStore } from '@/store/resume';
  import { reactive } from 'vue';

  const props = (defineProps({
    colorLabel: {
      type: String,
      default: '标题字体颜色'
    },
    fontSizeLabel: {
      type: String,
      default: '标题字体大小'
    },
    fontWeightLabel: {
      type: String,
      default: '标题字体粗细'
    }
    })
  );
  // store
  const { resumeJsonStore } = useResumeJsonStore();
  // 选中的模块
  const index = useModelIndex(); // 选中的索引
  const modelItem = reactive(resumeJsonStore.LIST[index]);
  // 字体大小
  const fontSizeList = useFontSizeList();
  // 字体粗细
  const fontWeightList = reactive([100, 200, 300, 400, 500, 600, 700, 800, 900]);
</script>
