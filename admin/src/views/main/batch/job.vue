<template>
  <div class="job-container">
    <a-card :bordered="false" class="job-card">
      <!-- 顶部工具栏 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
              <span class="table-page-search-submitButtons">
                <a-button type="primary" @click="handleAdd">
                  <template #icon><PlusOutlined /></template>
                  新增任务
                </a-button>
                <a-button style="margin-left: 8px" @click="handleQuery">
                  <template #icon><ReloadOutlined /></template>
                  刷新
                </a-button>
              </span>
        </a-form>
      </div>

      <!-- 表格区域 -->
      <a-table
          :dataSource="jobs"
          :columns="columns"
          :loading="loading"
          rowKey="name"
          :pagination="{ pageSize: 10 }"
          style="margin-top: 20px"
      >
        <!-- 状态列美化 -->
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'state'">
            <a-tag :color="getStateColor(record.state)">
              {{ record.state }}
            </a-tag>
          </template>

          <!-- 操作列美化 -->
          <template v-if="column.dataIndex === 'operation'">
            <div class="operation-buttons">
              <!-- 手动执行 -->
              <a-popconfirm
                  title="确定要立即执行一次吗？"
                  ok-text="执行"
                  cancel-text="取消"
                  @confirm="handleRun(record)"
              >
                <a-tooltip title="立即执行一次">
                  <a-button type="link" size="small">
                    <template #icon><PlayCircleOutlined /></template>
                  </a-button>
                </a-tooltip>
              </a-popconfirm>

              <a-divider type="vertical" />

              <!-- 暂停/恢复 (互斥显示) -->
              <a-popconfirm
                  v-if="record.state === 'PAUSED' || record.state === 'ERROR'"
                  title="确定恢复任务？"
                  ok-text="恢复"
                  cancel-text="取消"
                  @confirm="handleResume(record)"
              >
                <a-tooltip title="恢复任务">
                  <a-button type="link" size="small" style="color: #52c41a">
                    <template #icon><CheckCircleOutlined /></template>
                  </a-button>
                </a-tooltip>
              </a-popconfirm>

              <a-popconfirm
                  v-else
                  title="确定暂停任务？"
                  ok-text="暂停"
                  cancel-text="取消"
                  @confirm="handlePause(record)"
              >
                <a-tooltip title="暂停任务">
                  <a-button type="link" size="small" style="color: #faad14">
                    <template #icon><PauseCircleOutlined /></template>
                  </a-button>
                </a-tooltip>
              </a-popconfirm>

              <a-divider type="vertical" />

              <!-- 编辑 -->
              <a-tooltip title="编辑配置">
                <a-button type="link" size="small" @click="handleEdit(record)">
                  <template #icon><EditOutlined /></template>
                </a-button>
              </a-tooltip>

              <a-divider type="vertical" />

              <!-- 删除 -->
              <a-popconfirm
                  title="删除后不可恢复，确认删除?"
                  ok-text="删除"
                  cancel-text="取消"
                  @confirm="handleDelete(record)"
              >
                <a-tooltip title="删除任务">
                  <a-button type="link" size="small" danger>
                    <template #icon><DeleteOutlined /></template>
                  </a-button>
                </a-tooltip>
              </a-popconfirm>
            </div>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑 模态框 -->
    <a-modal
        :title="job.state ? '编辑任务 (将会重置触发器)' : '新增任务'"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
        width="600px"
    >
      <a-form
          ref="formRef"
          :model="job"
          :rules="rules"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="任务类名" name="name">
          <a-input v-model:value="job.name" placeholder="请输入完整的类路径 (Package.Class)" :disabled="!!job.state" />
        </a-form-item>

        <a-form-item label="任务分组" name="group">
          <a-input v-model:value="job.group" placeholder="默认 DEFAULT" :disabled="!!job.state"/>
        </a-form-item>

        <a-form-item label="Cron表达式" name="cronExpression">
          <a-input v-model:value="job.cronExpression" placeholder="例如: 0/5 * * * * ?" />
          <div style="margin-top: 8px">
            <a-alert type="info" show-icon message="常用示例">
              <template #description>
                <span style="font-size: 12px; color: #666">
                  每5秒: 0/5 * * * * ? <br/>
                  每5分钟: 0 0/5 * * * ? <br/>
                  每天凌晨1点: 0 0 1 * * ?
                </span>
              </template>
            </a-alert>
          </div>
        </a-form-item>

        <a-form-item label="任务描述" name="description">
          <a-textarea v-model:value="job.description" :rows="3" placeholder="请输入任务描述" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import { notification, message } from 'ant-design-vue';
import {
  PlusOutlined,
  ReloadOutlined,
  PlayCircleOutlined,
  PauseCircleOutlined,
  CheckCircleOutlined,
  EditOutlined,
  DeleteOutlined
} from '@ant-design/icons-vue';

export default defineComponent({
  name: 'BatchJobView',
  components: {
    PlusOutlined,
    ReloadOutlined,
    PlayCircleOutlined,
    PauseCircleOutlined,
    CheckCircleOutlined,
    EditOutlined,
    DeleteOutlined
  },
  setup () {
    const jobs = ref([]);
    const loading = ref(false);
    const formRef = ref();

    const columns = [
      { title: '分组', dataIndex: 'group', width: 100 },
      { title: '类名', dataIndex: 'name', width: 200, ellipsis: true },
      { title: '描述', dataIndex: 'description', ellipsis: true },
      { title: '状态', dataIndex: 'state', width: 100, align: 'center' },
      { title: 'Cron表达式', dataIndex: 'cronExpression', width: 150 },
      { title: '上次执行', dataIndex: 'preFireTime', width: 170 },
      { title: '下次执行', dataIndex: 'nextFireTime', width: 170 },
      { title: '操作', dataIndex: 'operation', width: 220, align: 'center', fixed: 'right' }
    ];

    // 表单校验规则
    const rules = {
      name: [{ required: true, message: '请输入任务类名', trigger: 'blur' }],
      group: [{ required: true, message: '请输入分组', trigger: 'blur' }],
      cronExpression: [{ required: true, message: '请输入Cron表达式', trigger: 'blur' }]
    };

    /**
     * 查询列表
     */
    const handleQuery = () => {
      loading.value = true;
      jobs.value = [];
      axios.get('/batch/admin/job/query').then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          jobs.value = data.content;
        } else {
          notification.error({ message: '查询失败', description: data.message });
        }
      });
    };

    // -------- 表单相关 ---------
    const job = ref({}); // 初始化为空对象
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    /**
     * 模态框确认
     */
    const handleModalOk = () => {
      // 触发表单验证
      formRef.value.validate().then(() => {
        modalLoading.value = true;
        let url = "add";
        // 如果有状态，说明是编辑（Reschedule），否则是新增
        if (job.value.state) {
          url = "reschedule";
        }

        axios.post('/batch/admin/job/' + url, job.value).then((response) => {
          modalLoading.value = false;
          const data = response.data;
          if (data.success) {
            modalVisible.value = false;
            message.success("保存成功");
            handleQuery();
          } else {
            notification.error({ message: '保存失败', description: data.message });
          }
        });
      }).catch(() => {
        // 校验失败
      });
    };

    /**
     * 新增按钮
     */
    const handleAdd = () => {
      modalVisible.value = true;
      job.value = {
        group: 'DEFAULT',
        state: null // 标记为新增
      };
    };

    /**
     * 编辑按钮
     */
    const handleEdit = (record) => {
      modalVisible.value = true;
      // 深拷贝，移除外部 Tool 依赖
      job.value = JSON.parse(JSON.stringify(record));
    };

    /**
     * 删除
     */
    const handleDelete = (record) => {
      axios.post('/batch/admin/job/delete', {
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("删除成功");
          handleQuery();
        } else {
          notification.error({ message: '删除失败', description: data.message });
        }
      });
    };

    /**
     * 暂停
     */
    const handlePause = (record) => {
      axios.post('/batch/admin/job/pause', {
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          message.warning("任务已暂停");
          handleQuery();
        } else {
          notification.error({ message: '暂停失败', description: data.message });
        }
      });
    };

    /**
     * 恢复 (注意：这里更正了接口，通常resume是/resume)
     */
    const handleResume = (record) => {
      // 修正：这里应该调用 /resume 接口，而不是 /reschedule
      // /reschedule 通常用于修改 Cron 表达式
      axios.post('/batch/admin/job/resume', {
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("任务已重启");
          handleQuery();
        } else {
          notification.error({ message: '重启失败', description: data.message });
        }
      });
    };

    /**
     * 手动执行
     */
    const handleRun = (record) => {
      axios.post('/batch/admin/job/run', record).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("指令已发送，任务正在后台执行");
        } else {
          notification.error({ message: '执行失败', description: data.message });
        }
      });
    };

    /**
     * 获取状态颜色
     */
    const getStateColor = (state) => {
      switch (state) {
        case 'NORMAL': return 'processing'; // 蓝色
        case 'PAUSED': return 'warning';    // 橙色
        case 'BLOCKED': return 'error';     // 红色
        case 'ERROR': return 'error';       // 红色
        case 'COMPLETE': return 'success';  // 绿色
        default: return 'default';
      }
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      columns,
      jobs,
      loading,
      formRef,
      rules,
      handleQuery,
      handleAdd,
      handleEdit,
      handleDelete,
      handleResume,
      handlePause,
      job,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleRun,
      getStateColor
    };
  }
})
</script>

<style scoped>
.job-container {
  padding: 24px;
  background-color: #f0f2f5; /* 灰色背景，衬托 Card */
  min-height: 100vh;
}

.job-card {
  border-radius: 4px;
}

/* 操作按钮区域样式 */
.operation-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  white-space: nowrap;
}

.operation-buttons .ant-btn {
  padding: 0 4px;
}

/* 覆盖 Ant Design 默认 Alert 的一些 margin */
.ant-alert {
  padding: 8px 15px;
}
</style>