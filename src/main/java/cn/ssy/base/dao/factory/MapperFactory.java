package cn.ssy.base.dao.factory;

import cn.ssy.base.core.utils.BatTaskUtil;
import cn.ssy.base.core.utils.mybatis.MybatisUtil;
import cn.ssy.base.dao.mapper.*;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.context.Application;

/**
 * @Description mapper工厂
 * @Author sunshaoyu
 * @Date 2020年05月25日-16:00
 */
public abstract class MapperFactory {

        public static TspTranControllerMapper getTspTranControllerMapper(String dataSourceId){
            return Application.getMybatisUtil().getMapper(dataSourceId, TspTranControllerMapper.class);
        }

        public static TspFlowStepControllerMapper getTspFlowStepControllerMapper(String dataSourceId){
            return Application.getMybatisUtil().getMapper(dataSourceId, TspFlowStepControllerMapper.class);
        }

        public static AppDateMapper getAppDateMapper(String dataSourceId){
            return Application.getMybatisUtil().getMapper(dataSourceId, AppDateMapper.class);
        }

        public static TspTaskMapper getTspTaskMapper(String dataSourceId){
            return Application.getMybatisUtil().getMapper(dataSourceId, TspTaskMapper.class);
        }

        public static TspServiceInMapper getTspServiceInMapper(String dataSourceId){
            return Application.getMybatisUtil().getMapper(dataSourceId, TspServiceInMapper.class);
        }

        public static SppDatasourceMapper getSppDatasourceMapper(){
            return Application.getMybatisUtil().getMapper(ApiConst.DATASOURCE_LOCAL, SppDatasourceMapper.class);
        }

        public static MspTransactionMapper getMspTransactionMapper(String dataSourceId){
            return Application.getMybatisUtil().getMapper(dataSourceId, MspTransactionMapper.class);
        }

        public static TspTaskExecutionMapper getTspTaskExecutionMapper(String dataSourceId){
            return Application.getMybatisUtil().getMapper(dataSourceId, TspTaskExecutionMapper.class);
        }
}
