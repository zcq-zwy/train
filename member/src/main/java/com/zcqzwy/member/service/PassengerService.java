package com.zcqzwy.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.zcqzwy.common.util.SnowUtil;
import com.zcqzwy.member.req.PassengerSaveReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.zcqzwy.member.mapper.PassengerMapper;
import com.zcqzwy.member.domain.Passenger;
@Service
public class PassengerService{

    @Resource
    private PassengerMapper passengerMapper;

    
    public int deleteByPrimaryKey(Long id) {
        return passengerMapper.deleteByPrimaryKey(id);
    }

    
    public int save(PassengerSaveReq record) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(record, Passenger.class);
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        return passengerMapper.insert(passenger);
    }

    
    public int insertSelective(Passenger record) {
        return passengerMapper.insertSelective(record);
    }

    
    public Passenger selectByPrimaryKey(Long id) {
        return passengerMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(Passenger record) {
        return passengerMapper.updateByPrimaryKeySelective(record);
    }

    
    public int updateByPrimaryKey(Passenger record) {
        return passengerMapper.updateByPrimaryKey(record);
    }

}
