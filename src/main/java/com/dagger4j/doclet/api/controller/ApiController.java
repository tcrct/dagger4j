package com.dagger4j.doclet.api.controller;

import com.dagger4j.doclet.api.service.ApiService;
import com.dagger4j.exception.MvcException;
import com.dagger4j.mvc.annotation.Controller;
import com.dagger4j.mvc.annotation.Import;
import com.dagger4j.mvc.annotation.Mapping;
import com.dagger4j.mvc.core.BaseController;
import com.dagger4j.mvc.http.enums.HttpMethod;
import com.dagger4j.vtor.annotation.NotEmpty;

/**
 * @author Created by laotang
 * @date createed in 2018/7/9.
 */
@Controller
@Mapping(value = "/dagger/api", desc="api接口")
public class ApiController extends BaseController {

    @Import
    private ApiService apiService;


    /**
     *  取Controller列表
     */
    @Mapping(value = "/list", desc = "controller列表")
    public void list() {
        if(!isLocalRequest()) {
            throw new MvcException("仅支持local环境请求!");
        }
        try {
            returnSuccessJson(apiService.list());
        } catch (Exception e) {
            returnFailJson(e, e.getMessage());
        }
    }

    /**
     * 取单个Controller下的所有Method
     * @param key
     */
    @Mapping(value = "/methods", method = HttpMethod.GET)
    public void methods(@NotEmpty String key) {
        if(!isLocalRequest()) {
            throw new MvcException("仅支持local环境请求!");
        }
        try {
            returnSuccessJson(apiService.methodList(key));
        } catch (Exception e) {
            returnFailJson(e, e.getMessage());
        }
    }

    /**
     * 单个Method方法的详细资料
     * @param key
     */
    @Mapping(value = "/detail", method = HttpMethod.GET)
    public void detail(@NotEmpty String key) {
        if(!isLocalRequest()) {
            throw new MvcException("仅支持local环境请求!");
        }
        try {
            returnSuccessJson(apiService.methodDetail(key));
        } catch (Exception e) {
            returnFailJson(e, e.getMessage());
        }
    }
}