package com.zcqzwy.member.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.zcqzwy.member.domain.Member;

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