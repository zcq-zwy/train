package com.zcqzwy.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zcqzwy.business.enums.SeatColEnum;
import com.zcqzwy.business.req.TrainCarriageQueryReq;
import com.zcqzwy.business.req.TrainCarriageSaveReq;
import com.zcqzwy.business.resp.TrainCarriageQueryResp;
import com.zcqzwy.common.exception.BusinessException;
import com.zcqzwy.common.exception.BusinessExceptionEnum;
import com.zcqzwy.common.resp.PageResp;
import com.zcqzwy.common.util.SnowUtil;
import jakarta.annotation.Resource;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.zcqzwy.business.mapper.TrainCarriageMapper;
import com.zcqzwy.business.domain.TrainCarriage;

@Service
public class TrainCarriageService {

  private static final Logger LOG = LoggerFactory.getLogger(TrainCarriageService.class);

  @Resource
  private TrainCarriageMapper trainCarriageMapper;


  public void save(TrainCarriageSaveReq req) {
    DateTime now = DateTime.now();

    // 自动计算出列数和总座位数
    List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
    req.setColCount(seatColEnums.size());
    req.setSeatCount(req.getColCount() * req.getRowCount());

    TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);
    if (ObjectUtil.isNull(trainCarriage.getId())) {

      // 保存之前，先校验唯一键是否存在
      TrainCarriage trainCarriageDB = selectByUnique(req.getTrainCode(), req.getIndex());
      if (ObjectUtil.isNotEmpty(trainCarriageDB)) {
        throw new BusinessException(
            BusinessExceptionEnum.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
      }

      trainCarriage.setId(SnowUtil.getSnowflakeNextId());
      trainCarriage.setCreateTime(now);
      trainCarriage.setUpdateTime(now);
      trainCarriageMapper.insert(trainCarriage);
    } else {
      trainCarriage.setUpdateTime(now);
      trainCarriageMapper.updateByPrimaryKey(trainCarriage);
    }
  }

  private TrainCarriage selectByUnique(String trainCode, Integer index) {
    List<TrainCarriage> list = trainCarriageMapper.selectByTrainCodeAndIndex(trainCode, index);
    if (CollUtil.isNotEmpty(list)) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public PageResp<TrainCarriageQueryResp> queryList(TrainCarriageQueryReq req) {

    LOG.info("查询页码：{}", req.getPage());
    LOG.info("每页条数：{}", req.getSize());
    PageHelper.startPage(req.getPage(), req.getSize());
    TrainCarriage trainCarriage = new TrainCarriage();
    BeanUtil.copyProperties(req, trainCarriage);
    List<TrainCarriage> trainCarriageList = trainCarriageMapper.selectbyallOrderByTrainCodeAndIndex(trainCarriage);

    PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriageList);
    LOG.info("总行数：{}", pageInfo.getTotal());
    LOG.info("总页数：{}", pageInfo.getPages());

    List<TrainCarriageQueryResp> list = BeanUtil.copyToList(trainCarriageList,
        TrainCarriageQueryResp.class);

    PageResp<TrainCarriageQueryResp> pageResp = new PageResp<>();
    pageResp.setTotal(pageInfo.getTotal());
    pageResp.setList(list);
    return pageResp;
  }

  public void delete(Long id) {
    trainCarriageMapper.deleteByPrimaryKey(id);
  }

  public List<TrainCarriage> selectByTrainCode(String trainCode) {

    return trainCarriageMapper.selectByTrainCodeOrderByIndex(trainCode);
  }

  public int deleteByPrimaryKey(Long id) {
    return trainCarriageMapper.deleteByPrimaryKey(id);
  }

  public int insert(TrainCarriage record) {
    return trainCarriageMapper.insert(record);
  }

}

