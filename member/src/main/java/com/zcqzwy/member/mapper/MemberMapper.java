package com.zcqzwy.member.mapper;

import com.zcqzwy.member.domain.Member;

public interface MemberMapper {

  int deleteByPrimaryKey(Long id);

  int insert(Member record);

  int insertSelective(Member record);

  Member selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(Member record);

  int updateByPrimaryKey(Member record);

  int count();
}