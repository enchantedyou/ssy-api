package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.SppEnumPriority;

public interface SppEnumPriorityMapper {
    int deleteByPrimaryKey(String enumType);

    int insert(SppEnumPriority record);

    int insertSelective(SppEnumPriority record);

    SppEnumPriority selectByPrimaryKey(String enumType);

    int updateByPrimaryKeySelective(SppEnumPriority record);

    int updateByPrimaryKey(SppEnumPriority record);
}