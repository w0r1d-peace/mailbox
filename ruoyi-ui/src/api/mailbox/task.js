import request from '@/utils/request'

// 查询邮箱任务列表
export function listTask(query) {
  return request({
    url: '/mailbox/task/list',
    method: 'get',
    params: query
  })
}

// 新增邮箱任务
export function addTask(data) {
  return request({
    url: '/mailbox/task/add',
    method: 'post',
    data: data
  })
}

// 拉取邮件
export function pullEmail() {
  return request({
    url: '/mailbox/task/pull',
    method: 'post'
  })
}

// 查询邮箱邮件列表
export function headerList(query) {
  return request({
    url: '/mailbox/task/header/list',
    method: 'get',
    params: query
  })
}

// 查询邮箱邮件详情
export function headerDetail(headerId) {
  return request({
    url: '/mailbox/task/header/detail/' + headerId,
    method: 'get'
  })
}

// 发送邮件
export function sendMail(data) {
  return request({
    url: '/mailbox/task/mail/send',
    method: 'post',
    data: data
  })
}

