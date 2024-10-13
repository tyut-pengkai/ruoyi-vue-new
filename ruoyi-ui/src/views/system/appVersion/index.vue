<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :inline="true"
      :model="queryParams"
    >
      <el-form-item label="版本名称" prop="versionName">
        <el-input
          v-model="queryParams.versionName"
          clearable
          placeholder="请输入版本名称"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本号" prop="versionNo">
        <el-input
          v-model="queryParams.versionNo"
          clearable
          placeholder="请输入版本号"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本状态" prop="status">
        <el-select
          v-model="queryParams.status"
          clearable
          placeholder="请选择版本状态"
          size="small"
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="软件MD5" prop="md5">
        <el-input
          v-model="queryParams.md5"
          clearable
          placeholder="请输入软件MD5"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          end-placeholder="结束日期"
          range-separator="-"
          size="small"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          size="mini"
          type="primary"
          @click="handleQuery"
          >搜索
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appVersion:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
          >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appVersion:edit']"
          :disabled="single"
          icon="el-icon-edit"
          plain
          size="mini"
          type="success"
          @click="handleUpdate"
          >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appVersion:remove']"
          :disabled="multiple"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          @click="handleDelete"
          >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:appVersion:export']"
          :loading="exportLoading"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
          >导出
        </el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-alert
      :closable="false"
      show-icon
      style="margin-bottom: 5px"
      type="info"
    >
      <template slot="title">
        <span>
          温馨提示：如果需要更新版本时请新建一个版本，并设置一个更大的版本号即可，而不要修改或删除已有的版本，如果版本被删除，那么对应版本的软件将无法使用。
        </span>
      </template>
    </el-alert>

    <el-table
      v-loading="loading"
      :data="appVersionList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="55" />
      <!-- <el-table-column align="center" label="" type="index"/> -->
      <el-table-column align="center" label="编号" prop="appVersionId" />
      <el-table-column
        align="center"
        :show-overflow-tooltip="true"
        label="软件名称"
      >
        <template slot-scope="scope">
          {{ scope.row.app.appName }}
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="版本名称"
      >
        <template slot-scope="scope">
          {{ scope.row.versionName }}({{ scope.row.versionNo }})
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="更新日志"
        prop="updateLog"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="下载地址"
        prop="downloadUrl"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        label="软件MD5"
        prop="md5"
      />
      <el-table-column align="center" label="创建者" prop="createBy" />
      <el-table-column
        align="center"
        label="创建时间"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column align="center" label="版本状态" prop="status">
<!--        <template slot-scope="scope">-->
<!--          <dict-tag-->
<!--            :options="dict.type.sys_normal_disable"-->
<!--            :value="scope.row.status"-->
<!--          />-->
<!--        </template>-->
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column align="center" label="强制更新" prop="forceUpdate">
<!--        <template slot-scope="scope">-->
<!--          <dict-tag-->
<!--            :options="dict.type.sys_yes_no"-->
<!--            :value="scope.row.forceUpdate"-->
<!--          />-->
<!--        </template>-->
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.forceUpdate"
            active-value="Y"
            inactive-value="N"
            @change="handleForceUpdateStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            icon="el-icon-upload2"
            size="mini"
            type="text"
            @click="handleImport(scope.row)"
            >快速接入
          </el-button>
          <el-button
            v-hasPermi="['system:appVersion:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            >修改
          </el-button>
          <el-button
            v-hasPermi="['system:appVersion:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改软件版本信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="800px">
      <el-form ref="form" :model="form" :rules="rules">
        <div v-if="app">
          <el-form-item>
            <el-col :span="12">
              <el-form-item label="所属软件">
                {{ this.app.appName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计费类型">
                <dict-tag
                  :options="dict.type.sys_bill_type"
                  :value="app.billType"
                />
              </el-form-item>
            </el-col>
          </el-form-item>
        </div>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="版本名称" prop="versionName">
              <span>
                <el-tooltip
                  content="版本名称可随意填写，如测试版v1.0、正式版2.0、国庆特别版等，用于软件显示"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
              <el-input
                v-model="form.versionName"
                placeholder="请输入版本名称"
                style="width: 200px"
                maxlength="30"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本号" prop="versionNo">
              <span>
                <el-tooltip
                  content="版本号用于升级，只限填写整数，同一软件版本号不可重复，版本越新，数字应该越大，建议使用发布时间，如20220101"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
              <el-input-number
                v-model="form.versionNo"
                :min="1"
                controls-position="right"
              />
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="更新日志" prop="updateLog">
          <el-input
            v-model="form.updateLog"
            autosize
            placeholder="请输入内容"
            type="textarea"
          />
        </el-form-item>
        <el-form-item label="下载地址（跳转）" prop="downloadUrl">
          <el-input v-model="form.downloadUrl" placeholder="请输入内容" maxlength="5000" show-word-limit>
          </el-input>
        </el-form-item>
        <el-form-item label="下载地址（直链）" prop="downloadUrlDirect">
          <el-input v-model="form.downloadUrlDirect" placeholder="请输入内容" maxlength="5000" show-word-limit>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="版本状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="强制更新" prop="forceUpdate">
              <el-radio-group v-model="form.forceUpdate">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item label="校验MD5" prop="checkMd5">
              <el-radio-group v-model="form.checkMd5">
                <el-radio
                  v-for="dict in dict.type.sys_yes_no"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12"></el-col>
        </el-form-item>
        <el-form-item label="软件MD5" prop="md5">
          <el-input v-model="form.md5" placeholder="请输入软件MD5" maxlength="500" show-word-limit/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            placeholder="请输入内容"
            type="textarea"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--快速接入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      append-to-body
      width="600px"
    >
      <el-alert
        :closable="false"
        show-icon
        style="margin-bottom: 10px"
        type="info"
      >
        <template slot="title">
          <span>
            快速接入安全性较低，建议接入后自行加固或加壳后再发布。
            <el-tooltip placement="top">
              <div slot="content">
                本功能主要用于开发者临时接单需要快速接入快速测试的场景，接入软件建议小于20M<br />目前可支持
                [全部模式] 的exe接入与 [单码计时] 模式的apk接入
              </div>
              <el-link type="primary">更多</el-link>
            </el-tooltip>
          </span>
        </template>
      </el-alert>
      <el-tabs style="width: 550px" type="border-card">
        <el-tab-pane
          v-if="app && app.authType === '1' && app.billType === '0'"
          label="APK(自动)"
        >
          <el-upload
            ref="upload"
            :action="
              upload.url +
              '?versionId=' +
              upload.quickAccessVersionId +
              '&updateMd5=' +
              upload.updateMd5 +
              '&apkOper=' +
              upload.apkOper +
              '&template=' +
              upload.template +
              '&skin=' +
              upload.skin +
              '&accessType=' +
              upload.accessType +
              '&activity=' +
              (upload.activity ? upload.activity : '') +
              '&method=' +
              (upload.method ? upload.method : '') +
              '&fullScreen=' +
              upload.fullScreen +
              '&enableOffline=' +
              upload.enableOffline +
              '&hideAutoLogin=' +
              upload.hideAutoLogin +
              '&enhancedMode=' +
              upload.enhancedMode +
              '&ignoreSplashActivity=' +
              upload.ignoreSplashActivity +
              '&ignoreActivityKeywords=' +
              upload.ignoreActivityKeywords
            "
            align="center"
            :auto-upload="false"
            :before-upload="onBeforeUpload"
            :disabled="upload.isUploading"
            :headers="upload.headers"
            :limit="1"
            :on-change="handleFileOnChange"
            :on-progress="handleFileUploadProgress"
            :on-success="handleFileSuccess"
            accept=".apk"
            drag
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              仅允许拖入或<em>点击上传</em>apk格式文件
            </div>
            <div slot="tip" class="el-upload__tip text-center">
              <!-- <span style="margin-top: 5px">允许导入exe或apk格式文件。</span> -->
              <!-- <div v-show="showExe" slot="tip" class="el-upload__tip">
                <el-checkbox v-model="upload.updateMd5" />
                <span>是否更新MD5</span>
                <span style="margin-left: 15px">
                  <el-tooltip
                    content="仅exe生效，会在已有MD5信息基础上累加"
                    placement="top"
                  >
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                  </el-tooltip>
                </span>
              </div>
              <div v-show="showApk" slot="tip" class="el-upload__tip"> -->
              <div style="margin-top: 10px">
                <span style="margin-right: 10px">接入方式</span>
                <el-radio-group v-model="upload.accessType">
                  <el-radio :label="1">全局模式</el-radio>
                  <el-radio :label="2">单例模式</el-radio>
                </el-radio-group>
                <span style="margin-left: 15px">
                  <el-tooltip
                    content="全局模式为全自动注入模式，无需人工操作，单例模式为半自动注入模式，需指定要注入的Activity和Method"
                    placement="top"
                  >
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                  </el-tooltip>
                </span>
              </div>
              <div style="margin-top: 10px">
                <span style="margin-right: 10px">接入类别</span>
                <el-select
                  v-model="upload.apkOper"
                  placeholder="请选择接入类别"
                  style="width: 120px; margin-right: 10px"
                >
                  <el-option
                    v-for="item in apkOperOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
                <span style="margin-right: 10px">是否全屏</span>
                <el-select
                  v-model="upload.fullScreen"
                  placeholder="请选择是否全屏"
                  style="width: 120px"
                >
                  <el-option
                    v-for="item in fullScreenOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
                <span style="margin-left: 15px">
                  <el-tooltip
                    content="可选择弹窗模式还是全屏模式，全屏模式在未登录成功前无法看到APP首页内容"
                    placement="top"
                  >
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                  </el-tooltip>
                </span>
              </div>
              <!-- <el-checkbox v-model="upload.signApk" />
            是否签名APK<br />(仅apk生效，建议将生成的apk加固后再发布) -->
              <!-- </div> -->
              <!-- <el-link
            type="primary"
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            @click="importTemplate"
            >下载模板</el-link
          > -->
              <div style="margin-top: 10px">
                <span style="margin-right: 10px">选择模板</span>
                <el-select
                  v-model="upload.template"
                  placeholder="请选择接入模板"
                  style="width: 120px; margin-right: 10px"
                  @change="handleChangeTemplate"
                >
                  <el-option
                    v-for="item in templateOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
                <span style="margin-right: 10px">选择皮肤</span>
                <el-select
                  v-model="upload.skin"
                  placeholder="请选择接入皮肤"
                  style="width: 120px"
                >
                  <el-option
                    v-for="item in skinOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
                <span style="margin-left: 15px">
                  <el-tooltip placement="top">
                    <i
                      class="el-icon-question"
                      style="margin-left: -12px; margin-right: 10px"
                    ></i>
                    <div slot="content"><div v-html="templateShow"></div></div>
                  </el-tooltip>
                </span>
              </div>
              <div style="margin-top: 10px; margin-left: 20px; margin-right: 20px">
                <el-row>
                  <el-col :span="8">
                    <el-checkbox v-model="upload.enableOffline">攻击自动离线</el-checkbox>
                    <span style="margin-left: 15px">
                    <el-tooltip
                      content="当服务器受到攻击时自动允许用户离线使用，当服务器恢复正常将自动恢复计费模式，注意弊端：一旦服务器停止运行，该软件用户将可免费使用，如果要经常更换服务器，建议关闭"
                      placement="top"
                    >
                      <i
                        class="el-icon-question"
                        style="margin-left: -12px; margin-right: 10px"
                      ></i>
                    </el-tooltip>
                  </span>
                  </el-col>
                  <el-col :span="8">
                    <el-checkbox v-model="upload.hideAutoLogin">隐藏自动登录</el-checkbox>
                    <span style="margin-left: 15px">
                      <el-tooltip
                        content="是否隐藏APP登录界面的自动登录复选框"
                        placement="top"
                      >
                        <i
                          class="el-icon-question"
                          style="margin-left: -12px; margin-right: 10px"
                        ></i>
                      </el-tooltip>
                    </span>
                  </el-col>
                  <el-col :span="8">
                    <el-checkbox v-model="upload.enhancedMode" :disabled="upload.accessType !== 1">增强模式</el-checkbox>
                    <span style="margin-left: 15px">
                      <el-tooltip
                        content="[仅对全局注入生效] 建议开启，开启后将尝试在每一个Activity中添加验证逻辑来增加破解难度"
                        placement="top"
                      >
                        <i
                          class="el-icon-question"
                          style="margin-left: -12px; margin-right: 10px"
                        ></i>
                      </el-tooltip>
                    </span>
                  </el-col>
                </el-row>
                <el-row style="margin-top: 10px">
                  <el-col :span="8">
                    <el-checkbox v-model="upload.ignoreSplashActivity" :disabled="upload.accessType !== 1">忽略SPLASH</el-checkbox>
                    <span style="margin-left: 15px">
                      <el-tooltip
                        content="[仅对全局注入生效] 是否忽略注入Splash Activity，建议开启，如果注入后登录窗口闪退，请尝试开启此选项；如果注入后不显示登录窗口，请尝试关闭此选项"
                        placement="top"
                      >
                        <i
                          class="el-icon-question"
                          style="margin-left: -12px; margin-right: 10px"
                        ></i>
                      </el-tooltip>
                    </span>
                  </el-col>
                  <el-col :span="16">
                    自定义忽略：<el-input v-model="upload.ignoreActivityKeywords" size="mini" placeholder="关键字，多个逗号分隔" style="width: 166px" :disabled="upload.accessType !== 1"></el-input>
                    <span style="margin-left: 15px">
                      <el-tooltip
                        content="[仅对全局注入生效] 自定义跳过的Activity，填写关键字即可，多个以英文逗号分隔"
                        placement="top"
                      >
                        <i
                          class="el-icon-question"
                          style="margin-left: -12px; margin-right: 10px"
                        ></i>
                      </el-tooltip>
                    </span>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-upload>
          <div align="center" style="margin-top: 20px">
            <el-button :loading="loading" type="primary" @click="submitFileForm"
            >启动上传并自动接入
            </el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane v-else label="APK(自动)" style="height: 300px">
          <span>APK快速接入目前只支持【单码计时】模式的软件</span>
        </el-tab-pane>
        <el-tab-pane
          v-if="app && app.authType === '1' && app.billType === '0'"
          label="APK(手动)"
        >
          <div align="center">
            <pre align="left">
    手动接入步骤：
    1、设置选项
    2、下载DEX文件
    3、重命名文件为classes${dex个数}.dex
    4、将DEX文件添加进APK文件
    5、找到注入点，将以下代码添加到目标位置
            </pre>
            <div style="margin-bottom: 25px">
              <CodeEditor
                v-model="insertCode"
                language="smali"
              ></CodeEditor>
            </div>
            <div style="margin-top: 10px">
              <span style="margin-right: 10px">接入类别</span>
              <el-select
                value="1"
                placeholder="请选择接入类别"
                style="width: 120px; margin-right: 10px"
                :disabled="true"
              >
                <el-option
                  key="1"
                  label="手动接入"
                  value="1"
                >
                </el-option>
              </el-select>
              <span style="margin-right: 10px">是否全屏</span>
              <el-select
                v-model="upload.fullScreen"
                placeholder="请选择是否全屏"
                style="width: 120px"
              >
                <el-option
                  v-for="item in fullScreenOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              <span style="margin-left: 15px">
                <el-tooltip
                  content="可选择弹窗模式还是全屏模式，全屏模式在未登录成功前无法看到APP首页内容"
                  placement="top"
                >
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                </el-tooltip>
              </span>
            </div>
            <div style="margin-top: 10px">
              <span style="margin-right: 10px">选择模板</span>
              <el-select
                v-model="upload.template"
                placeholder="请选择接入模板"
                style="width: 120px; margin-right: 10px"
                @change="handleChangeTemplate"
              >
                <el-option
                  v-for="item in templateOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              <span style="margin-right: 10px">选择皮肤</span>
              <el-select
                v-model="upload.skin"
                placeholder="请选择接入皮肤"
                style="width: 120px"
              >
                <el-option
                  v-for="item in skinOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
              <span style="margin-left: 15px">
                <el-tooltip placement="top">
                  <i
                    class="el-icon-question"
                    style="margin-left: -12px; margin-right: 10px"
                  ></i>
                  <div slot="content"><div v-html="templateShow"></div></div>
                </el-tooltip>
              </span>
            </div>
            <div style="margin-top: 10px; margin-left: 20px; margin-right: 20px">
              <el-row>
                <el-col :span="12">
                  <el-checkbox v-model="upload.enableOffline">攻击自动离线</el-checkbox>
                  <span style="margin-left: 15px">
                    <el-tooltip
                      content="当服务器受到攻击时自动允许用户离线使用，当服务器恢复正常将自动恢复计费模式，注意弊端：一旦服务器停止运行，该软件用户将可免费使用，如果要经常更换服务器，建议关闭"
                      placement="top"
                    >
                      <i
                        class="el-icon-question"
                        style="margin-left: -12px; margin-right: 10px"
                      ></i>
                    </el-tooltip>
                  </span>
                </el-col>
                <el-col :span="12">
                  <el-checkbox v-model="upload.hideAutoLogin">隐藏自动登录</el-checkbox>
                  <span style="margin-left: 15px">
                      <el-tooltip
                        content="是否隐藏APP登录界面的自动登录复选框"
                        placement="top"
                      >
                        <i
                          class="el-icon-question"
                          style="margin-left: -12px; margin-right: 10px"
                        ></i>
                      </el-tooltip>
                    </span>
                </el-col>
<!--                <el-col :span="8">-->
<!--                  <el-checkbox v-model="upload.enhancedMode">增强模式</el-checkbox>-->
<!--                  <span style="margin-left: 15px">-->
<!--                      <el-tooltip-->
<!--                        content="在自动注入时使用增强模式来增加破解难度"-->
<!--                        placement="top"-->
<!--                      >-->
<!--                        <i-->
<!--                          class="el-icon-question"-->
<!--                          style="margin-left: -12px; margin-right: 10px"-->
<!--                        ></i>-->
<!--                      </el-tooltip>-->
<!--                    </span>-->
<!--                </el-col>-->
              </el-row>
            </div>
          </div>
          <div align="center" style="margin-top: 20px">
            <el-button :loading="loading" type="primary" @click="downloadDexFile"
            >开始下载DEX文件
            </el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane v-else label="APK(手动)" style="height: 300px">
          <span>APK快速接入目前只支持【单码计时】模式的软件</span>
        </el-tab-pane>
        <el-tab-pane
          v-if="app && app.authType === '1' && app.billType === '0'"
          label="APK(扫码)"
        >
          <div>
            <el-alert
              :closable="false"
              show-icon
              style="margin-bottom: 10px"
              type="info"
            >
              <template slot="title">
                <span> 请在官方群共享下载红叶小助手APP </span>
              </template>
            </el-alert>
            <div align="center">
              <div style="
                width: 300px;
                padding: 15px;
                border-style: solid;
                border-radius: 20px;
                border-color: #3c8ce7;
              ">
                <div>
                  请打开APP<span class="my-price">红叶小助手</span>扫一扫
                </div>
                <el-skeleton :loading="apvLoading" animated style="width: 200px">
                  <template slot="template">
                    <el-skeleton-item style="width: 200px; height: 200px" variant="image"/>
                  </template>
                </el-skeleton>
                <div style="cursor: pointer;" @click="getUuid">
                  <vue-qr v-if="apvStrUrl" :logoSrc="logoUrl" :size="200" :text="apvStrUrl" ></vue-qr>
                </div>
                <div>
                  二维码一次性有效，可点击二维码刷新
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane v-else label="APK(扫码)" style="height: 300px">
          <span>APK快速接入目前只支持【单码计时】模式的软件</span>
        </el-tab-pane>
        <el-tab-pane label="EXE(自动)">
          <div style="width: 516px; height: 303px">
            <el-alert
              :closable="false"
              show-icon
              style="margin-bottom: 10px"
              type="info"
            >
              <template slot="title">
                <span> 请在官方群共享下载红叶快速接入工具 </span>
              </template>
            </el-alert>
            <div align="center" style="margin-top: 10px">
              <el-input
                v-show="apvStr && apvStr != ''"
                v-model="apvStr"
                :readonly="true"
                :rows="9"
                type="textarea"
              ></el-input>
              <el-button
                id="copyButton"
                style="margin-top: 20px"
                type="primary"
                @click="getQuickAccessParams(upload.quickAccessVersionId)"
              >
                一键复制对接参数
              </el-button>
            </div>
          </div>
        </el-tab-pane>
<!--        <el-tab-pane disabled>-->
<!--          <slot slot="label">-->
<!--            <el-button-->
<!--              type="text"-->
<!--              icon="el-icon-setting"-->
<!--              size="mini"-->
<!--              @click="handleExport"-->
<!--              style="margin-left: 5px"-->
<!--            >模板管理</el-button-->
<!--            >-->
<!--          </slot>-->
<!--        </el-tab-pane>-->
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!--下载进度条-->
    <el-dialog
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      :visible.sync="fileDown.loadDialogStatus"
      title="正在下载，请等待"
      width="20%"
    >
      <div style="text-align: center">
        <el-progress
          :percentage="fileDown.percentage"
          type="circle"
        ></el-progress>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="downloadClose">取消下载</el-button>
      </div>
    </el-dialog>

    <!--activity选择列表-->
    <el-dialog
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      :visible.sync="selectActivityMethod.loadActivityDialogStatus"
      title="请选择入口Activity"
      width="20%"
    >
      <div style="text-align: center">
        <el-select v-model="upload.activity" filterable placeholder="请选择">
          <el-option
            v-for="item in selectActivityMethod.activityList || []"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="activityClose">确定</el-button>
        <el-button
          @click="selectActivityMethod.loadActivityDialogStatus = false"
          >取 消
        </el-button>
      </div>
    </el-dialog>

    <!--method选择列表-->
    <el-dialog
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      :visible.sync="selectActivityMethod.loadMethodDialogStatus"
      title="请选择注入Method"
      width="20%"
    >
      <div style="text-align: center">
        <el-select v-model="upload.method" filterable placeholder="请选择">
          <el-option
            v-for="item in selectActivityMethod.methodList || []"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="methodClose">确定</el-button>
        <el-button @click="selectActivityMethod.loadMethodDialogStatus = false"
          >取 消
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addAppVersion,
  delAppVersion,
  exportAppVersion,
  getAppVersion,
  getFileResult,
  getQuickAccessParams,
  getQuickAccessTemplateList,
  listAppVersion,
  updateAppVersion,
  downloadDexFile,
  changeVersionForceUpdateStatus,
  changeVersionStatus,
} from "@/api/system/appVersion";
import { getToken } from "@/utils/auth";
import { getApp } from "@/api/system/app";
import axios from "axios";
import Clipboard from "clipboard";
import vueQr from "vue-qr";
import CodeEditor from "@/components/CodeEditor";
import {getUuid} from "@/api/login";

export default {
  name: "AppVersion",
  components: {vueQr, CodeEditor},
  dicts: ["sys_normal_disable", "sys_bill_type", "sys_yes_no"],
  data() {
    return {
      // 软件
      app: null,
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 软件版本信息表格数据
      appVersionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: this.$store.state.settings.pageSize,
        appId: null,
        versionName: null,
        versionNo: null,
        status: null,
        md5: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        versionName: [
          { required: true, message: "版本名称不能为空", trigger: "blur" },
        ],
        versionNo: [
          { required: true, message: "版本号不能为空", trigger: "blur" },
        ],
        downloadUrl: [
          { required: false, message: "下载地址不能为空", trigger: "blur" },
        ],
        status: [
          { required: true, message: "版本状态不能为空", trigger: "blur" },
        ],
        forceUpdate: [
          { required: true, message: "是否强制更新不能为空", trigger: "blur" },
        ],
        md5: [{ required: false, message: "软件MD5不能为空", trigger: "blur" }],
        checkMd5: [
          { required: true, message: "是否检查MD5不能为空", trigger: "blur" },
        ],
      },
      // 快速接入参数
      upload: {
        // 是否显示弹出层（快速接入）
        open: false,
        // 弹出层标题（快速接入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/appVersion/quickAccess",
        // 当前操作的版本ID
        quickAccessVersionId: null,
        // 是否自动更新MD5
        updateMd5: true,
        // 是否自动APK签名
        apkOper: "1",
        // 默认模板
        template: null,
        // 默认皮肤
        skin: null,
        // 接入方式
        accessType: 1, // 1全局 2单例
        // activity
        activity: null,
        // method
        method: null,
        // 文件路径
        oriPath: null,
        // 文件名
        oriName: null,
        uploaded: false,
        fullScreen: false,
        enableOffline: false,
        hideAutoLogin: false,
        enhancedMode: true,
        ignoreSplashActivity: true,
        ignoreActivityKeywords: null,
      },
      fileDown: {
        //弹出框控制的状态
        loadDialogStatus: false,
        //进度条的百分比
        percentage: 0,
        //取消下载时的资源对象
        source: {},
      },
      selectActivityMethod: {
        //弹出框控制的状态
        loadActivityDialogStatus: false,
        loadMethodDialogStatus: false,
        // 列表内容
        activityList: [],
        methodList: [],
      },
      apkOperOptions: [
        {
          value: "1",
          label: "注入并加签",
        },
        {
          value: "2",
          label: "仅注入",
        },
        {
          value: "3",
          label: "仅加签",
        },
      ],
      templateOptions: [
        // {
        //   value: "0",
        //   label: "默认",
        // },
      ],
      skinOptions: [
        // {
        //   value: "0",
        //   label: "默认",
        // },
      ],
      fullScreenOptions: [
        {
          value: true,
          label: "是",
        },
        {
          value: false,
          label: "否",
        },
      ],
      showExe: false,
      showApk: false,
      apvStr: "",
      apvStrUrl: "",
      apvLoading: false,
      templateList: [],
      templateShow: "",
      logoUrl: require("../../../assets/logo/logo.png"),
      insertCode: "goto :goto_36\n" +
        ":catchall_30\n" +
        "move-exception v0\n" +
        "invoke-virtual {v0}, Ljava/lang/Throwable;->getCause()Ljava/lang/Throwable;\n" +
        "move-result-object v0\n" +
        "throw v0\n" +
        ":goto_36\n" +
        ":try_start_36\n" +
        "invoke-static {p0}, Lcom/App;->show(Landroid/content/Context;)V\n" +
        ":try_end_54\n" +
        ".catchall {:try_start_36 .. :try_end_54}\n" +
        ":catchall_30",
      curAppVersionId: "",
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      // const appId = this.$route.params && this.$route.params.appId;
      const appId = this.$route.query && this.$route.query.appId;
      // const appName = this.$route.query && this.$route.query.appName;
      if (appId != undefined && appId != null) {
        getApp(appId).then((response) => {
          this.app = response.data;
          const title = "版本管理";
          const appName = this.app.appName;
          const route = Object.assign({}, this.$route, {
            title: `${title}-${appName}`,
          });
          this.$store.dispatch("tagsView/updateVisitedView", route);
          this.getList();
        });
      } else {
        this.$modal.alertError("未获取到当前软件信息");
      }
    },
    initUpload() {
      this.upload.isUploading = false;
      this.upload.activity = null;
      this.upload.method = null;
      this.upload.oriPath = null;
      this.upload.oriName = null;
      if (this.$refs.upload) {
        this.$refs.upload.clearFiles();
      }
      this.upload.uploaded = false;
      this.upload.fullScreen = false;
      this.upload.enableOffline = false;
      this.upload.hideAutoLogin = false;
      this.upload.enhancedMode = true;
      this.upload.ignoreSplashActivity = true;
      this.upload.ignoreActivityKeywords = null;
    },
    /** 查询软件版本信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && "" != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] =
          this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      this.queryParams.appId = this.app.appId;
      listAppVersion(this.queryParams).then((response) => {
        this.appVersionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 版本状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal
        .confirm('确认要' + text + '"' + row.versionName + '"版本吗？')
        .then(function () {
          return changeVersionStatus(row.appVersionId, row.status);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    // 版本强制更新状态修改
    handleForceUpdateStatusChange(row) {
      let text = row.forceUpdate === "Y" ? "开启" : "关闭";
      this.$modal
        .confirm('确认要' + text + '"' + row.versionName + '"版本的强制更新吗？')
        .then(function () {
          return changeVersionForceUpdateStatus(row.appVersionId, row.forceUpdate);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.forceUpdate = row.forceUpdate === "Y" ? "N" : "Y";
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        appVersionId: undefined,
        appId: this.app.appId,
        versionName: undefined,
        versionNo: undefined,
        updateLog: '优化了一些内容',
        downloadUrl: undefined,
        status: "0",
        delFlag: undefined,
        md5: undefined,
        remark: undefined,
        forceUpdate: "N",
        checkMd5: "N",
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.appVersionId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加软件版本信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$modal
        .confirm(
          "是否确认修改数据项？温馨提示：如果需要更新版本，请新建一个版本，并设置一个更大的版本号即可，而不要修改已有的版本。"
        )
        .then(() => {
          this.reset();
          const appVersionId = row.appVersionId || this.ids;
          getAppVersion(appVersionId).then((response) => {
            this.form = response.data;
            this.open = true;
            this.title = "修改软件版本信息";
          });
        })
        .catch(() => {});
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.appVersionId != null) {
            updateAppVersion(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppVersion(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const appVersionIds = row.appVersionId || this.ids;
      this.$modal
        .confirm(
          "是否确认删除数据项？温馨提示：如果需要版本，请新建一个版本，而不要修改或删除已有的版本，如果版本被删除，那么对应版本的软件将无法使用。"
        )
        .then(function () {
          return delAppVersion(appVersionIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal
        .confirm("是否确认导出所有软件版本信息数据项？")
        .then(() => {
          this.exportLoading = true;
          return exportAppVersion(queryParams);
        })
        .then((response) => {
          this.$download.name(response.msg);
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    /** 导入按钮操作 */
    handleImport(row) {
      this.curAppVersionId = row.appVersionId;
      this.upload.title = "快速接入";
      this.upload.quickAccessVersionId = row.appVersionId;
      this.apvStr = "";
      this.getUuid();
      this.apvLoading = true;
      getQuickAccessParams(row.appVersionId).then((response) => {
        this.apvStr = response.apvStr;
        this.apvLoading = false;
      });
      getQuickAccessTemplateList().then((response) => {
        this.templateList = response.data;
        this.templateOptions = [];
        for (let item of this.templateList) {
          this.templateOptions.push({
            value: item.fileName,
            label: item.name,
          });
        }
        if (this.templateOptions.length > 0) {
          this.upload.template = this.templateOptions[0].value;
          this.handleChangeTemplate(this.templateOptions[0].value);
        }
      });
      this.initUpload();
      this.upload.open = true;
    },
    /** 注入模板被改变，重新加载对应皮肤列表 */
    handleChangeTemplate(v) {
      this.skinOptions = [];
      this.templateShow = "每个模板中可包含多个皮肤供用户选择";
      for (let item of this.templateList) {
        if (item.fileName == v) {
          for (let skin of item.skinList) {
            this.skinOptions.push({
              value: skin,
              label: skin,
            });
          }
          if (this.skinOptions.length == 0) {
            this.skinOptions.push({
              value: "默认",
              label: "默认",
            });
          }
          this.upload.skin = this.skinOptions[0].value;
          // 加载模板说明
          this.templateShow =
            "模板名称：" +
            item.name +
            "<br>模板描述：" +
            item.description +
            "<br>模板作者：" +
            item.author +
            "<br>模板版本：" +
            item.version +
            "<br>联系方式：" +
            item.contact +
            "<br>附加信息：" +
            item.remark;
          break;
        }
      }
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      if (response.code == 200) {
        if (response.data.step === "file") {
          // this.$download.name(response.msg);
          this.downFile(response.data.data);
          this.upload.open = false;
          this.upload.isUploading = false;
          this.$refs.upload.clearFiles();
        } else if (response.data.step === "activityList") {
          this.selectActivityMethod.activityList = [];
          for (let item of response.data.list || []) {
            this.selectActivityMethod.activityList.push({
              value: item,
              label: item,
            });
          }
          this.selectActivityMethod.loadActivityDialogStatus = true;
          this.upload.oriPath = response.data.oriPath;
          this.upload.oriName = response.data.oriName;
        }
      } else {
        this.$modal.alert("接入失败：" + response.msg);
      }
    },
    // 提交上传文件
    submitFileForm() {
      if (
        // this.upload.accessType == "1" ||
        // (this.upload.accessType == "2" && !this.upload.activity)
        !this.upload.uploaded
      ) {
        this.loading = true;
        this.$refs.upload.submit();
        this.upload.uploaded = true;
        this.loading = false;
      } else {
        this.loading = true;
        this.getFileResult()
          .then((response) => {
            if (response.data.step === "activityList") {
              this.selectActivityMethod.activityList = [];
              for (let item of response.data.list || []) {
                this.selectActivityMethod.activityList.push({
                  value: item,
                  label: item,
                });
              }
              this.selectActivityMethod.loadActivityDialogStatus = true;
              this.upload.oriPath = response.data.oriPath;
              this.upload.oriName = response.data.oriName;
            } else if (response.data.step === "methodList") {
              this.selectActivityMethod.methodList = [];
              for (let item of response.data.list || []) {
                this.selectActivityMethod.methodList.push({
                  value: item,
                  label: item,
                });
              }
              this.selectActivityMethod.loadMethodDialogStatus = true;
            } else if (response.data.step === "file") {
              this.downFile(response.data.data);
              this.upload.open = false;
            }
          })
          .catch((error) => {
            console.info(error);
            this.$modal.alert("接入失败：" + error);
          })
          .finally(() => {
            this.loading = false;
          });
      }
    },
    // 校验文件
    onBeforeUpload(file) {
      // const isLt1M = file.size / 1024 / 1024 < 20;
      // if (!isLt1M) {
      //   this.$message.error("上传文件大小不能超过 20MB!");
      // }
      // return isLt1M;
    },
    // 添加文件
    handleFileOnChange(file, fileList) {
      if (file && file.name) {
        String.prototype.endWith = function (endStr) {
          var d = this.length - endStr.length;
          return d >= 0 && this.lastIndexOf(endStr) == d;
        };
        if (file.name.endWith(".exe")) {
          this.showExe = true;
          this.showApk = false;
        } else if (file.name.endWith(".apk")) {
          this.showApk = true;
          this.showExe = false;
        }
      } else {
        this.showApk = false;
        this.showExe = false;
      }
    },
    downFile(name) {
      //这里放参数
      var param = {};
      this.fileDown.loadDialogStatus = true;
      this.fileDown.percentage = 0;
      const instance = this.initInstance();
      instance({
        method: "get",
        withCredentials: true,
        url:
          process.env.VUE_APP_BASE_API +
          "/common/download?fileName=" +
          encodeURI(name) +
          "&delete=false",
        params: param,
        responseType: "blob",
        headers: { Authorization: "Bearer " + getToken() },
      })
        .then((res) => {
          this.fileDown.loadDialogStatus = false;
          console.info(res);
          const content = res.data;
          if (content.size == 0) {
            this.loadClose();
            this.$modal.alert("下载失败");
            return;
          }
          const blob = new Blob([content]);
          const fileName = decodeURI(res.headers["download-filename"]); //替换文件名
          if ("download" in document.createElement("a")) {
            // 非IE下载
            const elink = document.createElement("a");
            elink.download = fileName;
            elink.style.display = "none";
            elink.href = URL.createObjectURL(blob);
            document.body.appendChild(elink);
            elink.click();
            setTimeout(function () {
              URL.revokeObjectURL(elink.href); // 释放URL 对象
              document.body.removeChild(elink);
            }, 100);
          } else {
            // IE10+下载
            navigator.msSaveBlob(blob, fileName);
          }
        })
        .catch((error) => {
          this.fileDown.loadDialogStatus = false;
          console.info(error);
          let href =
            process.env.VUE_APP_BASE_API +
            "/common/download?fileName=" +
            encodeURI(name) +
            "&delete=false";
          // this.$modal.alert(
          //   "下载失败：" + error + "；可尝试点击链接重新下载：" + href
          // );
          this.$alert(
            "下载失败：" +
              error +
              '<br><a style="text-decoration:underline;color: #FF0000" href="' +
              href +
              '"' +
              ' target="_blank">可尝试点击此处重新下载</a>',
            "下载失败",
            {
              dangerouslyUseHTMLString: true,
            }
          );
        });
    },
    initInstance() {
      var _this = this;
      //取消时的资源标记
      this.fileDown.source = axios.CancelToken.source();
      const instance = axios.create({
        //axios 这个对象要提前导入 或者替换为你们全局定义的
        onDownloadProgress: function (ProgressEvent) {
          const load = ProgressEvent.loaded;
          const total = ProgressEvent.total;
          const progress = (load / total) * 100;
          // console.log("progress=" + progress);
          //一开始已经在计算了 这里要超过先前的计算才能继续往下
          if (progress > _this.fileDown.percentage) {
            _this.fileDown.percentage = Math.floor(progress);
          }
          if (progress == 100) {
            //下载完成
            _this.fileDown.open = false;
          }
        },
        cancelToken: this.fileDown.source.token, //声明一个取消请求token
      });
      return instance;
    },
    downloadClose() {
      //中断下载
      this.$modal
        .confirm("点击关闭后将中断下载，是否确定关闭？")
        .then(() => {
          //中断下载回调
          this.fileDown.source.cancel("log==客户手动取消下载");
        })
        .catch(() => {
          //取消--什么都不做
        });
    },
    getQuickAccessParams(appVersionId) {
      var clipboard = new Clipboard("#copyButton", {
        text: () => {
          // 如果想从其它DOM元素内容复制。应该是target:function(){return: };
          return this.apvStr;
        },
      });
      clipboard.on("success", (e) => {
        this.$modal.msgSuccess("已成功复制对接参数");
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
        this.$modal.msgError(
          "复制失败，您的浏览器不支持复制，请自行复制下方的对接参数"
        );
        clipboard.destroy();
      });
    },
    getFileResult() {
      return getFileResult(
        "versionId=" +
          this.upload.quickAccessVersionId +
          "&updateMd5=" +
          this.upload.updateMd5 +
          "&apkOper=" +
          this.upload.apkOper +
          "&template=" +
          this.upload.template +
          "&skin=" +
          this.upload.skin +
          "&accessType=" +
          this.upload.accessType +
          "&activity=" +
          (this.upload.activity ? this.upload.activity : "") +
          "&method=" +
          (this.upload.method ? encodeURIComponent(this.upload.method) : "") +
          "&oriPath=" +
          (this.upload.oriPath ? encodeURIComponent(this.upload.oriPath) : "") +
          "&oriName=" +
          (this.upload.oriName ? encodeURIComponent(this.upload.oriName) : "") +
          "&fullScreen=" +
          this.upload.fullScreen +
          "&enableOffline=" +
          this.upload.enableOffline +
          "&hideAutoLogin=" +
          this.upload.hideAutoLogin +
          "&enhancedMode=" +
          this.upload.enhancedMode +
          "&ignoreSplashActivity=" +
          this.upload.ignoreSplashActivity +
          "&ignoreActivityKeywords=" +
          this.upload.ignoreActivityKeywords
      );
    },
    activityClose() {
      this.selectActivityMethod.loadActivityDialogStatus = false;
      if (this.upload.activity) {
        this.submitFileForm();
      }
    },
    methodClose() {
      this.selectActivityMethod.loadMethodDialogStatus = false;
      if (this.upload.method) {
        this.submitFileForm();
      }
    },
    downloadDexFile() {
      this.loading = true;
      var params = "versionId=" +
        this.upload.quickAccessVersionId +
        "&template=" +
        this.upload.template +
        "&skin=" +
        this.upload.skin +
        "&fullScreen=" +
        this.upload.fullScreen +
        "&enableOffline=" +
        this.upload.enableOffline +
        "&hideAutoLogin=" +
        this.upload.hideAutoLogin +
        "&enhancedMode=" +
        this.upload.enhancedMode;
      downloadDexFile(params).then((response) => {
        this.downFile(response.data.data);
      }).finally(() => {
        this.loading = false;
      });
    },
    getUuid() {
      getUuid().then(res => {
        let href = window.location.href;
        this.apvStrUrl = href.substring(0, href.indexOf('/', 8)) + // 跳过前8个字符：https://
          "/prod-api/system/appVersion/scan/" + this.curAppVersionId + "/" + res.uuid;
      });
    },
  },
  watch: {
    //动态监听路由变化 -以便动态更改导航背景色事件效果等
    $route(to, from) {
      // 对路由变化作出响应...
      console.log("to.path----", to); //跳转后路由
      console.log("from----", from); //跳转前路由

      if (
        (from.path == "/verify/app" || from.path == "/admin/index") &&
        to.path == "/verify/appVersion" &&
        to.query.appId != this.app.appId
      ) {
        this.init();
      }
    },
  },
};
</script>
<style scoped>
.my-price {
  font-weight: 600;
  color: #3c8ce7;
  margin-right: 5px;
  font-size: 18px;
}
.echart-pie-wrap {
  height: 240px;
}
</style>
