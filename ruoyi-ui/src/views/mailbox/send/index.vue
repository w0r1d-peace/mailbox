<template>
  <div>
    <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
      <el-form-item label="发件人" prop="taskId">
        <el-select v-model="formData.taskId" placeholder="请选择发件人" clearable :style="{width: '100%'}">
          <el-option v-for="(item, index) in taskIdOptions" :key="index" :label="item.email" :value="item.id" :disabled="item.disabled"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="收件人" prop="receiver">
        <el-input v-model="formData.receiver" placeholder="请输入收件人" clearable :style="{width: '100%'}">
        </el-input>
      </el-form-item>
      <el-form-item label="主题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入主题" clearable :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input v-model="formData.content" type="textarea" placeholder="请输入内容"
                  :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item size="large">
        <el-button type="primary" @click="sendMail">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>

import {sendMail, listTask} from "@/api/mailbox/task";
import { getToken } from "@/utils/auth";
export default {
  components: {},
  props: [],
  data() {
    return {
      formData: {
        taskId: undefined,
        receiver: undefined,
        title: undefined,
        content: undefined,
      },
      rules: {
        taskId: [{
          required: true,
          message: '请选择发件人',
          trigger: 'change'
        }],
        receiver: [{
          required: true,
          message: '请输入收件人',
          trigger: 'blur'
        }],
        title: [{
          required: true,
          message: '请输入主题',
          trigger: 'blur'
        }],
        content: [{
          required: true,
          message: '请输入内容',
          trigger: 'blur'
        }],
      },
      taskIdOptions: [],
    }
  },
  computed: {},
  watch: {},
  mounted() {},
  methods: {
    sendMail() {
      this.$refs['elForm'].validate(valid => {
        if (valid) {
          // Validation passed, proceed with form submission
          sendMail(this.formData).then((response) => {
            this.$message.success("发送成功");
            this.closeDialog();
          });
        } else {
          // Validation failed, display error messages
          this.$message.error("请填写正确的表单信息");
        }
      })
    },
    resetForm() {
      this.$refs['elForm'].resetFields()
    }
  },
  created() {
    listTask().then((response) => {
      this.taskIdOptions = response.rows;
    });
  }
}

</script>
<style>
</style>
