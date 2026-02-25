import { View, Text, Button } from '@tarojs/components';
import { useEffect, useState } from 'react';
import Taro, { useDidShow } from '@tarojs/taro';
import { get } from '../../utils/request';
import './index.scss'; // 引入样式

export default function MyAppointments() {
  const [appointments, setAppointments] = useState([]);

  // 页面加载时获取列表
  useEffect(() => {
    fetchList();
  }, []);

  // 页面每次显示时刷新列表（从新建页面返回后会触发）
  useDidShow(() => {
    fetchList();
  });

  const fetchList = async () => {
    try {
      const res: any = await get('/api/appointments');
      if (res.code === 200) {
        setAppointments(res.data);
      }
    } catch (e) {
      Taro.showToast({ title: '加载失败', icon: 'none' });
    }
  };

  const handleCreate = () => {
    // 跳转到新建预约页面，由新页面填写服务名称、日期和时间段后再提交
    Taro.navigateTo({
      url: '/pages/create-appointment/index',
    });
  };

  return (
    <View className="page-container">
      {/* 头部区域 */}
      <View className="header">
        <Text className="title">我的预约</Text>
        <Button className="add-btn" onClick={handleCreate}>+ 新建</Button>
      </View>

      {/* 列表区域 */}
      <View className="list-container">
        {appointments.length > 0 ? (
          appointments.map((item: any) => (
            <View key={item.id} className="appointment-card">
              <View className="service-name">
                {item.serviceName}
                {item.status === 'BOOKED' && <Text className="status-tag">已预约</Text>}
              </View>
              <View className="time-info">
                <Text className="date">{item.appointmentDate}</Text>
                <Text>{item.timeSlot}</Text>
              </View>
            </View>
          ))
        ) : (
          <View className="empty-state">
            <Text>暂无预约记录</Text>
          </View>
        )}
      </View>
    </View>
  );
}