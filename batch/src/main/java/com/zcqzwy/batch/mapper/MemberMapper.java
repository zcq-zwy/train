package com.zcqzwy.batch.mapper;


import com.zcqzwy.batch.domain.Member;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper {

  int deleteByPrimaryKey(Long id);

  int insert(Member record);

  int insertSelective(Member record);

  Member selectByPrimaryKey(Long id);

  List<Member> selectAllByMobile(@Param("mobile")String mobile);



  int updateByPrimaryKeySelective(Member record);

  int updateByPrimaryKey(Member record);

  int count();
}