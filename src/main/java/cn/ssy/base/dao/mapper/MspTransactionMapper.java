package cn.ssy.base.dao.mapper;

import cn.ssy.base.entity.mybatis.MspTransaction;

import java.util.List;

public interface MspTransactionMapper {
    int deleteByPrimaryKey(String trxnCode);

    int insert(MspTransaction record);

    int insertSelective(MspTransaction record);

    MspTransaction selectByPrimaryKey(String trxnCode);

    int updateByPrimaryKeySelective(MspTransaction record);

    int updateByPrimaryKey(MspTransaction record);

    //根据交易码模糊查询
    List<MspTransaction> selectAll_odb1(String trxnCode);
}