<template>
  <div class="app-container open3rd-preview">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>文档中台预览</span>
      </div>
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="文件 ID" prop="fileId" :rules="[{ required: true, message: '请输入文件 ID', trigger: 'blur' }]">
          <el-input v-model="form.fileId" placeholder="请输入文件 ID（需匹配后端配置）" />
        </el-form-item>
        <el-form-item label="文件 Token">
          <el-input v-model="form.fileToken" placeholder="可选：用于区分用户权限" />
        </el-form-item>
        <el-form-item label="文档类型">
          <el-select v-model="form.officeType" clearable placeholder="自动识别或手动选择">
            <el-option v-for="item in officeTypes" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handlePreview">打开预览</el-button>
          <span class="tips">请确保后端 open3rd 配置中已有对应文件映射。</span>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="box-card preview-card">
      <div slot="header" class="clearfix">
        <span>预览窗口</span>
      </div>
      <div ref="previewContainer" class="preview-container"></div>
    </el-card>
  </div>
</template>

<script>
import { WebOfficeSdk } from '@/prod/web-office-sdk-v1.2.31.es.js'
import { getOpen3rdSignature } from '@/api/open3rd'

export default {
  name: 'Open3rdPreview',
  data() {
    return {
      form: {
        fileId: '',
        fileToken: '',
        officeType: ''
      },
      officeTypes: [
        { label: 'Word (doc/docx)', value: 'docx' },
        { label: 'Excel (xls/xlsx)', value: 'xlsx' },
        { label: 'PPT (ppt/pptx)', value: 'pptx' },
        { label: 'PDF', value: 'pdf' },
        { label: 'TXT', value: 'txt' }
      ],
      loading: false,
      instance: null
    }
  },
  methods: {
    async handlePreview() {
      this.$refs.form.validate(async valid => {
        if (!valid) {
          return
        }
        this.loading = true
        try {
          const { data } = await getOpen3rdSignature()
          if (!data || !data.appId || !data.signature) {
            this.$message.error('未获取到签名配置，请检查后端 open3rd 配置')
            return
          }
          this.createInstance(data.appId, data.signature)
        } catch (error) {
          this.$message.error('获取签名失败，请检查服务状态')
        } finally {
          this.loading = false
        }
      })
    },
    createInstance(appId, signature) {
      if (this.instance) {
        this.instance.destroy()
        this.instance = null
      }
      const container = this.$refs.previewContainer
      if (container) {
        container.innerHTML = ''
      }

      const sdk = new WebOfficeSdk({
        appId,
        mount: container,
        fileToken: this.form.fileToken || undefined
      })

      this.instance = sdk.openFileId({
        fileId: this.form.fileId,
        signature,
        fileToken: this.form.fileToken || undefined,
        officeType: this.form.officeType || undefined
      })
    }
  }
}
</script>

<style scoped>
.open3rd-preview .preview-card {
  margin-top: 20px;
}

.open3rd-preview .preview-container {
  min-height: 600px;
  border: 1px dashed #dcdfe6;
}

.open3rd-preview .tips {
  margin-left: 12px;
  color: #909399;
}
</style>
