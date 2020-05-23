package cn.ssy.base.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.ssy.base.entity.mybatis.TspFlowStepController;
import cn.ssy.base.entity.mybatis.TspFlowStepControllerKey;

public interface TspFlowStepControllerMapper {
    int deleteByPrimaryKey(TspFlowStepControllerKey key);

    int insert(TspFlowStepController record);

    int insertSelective(TspFlowStepController record);

    TspFlowStepController selectByPrimaryKey(TspFlowStepControllerKey key);

    int updateByPrimaryKeySelective(TspFlowStepController record);

    int updateByPrimaryKey(TspFlowStepController record);
    
    TspFlowStepController selectOne_odb1(@Param("tranCode")String tranCode, @Param("stepId")String stepId);
    
    List<TspFlowStepController> selectAll();
}