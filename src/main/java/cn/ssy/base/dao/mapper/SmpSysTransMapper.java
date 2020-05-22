package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.SmpSysTrans;

public interface SmpSysTransMapper {
    int insert(SmpSysTrans record);

    int insertSelective(SmpSysTrans record);
}