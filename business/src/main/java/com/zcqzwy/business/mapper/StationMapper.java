package com.zcqzwy.business.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Date;

import com.zcqzwy.business.domain.Station;

public interface StationMapper {

  int deleteByPrimaryKey(Long id);

  int insert(Station record);

  int insertSelective(Station record);

  Station selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(Station record);

  int updateByPrimaryKey(Station record);

  List<Station> selectByAllOrderByNamePy(Station station);

  List<Station> selectAllOrderByNamePinyin();

  List<Station> selectAllByName(@Param("name")String name);

  List<Station> selectallOrderByIdDesc();

  List<Station> selectByAllOrderByIdDesc(Station station);










}