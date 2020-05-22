package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.SppDictPriority;

public interface SppDictPriorityMapper {
    int deleteByPrimaryKey(String dictType);

    int insert(SppDictPriority record);

    int insertSelective(SppDictPriority record);

    SppDictPriority selectByPrimaryKey(String dictType);

    int updateByPrimaryKeySelective(SppDictPriority record);

    int updateByPrimaryKey(SppDictPriority record);
}