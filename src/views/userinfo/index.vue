<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入用户名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="用户状态" clearable style="width: 200px">
          <el-option
            v-for="dict in sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="用户类型" clearable style="width: 200px">
          <el-option label="普通用户" value="1" />
          <el-option label="VIP用户" value="2" />
          <el-option label="管理员" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['userinfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['userinfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['userinfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['userinfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="userinfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="用户名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="总积分" align="center" prop="totalPoints" width="100" />
      <el-table-column label="周积分" align="center" prop="weekPoints" width="100" />
      <el-table-column label="日积分" align="center" prop="dayPoints" width="100" />
      <el-table-column label="OpenID" align="center" prop="openid" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="专业" align="center" prop="major" :show-overflow-tooltip="true" />
      <el-table-column label="类型" align="center" prop="type" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.type === '1'" type="info">普通用户</el-tag>
          <el-tag v-else-if="scope.row.type === '2'" type="success">VIP用户</el-tag>
          <el-tag v-else-if="scope.row.type === '3'" type="warning">管理员</el-tag>
          <el-tag v-else type="info">{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="头像" align="center" prop="avatar" width="100">
        <template #default="scope">
          <el-image
            v-if="scope.row.avatar"
            :src="scope.row.avatar"
            :preview-src-list="[scope.row.avatar]"
            fit="cover"
            style="width: 40px; height: 40px; border-radius: 50%"
          />
          <el-avatar v-else icon="UserFilled" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="150">
        <template #default="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="150">
        <template #default="scope">
          <span>{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['userinfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['userinfo:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户信息对话框 -->
    <el-dialog :title="title" v-model="open" width="780px" append-to-body>
      <el-form ref="userinfoRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入用户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :value="dict.value"
                >{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="总积分" prop="totalPoints">
              <el-input-number v-model="form.totalPoints" :min="0" placeholder="请输入总积分" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="周积分" prop="weekPoints">
              <el-input-number v-model="form.weekPoints" :min="0" placeholder="请输入周积分" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="日积分" prop="dayPoints">
              <el-input-number v-model="form.dayPoints" :min="0" placeholder="请输入日积分" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="OpenID" prop="openid">
              <el-input v-model="form.openid" placeholder="请输入OpenID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input v-model="form.major" placeholder="请输入专业" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择用户类型">
                <el-option label="普通用户" value="1" />
                <el-option label="VIP用户" value="2" />
                <el-option label="管理员" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="头像" prop="avatar">
              <image-upload v-model="form.avatar" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password" v-if="!form.id">
              <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Userinfo">
// import { listUserinfo, getUserinfo, delUserinfo, addUserinfo, updateUserinfo } from "@/api/userinfo"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const userinfoList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    status: undefined,
    type: undefined
  },
  rules: {
    name: [
      { required: true, message: "用户名称不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "用户状态不能为空", trigger: "change" }
    ],
    type: [
      { required: true, message: "用户类型不能为空", trigger: "change" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询用户信息列表 */
function getList() {
  loading.value = true
  // 模拟数据加载延迟
  setTimeout(() => {
    // 模拟后端返回的数据结构
    const mockData = [
      {
        id: 1,
        name: '张三',
        status: '0',
        totalPoints: 1250,
        weekPoints: 180,
        dayPoints: 25,
        openid: 'wx_1234567890abcdef',
        major: '计算机科学与技术',
        type: '1',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        password: '123456',
        isDelete: '0',
        createTime: '2024-01-15 10:30:00',
        updateTime: '2024-01-20 14:20:00'
      },
      {
        id: 2,
        name: '李四',
        status: '0',
        totalPoints: 890,
        weekPoints: 120,
        dayPoints: 15,
        openid: 'wx_0987654321fedcba',
        major: '软件工程',
        type: '2',
        avatar: 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9cpng.png',
        password: '123456',
        isDelete: '0',
        createTime: '2024-01-10 09:15:00',
        updateTime: '2024-01-19 16:45:00'
      },
      {
        id: 3,
        name: '王五',
        status: '0',
        totalPoints: 2100,
        weekPoints: 250,
        dayPoints: 35,
        openid: 'wx_abcdef1234567890',
        major: '人工智能',
        type: '3',
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        password: '123456',
        isDelete: '0',
        createTime: '2024-01-05 11:20:00',
        updateTime: '2024-01-21 10:30:00'
      },
      {
        id: 4,
        name: '赵六',
        status: '1',
        totalPoints: 650,
        weekPoints: 80,
        dayPoints: 10,
        openid: 'wx_123456789abcdef0',
        major: '数据科学',
        type: '1',
        avatar: '',
        password: '123456',
        isDelete: '0',
        createTime: '2024-01-12 15:40:00',
        updateTime: '2024-01-18 13:15:00'
      },
      {
        id: 5,
        name: '孙七',
        status: '0',
        totalPoints: 1580,
        weekPoints: 200,
        dayPoints: 28,
        openid: 'wx_fedcba0987654321',
        major: '网络工程',
        type: '2',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        password: '123456',
        isDelete: '0',
        createTime: '2024-01-08 14:25:00',
        updateTime: '2024-01-20 09:50:00'
      }
    ]
    
    // 模拟搜索过滤
    let filteredData = [...mockData]
    
    if (queryParams.value.name) {
      filteredData = filteredData.filter(item => 
        item.name.includes(queryParams.value.name)
      )
    }
    
    if (queryParams.value.status !== undefined && queryParams.value.status !== '') {
      filteredData = filteredData.filter(item => 
        item.status === queryParams.value.status
      )
    }
    
    if (queryParams.value.type !== undefined && queryParams.value.type !== '') {
      filteredData = filteredData.filter(item => 
        item.type === queryParams.value.type
      )
    }
    
    // 模拟分页
    const start = (queryParams.value.pageNum - 1) * queryParams.value.pageSize
    const end = start + queryParams.value.pageSize
    userinfoList.value = filteredData.slice(start, end)
    total.value = filteredData.length
    loading.value = false
  }, 500) // 模拟网络延迟
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: undefined,
    name: undefined,
    status: "0",
    totalPoints: 0,
    weekPoints: 0,
    dayPoints: 0,
    openid: undefined,
    major: undefined,
    type: "1",
    avatar: undefined,
    password: undefined,
    isDelete: "0"
  }
  proxy.resetForm("userinfoRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加用户信息"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const id = row.id || ids.value[0]
  // 模拟获取用户详情
  const user = userinfoList.value.find(item => item.id === id)
  if (user) {
    form.value = { ...user }
    open.value = true
    title.value = "修改用户信息"
  }
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userinfoRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        // 模拟修改操作
        const index = userinfoList.value.findIndex(item => item.id === form.value.id)
        if (index !== -1) {
          userinfoList.value[index] = { ...form.value }
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        }
      } else {
        // 模拟新增操作
        const newId = Math.max(...userinfoList.value.map(item => item.id)) + 1
        const newUser = {
          ...form.value,
          id: newId,
          createTime: new Date().toLocaleString('zh-CN'),
          updateTime: new Date().toLocaleString('zh-CN')
        }
        userinfoList.value.unshift(newUser)
        proxy.$modal.msgSuccess("新增成功")
        open.value = false
        getList()
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除用户信息编号为"' + ids + '"的数据项？').then(function() {
    // 模拟删除操作
    if (Array.isArray(ids)) {
      // 批量删除
      ids.forEach(id => {
        const index = userinfoList.value.findIndex(item => item.id === id)
        if (index !== -1) {
          userinfoList.value.splice(index, 1)
        }
      })
    } else {
      // 单个删除
      const index = userinfoList.value.findIndex(item => item.id === ids)
      if (index !== -1) {
        userinfoList.value.splice(index, 1)
      }
    }
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  // 模拟导出操作
  const data = userinfoList.value.map(item => ({
    'ID': item.id,
    '用户名称': item.name,
    '状态': item.status === '0' ? '启用' : '停用',
    '总积分': item.totalPoints,
    '周积分': item.weekPoints,
    '日积分': item.dayPoints,
    'OpenID': item.openid,
    '专业': item.major,
    '类型': item.type === '1' ? '普通用户' : item.type === '2' ? 'VIP用户' : '管理员',
    '创建时间': item.createTime
  }))
  
  // 创建CSV内容
  const headers = Object.keys(data[0]).join(',')
  const csvContent = [headers, ...data.map(row => Object.values(row).join(','))].join('\n')
  
  // 下载文件
  const blob = new Blob(['\ufeff' + csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  link.setAttribute('href', url)
  link.setAttribute('download', `userinfo_${new Date().getTime()}.csv`)
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  
  proxy.$modal.msgSuccess("导出成功")
}

getList()
</script>

<style scoped>
.app-container {
  padding: 20px;
}
</style>