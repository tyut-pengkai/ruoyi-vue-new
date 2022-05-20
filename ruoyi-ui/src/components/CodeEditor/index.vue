<template>
  <div class="echart-pie-wrap">
    <!-- <div class="myEditorTop">{{ title }}</div> -->
    <editor
      ref="myEditor"
      :lang="language"
      :options="options"
      :value="value"
      theme="tomorrow_night_eighties"
      @init="editorInit"
    ></editor>
  </div>
</template>

<script>
import Editor from "vue2-ace-editor";
// 主题
import "brace/theme/tomorrow_night_eighties";
// import "brace/theme/monokai";
// import "brace/theme/chrome";
// import "brace/theme/tomorrow_night_blue";
// 代码片段
import "brace/snippets/javascript";
// import "brace/snippets/dockerfile";
import "brace/snippets/python";
import "brace/snippets/php";
// import "brace/snippets/html";
// import "brace/snippets/css";
// import "brace/snippets/ruby";
// 代码语言
import "brace/mode/javascript";
// import "brace/mode/dockerfile";
import "brace/mode/python";
import "brace/mode/php";
// import "brace/mode/html";
// import "brace/mode/ruby";
// import "brace/mode/css";
// 语言扩展
import "brace/ext/language_tools";
import "brace/ext/emmet";
import "brace/ext/beautify";
import "brace/ext/searchbox";
import "brace/ext/chromevox";
import "brace/ext/error_marker";
import "brace/ext/keybinding_menu";
import "brace/ext/linking";
import "brace/ext/modelist";
import "brace/ext/old_ie";
import "brace/ext/settings_menu";
import "brace/ext/spellcheck";
import "brace/ext/split";
import "brace/ext/static_highlight";
import "brace/ext/statusbar";
import "brace/ext/whitespace";
import "brace/ext/textarea";
import "brace/ext/themelist";

export default {
  components: {
    Editor,
  },

  props: {
    title: {type: String, default: "编辑器"},
    value: {
      type: String,
      default: "",
    },
    language: {type: String, default: "javascript"},
  },

  model: {
    prop: "value",
    event: "change",
  },
  data() {
    return {
      options: {
        enableBasicAutocompletion: true, // 启用基本自动完成
        enableSnippets: true, // 启用代码段
        enableLiveAutocompletion: true, // 启用实时自动完成
        printMarginColumn: 30,
        displayIndentGuides: false, // 显示参考线
        enableEmmet: true, // 启用Emmet
        tabSize: 6, // 标签大小
        fontSize: 14, // 设置字号
        useWorker: true, // 使用辅助对象
        showPrintMargin: false, //去除编辑器里的竖线
        enableMultiselect: true, //     选中多处
        readOnly: false, // 是否只读
        showFoldWidgets: true, // 显示折叠部件
        fadeFoldWidgets: true, // 淡入折叠部件
        wrap: true, //换行
      },
    };
  },
  methods: {
    editorInit: function (editor) {
      // console.log("debug log --> ", editor);
      // 窗口自适应
      // editor.resize();
      // 监听编辑器变化
      editor.getSession().on("change", (val) => {
        // console.log("debug log --> ", editor.getValue());

        this.$emit("change", editor.getValue());
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.echart-pie-wrap {
  width: 100%;
  height: 500px;

  .myEditorTop {
    height: 45px;
    display: flex;
    align-items: center;
    background-color: #333;
    padding-left: 50px;
    color: rgb(241, 238, 8);
    font-weight: bold;
  }
}
</style>
