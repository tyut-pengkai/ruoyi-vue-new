<template>
  <div>
    <span>
      <el-input-number
        v-model="num"
        controls-position="right"
        @change="handleChange"
        :min="1"
        style="width: 142px"
      >
      </el-input-number>
    </span>
    <span>
      <el-select
        v-model="unit"
        placeholder="单位"
        @change="handleChange"
        style="width: 75px; margin-left: 2px"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
    </span>
  </div>
</template>
<script>
export default {
  name: "DateDuration",
  data() {
    return {
      totalSeconds: 86400,
      num: null,
      options: [
        {
          value: "year",
          label: "年",
        },
        {
          value: "month",
          label: "月",
        },
        {
          value: "day",
          label: "日",
        },
        {
          value: "hour",
          label: "时",
        },
        {
          value: "minute",
          label: "分",
        },
        {
          value: "second",
          label: "秒",
        },
      ],
      unit: null,
    };
  },
  methods: {
    handleChange(value) {
      let multiple = null;
      switch (this.unit) {
        case "year":
          multiple = 12 * 30 * 24 * 60 * 60;
          break;
        case "month":
          multiple = 30 * 24 * 60 * 60;
          break;
        case "day":
          multiple = 24 * 60 * 60;
          break;
        case "hour":
          multiple = 60 * 60;
          break;
        case "minute":
          multiple = 60;
          break;
        case "second":
          multiple = 1;
          break;
      }
      if (this.num != undefined && this.num != null) {
        this.totalSeconds = this.num * multiple;
      } else {
        this.totalSeconds = null;
      }
      this.$emit("totalSeconds", this.totalSeconds);
    },
  },
};
</script>