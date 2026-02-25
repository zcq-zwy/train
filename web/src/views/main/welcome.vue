<template>
  <div class="welcome-container">
    <!-- 头部欢迎语 -->
    <div class="welcome-header">
      <h1>🚀 欢迎体验 12306 售票系统演示端</h1>
      <p class="subtitle">基于 Spring Cloud + Vue3 的高并发列车售票系统实战项目</p>
    </div>

    <a-row :gutter="24">
      <!-- 左侧：核心技术亮点 -->
      <a-col :span="24" :lg="14">
        <a-card class="box-card" title="🔥 核心技术架构 (10万人抢1000张票解决方案)">
          <template #extra>
            <a-tag color="orange">硬核实战</a-tag>
          </template>

          <div class="tech-list">
            <div class="tech-item" v-for="(item, index) in techPoints" :key="index">
              <div class="tech-icon">{{ index + 1 }}</div>
              <div class="tech-content">
                <div class="tech-title">
                  {{ item.title }}
                  <a-tag :color="item.color" class="tech-tag">{{ item.tag }}</a-tag>
                </div>
                <div class="tech-desc">{{ item.desc }}</div>
              </div>
            </div>
          </div>
        </a-card>
      </a-col>

      <!-- 右侧：功能引导与说明 -->
      <a-col :span="24" :lg="10">
        <!-- 功能引导卡片 -->
        <a-card class="box-card" title="💡 快速开始">
          <div class="action-area">
            <p>如果您还没有乘车人，请先添加：</p>
            <router-link to="/passenger">
              <a-button type="primary" ghost block class="mb-10">👤 添加乘车人</a-button>
            </router-link>

            <p>查询余票并进行选座购票：</p>
            <router-link to="/ticket">
              <a-button type="primary" block class="mb-10">🎫 查询余票 / 购票</a-button>
            </router-link>

            <p>查看订单与座位销售情况：</p>
            <div class="flex-btn">
              <router-link to="/my-ticket" style="flex: 1; margin-right: 10px">
                <a-button block>我的车票</a-button>
              </router-link>
              <router-link to="/seat" style="flex: 1">
                <a-button block>座位销售图</a-button>
              </router-link>
            </div>
          </div>
        </a-card>

        <!-- 说明卡片 -->
        <a-card class="box-card mt-20" title="📢 系统说明">
          <a-alert
              message="控台暂未开放"
              description="本项目还有一套控台管理模块（维护车次、调度任务等），为防止演示数据被误改，暂不开放体验。"
              type="warning"
              show-icon
              class="mb-10"
          />
          <a-alert
              message="演示提示"
              description="为了方便体验，系统未对“同人重复购票”做严格拦截，您可以重复购买同一车次。"
              type="info"
              show-icon
          />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: "welcome-view",
  setup() {
    const techPoints = ref([
      { title: '页面加速', tag: 'CDN', color: 'blue', desc: '利用内容分发网络，提高用户静态资源加载速度' },
      { title: '高性能查询', tag: 'Redis缓存', color: 'green', desc: '秒杀前提供高性能余票查询，解决缓存穿透、击穿、雪崩问题' },
      { title: '削峰填谷 (一)', tag: '前端验证码', color: 'cyan', desc: '纯前端削弱瞬时高峰，将100ms内的10万请求分散至1~2秒' },
      { title: '削峰填谷 (二)', tag: '后端验证码', color: 'cyan', desc: '进一步分散请求，同时识别并拦截机器人刷票' },
      { title: '请求拦截', tag: '限流降级', color: 'red', desc: '将90%无意义请求快速失败（直接告知无票），减轻系统压力' },
      { title: '流量控制', tag: '令牌桶算法', color: 'purple', desc: '控制抢票总量，开放2000令牌给1000张票，过滤无效流量' },
      { title: '数据一致性', tag: '分布式锁', color: 'orange', desc: '防止超卖现象，确保2000人抢1000张票最终只卖出1000张' },
      { title: '异步处理', tag: 'MQ排队', color: 'pink', desc: '异步削峰，最短时间反馈用户（排队中/无票），提升吞吐量' },
      { title: '最终一致性', tag: 'Seata/事务', color: 'blue', desc: '分布式事务保障，避免出现“扣了库存却没出票”的异常' },
    ]);

    return {
      techPoints
    };
  },
});
</script>

<style scoped>
.welcome-container {
  padding: 20px;
  background-color: #f0f2f5; /* 浅灰色背景，护眼且显层次 */
  min-height: 100vh;
}

.welcome-header {
  text-align: center;
  margin-bottom: 30px;
}

.welcome-header h1 {
  font-size: 28px;
  color: #1890ff;
  font-weight: 600;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 16px;
  color: #666;
}

/* 卡片通用样式 */
.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
  transition: all 0.3s;
}

.box-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 技术列表样式 */
.tech-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.tech-item {
  display: flex;
  align-items: flex-start;
  padding: 10px;
  background: #f9f9f9;
  border-radius: 6px;
  transition: background 0.3s;
}

.tech-item:hover {
  background: #e6f7ff;
}

.tech-icon {
  width: 24px;
  height: 24px;
  background: #1890ff;
  color: #fff;
  border-radius: 50%;
  text-align: center;
  line-height: 24px;
  font-weight: bold;
  font-size: 12px;
  margin-right: 12px;
  flex-shrink: 0;
  margin-top: 2px;
}

.tech-content {
  flex: 1;
}

.tech-title {
  font-weight: bold;
  font-size: 15px;
  color: #333;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
}

.tech-tag {
  margin-left: 8px;
  font-weight: normal;
  font-size: 12px;
}

.tech-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
}

/* 右侧操作区样式 */
.action-area p {
  margin-bottom: 8px;
  color: #555;
  font-weight: 500;
}

.mb-10 {
  margin-bottom: 15px;
}

.mt-20 {
  margin-top: 20px;
}

.flex-btn {
  display: flex;
  justify-content: space-between;
}
</style>