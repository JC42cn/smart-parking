//
//package com.smart.smartparking.common;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.sql.SQLIntegrityConstraintViolationException;
//
//
///*
//* 全局异常处理
//*
//*
//* */
//
//@ControllerAdvice(annotations = {RestController.class, Controller.class})
//@ResponseBody
//@Slf4j
//
///*
//* sql异常处理方法* 异常信息输出 */
//
//class GlobalExceptionHandler {
//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public R<String> exceptionHandel(SQLIntegrityConstraintViolationException ex){
//        log.error(ex.getMessage());//异常信息
//        if(ex.getMessage().contains("Duplicate entry")){//异常当中是否存在"Duplicate entry"关键字
//            String[] split = ex.getMessage().split(" ");//根据空格隔开 取"Duplicate entry"后的信息 即第2位
//            String msg = split[2] + "已存在";
//            return R.error(msg);//放回信息
//        }
//
//        return R.error("未知错误");
//
//
//    }
//}
//
