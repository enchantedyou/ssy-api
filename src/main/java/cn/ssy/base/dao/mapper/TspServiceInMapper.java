package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.TspServiceIn;
import cn.ssy.base.entity.mybatis.TspServiceInKey;

import java.util.List;

public interface TspServiceInMapper {
    int deleteByPrimaryKey(TspServiceInKey key);

    int insert(TspServiceIn record);

    int insertSelective(TspServiceIn record);

    TspServiceIn selectByPrimaryKey(TspServiceInKey key);

    int updateByPrimaryKeySelective(TspServiceIn record);

    int updateByPrimaryKey(TspServiceIn record);

    //根据flowtranId模糊查询
    TspServiceIn selectOne_odb1(String flowtranId);

    //根据服务目录查询
    List<TspServiceIn> selectAll_odb1(Character serviceCategory);
}