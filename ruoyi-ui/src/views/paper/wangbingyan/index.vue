<template>
  <div class="paper-container">
    <el-card class="info-card" style="margin-bottom: 20px;">
      <div slot="header" class="card-header">
        <span>个人基本信息</span>
      </div>
      <el-form :model="studentInfo" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名">
              <span>{{ studentInfo.name }}</span>
            </el-form-item>
            <el-form-item label="学号">
              <span>{{ studentInfo.studentId }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <span>{{ studentInfo.phone }}</span>
            </el-form-item>
            <el-form-item label="学院">
              <span>{{ studentInfo.college }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="邮箱">
              <span>{{ studentInfo.email }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="info-card" style="margin-bottom: 20px;">
      <div slot="header" class="card-header">
        <span>毕业论文基本信息</span>
      </div>
      <el-form :model="paperInfo" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="论文题目">
              <span>{{ paperInfo.paperTitle }}</span>
            </el-form-item>
            <el-form-item label="论文类型">
              <span>{{ paperInfo.paperType }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提交日期">
              <span>{{ paperInfo.submitDate }}</span>
            </el-form-item>
            <el-form-item label="论文状态">
              <el-tag type="success">{{ paperInfo.status }}</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="论文摘要">
              <p style="line-height: 1.6;">{{ paperInfo.paperAbstract }}</p>
            </el-form-item>
          </el-col>
        </el-row>
        <div style="text-align: right; margin-top: 10px;">
          <el-button type="primary" size="small">编辑论文信息</el-button>
          <el-button type="success" size="small" style="margin-left: 10px;">上传论文</el-button>
        </div>
      </el-form>
    </el-card>

    <el-card class="info-card">
      <div slot="header" class="card-header">
        <span>毕业论文成果详细介绍</span>
      </div>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="研究内容">
          <div class="tab-content">
            <p>{{ paperInfo.researchContent }}</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="研究方法">
          <div class="tab-content">
            <p>{{ paperInfo.researchMethod }}</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="创新点">
          <div class="tab-content">
            <p>{{ paperInfo.innovationPoints }}</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'WangbingyanPaper',
  data() {
    return {
      studentInfo: {
        name: '王冰堰',
        studentId: '2023001002',
        phone: '13800138033',
        college: '工程技术学院',
        email: '78991@qq.com'
      },
      paperInfo: {
        paperTitle: '基于深度学习的图像识别算法研究',
        paperType: '学术论文',
        submitDate: '2023-05-15',
        status: '已通过',
        paperAbstract: '本文针对传统图像识别算法在复杂场景下准确率不足的问题，提出了一种基于改进卷积神经网络的图像识别模型。通过引入注意力机制和特征融合策略，实验结果表明该模型在公开数据集上的识别准确率达到了92.3%，相比现有方法提升了约5.7个百分点。',
        researchContent: '本研究主要包括以下内容：\n1. 分析了当前图像识别领域的研究现状和挑战\n2. 设计了一种融合注意力机制的卷积神经网络架构\n3. 提出了多尺度特征融合策略以提升模型性能\n4. 在多个公开数据集上进行了对比实验验证',
        researchMethod: '研究采用了以下方法：\n1. 文献研究法：系统梳理了国内外相关研究进展\n2. 实验研究法：通过控制变量法设计对比实验\n3. 定量分析法：使用准确率、召回率等指标评估模型性能\n4. 定性分析法：分析错误案例以改进模型结构',
        innovationPoints: '1. 提出了一种新的注意力机制实现方式，能够自适应调整特征权重\n2. 设计了轻量化网络结构，在保证精度的同时降低计算复杂度\n3. 构建了一个新的复杂场景图像数据集，包含10万张标注样本'
      },
      activeTab: '0'
    }
  },
  mounted() {
    // 在实际应用中，这里应该通过API从后端获取数据
    this.loadPaperData();
  },
  methods: {
    loadPaperData() {
      // 模拟API请求
      this.$api.system.paper.getPaperInfo({ studentName: '王冰堰' }).then(response => {
        if (response.code === 200) {
          this.paperInfo = response.data;
        }
      });
    }
  }
}
</script>

<style scoped>
.info-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 500;
  font-size: 14px;
}
.tab-content {
  padding: 15px;
  line-height: 1.8;
  color: #606266;
}
.el-tag {
  margin-left: 5px;
}
@media (max-width: 768px) {
  .el-col {
    flex: 0 0 100%;
    max-width: 100%;
  }
}
</style>