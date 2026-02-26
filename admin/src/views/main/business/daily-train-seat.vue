<template>
  <p>
    <a-space>
      <train-select-view v-model="params.trainCode" width="200px"></train-select-view>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
      <!-- 添加了新增按钮 -->
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainSeats"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <!-- 填充了操作列：编辑和删除 -->
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'col'">
        <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.col && item.type === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>

  <!-- 添加了新增/编辑的模态框弹窗 -->
  <a-modal v-model:visible="visible" title="每日座位" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrainSeat" :label-col="{span: 5}" :wrapper-col="{ span: 18 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrainSeat.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" style="width: 100%" />
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select-view v-model="dailyTrainSeat.trainCode"></train-select-view>
      </a-form-item>
      <a-form-item label="箱序">
        <a-input v-model:value="dailyTrainSeat.carriageIndex" />
      </a-form-item>
      <a-form-item label="排号">
        <a-input v-model:value="dailyTrainSeat.row" />
      </a-form-item>
      <a-form-item label="列号">
        <a-select v-model:value="dailyTrainSeat.col" placeholder="请选择列号">
          <a-select-option v-for="item in SEAT_COL_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="dailyTrainSeat.seatType" placeholder="请选择座位类型">
          <a-select-option v-for="item in SEAT_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="同车箱座序">
        <a-input v-model:value="dailyTrainSeat.carriageSeatIndex" />
      </a-form-item>
      <a-form-item label="售卖情况">
        <a-input v-model:value="dailyTrainSeat.sell" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectView from "@/components/train-select";

export default defineComponent({
  name: "daily-train-seat-view",
  components: {TrainSelectView},
  setup() {
    const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
    const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
    const visible = ref(false);
    let dailyTrainSeat = ref({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      carriageIndex: undefined,
      row: undefined,
      col: undefined,
      seatType: undefined,
      carriageSeatIndex: undefined,
      sell: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrainSeats = ref([]);
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      trainCode: null
    });
    const columns =[
      {
        title: '日期',
        dataIndex: 'date',
        key: 'date',
      },
      {
        title: '车次编号',
        dataIndex: 'trainCode',
        key: 'trainCode',
      },
      {
        title: '箱序',
        dataIndex: 'carriageIndex',
        key: 'carriageIndex',
      },
      {
        title: '排号',
        dataIndex: 'row',
        key: 'row',
      },
      {
        title: '列号',
        dataIndex: 'col',
        key: 'col',
      },
      {
        title: '座位类型',
        dataIndex: 'seatType',
        key: 'seatType',
      },
      {
        title: '同车箱座序',
        dataIndex: 'carriageSeatIndex',
        key: 'carriageSeatIndex',
      },
      {
        title: '售卖情况',
        dataIndex: 'sell',
        key: 'sell',
      },
      // 注意这里：新增了操作列
      {
        title: '操作',
        dataIndex: 'operation',
        key: 'operation',
      }
    ];

    // ============ 下面是新增的 4 个方法 ============
    const onAdd = () => {
      dailyTrainSeat.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      // 如果 window.Tool 报错，可以使用 JSON.parse(JSON.stringify(record)) 替代
      dailyTrainSeat.value = window.Tool ? window.Tool.copy(record) : JSON.parse(JSON.stringify(record));
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/daily-train-seat/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/daily-train-seat/save", dailyTrainSeat.value).then((response) => {
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
    // ===========================================

    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/business/admin/daily-train-seat/query-list", {
        params: {
          page: param.page,
          size: param.size,
          trainCode: params.value.trainCode
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          dailyTrainSeats.value = data.content.list;
          pagination.value.current = param.page;
          pagination.value.total = data.content.total;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleTableChange = (page) => {
      pagination.value.pageSize = page.pageSize;
      handleQuery({
        page: page.current,
        size: page.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      SEAT_COL_ARRAY,
      SEAT_TYPE_ARRAY,
      dailyTrainSeat,
      visible,
      dailyTrainSeats,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      params,
      // 注意这里：导出这 4 个方法给 template 绑定事件
      onAdd,
      handleOk,
      onEdit,
      onDelete
    };
  },
});
</script>