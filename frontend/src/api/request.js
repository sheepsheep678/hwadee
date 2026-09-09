import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

request.interceptors.request.use(
  (config) => {
    // 老人端接口使用 elderAccessToken，其余（医生端等）使用 accessToken
    const isElder = (config.url || '').startsWith('/elder')

    const token = localStorage.getItem(isElder ? 'elderAccessToken' : 'accessToken')

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

request.interceptors.response.use(
  (response) => {
    const result = response.data

    if (result.code === 200) {
      return result
    }

    ElMessage.error(result.message || '请求失败')
    return Promise.reject(new Error(result.message || '请求失败'))
  },
  (error) => {
    ElMessage.error(error.response?.data?.message || '服务器连接失败')
    return Promise.reject(error)
  },
)

export default request
