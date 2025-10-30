<template>
  <div class="dashboard-editor-container">
    <el-card class="paper-home-card">
      <template #header>
        <div class="paper-home-header">
          <span>论文首页（郑瑜甜）</span>
        </div>
      </template>

      <!-- 个人信息展示区 -->
      <el-card class="info-card" style="margin-bottom: 20px;">
        <div class="personal-info">
          <div class="avatar">
            <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" alt="用户头像" />
          </div>
          <div class="info-content">
            <h3>郑瑜甜</h3>
            <div class="info-item">
              <svg-icon icon-class="user" class="info-icon" />
              <span>学号：2023001001</span>
            </div>
            <div class="info-item">
              <svg-icon icon-class="phone" class="info-icon" />
              <span>联系电话：13800138000</span>
            </div>
            <div class="info-item">
              <svg-icon icon-class="building" class="info-icon" />
              <span>学院：计算机科学与技术学院</span>
            </div>
            <div class="info-item">
              <svg-icon icon-class="email" class="info-icon" />
              <span>邮箱：test@example.com</span>
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
                <span>{{ paperInfo.title }}</span>
              </el-form-item>
              <el-form-item label="论文类型">
                <span>{{ paperInfo.type }}</span>
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
                <p style="line-height: 1.6;">{{ paperInfo.abstract }}</p>
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
              <p>本研究基于深度学习算法，提出了一种改进的图像识别模型。研究主要包括以下内容：</p>
              <ol>
                <li>分析了当前图像识别领域的研究现状和存在的问题</li>
                <li>提出了一种基于卷积神经网络的改进模型架构</li>
                <li>设计了特殊的数据增强方法，提高模型的泛化能力</li>
                <li>通过实验验证了模型在多个数据集上的性能表现</li>
              </ol>
            </div>
          </el-tab-pane>
          <el-tab-pane label="研究方法">
            <div class="tab-content">
              <p>本研究采用了以下研究方法：</p>
              <ol>
                <li><strong>文献研究法</strong>：系统梳理了国内外相关研究文献，了解研究现状和发展趋势</li>
                <li><strong>实验研究法</strong>：设计了对比实验，验证了所提方法的有效性</li>
                <li><strong>定量分析法</strong>：使用准确率、召回率、F1值等指标对实验结果进行量化分析</li>
                <li><strong>案例分析法</strong>：通过具体案例展示了模型在实际应用中的表现</li>
              </ol>
            </div>
          </el-tab-pane>
          <el-tab-pane label="研究成果">
            <div class="tab-content">
              <p>本研究取得了以下成果：</p>
              <ol>
                <li>提出了一种新的卷积神经网络架构，在标准数据集上的准确率达到95.8%</li>
                <li>开发了一套完整的图像识别系统原型，具有良好的实时性能</li>
                <li>发表了相关学术论文2篇，申请了1项发明专利</li>
                <li>研究成果在实际场景中进行了试点应用，取得了良好效果</li>
              </ol>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'PaperHome',
  data() {
    return {
      activeTab: '0',
      paperInfo: {
        title: '基于深度学习的图像识别系统研究与实现',
        type: '工程设计',
        submitDate: '2023-05-15',
        status: '已提交',
        abstract: '本文研究了基于深度学习的图像识别技术，提出了一种改进的卷积神经网络模型，并在实际应用场景中进行了测试和验证。实验结果表明，该模型在图像分类和识别任务上具有较高的准确率和效率，为相关领域的应用提供了新的技术方案。'
      }
    }
  },
  mounted() {
    // 可以在这里加载真实数据
    // this.loadPaperInfo();
  },
  methods: {
    // 加载论文信息
    loadPaperInfo() {
      // 实际项目中，这里应该调用API获取数据
      // 示例：this.$http.get('/api/paper/info').then(res => { this.paperInfo = res.data; });
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.paper-home-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.paper-home-header {
  font-size: 20px;
  font-weight: bold;
  color: #1f2937;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 0;
}

.info-card {
  margin-bottom: 20px;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.1);
}

.card-header {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.personal-info {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 30px;
  border: 3px solid #e8e8e8;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-content h3 {
  margin: 0 0 20px 0;
  font-size: 22px;
  color: #1f2937;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  color: #666;
}

.info-icon {
  width: 20px;
  height: 20px;
  margin-right: 10px;
  color: #409eff;
}

.tab-content {
  padding: 20px 0;
  line-height: 1.8;
  color: #666;
}

.tab-content p {
  margin-bottom: 15px;
  font-size: 15px;
}

.tab-content ol {
  padding-left: 25px;
}

.tab-content li {
  margin-bottom: 10px;
}

.tab-content strong {
  color: #333;
  font-weight: 600;
}

@media (max-width: 768px) {
  .dashboard-editor-container {
    padding: 10px;
  }
  
  .personal-info {
    flex-direction: column;
    text-align: center;
  }
  
  .avatar {
    margin-right: 0;
    margin-bottom: 20px;
  }
  
  .paper-home-header {
    font-size: 18px;
  }
}
</style>