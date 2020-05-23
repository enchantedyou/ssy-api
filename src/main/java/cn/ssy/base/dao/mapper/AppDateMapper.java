package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.AppDate;

public interface AppDateMapper {
    int deleteByPrimaryKey(String busiOrgId);

    int insert(AppDate record);

    int insertSelective(AppDate record);

    AppDate selectByPrimaryKey(String busiOrgId);

    int updateByPrimaryKeySelective(AppDate record);

    int updateByPrimaryKey(AppDate record);
}