<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户名称
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />手机号码
                <div class="pull-right">{{ user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email" />用户邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree"/>
                所属部门
                <div v-if="user.dept" class="pull-right">
                  {{ user.dept.deptName }} / {{ postGroup }}
                </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples"/>
                所属角色
                <div class="pull-right">{{ roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date"/>
                创建日期
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date"/>
                最后登录
                <div class="pull-right">
                  {{ parseTime(user.loginDate) }} / {{ user.loginIp }}
                </div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card style="max-height: 58px">
          <el-tag
          >账户余额
            <span>
              <count-to
                :decimals="2"
                :duration="2600"
                :end-val="user.availablePayBalance"
                :start-val="0"
                class="card-num"
                prefix="￥ "
              />
            </span>
            <!-- <el-divider direction="vertical"></el-divider>
            账户余额(赠)
            <span>
              <count-to
                :decimals="2"
                :duration="2600"
                :end-val="user.availableFreeBalance"
                :start-val="0"
                class="card-num"
                prefix="￥ "
              />
            </span> -->
          </el-tag>
          <el-divider direction="vertical"></el-divider>
          <el-link type="primary">充值</el-link>
          <el-divider direction="vertical"></el-divider>
          <el-link type="primary">提现</el-link>
          <el-tag style="margin-left: 50px" type="info">
            账户余额冻结
            <span>
              <count-to
                :decimals="2"
                :duration="2600"
                :end-val="user.freezeFreeBalance"
                :start-val="0"
                class="card-num"
                prefix="￥ "
              />
            </span>
            <!-- <el-divider direction="vertical"></el-divider>
            账户余额冻结(赠)
            <span>
              <count-to
                :decimals="2"
                :duration="2600"
                :end-val="user.freezeFreeBalance"
                :start-val="0"
                class="card-num"
                prefix="￥ "
              />
            </span> -->
          </el-tag>
        </el-card>
        <el-card style="margin-top: 15px">
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd :user="user"/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import {getUserProfile} from "@/api/system/user";
import CountTo from "vue-count-to";

export default {
  name: "Profile",
  components: {userAvatar, userInfo, resetPwd, CountTo},
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo",
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then((response) => {
        this.user = response.data;
        this.roleGroup = response.roleGroup;
        this.postGroup = response.postGroup;
      });
    },
  },
};
</script>
