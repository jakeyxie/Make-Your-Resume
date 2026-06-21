<template>
  <div class="my-integral-box">
    待开发
  </div>
</template>
<script setup>
  import appStore from '@/store';

</script>
<style lang="scss" scoped>
  .my-integral-box {
    :deep(.el-tabs__content) {
      padding: 0;
    }
    .my-integral-info-box {
      padding-top: 20px;
      .integral-line {
        display: flex;
        align-items: center;
        span {
          margin-right: 10px;
        }
        p {
          padding: 3px 10px;
          text-align: center;
          font-size: 14px;
          border-radius: 5px;
          display: flex;
          align-items: center;
          cursor: pointer;
          -webkit-transition: all 0.2s;
          -moz-transition: all 0.2s;
          -ms-transition: all 0.2s;
          transition: all 0.2s;
          -webkit-user-select: none;
          -moz-user-select: none;
          -ms-user-select: none;
          user-select: none;
          img {
            margin-left: 10px;
          }
        }
        .get-bi-method {
          font-size: 12px;
          border: 1px solid burlywood;
          padding: 2px 5px;
          border-radius: 10px;
          cursor: pointer;
          color: burlywood;
          margin-left: 5px;
          margin-top: 2px;
          transition: all 0.3s;
          &:hover {
            opacity: 0.7;
          }
        }
      }
    }

    .integral-log-box {
      ul {
        li {
          display: flex;
          justify-content: space-between;
          align-items: center;
          font-size: 14px;
          color: #333;
          height: 50px;
          border-bottom: 1px solid #eee;
          .add-integral {
            color: green;
            letter-spacing: 2px;
          }
          .sub-integral {
            color: red;
            letter-spacing: 2px;
          }
          .integral-value {
            display: flex;
            align-items: center;
            img {
              width: 24px;
              margin-left: 5px;
            }
          }
        }
      }
    }
  }
</style>
