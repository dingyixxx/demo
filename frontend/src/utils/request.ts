import Taro from '@tarojs/taro';

// 【新增】定义后端基础地址
// 小程序开发环境直接写死 localhost:8080
// 注意：真机调试时，localhost 需要换成你电脑的局域网 IP (如 192.168.1.x)
const BASE_URL = 'http://localhost:8080'; 

const request = (options: any) => {
  const { url, method, data, header = {} } = options;

  // 【修改点】拼接完整 URL
  // 如果 url 已经是 http 开头，则直接用；否则加上 BASE_URL
  const fullUrl = url.startsWith('http') ? url : `${BASE_URL}${url}`;

  // 1. 从本地存储获取 Token
  const token = Taro.getStorageSync('token');

  // 2. 构建 Headers
  const headers = {
    'Content-Type': 'application/json',
    ...header,
  };

  // 如果有 token，添加到 Authorization 头
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  return new Promise((resolve, reject) => {
    Taro.request({
      url: fullUrl, // 【关键】使用拼接后的完整地址，例如 http://localhost:8080/api/login
      method: method || 'GET',
      data: data,
      header: headers,
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          Taro.showToast({ title: res.data?.message || '请求失败', icon: 'none' });
          reject(res.data);
        }
      },
      fail: (err) => {
        console.error('Request error:', err);
        Taro.showToast({ title: '网络错误或后端未启动', icon: 'none' });
        reject(err);
      }
    });
  });
};

export const get = (url: string, data?: any) => request({ url, method: 'GET', data });
export const post = (url: string, data?: any) => request({ url, method: 'POST', data });
export const put = (url: string, data?: any) => request({ url, method: 'PUT', data });
export const del = (url: string, data?: any) => request({ url, method: 'DELETE', data });

export default request;