<template>
  <div class="passenger-container">
    <!-- 使用 Card 包裹页面，增加高级感 -->
    <a-card :bordered="false" class="main-card">

      <!-- 顶部工具栏 -->
      <div class="table-toolbar">
        <div class="toolbar-left">
          <span class="page-title">乘车人列表</span>
        </div>
        <div class="toolbar-right">
          <a-space>
            <a-button type="default" @click="handleQuery()">
              <template #icon><SyncOutlined /></template>
              刷新
            </a-button>
            <a-button type="primary" @click="onAdd">
              <template #icon><PlusOutlined /></template>
              新增乘车人
            </a-button>
          </a-space>
        </div>
      </div>

      <!-- 数据表格 -->
      <!-- row-key 必须指定，否则 Vue 会报 key 警告 -->
      <a-table
          :dataSource="passengers"
          :columns="columns"
          :pagination="pagination"
          @change="handleTableChange"
          :loading="loading"
          row-key="id"
          class="custom-table"
      >
        <template #bodyCell="{ column, record }">

          <!-- 旅客类型渲染为多彩标签 -->
          <template v-if="column.dataIndex === 'type'">
            <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
              <a-tag v-if="item.code === record.type" :color="getTypeColor(record.type)">
                {{item.desc}}
              </a-tag>
            </span>
          </template>

          <!-- 操作列美化 -->
          <template v-else-if="column.dataIndex === 'operation'">
            <a-space size="small">
              <a-button type="link" size="small" @click="onEdit(record)">
                <template #icon><EditOutlined /></template>
                编辑
              </a-button>

              <a-popconfirm
                  title="删除后不可恢复，确认删除?"
                  @confirm="onDelete(record)"
                  ok-text="确认"
                  cancel-text="取消"
              >
                <!-- 使用 danger 属性，文字和图标自动变红 -->
                <a-button type="link" danger size="small">
                  <template #icon><DeleteOutlined /></template>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 弹窗美化 -->
    <a-modal
        v-model:visible="visible"
        title="乘车人信息"
        @ok="handleOk"
        ok-text="保存"
        cancel-text="取消"
        destroyOnClose
        :width="500"
    >
      <a-form :model="passenger" :label-col="{span: 5}" :wrapper-col="{ span: 18 }">
        <a-form-item label="姓名" name="name">
          <a-input v-model:value="passenger.name" placeholder="请输入真实姓名" allow-clear />
        </a-form-item>

        <a-form-item label="身份证" name="idCard">
          <a-input v-model:value="passenger.idCard" placeholder="请输入18位身份证号码" allow-clear />
        </a-form-item>

        <a-form-item label="旅客类型" name="type">
          <a-select v-model:value="passenger.type" placeholder="请选择旅客类型" allow-clear>
            <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code" :value="item.code">
              {{item.desc}}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import { notification } from "ant-design-vue";
// 按需引入图标
import {
  SyncOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined
} from '@ant-design/icons-vue';
import axios from "axios";

export default defineComponent({
  name: "passenger-view",
  components: {
    SyncOutlined,
    PlusOutlined,
    EditOutlined,
    DeleteOutlined
  },
  setup() {
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;
    const visible = ref(false);
    let passenger = ref({
      id: undefined,
      memberId: undefined,
      name: undefined,
      idCard: undefined,
      type: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const passengers = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
      showSizeChanger: true, // 允许改变每页条数
      showTotal: total => `共 ${total} 条数据` // 显示总条数更直观
    });
    let loading = ref(false);

    const columns =[
      {
        title: '姓名',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '身份证',
        dataIndex: 'idCard',
        key: 'idCard',
      },
      {
        title: '旅客类型',
        dataIndex: 'type',
        key: 'type',
        align: 'center', // 标签居中比较好看
      },
      {
        title: '操作',
        dataIndex: 'operation',
        align: 'center', // 操作区居中
        width: '200px' // 固定操作列宽度
      }
    ];

    // 新增：给不同的旅客类型返回不同的 Antd 标签颜色
    const getTypeColor = (typeCode) => {
      // 假设你的 code 对应：1 成人，2 儿童，3 学生，4 伤残军人 (按 12306 的习惯)
      const colorMap = {
        '1': 'blue',     // 成人-蓝色
        '2': 'green',    // 儿童-绿色
        '3': 'orange',   // 学生-橙色
        '4': 'purple'    // 军人-紫色
      };
      // 如果没有匹配的，默认返回青色
      return colorMap[typeCode] || 'cyan';
    };

    const onAdd = () => {
      passenger.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      passenger.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/member/passenger/delete/" + record.id).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({description: "删除成功！"});
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleOk = () => {
      axios.post("/member/passenger/save", passenger.value).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({description: "保存成功！"});
          visible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/member/passenger/query-list", {
        params: {
          page: param.page,
          size: param.size
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          passengers.value = data.content.list;
          pagination.value.current = param.page;
          pagination.value.total = data.content.total;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleTableChange = (pagination) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      PASSENGER_TYPE_ARRAY,
      passenger,
      visible,
      passengers,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      getTypeColor // 暴露给模板使用
    };
  },
});
</script>

<style scoped>
/* 整个视图的背景容器 */
.passenger-container {
  padding: 16px; /* 让卡片和页面边缘留有呼吸感 */
  background-color: #f0f2f5; /* 经典的后台浅灰背景 */
  min-height: calc(100vh - 64px); /* 视你的 Header 高度而定 */
}

/* 主卡片样式 */
.main-card {
  border-radius: 8px; /* 圆角柔和 */
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.03); /* 微弱的投影 */
}

/* 顶部工具栏 Flex 布局 */
.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px; /* 与表格拉开距离 */
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0; /* 分割线 */
}

/* 左侧标题 */
.page-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f1f1f;
}

/* 表格样式微调 */
:deep(.custom-table .ant-table-thead  > tr > th) {
  background-color: #fafafa;
  font-weight: 600;
}
</style>