package com.zcqzwy.business.mapper;
import java.util.Date;

import com.zcqzwy.business.domain.TrainCarriage;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TrainCarriageMapper {

  int deleteByPrimaryKey(Long id);

  int insert(TrainCarriage record);

  int insertSelective(TrainCarriage record);

  TrainCarriage selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(TrainCarriage record);

  int updateByPrimaryKey(TrainCarriage record);

  List<TrainCarriage> selectByTrainCodeAndIndex(@Param("trainCode")String trainCode,@Param("index")Integer index);

  List<TrainCarriage> selectbyallOrderByTrainCodeAndIndex(TrainCarriage trainCarriage);


   List<TrainCarriage> selectByTrainCodeOrderByIndex(@Param("trainCode")String trainCode);







}