package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.CtpControl;

public interface CtpControlMapper {
    int deleteByPrimaryKey(String fieldName);

    int insert(CtpControl record);

    int insertSelective(CtpControl record);

    CtpControl selectByPrimaryKey(String fieldName);

    int updateByPrimaryKeySelective(CtpControl record);

    int updateByPrimaryKey(CtpControl record);
}