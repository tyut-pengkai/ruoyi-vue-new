<template>
  <div>
    <el-card style="margin: 10px; min-height: 85vh">
      <div slot="header">
        <span>网站设置</span>
      </div>
      <div>
        <el-form ref="form" :model="form" style="max-width: 1400px">
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="网站icon">
                <image-upload
                  :fileSize="0.5"
                  :limit="1"
                  @input="handleFileFavicon"
                ></image-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <image-preview
                :src="form.favicon"
                height="146px"
                width="146px"
              ></image-preview>
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="网站logo">
                <image-upload
                  :fileSize="0.5"
                  :limit="1"
                  @input="handleFileLogo"
                ></image-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <image-preview
                :src="form.logo"
                height="146px"
                width="146px"
              ></image-preview>
            </el-col>
          </el-form-item>
          <el-form-item label="网站名称">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="网站简称">
            <el-input v-model="form.shortName"></el-input>
          </el-form-item>
          <el-form-item label="前台名称">
            <el-input v-model="form.shopName"></el-input>
          </el-form-item>
          <el-form-item label="是否开启前台">
            <el-radio-group v-model="form.enableFrontEnd">
              <el-radio
                v-for="dict in dict.type.sys_yes_no"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="网站域名">
            <el-input v-model="form.domain"></el-input>
          </el-form-item>
          <el-form-item label="联系方式">
            <el-input v-model="form.contact"></el-input>
          </el-form-item>
          <el-form-item label="关键字">
            <el-input v-model="form.keywords"></el-input>
          </el-form-item>
          <el-form-item label="网站描述">
            <el-input v-model="form.description"></el-input>
          </el-form-item>
          <el-form-item label="网站备案号">
            <el-input v-model="form.icp"></el-input>
          </el-form-item>
          <!-- <el-form-item label="网站状态">
              <el-radio-group v-model="form.status">
              <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}</el-radio
              >
              </el-radio-group>
          </el-form-item> -->
          <el-form-item label="自定义后台登录入口，只支持字母数字">
            <span>
              <el-tooltip
                content="修改后台登录入口后，您只能通过此地址 [ http(s)://网站域名/login/设置的内容 ] 来登录您的验证管理后台，http到/login/无需输入，只输入/login/后的内容即可，输入内容只支持字母数字"
                placement="top"
              >
                <i
                  class="el-icon-question"
                  style="margin-left: -12px; margin-right: 10px"
                ></i>
              </el-tooltip>
            </span>
            <el-input
              v-model="form.safeEntrance"
              placeholder="修改后台登录入口后，您只能通过此地址 [ http(s)://网站域名/login/设置的内容 ] 来登录您的验证管理后台，http到/login/无需输入，只输入/login/后的内容即可，输入内容只支持字母数字"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm">确 定</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script>
import ImageUpload from "@/components/ImageUpload";
import ImagePreview from "@/components/ImagePreview";
import {getWebsiteConfig, updateWebsiteConfig} from "@/api/system/website";

export default {
  name: "Website",
  components: {ImageUpload, ImagePreview},
  dicts: ["sys_normal_disable", "sys_yes_no"],
  data() {
    return {
      form: {
        favicon: "",
        logo: "",
        name: "",
        shortName: "",
        domain: "",
        contact: "",
        keywords: "",
        description: "",
        safeEntrance: "",
      },
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      getWebsiteConfig().then((response) => {
        this.form = response.data;
        this.form.favicon = response.data.favicon;
        this.form.logo = response.data.logo;
      });
    },
    handleFileFavicon(file) {
      this.form.favicon = file;
    },
    handleFileLogo(file) {
      this.form.logo = file;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          updateWebsiteConfig(this.form).then((response) => {
            this.$modal.msgSuccess("修改成功");
          });
        }
      });
    },
  },
};
</script>
