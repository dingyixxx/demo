// config/dev.ts
import type { UserConfigExport } from "@tarojs/cli";

export default {
  logger: {
    quiet: false,
    stats: true
  },
  mini: {},
  h5: {
    // 【关键配置】开发服务器代理
    devServer: {
      port: 10086, // 前端端口，可自定义
      host: '0.0.0.0',
      proxy: {
        // 所有以 /api 开头的请求，都会转发到 localhost:8080
        '/api': {
          target: 'http://localhost:8080',
          changeOrigin: true, // 必须为 true，否则后端可能识别不到 Host
          secure: false,      // 如果是 https 需要配，http 不需要
          pathRewrite: {
            '^/api': '/api'   // 保持路径不变，如果后端没有 /api 前缀则改为 ''
          }
        }
      }
    }
  }
} as UserConfigExport;