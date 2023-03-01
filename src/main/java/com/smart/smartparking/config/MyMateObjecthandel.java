package com.smart.smartparking.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.smart.smartparking.common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//
//@Component
//@Slf4j
//public class MyMateObjecthandel implements MetaObjectHandler{
//
//
//    /*
//    * 自定义源数据对象处理器
//    *
//    * */
//    @Override
//    public void insertFill(MetaObject metaObject) {//插入时 自动填充
//        log.info("公共字段自动填充[insert].....");
//        log.info(metaObject.toString());
//        metaObject.setValue("createTime", LocalDateTime.now());
//
//
//
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {//更新时
//        log.info("公共字段自动填充[update].....");
//        log.info(metaObject.toString());
//        metaObject.setValue("updateTime",LocalDateTime.now());
//
//    }
//}
