<template>
  <div :class="['title', { 'collapse-center': !data.status }]">
    <h1 v-show="data.status">{{ title }}</h1>
    <el-tooltip class="box-item" effect="dark" :content="data.tooltipTitle" v-if="showCollapse">
      <div class="unfold-collapse-box" @click="unfoldOrCollapse" ref="switchRef">
        <svg-icon iconName="icon-shouqi" color="#409EFF" size="24px"></svg-icon>
      </div>
    </el-tooltip>
  </div>
</template>
<script setup >
  import { ref, reactive } from 'vue';

  const props = defineProps({
    title: {
      type: String,
      default: '模块选择'
    },
    showCollapse: Boolean
  })

  const emit = defineEmits(['unfoldOrCollapse']);
  const data = reactive({
    tooltipTitle: '收起',
    status: true, // 展开或收起
  })

  const switchRef = ref(null);
  const unfoldOrCollapse = () => {
    if (data.status) {
      data.tooltipTitle = '展开';
      data.status= false;
      // 对绑定的ref对象进行操作
      switchRef.value.style.transform = 'rotate(180deg)';
    } else {
      data.tooltipTitle = '收起';
      data.status = true;
      switchRef.value.style.transform = 'rotate(0deg)';
    }
    emit('unfoldOrCollapse', data.status);
  };
</script>
<style lang="scss" scoped>
  .title {

    height: 60px;
    width: 100%;
    display: flex;
    background-color: #fff;
    align-items: center;
    justify-content: space-between;
    position: sticky;
    top: 0;
    z-index: 10;
    padding: 0 12px;
    box-sizing: border-box;
    box-shadow: 0 5px 21px 0 rgb(78 78 78 / 8%);
    &::after {
      content: '';
      width: 100%;
      height: 1px;
      background-color: #eee;
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translate(-50%, 0);
    }

    h1 {
      font-size: 14px;
      color: #333333;
      font-weight: bold;
      letter-spacing: 3px;
      height: 60px;
      line-height: 60px;
    }
    .unfold-collapse-box {
      cursor: pointer;
      transition: all 0.3s;
      &:hover {
        opacity: 0.8;
      }
    }
  }
  .collapse-center {
    justify-content: center;
  }
</style>
