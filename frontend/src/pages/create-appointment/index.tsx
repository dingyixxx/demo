import { View, Input, Button, Picker, RadioGroup, Radio, Label, Text } from '@tarojs/components';
import { useEffect, useState } from 'react';
import Taro from '@tarojs/taro';
import { get, post } from '../../utils/request';
import './index.scss';

export default function Appointment() {
  const [serviceName, setServiceName] = useState('');
  const [date, setDate] = useState('2026-03-01');
  const [timeSlot, setTimeSlot] = useState('10:00-11:00');
  const [appointments, setAppointments] = useState<any[]>([]);

  const timeSlots = [
    '09:00-10:00',
    '10:00-11:00',
    '11:00-12:00',
    '14:00-15:00',
    '15:00-16:00',
  ];

  // 加载当前用户已有的预约，用于前端做时间冲突校验
  useEffect(() => {
    const fetchMyAppointments = async () => {
      try {
        const res: any = await get('/api/appointments');
        if (res.code === 200) {
          setAppointments(res.data || []);
        }
      } catch (e) {
        // 如果未登录等，这里先忽略，让后端在提交时再校验
      }
    };

    fetchMyAppointments();
  }, []);

  // 判断某个时间段在当前日期是否已被预约
  const isSlotDisabled = (slot: string) =>
    appointments.some(
      (item: any) =>
        item.appointmentDate === date &&
        item.timeSlot === slot &&
        item.status === 'BOOKED'
    );

  // 当日期或预约列表变化时，如果当前选中的时间段已被占用，则自动切换到第一个可用时间段
  useEffect(() => {
    const currentDisabled = isSlotDisabled(timeSlot);
    if (!currentDisabled) {
      return;
    }
    const firstAvailable = timeSlots.find((slot) => !isSlotDisabled(slot));
    if (firstAvailable) {
      setTimeSlot(firstAvailable);
    }
  }, [date, appointments]);

  const handleCreate = async () => {
    if (!serviceName.trim()) {
      Taro.showToast({ title: '请填写服务名称', icon: 'none' });
      return;
    }

    // 前端时间冲突校验：同一天、相同时间段且已是 BOOKED 状态
    const hasConflict = isSlotDisabled(timeSlot);

    if (hasConflict) {
      Taro.showToast({ title: '该时间段已有预约，请选择其他时间', icon: 'none' });
      return;
    }

    try {
      // 调用后端接口：POST /api/appointments
      const res: any = await post('/api/appointments', {
        serviceName,
        date,
        timeSlot
      });

      if (res.code === 200) {
        Taro.showToast({ title: '预约成功', icon: 'success' });
        // 返回“我的预约”页面并刷新
        setTimeout(() => {
          Taro.navigateBack();
        }, 500);
      } else {
        Taro.showToast({ title: res.message, icon: 'none' });
      }
    } catch (e) {
      // 捕获重复提交的错误
      const msg = e?.message || '操作失败';
      Taro.showToast({ title: msg, icon: 'none' });
    }
  };

  return (
    <View className="container">
      <View className="form">
        {/* 服务名称 */}
        <Text className="label">服务名称</Text>
        <Input
          className="input"
          value={serviceName}
          onInput={(e) => setServiceName(e.detail.value)}
          placeholder="请输入服务名称，例如：洗牙"
        />

        {/* 预约日期 */}
        <Text className="label">预约日期</Text>
        <Picker
          mode="date"
          value={date}
          onChange={(e) => setDate(e.detail.value)}
        >
          <View className="picker">
            {date || '请选择日期'}
          </View>
        </Picker>

        {/* 时间段选择（单选框） */}
        <Text className="label">预约时间段</Text>
        <RadioGroup
          onChange={(e) => setTimeSlot(e.detail.value as string)}
          className="radio-group"
        >
          {timeSlots.map((slot) => {
            const disabled = isSlotDisabled(slot);
            const checked = slot === timeSlot && !disabled;
            return (
              <Label
                key={slot}
                className={`radio-item ${disabled ? 'radio-item--disabled' : ''}`}
              >
                <Radio value={slot} checked={checked} disabled={disabled} />
                <Text
                  className={`radio-text ${
                    disabled ? 'radio-text--disabled' : ''
                  }`}
                >
                  {slot}
                  {disabled ? '（已约满）' : ''}
                </Text>
              </Label>
            );
          })}
        </RadioGroup>

        <Button className="btn" type="primary" onClick={handleCreate}>
          提交预约
        </Button>
      </View>
    </View>
  );
}