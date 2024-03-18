<template>
  <span>
    <span>
      <el-input-number
        v-model="num"
        controls-position="right"
        @change="handleChange"
        @blur="handleChange"
        :min="min"
        style="width: 142px"
        :disabled="disabled"
      >
      </el-input-number>
    </span>
    <span>
      <el-select
        v-model="unit"
        placeholder="单位"
        @change="handleChange"
        @blur="handleChange"
        style="width: 75px; margin-left: 2px"
        :disabled="disabled"
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
  </span>
</template>
<script>
import {day, hour, minute, month, parseSeconds, second, year,} from "@/utils/my";

export default {
  name: "DateDuration",
  props: {
    seconds: {
      type: Number
    },
    min: {
      type: Number,
      default: 0
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  created: function () {
    this.initSelf();
  },
  watch: {
    seconds: function () {
      this.initSelf();
    },
  },
  data() {
    return {
      totalSeconds: undefined,
      num: undefined,
      unit: undefined,
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
          label: "天",
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
    };
  },
  methods: {
    handleChange(value) {
      let multiple = null;
      switch (this.unit) {
        case "year":
          multiple = year;
          break;
        case "month":
          multiple = month;
          break;
        case "day":
          multiple = day;
          break;
        case "hour":
          multiple = hour;
          break;
        case "minute":
          multiple = minute;
          break;
        case "second":
          multiple = second;
          break;
      }
      if (this.num != undefined && this.num != null && multiple != null) {
        if (this.num > 0) {
          this.totalSeconds = this.num * multiple;
        } else {
          this.totalSeconds = this.num;
        }
      } else {
        this.totalSeconds = null;
      }
      this.$emit("totalSeconds", this.totalSeconds);
    },
    initSelf() {
      if (this.seconds != null) {
        let parse = parseSeconds(this.seconds);
        this.num = parse[0];
        this.unit = parse[1];
        this.handleChange(this.num);
      } else {
        this.num = undefined;
        this.unit = undefined;
        this.totalSeconds = undefined;
      }
    },
  },
};
</script>
