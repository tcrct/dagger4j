package com.dagger4j.mvc.render;


import com.dagger4j.kit.PropKit;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;
import com.dagger4j.mvc.http.enums.ConstEnums;
import io.netty.handler.codec.http.HttpHeaderNames;

import java.io.Serializable;


public abstract class Render implements Serializable {
	
	private static final long serialVersionUID = -8406693915721288408L;
	protected  static final String ENCODING = PropKit.get("encoding","UTF-8");
	protected IRequest request;
	protected IResponse response;
	protected Object obj;
	protected String view;

	
	public final Render setContext(IRequest request, IResponse response) {
		this.request = request;
		this.response = response;
		return this;
	}
	
	public final Render setContext(IRequest request, IResponse response, String view) {
		this.request = request;
		this.response = response;
		this.view = view;
		return this;
	}
	
	public String getView() {
		return view;
	}
	
	public void setView(String view) {
		this.view = view;
	}
	
	
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	protected void setDefaultValue2Response() {
		response.setHeader(HttpHeaderNames.PRAGMA.toString(), "no-cache");
		response.setHeader(HttpHeaderNames.CACHE_CONTROL.toString(), "no-cache");
		response.setHeader(HttpHeaderNames.EXPIRES.toString(), "0");
		response.setHeader(ConstEnums.FRAMEWORK_OWNER_FILED.getValue(), ConstEnums.FRAMEWORK_OWNER.getValue());
		response.setHeader(ConstEnums.RESPONSE_STATUS.getValue(), "200");
	}

	public abstract void render();
}
