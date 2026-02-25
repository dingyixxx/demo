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
        const body: any = res.data || {};

        // 1. 处理未登录 / Token 失效（兼容 HTTP 401 或 200 + 业务 code 401）
        const isUnauthorized =
          res.statusCode === 401 || body?.code === 401;

        if (isUnauthorized) {
          // 清除本地 token
          Taro.removeStorageSync('token');
          Taro.showToast({
            title: body?.message || '登录状态已失效，请重新登录',
            icon: 'none',
          });

          // 跳转到登录页
          setTimeout(() => {
            Taro.reLaunch({ url: '/pages/login/index' });
          }, 500);

          reject(body);
          return;
        }

        // 2. 正常返回（HTTP 200 并且业务 code 为 200 或没有 code 字段）
        if (res.statusCode === 200 && (body.code === 200 || body.code === undefined)) {
          resolve(body);
          return;
        }

        // 3. 其它错误情况
        Taro.showToast({ title: body?.message || '请求失败', icon: 'none' });
        reject(body);
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