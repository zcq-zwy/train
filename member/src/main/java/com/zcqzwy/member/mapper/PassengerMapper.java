package com.zcqzwy.member.mapper;

import com.zcqzwy.member.domain.Passenger;

public interface PassengerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Passenger record);

    int insertSelective(Passenger record);

    Passenger selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Passenger record);

    int updateByPrimaryKey(Passenger record);
}