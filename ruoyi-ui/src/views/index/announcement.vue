<template>
  <div>
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span>系统公告</span>
<!--        <el-button style="float: right; padding: 3px 0" type="text">查看更多</el-button>-->
      </div>
      <div style="height: 150px; overflow:auto">
        <el-table
          :data="notice"
          :show-header="false"
          style="width: 100%"
          size="mini"
          row-class-name="table-row-pointer"
          @row-click="handleDetail">
          <el-table-column
            prop="noticeTitle"
            label="标题"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <el-tag size="mini" style="margin-right: 5px"><i class="el-icon-bell"></i>{{getNoticeType(scope.row)}}</el-tag>{{ scope.row.noticeTitle || '暂无消息' }}
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="发布时间"
            width="100"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              {{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}
            </template>
          </el-table-column>
<!--          <el-table-column-->
<!--            label="操作"-->
<!--            align="center"-->
<!--            width="50"-->
<!--            class-name="small-padding fixed-width"-->
<!--          >-->
<!--            <template slot-scope="scope">-->
<!--              <el-button-->
<!--                size="mini"-->
<!--                type="text"-->
<!--                icon="el-icon-edit"-->
<!--                @click="handleDetail(scope.row)"-->
<!--              >详情</el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
        </el-table>
      </div>
    </el-card>

    <!-- 添加或修改公告对话框 -->
    <el-dialog :title="row['noticeTitle'] || ''" :visible.sync="open" width="780px" append-to-body>
      <div class="ql-container ql-bubble">
        <div class="ql-editor">
          <div>
            <div v-if="row && row['noticeContent']">
              <span v-html="row['noticeContent']"></span>
            </div>
            <!-- <div v-else>暂无公告</div> -->
          </div>
        </div>
      </div>
      <el-divider><span style="color: #909399">发布时间：{{row['createTime']}}</span></el-divider>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancel">确 定</el-button>
      </div>
    </el-dialog>

  </div>

</template>

<script>
import { getNotice } from "@/api/system/index";
export default {
  name: "Announcement",
  data() {
    return {
      // 用户公告
      notice: [],
      open: false,
      row: {},
    }
  },
  methods: {
    getNotice,
    getNoticeType(row) {
      if(row.noticeType === '1') {
        return "系统"
      } else if(row.noticeType === '2') {
        return "代理"
      } else if(row.noticeType === '3') {
        return "官方"
      }
    },
    // 详情按钮
    handleDetail(row, column, event) {
      this.open = true;
      this.row = row;
    },
    // 取消按钮
    cancel() {
      this.open = false;
    },
  },
  created() {
    getNotice().then((res) => {
      this.notice = res.rows;
    });
  },
  mounted() {
    // 添加事件监听器
    document.addEventListener('DOMContentLoaded', function() {
      const rows = document.getElementsByClassName('table-row-pointer');
      for (let i = 0; i < rows.length; i++) {
        rows[i].addEventListener('mouseover', function() {
          this.style.cursor = 'pointer';
        });
        rows[i].addEventListener('mouseout', function() {
          this.style.cursor = 'default';
        });
      }
    });
  }

}


</script>

<style>
  /* 添加一个自定义类用于行 */
.table-row-pointer {
  cursor: pointer;
}
</style>
