<template>
  <div>
    <el-card style="margin: 10px; min-height: 85vh">
      <div slot="header">
        <span>批量操作</span>
      </div>
      <el-tabs type="border-card">
        <el-tab-pane label="按输入操作">
          <input-operation></input-operation>
        </el-tab-pane>
        <el-tab-pane label="按范围操作">
          <scope-operation></scope-operation>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import inputOperation from "./inputOperation";
import scopeOperation from "./scopeOperation";

export default {
  name: "batchOperation",
  components: {inputOperation, scopeOperation},
};
</script>
