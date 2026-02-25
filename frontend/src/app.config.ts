export default defineAppConfig({
  pages: [
    'pages/login/index',             // 1. 首页设为登录页
    'pages/my-appointments/index',   // 2. 我的预约列表
    'pages/create-appointment/index' // 3. 新建预约
  ],
  window: {
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#fff',
    navigationBarTitleText: '日程预约系统',
    navigationBarTextStyle: 'black'
  }
});