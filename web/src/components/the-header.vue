<template>
  <a-layout-header class="header">
    <div class="header-content">
      <!-- 1. 左侧 Logo 区 -->
      <router-link to="/welcome" class="logo">
        <rocket-two-tone two-tone-color="#1890ff" style="font-size: 24px; margin-right: 8px;" />
        <span class="logo-text">WIKI 12306</span>
      </router-link>

      <!-- 2. 中间菜单区 -->
      <a-menu
          v-model:selectedKeys="selectedKeys"
          theme="dark"
          mode="horizontal"
          class="nav-menu"
      >
        <a-menu-item key="/welcome">
          <router-link to="/welcome">
            <coffee-outlined /> 欢迎
          </router-link>
        </a-menu-item>
        <a-menu-item key="/passenger">
          <router-link to="/passenger">
            <user-outlined /> 乘车人管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/ticket">
          <router-link to="/ticket">
            <search-outlined /> 余票查询
          </router-link>
        </a-menu-item>
        <a-menu-item key="/my-ticket">
          <router-link to="/my-ticket">
            <idcard-outlined /> 我的车票
          </router-link>
        </a-menu-item>
        <a-menu-item key="/seat">
          <router-link to="/seat">
            <appstore-outlined /> 座位销售图
          </router-link>
        </a-menu-item>
        <a-menu-item key="/admin">
          <router-link to="/admin">
            <desktop-outlined /> 控台管理
          </router-link>
        </a-menu-item>
      </a-menu>

      <!-- 3. 右侧用户信息区 -->
      <div class="user-action">
        <span v-if="!member.id" class="login-link">
           <router-link to="/login">登录 / 注册</router-link>
        </span>

        <a-dropdown v-else>
          <span class="user-profile">
            <span class="welcome-text">您好，{{ member.mobile }}</span>
            <a-avatar style="background-color: #1890ff">
              <template #icon><user-outlined /></template>
            </a-avatar>
          </span>
          <template #overlay>
            <a-menu>
              <a-menu-item key="logout" @click="onLogout">
                <logout-outlined /> 退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </div>
  </a-layout-header>
</template>

<script>
import { defineComponent, ref, watch, computed } from 'vue';
import store from "@/store";
import router from '@/router';
// 引入需要的图标
import {
  UserOutlined,
  CoffeeOutlined,
  SearchOutlined,
  IdcardOutlined,
  AppstoreOutlined,
  DesktopOutlined,
  LogoutOutlined,
  RocketTwoTone
} from '@ant-design/icons-vue';
import { notification } from "ant-design-vue";

export default defineComponent({
  name: "the-header-view",
  components: {
    UserOutlined,
    CoffeeOutlined,
    SearchOutlined,
    IdcardOutlined,
    AppstoreOutlined,
    DesktopOutlined,
    LogoutOutlined,
    RocketTwoTone
  },
  setup() {
    // ⭐️ 重点修复：使用 computed 确保 member 是响应式的
    // 如果不用 computed，你登录成功后 Header 不会变，除非刷新页面
    const member = computed(() => store.state.member || {});

    const selectedKeys = ref([]);

    // 监听路由变化，高亮对应的菜单
    watch(() => router.currentRoute.value.path, (newValue) => {
      selectedKeys.value = [newValue];
    }, { immediate: true });

    // 退出登录逻辑
    const onLogout = () => {
      // 1. 清空 Store
      store.commit("setMember", {});
      // 2. 提示
      notification.success({ description: "退出登录成功" });
      // 3. 跳转回登录页
      router.push("/login");
    };

    return {
      member,
      selectedKeys,
      onLogout
    };
  },
});
</script>

<style scoped>
.header {
  padding: 0 24px;
  background: #001529; /* 深色背景 */
  box-shadow: 0 2px 8px #f0f1f2;
  z-index: 10;
}

/* Flex 容器，实现左右布局 */
.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1400px; /* 限制最大宽度，防止在大屏上太散 */
  margin: 0 auto;
  height: 64px;
}

/* 1. Logo 样式 */
.logo {
  display: flex;
  align-items: center;
  color: white;
  text-decoration: none;
  font-size: 20px;
  font-weight: 600;
  min-width: 200px;
  transition: all 0.3s;
}
.logo:hover {
  opacity: 0.8;
}
.logo-text {
  letter-spacing: 1px;
}

/* 2. 菜单样式 */
.nav-menu {
  flex: 1; /* 占据中间剩余空间 */
  border-bottom: none;
  display: flex;
  justify-content: center; /* 菜单居中显示，如果想靠左改为 flex-start */
  background: transparent;
}

/* 3. 用户区样式 */
.user-action {
  min-width: 150px;
  display: flex;
  justify-content: flex-end;
  color: white;
}

.user-profile {
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 0 12px;
  border-radius: 4px;
  transition: background 0.3s;
}

.user-profile:hover {
  background: rgba(255, 255, 255, 0.1);
}

.welcome-text {
  margin-right: 10px;
  font-size: 14px;
}

.login-link a {
  color: rgba(255, 255, 255, 0.85);
}
.login-link a:hover {
  color: white;
}
</style>