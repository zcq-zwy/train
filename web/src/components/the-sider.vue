<template>
  <a-layout-sider
      width="220"
      class="sider-wrapper"
      :trigger="null"
      collapsible
  >
    <!-- 顶部 Logo 区域 -->
    <div class="logo-box">
      <!-- 建议将这里的 src 换成你项目的真实 logo -->
      <img src="https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg" alt="logo" class="logo-img" />
      <h1 class="logo-text">12306 售票系统</h1>
    </div>

    <!-- 菜单区域 -->
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        class="custom-menu"
    >
      <a-menu-item key="/welcome">
        <template #icon><CoffeeOutlined /></template>
        <router-link to="/welcome">欢迎使用</router-link>
      </a-menu-item>

      <a-menu-item key="/passenger">
        <template #icon><UserOutlined /></template>
        <router-link to="/passenger">乘车人管理</router-link>
      </a-menu-item>

      <a-menu-item key="/ticket">
        <template #icon><BorderOuterOutlined /></template>
        <router-link to="/ticket">余票查询</router-link>
      </a-menu-item>

      <a-menu-item key="/my-ticket">
        <template #icon><IdcardOutlined /></template>
        <router-link to="/my-ticket">我的车票</router-link>
      </a-menu-item>

      <a-menu-item key="/seat">
        <template #icon><UsergroupAddOutlined /></template>
        <router-link to="/seat">座位销售图</router-link>
      </a-menu-item>

      <a-menu-item key="/admin">
        <template #icon><DesktopOutlined /></template>
        <router-link to="/admin">控台管理</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import { defineComponent, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
// 按需引入图标，这是 Vue 3 + Ant Design 的规范做法
import {
  CoffeeOutlined,
  UserOutlined,
  BorderOuterOutlined,
  IdcardOutlined,
  UsergroupAddOutlined,
  DesktopOutlined
} from '@ant-design/icons-vue';

export default defineComponent({
  name: "the-sider-view",
  components: {
    CoffeeOutlined,
    UserOutlined,
    BorderOuterOutlined,
    IdcardOutlined,
    UsergroupAddOutlined,
    DesktopOutlined
  },
  setup() {
    const route = useRoute();
    const selectedKeys = ref([]);

    // 监听当前路由路径，动态改变菜单的高亮状态
    watch(() => route.path, (newPath) => {
      selectedKeys.value = [newPath];
    }, { immediate: true });

    return {
      selectedKeys
    };
  },
});
</script>

<style scoped>
/* 侧边栏外层包裹，增加右侧阴影，让层次感更强 */
.sider-wrapper {
  background: #ffffff;
  box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05); /* 现代感淡阴影 */
  z-index: 10;
  height: 100vh; /* 假设撑满整个屏幕高度 */
  overflow-y: auto; /* 菜单过多时允许滚动 */
}

/* 隐藏自带的滚动条但保留滚动功能 (更加美观) */
.sider-wrapper::-webkit-scrollbar {
  width: 0;
}

/* Logo 区域样式 */
.logo-box {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  background: #ffffff;
  border-bottom: 1px solid #f0f0f0; /* 与下方菜单稍微隔开 */
  overflow: hidden;
  transition: all 0.3s;
}

.logo-img {
  width: 32px;
  height: 32px;
  margin-right: 10px;
}

.logo-text {
  margin-bottom: 0;
  font-size: 18px;
  font-weight: 600;
  color: #001529;
  white-space: nowrap;
}

/* 自定义菜单样式，去掉原生的右边框 */
.custom-menu {
  border-right: none;
  padding-top: 8px;
}

/* 美化 Router-link：去下划线，撑满点击区域 */
.custom-menu a {
  text-decoration: none;
  color: inherit;
  display: block; /* 让整个文字区域都能被点击 */
}

/* 菜单项悬浮和选中的圆角优化（如果当前版本的 AntD 不是圆角的话） */
:deep(.ant-menu-item) {
  border-radius: 8px; /* 增加圆角显得更柔和现代化 */
  margin: 4px 8px;    /* 左右留出一点空隙 */
  width: calc(100% - 16px);
}
</style>