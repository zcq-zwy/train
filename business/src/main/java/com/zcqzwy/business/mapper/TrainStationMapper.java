package com.zcqzwy.business.mapper;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.zcqzwy.business.domain.TrainStation;

public interface TrainStationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TrainStation record);

    int insertSelective(TrainStation record);

    TrainStation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TrainStation record);

    int updateByPrimaryKey(TrainStation record);

    List<TrainStation> selectByIndexAndTrainCode(@Param("index")Integer index,@Param("trainCode")String trainCode);


    List<TrainStation> selectByTrainCodeAndName(@Param("trainCode")String trainCode,@Param("name")String name);


   List<TrainStation> selectByAllOrderByTrainCodeAndIndex(TrainStation trainStation);

   List<TrainStation> selectByTrainCodeOrderByIndex(@Param("trainCode")String trainCode);










}