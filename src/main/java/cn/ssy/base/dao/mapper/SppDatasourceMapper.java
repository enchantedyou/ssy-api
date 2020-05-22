package cn.ssy.base.dao.mapper;

import java.util.List;

import cn.ssy.base.entity.mybatis.SppDatasource;
import cn.ssy.base.entity.mybatis.SppDatasourceKey;

public interface SppDatasourceMapper {
    int deleteByPrimaryKey(SppDatasourceKey key);

    int insert(SppDatasource record);

    int insertSelective(SppDatasource record);

    SppDatasource selectByPrimaryKey(SppDatasourceKey key);

    int updateByPrimaryKeySelective(SppDatasource record);

    int updateByPrimaryKey(SppDatasource record);
    
    List<SppDatasource> selectAll();
}