package com.rxtx.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2020/12/25 15:35
 */
@CrossOrigin
@RestController
@Slf4j
@Api(tags = "异常转发")
@ApiIgnore
public class ErrorController {


    /**
     * 重新抛出异常
     */
    @RequestMapping("/error/exthrow")
    public void rethrow(HttpServletRequest request) throws Throwable {
        throw (Throwable) request.getAttribute("filter.error");
    }
}
