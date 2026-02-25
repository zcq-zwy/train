package com.zcqzwy.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcqzwy.common.context.LoginMemberContext;
import com.zcqzwy.common.resp.PageResp;
import com.zcqzwy.common.util.SnowUtil;
import com.zcqzwy.member.req.PassengerQueryReq;
import com.zcqzwy.member.req.PassengerSaveReq;
import com.zcqzwy.member.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.zcqzwy.member.mapper.PassengerMapper;
import com.zcqzwy.member.domain.Passenger;
@Service
public class PassengerService{


    private static final Logger LOG = LoggerFactory.getLogger(PassengerService.class);

    @Resource
    private PassengerMapper passengerMapper;


    public int deleteByPrimaryKey(Long id) {
        return passengerMapper.deleteByPrimaryKey(id);
    }

    
    public int save(PassengerSaveReq record) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(record, Passenger.class);
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setMemberId(LoginMemberContext.getId());
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

    public PageResp<PassengerQueryResp> queryList(PassengerQueryReq passengerQueryReq) {
        Passenger passenger = BeanUtil.copyProperties(passengerQueryReq, Passenger.class);
        LOG.info("查询页码：{}", passengerQueryReq.getPage());
        LOG.info("每页条数：{}", passengerQueryReq.getSize());
        PageHelper.startPage(passengerQueryReq.getPage(), passengerQueryReq.getSize());
        List<Passenger> passengers = passengerMapper.selectByAll(passenger);

        PageInfo<Passenger> pageInfo = new PageInfo<>(passengers);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
        List<PassengerQueryResp> passengerQueryResps = BeanUtil.copyToList(passengers,
            PassengerQueryResp.class);
        PageResp<PassengerQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(passengerQueryResps);
        return pageResp;

    }


}
