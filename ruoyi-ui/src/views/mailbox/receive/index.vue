<template>
  <div id="app" class="foxmail-container">
    <!-- 邮箱列表 -->
    <el-card class="email-list-card" :style="{ width: '23%' }">
      <div slot="header" class="email-list-header">
        邮箱列表
        <el-button type="primary" class="add-button" @click="pullEmail">拉取</el-button>
        <el-button type="primary" class="add-button" @click="showAddDialog">+</el-button>
      </div>

      <el-menu default-active="inbox" class="email-menu">
        <el-menu-item index="inbox" v-for="email in emails" :key="email.id" @click="handleEmailClick(email.id)">
          {{ email.name }}
        </el-menu-item>
      </el-menu>
    </el-card>

    <!-- 邮件列表 -->
    <el-card class="mail-list-card" :style="{ width: '33%' }">
      <div slot="header" class="mail-list-header">邮件列表</div>
      <el-scrollbar wrap-class="mail-list-scrollbar">
        <el-menu default-active="1" class="mail-menu" @select="handleMailClick">
          <el-menu-item v-for="mail in mails" :key="mail.id" :index="mail.id"  @click="handleMailClick(mail.id)">
            {{ mail.title }}
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-card>

    <!-- 邮件内容 -->
    <el-card class="mail-content-card" :style="{ width: '43%' }">
      <div slot="header" class="mail-content-header">邮件内容</div>
      <div class="mail-content">
        <h3>{{ selectedMail.title }}</h3>
        <p>发件人：{{ selectedMail.fromer }}</p>
        <p>日期：{{ selectedMail.sendDate }}</p>
        <hr>
        <div v-html="selectedMail.content"></div>
      </div>
    </el-card>

    <!-- 表单弹窗 -->
    <el-dialog title="添加邮箱" :visible.sync="dialogVisible" width="40%">
      <el-form ref="emailForm" :model="form" :rules="formRules" label-width="80px">
        <el-col :span="22">
          <el-form-item label="账号" prop="email" required>
            <el-input v-model="form.email" placeholder="请输入账号" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="22">
          <el-form-item label="密码" prop="password" required>
            <el-input v-model="form.password" placeholder="请输入密码" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务器" prop="host" required>
            <el-input v-model="form.host" placeholder="请输入服务器" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="SSL" prop="hasSSL">
            <el-switch v-model="form.hasSsl"></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="端口" prop="port" required>
            <el-input v-model="form.port" placeholder="请输入端口" :style="{width: '100%'}">
            </el-input>
          </el-form-item>
        </el-col>
        <el-form-item>
          <el-button type="primary" @click="saveEmail">保存</el-button>
          <el-button @click="closeDialog">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {addTask, headerDetail, headerList, listTask, pullEmail} from "@/api/mailbox/task";
import { getToken } from "@/utils/auth";

export default {
  data() {
    return {
      emails: [],
      mails: [],
      selectedMail: {},
      dialogVisible: false,
      form: {
        email: "",
        password: "",
        host: "",
        hasSsl: false,
        port: "",
      },
      formRules: {
        email: [
          { required: true, message: "请输入账号", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
        ],
        host: [
          { required: true, message: "请输入服务器", trigger: "blur" },
        ],
        port: [
          { required: true, message: "请输入端口", trigger: "blur" },
        ],
      },
    }
  },
  methods: {
    handleEmailClick(emailId) {
      console.log("Clicked email ID:", emailId);
      headerList(emailId).then((response) => {
        this.mails = response.rows;
        this.mails.forEach((mail) => {
          mail.id = mail.id;
          mail.title = mail.title;
        });
      });
    },
    handleMailClick(mailId) {
      console.log("Clicked mail ID:", mailId);
      headerDetail(mailId).then((response) => {
        this.selectedMail = response.data;
        selectedMail.subject = selectedMail.title;
      });
    },
    showAddDialog() {
      this.dialogVisible = true;
    },
    closeDialog() {
      this.dialogVisible = false;
    },
    saveEmail() {
      this.$refs.emailForm.validate((valid) => {
        if (valid) {
          // Validation passed, proceed with form submission
          addTask(this.form).then((response) => {
            this.$message.success("新增成功");
            this.closeDialog();
          });
        } else {
          // Validation failed, display error messages
          this.$message.error("请填写正确的表单信息");
        }
      });
    },
    pullEmail() {
      pullEmail().then((response) => {
        this.$message.success("拉取成功");
      });
    }
  },
  created() {
    listTask().then((response) => {
      this.emails = response.rows;
      this.emails.forEach((email) => {
        email.id = email.id;
        email.name = email.email;
      });
    });
  }
};
</script>

<style scoped>
.foxmail-container {
  display: flex;
}

.email-list-card,
.mail-list-card,
.mail-content-card {
  margin-right: 10px;
}

.email-list-header,
.mail-list-header,
.mail-content-header {
  font-size: 18px;
  font-weight: bold;
  padding: 10px;
}

.email-menu,
.mail-menu {
  border: none;
  max-height: 400px;
  overflow-y: auto;
}

.mail-list-scrollbar {
  height: 100%;
}

.mail-content {
  padding: 10px;
}

.add-button {
  float: right;
}

.el-col {
  float: none;
  display: inline-block;
}
</style>
