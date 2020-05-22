package cn.ssy.base.dao.mapper;

import java.util.List;

import cn.ssy.base.entity.mybatis.SmpSysDict;
import cn.ssy.base.entity.mybatis.SmpSysDictKey;

public interface SmpSysDictMapper {
    int deleteByPrimaryKey(SmpSysDictKey key);

    int insert(SmpSysDict record);

    int insertSelective(SmpSysDict record);

    SmpSysDict selectByPrimaryKey(SmpSysDictKey key);

    int updateByPrimaryKeySelective(SmpSysDict record);

    int updateByPrimaryKey(SmpSysDict record);
    
    List<SmpSysDict> selectAll();
}