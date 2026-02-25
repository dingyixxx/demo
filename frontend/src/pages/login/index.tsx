import { View, Input, Button, Text } from '@tarojs/components';
import { useState } from 'react';
import Taro from '@tarojs/taro';
import { post } from '../../utils/request';
import './index.scss'; // 【关键】确保引入了样式文件

export default function Login() {
  const [phone, setPhone] = useState('');
  const [code, setCode] = useState('');

  const handleLogin = async () => {
    if (!phone || !code) {
      Taro.showToast({ title: '请填写完整信息', icon: 'none' });
      return;
    }
    try {
      const res: any = await post('/api/login', { phone, code });
      if (res.code === 200) {
        Taro.setStorageSync('token', res.data);
        Taro.showToast({ title: '登录成功', icon: 'success' });
        setTimeout(() => Taro.redirectTo({ url: '/pages/my-appointments/index' }), 1000);
      } else {
        Taro.showToast({ title: res.message, icon: 'none' });
      }
    } catch (e) {
      Taro.showToast({ title: '网络错误', icon: 'none' });
    }
  };

  return (
    // 【关键】最外层 View 加上 className
    <View className="login-container">
      <Text className="title">欢迎登录</Text>
      
      <Input 
        type="number" 
        placeholder="请输入手机号" 
        value={phone} 
        onInput={(e) => setPhone(e.detail.value)} 
      />
      
      <Input 
        type="number" 
        placeholder="验证码 (123456)" 
        value={code} 
        onInput={(e) => setCode(e.detail.value)} 
      />
      
      <Button onClick={handleLogin}>登 录</Button>
    </View>
  );
}