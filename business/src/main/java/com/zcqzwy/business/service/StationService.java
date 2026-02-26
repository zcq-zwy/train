package com.zcqzwy.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcqzwy.business.req.StationQueryReq;
import com.zcqzwy.business.req.StationSaveReq;
import com.zcqzwy.business.resp.StationQueryResp;
import com.zcqzwy.common.exception.BusinessException;
import com.zcqzwy.common.exception.BusinessExceptionEnum;
import com.zcqzwy.common.resp.PageResp;
import com.zcqzwy.common.util.SnowUtil;
import jakarta.annotation.Resource;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zcqzwy.business.mapper.StationMapper;
import com.zcqzwy.business.domain.Station;
@Service
public class StationService{

    @Resource
    private StationMapper stationMapper;

  private static final Logger LOG = LoggerFactory.getLogger(StationService.class);

    


  public List<StationQueryResp> queryAll() {
      List<Station> stations = stationMapper.selectAllOrderByNamePinyin();
      return BeanUtil.copyToList(stations, StationQueryResp.class);
  }

  public void delete(Long id) {
    stationMapper.deleteByPrimaryKey(id);
  }

  private Station selectByUnique(String name) {
    List<Station> list = stationMapper.selectAllByName(name);
    if (CollUtil.isNotEmpty(list)) {
      return list.get(0);
    } else {
      return null;
    }
  }


  public void save(StationSaveReq req) {
    DateTime now = DateTime.now();
    Station station = BeanUtil.copyProperties(req, Station.class);
    if (ObjectUtil.isNull(station.getId())) {

      // 保存之前，先校验唯一键是否存在
      Station stationDB = selectByUnique(req.getName());
      if (ObjectUtil.isNotEmpty(stationDB)) {
        throw new BusinessException(BusinessExceptionEnum.BUSINESS_STATION_NAME_UNIQUE_ERROR);
      }

      station.setId(SnowUtil.getSnowflakeNextId());
      station.setCreateTime(now);
      station.setUpdateTime(now);
      stationMapper.insert(station);
    } else {
      station.setUpdateTime(now);
      stationMapper.updateByPrimaryKey(station);
    }
  }


  public PageResp<StationQueryResp> queryList(StationQueryReq req) {


    LOG.info("查询页码：{}", req.getPage());
    LOG.info("每页条数：{}", req.getSize());
    PageHelper.startPage(req.getPage(), req.getSize());
    Station station = new Station();
    BeanUtil.copyProperties(req, station);
    List<Station> stationList = stationMapper.selectByAllOrderByIdDesc(station);

    PageInfo<Station> pageInfo = new PageInfo<>(stationList);
    LOG.info("总行数：{}", pageInfo.getTotal());
    LOG.info("总页数：{}", pageInfo.getPages());

    List<StationQueryResp> list = BeanUtil.copyToList(stationList, StationQueryResp.class);

    PageResp<StationQueryResp> pageResp = new PageResp<>();
    pageResp.setTotal(pageInfo.getTotal());
    pageResp.setList(list);
    return pageResp;
  }
}
