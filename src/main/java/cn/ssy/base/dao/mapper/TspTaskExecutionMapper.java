package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.TspTaskExecution;
import cn.ssy.base.entity.mybatis.TspTaskExecutionKey;
import cn.ssy.base.entity.mybatis.TspTaskExecutionWithBLOBs;

public interface TspTaskExecutionMapper {
    int deleteByPrimaryKey(TspTaskExecutionKey key);

    int insert(TspTaskExecutionWithBLOBs record);

    int insertSelective(TspTaskExecutionWithBLOBs record);

    TspTaskExecutionWithBLOBs selectByPrimaryKey(TspTaskExecutionKey key);

    int updateByPrimaryKeySelective(TspTaskExecutionWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TspTaskExecutionWithBLOBs record);

    int updateByPrimaryKey(TspTaskExecution record);
}