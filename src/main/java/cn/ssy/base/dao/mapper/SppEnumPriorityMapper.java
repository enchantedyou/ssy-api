package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.SppEnumPriority;

import java.util.List;

public interface SppEnumPriorityMapper {
    int deleteByPrimaryKey(String enumType);

    int insert(SppEnumPriority record);

    int insertSelective(SppEnumPriority record);

    SppEnumPriority selectByPrimaryKey(String enumType);

    int updateByPrimaryKeySelective(SppEnumPriority record);

    int updateByPrimaryKey(SppEnumPriority record);

    List<SppEnumPriority> selectAll();
}