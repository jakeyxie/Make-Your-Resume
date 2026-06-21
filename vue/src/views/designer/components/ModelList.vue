<template>
  <div class="model-list-box">
    <div class="model-ul">
<!--   vue中的可拖拽表格list绑定数组拖拽会改变数组顺序，id标识符 ghost-class拖动透明 chosen-class被选中的   -->

      <draggable
        :list="resumeJsonStore.LIST"
        itemKey="id"
        ghost-class="ghost"
        chosen-class="chosenClass"
        animation="300"
        @start="onStart"
        @end="onEnd"
      >
        <template #item="{ element }">
          <div
            :class="[
              'model-list-item',
              { active: resumeModel.id === element.id },
              { 'collapse-center': !leftShowStatus }
            ]"
            @click="selectModel(element)"
          >
            <!-- 是否添加模块 -->
            <div class="left">
              <div :class="['icon-box', { 'collapse-size': !leftShowStatus }]">
<!--    effect主题 placement提示框位置 :content绑定的内容 disable当侧边栏展开式禁用          -->
                <el-tooltip class="box-item" placement="right" effect="dark" :content="element.title" :disabled="leftShowStatus">
                  <svg-icon
                    :iconName="element.iconfont"
                    className="icon"
                    size="24px"
                    :color="leftShowStatus ? '#303133' : '#409EFF'"
                  ></svg-icon>
                </el-tooltip>
              </div>

              <p v-show="leftShowStatus">{{ element.title }}</p>
            </div>

            <div class="right" v-show="leftShowStatus">
              <!-- 模块开关 -->
              <el-switch v-model="element.show" active-color="red" />
            </div>
          </div>
        </template>
      </draggable>
    </div>
  </div>
</template>
<script setup >
  import { useModelOptionsComName } from '@/hooks/useModelOptionsComName';
  import { useResumeModelStore, useResumeJsonStore } from '@/store/resume';
  // import { getUuid } from '@/utils/common';
  import draggable from 'vuedraggable';
  const props = defineProps({
    leftShowStatus: Boolean
  })
  // 列表数据
  const { resumeJsonStore } = useResumeJsonStore();
  // 选中模块
  const resumeModel = useResumeModelStore();
  const selectModel = (item) => {
    // 返回optionsName 模版名+模块
    let optionsName = useModelOptionsComName(`${resumeJsonStore.NAME}-${item.model}`);
    
    let updateData = {
      model: item.model,
      optionsName: optionsName,
      title: item.title,
      id: item.id
    };
    //更新模板
    resumeModel.setResumeModel(updateData);
  };

  // // 下移模块
  // const down = (index) => {
  //   if (index === resumeJsonStore.LIST.length - 1) {
  //     return;
  //   }
  //   console.log(index)
  //   let temp = resumeJsonStore.LIST[index];
  //   resumeJsonStore.LIST[index] = resumeJsonStore.LIST[index + 1];
  //   resumeJsonStore.LIST[index + 1] = temp;
  // };
  // // 上移模块
  // const up = (index) => {
  //   if (index === 0) {
  //     return;
  //   }
  //   let temp = resumeJsonStore.LIST[index];
  //   resumeJsonStore.LIST[index] = resumeJsonStore.LIST[index - 1];
  //   resumeJsonStore.LIST[index - 1] = temp;
  // };
  // 添加模块
  // const add = (index) => {
  //   let temp = JSON.parse(JSON.stringify(resumeJsonStore.LIST[index]));
  //   temp.id = getUuid();
  //   resumeJsonStore.LIST.splice(index, 0, temp);
  // };
  //拖拽开始的事件
  const onStart = () => {
    console.log('开始拖拽');
  };
  //拖拽结束的事件
  const onEnd = () => {
    console.log('结束拖拽');
  };
</script>
<style lang="scss" scoped>
  .model-list-box {
    padding: 0 1px 30px 0;
    .model-ul {
      width: 100%;
      display: flex;
      flex-direction: column;
      padding: 0;
      margin: 0;
      .icon-box {
        width: 36px;
        height: 36px;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px solid #c4c4c4;
        border-radius: 50%;
      }
      .collapse-size {
        transform: scale(1.2);
      }
      .model-list-item {
        height: 60px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        cursor: all-scroll;
        border: 1px solid transparent;
        padding: 0 12px;
        position: relative;
        &:hover {
          border: 1px dashed #606266;
        }
        &::after {
          content: '';
          width: 80%;
          height: 1px;
          background-color: #f4f4f4;
          position: absolute;
          bottom: 0;
          left: 50%;
          transform: translate(-50%, 0);
        }
        .left {
          display: flex;
          align-items: center;
          .box-item{
          }
          p {
            margin-left: 10px;
            padding: 0;
            display: inline-block;
            height: 64px;
            max-width: 64px;
            line-height: 64px;
            font-size: 14px;
            color: #666666;
            cursor: pointer;
            overflow: hidden;
            text-overflow: ellipsis;
            word-break: break-word;
            white-space: nowrap;
          }
        }
        .right {
          display: flex;
          align-items: center;
          :deep(.el-switch__core)  {
              transform: scale(1.1);
              border: none;
          }
          :deep(.el-switch.is-checked .el-switch__core){
            background-color: #409EFF;
          }
          .add-model-box {
            cursor: pointer;
            display: flex;
            align-items: center;
            margin-left: 10px;
          }
        }
      }

      .collapse-center {
        justify-content: center;
      }
      .active {
        background-color: rgba(227, 231, 234, 0.6);
      }
    }

  }

</style>
