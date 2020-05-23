package cn.ssy.base.dao.mapper;

import java.util.List;

import cn.ssy.base.entity.mybatis.TspTask;
import cn.ssy.base.entity.mybatis.TspTaskKey;
import cn.ssy.base.entity.mybatis.TspTaskWithBLOBs;

public interface TspTaskMapper {
    int deleteByPrimaryKey(TspTaskKey key);

    int insert(TspTaskWithBLOBs record);

    int insertSelective(TspTaskWithBLOBs record);

    TspTaskWithBLOBs selectByPrimaryKey(TspTaskKey key);

    int updateByPrimaryKeySelective(TspTaskWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TspTaskWithBLOBs record);

    int updateByPrimaryKey(TspTask record);
    
    List<TspTaskWithBLOBs> selectAll_odb1(String tranDate);
}