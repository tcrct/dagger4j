package com.dagger4j.mvc.core;

import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;
import com.dagger4j.mvc.render.Render;
import com.dagger4j.mvc.render.TextRender;

/**
 * Created by laotang on 2018/6/15.
 */
public abstract class Controller {

    private IRequest request;
    private IResponse response;
    private Render render;

    public void init(IRequest request, IResponse response) {
        this.request = request;
        this.response = response;
    }

    public Render getRender() {
        if(null == render) {
            render = new TextRender("request is not set render");
        }
        return render;
    }
}
