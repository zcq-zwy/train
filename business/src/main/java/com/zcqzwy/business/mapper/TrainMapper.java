package com.zcqzwy.business.mapper;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.zcqzwy.business.domain.Train;

public interface TrainMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Train record);

    int insertSelective(Train record);

    Train selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Train record);

    int updateByPrimaryKey(Train record);

    List<Train> selectByCode(@Param("code")String code);

    List<Train> selectByAllOrderByCode(Train train);

    List<Train> selectOrderByCode();






}