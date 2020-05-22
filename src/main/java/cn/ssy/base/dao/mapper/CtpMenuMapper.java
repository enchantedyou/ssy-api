package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.CtpMenu;

public interface CtpMenuMapper {
    int deleteByPrimaryKey(String menuCode);

    int insert(CtpMenu record);

    int insertSelective(CtpMenu record);

    CtpMenu selectByPrimaryKey(String menuCode);

    int updateByPrimaryKeySelective(CtpMenu record);

    int updateByPrimaryKey(CtpMenu record);
}