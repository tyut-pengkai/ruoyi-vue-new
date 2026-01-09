<template>
  <div class="paper-container">
    <!-- 个人信息展示区 -->
    <el-card class="info-card" style="margin-bottom: 20px;">
      <div class="personal-info">
        <div class="avatar">
          <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" alt="张佳琳头像" />
        </div>
        <div class="info-content">
          <h3>张佳琳</h3>
          <div class="info-item">
            <svg-icon icon-class="user" class="info-icon" />
            <span>学号：2023001003</span>
          </div>
          <div class="info-item">
            <svg-icon icon-class="phone" class="info-icon" />
            <span>联系电话：13800138002</span>
          </div>
          <div class="info-item">
            <svg-icon icon-class="building" class="info-icon" />
            <span>学院：计算机科学与技术学院</span>
          </div>
          <div class="info-item">
            <svg-icon icon-class="email" class="info-icon" />
            <span>邮箱：zhangjialin@example.com</span>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 毕业论文基本信息板块 -->
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

    <!-- 论文成果介绍部分 -->
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
  name: 'ZhangjialinPaper',
  data() {
    return {
      activeTab: '0',
      paperInfo: {
        paperTitle: '基于机器学习的智能推荐系统研究与实现',
        paperType: '学术论文',
        submitDate: '2023-05-20',
        status: '已通过',
        paperAbstract: '本文针对传统推荐系统存在的冷启动和数据稀疏性问题，提出了一种基于深度学习和协同过滤相结合的智能推荐模型。通过引入注意力机制和图神经网络技术，有效提升了推荐系统的准确性和用户体验。实验结果表明，该模型在多个数据集上的性能优于现有主流推荐算法。',
        researchContent: '本研究主要包括以下内容：1) 分析了当前推荐系统的研究现状和存在的关键问题；2) 提出了一种融合深度学习和协同过滤的混合推荐模型；3) 设计了基于注意力机制的特征提取模块，增强了模型对用户兴趣的捕捉能力；4) 实现了基于图神经网络的社交关系建模，利用用户社交网络信息提升推荐效果；5) 通过大量实验验证了模型的有效性和泛化能力。',
        researchMethod: '本研究采用了以下研究方法：1) 文献研究法：系统梳理了推荐系统领域的相关文献，了解研究现状和发展趋势；2) 模型构建法：结合深度学习和传统推荐算法，构建了新的混合推荐模型；3) 实验验证法：在公开数据集上进行对比实验，验证了所提模型的性能优势；4) 量化分析法：使用准确率、召回率、F1值等指标对实验结果进行量化分析，客观评价模型性能。',
        innovationPoints: '本研究的主要创新点包括：1) 提出了一种基于注意力机制的深度协同过滤模型，有效解决了传统推荐系统的冷启动问题；2) 引入图神经网络技术，实现了对用户社交网络的有效建模，提升了推荐系统的准确性；3) 设计了一种自适应的特征融合策略，能够根据不同数据集自动调整特征权重，增强了模型的泛化能力；4) 通过实验验证了所提模型在多个数据集上的性能优势，为推荐系统的实际应用提供了新的思路和方法。'
      }
    }
  }
}
</script>

<style scoped>
.paper-container {
  padding: 20px;
}

.personal-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 20px;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-content {
  flex: 1;
}

.info-content h3 {
  margin: 0 0 15px 0;
  font-size: 20px;
  font-weight: bold;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  font-size: 14px;
}

.info-icon {
  margin-right: 8px;
  color: #409EFF;
}

.card-header {
  font-size: 16px;
  font-weight: bold;
}

.tab-content {
  padding: 10px 0;
  line-height: 1.8;
}

.tab-content p {
  margin: 0 0 10px 0;
}

.tab-content ol {
  margin: 10px 0;
  padding-left: 20px;
}

.tab-content li {
  margin-bottom: 5px;
}
</style>