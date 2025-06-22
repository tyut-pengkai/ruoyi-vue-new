<template>
  <div>
    <template v-for="(valueItem, number) in values">
      <template v-for="item in matchedOptions(valueItem)">
        <template v-if="number < limit">
        <span
          v-if="(item.raw.listClass == 'default' || item.raw.listClass == '') && (item.raw.cssClass == '' || item.raw.cssClass == null)"
          :class="item.raw.cssClass"
        >{{ item.label + ' ' }}</span>
          <el-tag v-else
                  :disable-transitions="true"
                  :type="item.raw.listClass == 'primary' ? '' : item.raw.listClass"
                  :class="item.raw.cssClass"
          >
            {{ item.label + ' ' }}
          </el-tag>
        </template>
      </template>
    </template>
    <template v-if="unmatch && showValue">
      {{ unmatchArray | handleArray }}
    </template>
    <template v-if="values.length > limit">
      <el-tooltip class="item" effect="dark" :content="moreText" placement="top">
        <span>...</span>
      </el-tooltip>
    </template>
  </div>
</template>

<script>
export default {
  name: "DictTag",
  props: {
    options: {
      type: Array,
      default: null,
    },
    value: [Number, String, Array],
    // 最大显示数量
    limit: {
      type: Number,
      default: Number.MAX_VALUE,
    },
    // 当未找到匹配的数据时，显示value
    showValue: {
      type: Boolean,
      default: true,
    },
    separator: {
      type: String,
      default: ","
    }
  },
  data() {
    return {
      unmatchArray: [], // 记录未匹配的项
      valueMap: null // 字典Map集合
    }
  },
  computed: {
    values() {
      if (this.value === null || typeof this.value === 'undefined' || this.value === '') return []
      const result = Array.isArray(this.value) ?
        this.value.map(item => String(item)) :
        String(this.value).split(this.separator)
      this.valueMap = new Map(this.options
        .filter(option => typeof option.value !== 'undefined')
        .map(option => [String(option.value), option]))
      return result
    },
    unmatch() {
      const unmatchArray = []
      let unmatch = false

      if (this.value === null || typeof this.value === 'undefined' ||
        this.value === '' || this.options.length === 0) {
        return false
      }

      this.values.forEach(item => {
        if (!this.valueMap.has(item)) {
          if (unmatchArray.length < this.limit) {
            unmatchArray.push(item)
            unmatch = true
          }
        }
      })

      this.unmatchArray = unmatchArray
      return unmatch
    },
    moreText() {
      if (this.value === null || typeof this.value === 'undefined' ||
        this.value === '' || this.options.length === 0) {
        return ''
      }

      const matchText = this.values
        .map(value => this.valueMap.get(value))
        .filter(Boolean)
        .map(option => option.label)
        .join(' ')

      const unmatchText = this.values
        .filter(value => !this.valueMap.has(value))
        .join(' ')

      return `${matchText} ${unmatchText}`
    }
  },
  filters: {
    handleArray(array) {
      return array.length === 0 ? '' : array.join(' ')
    }
  },
  methods: {
    matchedOptions(valueItem) {
      const valueStr = String(valueItem)
      return this.options.filter(item => String(item.value) === valueStr)
    }
  }
}
</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
