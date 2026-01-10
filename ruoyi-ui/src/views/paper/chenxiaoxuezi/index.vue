<template>
  <div class="paper-container">
    <!-- 个人信息展示区 -->
    <el-card class="info-card" style="margin-bottom: 20px;">
      <div class="personal-info">
        <div class="avatar">
          <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" alt="陈潇雪子头像" />
        </div>
        <div class="info-content">
          <h3>陈潇雪子</h3>
          <div class="info-item">
            <svg-icon icon-class="user" class="info-icon" />
            <span>学号：116420220066</span>
          </div>
          <div class="info-item">
            <svg-icon icon-class="phone" class="info-icon" />
            <span>联系电话：1830583962</span>
          </div>
          <div class="info-item">
            <svg-icon icon-class="building" class="info-icon" />
            <span>学院：工程技术学院</span>
          </div>
          <div class="info-item">
            <svg-icon icon-class="email" class="info-icon" />
            <span>邮箱：chenxiaoxuezi@example.com</span>
          </div>
          <div class="info-item">
            <svg-icon icon-class="peoples" class="info-icon" />
            <span>专业：数据科学与大数据技术</span>
          </div>
          <div class="info-item">
            <svg-icon icon-class="tree" class="info-icon" />
            <span>班级：226402</span>
          </div>
          <div class="info-item">
            <svg-icon icon-class="edit" class="info-icon" />
            <span>指导老师：张展</span>
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
  name: 'ChenxiaoxueziPaper',
  data() {
    return {
      activeTab: '0',
      paperInfo: {
        paperTitle: '基于Python的音乐个性化推荐系统',
        paperType: '毕业设计（论文）',
        submitDate: '2026-01-10',
        status: '撰写中',
        paperAbstract: '随着在线音乐平台的快速发展，用户可选择的曲库规模持续扩大，传统的基于检索与榜单的内容获取方式已难以满足用户快速发现符合个人兴趣音乐的需求。本文面向音乐平台个性化推荐场景，设计并实现了一套基于Python的音乐个性化推荐系统。系统以用户-歌曲交互数据与歌曲内容特征为基础，综合采用协同过滤与内容推荐方法，构建混合推荐策略；通过数据清洗、特征工程、相似度计算与排序融合，实现对用户兴趣的建模与推荐结果的生成。系统后端采用Python完成离线训练与在线推断接口，前端提供推荐展示与反馈入口。实验基于公开数据集从准确率、召回率与覆盖率等指标进行评估，结果表明所提系统在冷启动与数据稀疏条件下具有更稳定的推荐效果，并能在保证准确性的同时提升推荐多样性。',
        researchContent: '本课题围绕“音乐个性化推荐”展开，主要研究内容包括：\n1) 需求分析与总体方案设计：明确系统用户画像、交互流程、推荐场景与评价指标，形成系统整体架构。\n2) 数据获取与预处理：对用户播放/收藏/点赞等行为数据进行清洗与标准化；对歌曲元数据（歌手、专辑、风格标签、歌词等）进行结构化处理。\n3) 推荐模型设计：实现基于物品的协同过滤（ItemCF）以捕捉群体偏好；实现基于内容的相似度推荐以利用歌曲特征；提出加权融合的混合推荐策略以兼顾准确性与可解释性。\n4) 系统实现：使用Python完成训练、召回与排序模块，提供可复用的推荐服务接口；实现推荐列表展示、用户反馈收集与结果更新机制。\n5) 实验评估与分析：对比不同模型在Top-N推荐任务上的表现，分析冷启动、长尾歌曲覆盖与多样性表现，给出可改进方向。',
        researchMethod: '研究采用的方法主要包括：\n1) 文献研究法：梳理协同过滤、矩阵分解、内容推荐与混合推荐等主流方法与工程实践。\n2) 数据分析与特征工程：通过统计分析识别数据稀疏性与长尾分布特征，构造用户偏好向量与歌曲内容向量。\n3) 算法实现与对比实验：实现ItemCF、内容相似度推荐与融合策略，在相同数据集与统一评价指标下进行对比。\n4) 系统工程方法：采用模块化设计，将数据层、算法层与服务层解耦；通过离线训练+在线推断实现可扩展部署。\n5) 指标评估与误差分析：使用Precision@K、Recall@K、Coverage、Diversity等指标量化效果，并对不佳样本进行原因分析。',
        innovationPoints: '本研究的主要创新点与特色包括：\n1) 面向音乐场景的混合推荐：将ItemCF与内容相似度召回进行加权融合，并通过简单可控的策略提升冷启动下的可用性。\n2) 可解释的推荐结果展示：基于“相似歌曲/相似用户行为/相同风格标签”等维度生成解释信息，增强用户信任与交互转化。\n3) 兼顾多样性与覆盖率的排序策略：在保证相关性的前提下引入多样性约束，改善热门歌曲垄断带来的体验问题。\n4) Python工程化落地：将数据处理、训练评估与在线服务接口统一到Python生态中，降低迭代成本并便于扩展为深度学习推荐模型。'
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

