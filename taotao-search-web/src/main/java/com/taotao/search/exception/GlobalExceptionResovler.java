package com.taotao.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalExceptionResovler implements HandlerExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionResovler.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        LOGGER.info("进入异常处理器......");
        LOGGER.info("handler : "+o.getClass());
        LOGGER.error("系统出现异常",e);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message","网络连接失败，请稍后重试");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
