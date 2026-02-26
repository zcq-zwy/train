package com.zcqzwy.business.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

import com.zcqzwy.business.domain.TrainSeat;

public interface TrainSeatMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TrainSeat record);

    int insertSelective(TrainSeat record);

    TrainSeat selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TrainSeat record);

    int updateByPrimaryKey(TrainSeat record);

    List<TrainSeat> selectByAllOrderByTrainCodeAndCarriageIndexAndCarriageSeatIndex(TrainSeat trainSeat);

    int deleteByTrainCode(@Param("trainCode")String trainCode);


     List<TrainSeat> selectByTrainCodeOrderById(@Param("trainCode")String trainCode);



}