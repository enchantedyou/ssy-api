package cn.ssy.base.dao.mapper;

import java.util.List;

import cn.ssy.base.entity.mybatis.TspTranController;
import cn.ssy.base.entity.mybatis.TspTranControllerKey;

public interface TspTranControllerMapper {
    int deleteByPrimaryKey(TspTranControllerKey key);

    int insert(TspTranController record);

    int insertSelective(TspTranController record);

    TspTranController selectByPrimaryKey(TspTranControllerKey key);

    int updateByPrimaryKeySelective(TspTranController record);

    int updateByPrimaryKeyWithBLOBs(TspTranController record);

    int updateByPrimaryKey(TspTranController record);
    
    List<TspTranController> selectAll();
    
    TspTranController selectOne_odb1(String tranGroupId);
}