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



