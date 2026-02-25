<template>
  <div class="login-container">
    <div class="login-content">
      <!-- 头部 Logo 区 -->
      <div class="login-header">
        <rocket-two-tone two-tone-color="#1890ff" style="font-size: 40px" />
        <h1 class="login-title">售票系统</h1>
      </div>

      <!-- 表单区 -->
      <a-card class="login-card" :bordered="false">
        <a-form
            :model="loginForm"
            name="login-form"
            layout="vertical"
            autocomplete="off"
            @finish="onFinish"
        >
          <!-- 手机号 -->
          <a-form-item
              name="mobile"
              :rules="[{ required: true, message: '请输入手机号!', pattern: /^1\d{10}$/, message: '手机号格式不正确' }]"
          >
            <a-input
                v-model:value="loginForm.mobile"
                size="large"
                placeholder="请输入手机号"
            >
              <template #prefix>
                <mobile-outlined style="color: rgba(0,0,0,.25)" />
              </template>
            </a-input>
          </a-form-item>

          <!-- 验证码 -->
          <a-form-item
              name="code"
              :rules="[{ required: true, message: '请输入验证码!' }]"
          >
            <div class="code-group">
              <a-input
                  v-model:value="loginForm.code"
                  size="large"
                  placeholder="请输入验证码"
                  style="width: 60%; margin-right: 10px"
              >
                <template #prefix>
                  <safety-certificate-outlined style="color: rgba(0,0,0,.25)" />
                </template>
              </a-input>

              <!-- 发送验证码按钮 (带倒计时) -->
              <a-button
                  size="large"
                  style="width: 35%"
                  :disabled="!!countDown"
                  :loading="sendLoading"
                  @click="sendCode"
              >
                {{ countDown ? `${countDown}秒后重发` : '获取验证码' }}
              </a-button>
            </div>
          </a-form-item>

          <!-- 登录按钮 -->
          <a-form-item>
            <a-button
                type="primary"
                html-type="submit"
                block
                size="large"
                :loading="loginLoading"
                class="login-btn"
            >
              登 录
            </a-button>
          </a-form-item>

          <div class="login-footer">
            提示：默认测试手机号 13000000000
          </div>
        </a-form>
      </a-card>
    </div>
  </div>
</template>

<script>
import { defineComponent, reactive, ref } from 'vue';
import axios from 'axios';
import { notification } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import store from "@/store";
import { MobileOutlined, SafetyCertificateOutlined, RocketTwoTone } from '@ant-design/icons-vue';

export default defineComponent({
  name: "login-view",
  components: {
    MobileOutlined,
    SafetyCertificateOutlined,
    RocketTwoTone
  },
  setup() {
    const router = useRouter();
    const loginLoading = ref(false);
    const sendLoading = ref(false);
    const countDown = ref(0); // 倒计时数字

    const loginForm = reactive({
      mobile: '15778909087',
      code: '',
    });

    // 发送验证码
    const sendCode = () => {
      // 简单校验手机号是否为空
      if(!loginForm.mobile) {
        notification.error({ description: '请先输入手机号' });
        return;
      }

      sendLoading.value = true;
      axios.post("http://127.0.0.1:8000/member/member/send-code", {
        mobile: loginForm.mobile
      }).then(response => {
        let data = response.data;
        if (data.success) {
          notification.success({ description: '发送验证码成功！' });
          loginForm.code = "9527"; // 方便测试，自动回填

          // 启动倒计时
          countDown.value = 60;
          const timer = setInterval(() => {
            countDown.value--;
            if (countDown.value <= 0) {
              clearInterval(timer);
            }
          }, 1000);

        } else {
          notification.error({ description: data.message });
        }
      }).finally(() => {
        sendLoading.value = false;
      });
    };

    // 执行登录 (表单校验通过后触发)
    const onFinish = () => {
      loginLoading.value = true;
      axios.post("http://127.0.0.1:8000/member/member/login", loginForm).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({ description: '登录成功！' });
          router.push("/");
          store.commit("setMember", data.content);
        } else {
          notification.error({ description: data.message });
        }
      }).finally(() => {
        loginLoading.value = false;
      });
    };

    return {
      loginForm,
      sendCode,
      onFinish,
      loginLoading,
      sendLoading,
      countDown
    };
  },
});
</script>

<style scoped>
/* 全屏容器 */
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  /* 漂亮的渐变背景，你也可以换成 background-image: url(...) */
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  /* 或者用深色科技风： */
  /* background: linear-gradient(to right, #0f2027, #203a43, #2c5364); */
}

/* 登录内容区 */
.login-content {
  width: 400px;
  padding: 20px;
}

/* 头部样式 */
.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-title {
  margin-top: 10px;
  font-size: 28px;
  font-weight: bold;
  color: #1890ff; /* 品牌色 */
  letter-spacing: 2px;
}

.login-subtitle {
  color: #888;
  font-size: 14px;
  margin-top: 5px;
}

/* 卡片样式优化 */
.login-card {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08); /* 柔和阴影 */
  border-radius: 8px; /* 圆角 */
}

/* 验证码组合样式 */
.code-group {
  display: flex;
  justify-content: space-between;
}

/* 登录按钮样式 */
.login-btn {
  height: 40px;
  font-size: 16px;
  border-radius: 4px;
  background: linear-gradient(90deg, #1890ff 0%, #36cfc9 100%); /* 渐变按钮 */
  border: none;
  transition: all 0.3s;
}

.login-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.4);
}

.login-footer {
  text-align: center;
  color: #bbb;
  font-size: 12px;
  margin-top: -10px;
}
</style>