package com.dagger4j.mvc.core;

import com.dagger4j.exception.MvcException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.route.Route;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Method Invocation
 *  Action反射执行类，用于执行Controller指定的方法，如有拦截器，则先按顺序执行拦截器里的方法
 *  @author  laotang
 * @since  1.0
 */
public class ActionInvocation {

    private BaseController controller;		// 要执行的Controller类
	private Route route;					// Controller类里的方法封装类对象
	private Interceptor[] inters;			// 拦截器，支持多个执行
	private Method method;		//执行的方法
	private int index = 0;
	private static final Object[] NULL_ARGS = new Object[0];		// 默认参数
	private String target;				// URI


	/**
	 * 构造函数
	 */
	protected ActionInvocation() {

	}

	/**
	 * 构造函数
	 * @param route			Route对象
	 * @param controller		Controller对象
	 * @param method		方法对象
	 * @param target	请求URI
	 */
	public ActionInvocation(Route route, BaseController controller, Method method, String target) {
		this.route = route;
		this.controller = controller;
		this.method = method;
		this.inters = route.getInterceptors();
		this.target = target;
	}

	/**
	 * Invoke the action. 反射方式执行该方法
	 * 
	 * @throws Throwable
	 */
	public void invoke() throws Exception {
		// 如果方法设置了拦截器，则先按书写顺序从上至下执行拦截器
		if (inters != null && index < inters.length) {
			inters[index++].intercept(this);
		} else {
			Parameter[] actionParams = method.getParameters();
			if (ToolsKit.isNotEmpty(actionParams)) {
                String[] parameterNames = MethodParameterNameDiscoverer.getParameterNames(controller.getClass(), method);
                if(actionParams.length != parameterNames.length) {
                    throw new MvcException("参数长度不一致!");
                }
                for(int i=0; i<actionParams.length; i++) {
                    Class<?> parameterType = actionParams[i].getType();
                    Annotation[]   annotations = actionParams[i].getAnnotations();
                    if(ToolsKit.isNotEmpty(annotations)) {
                        for(Annotation annotation : annotations) {
                            System.out.println(annotation.annotationType() + "                      " + parameterType.getName() + "                  " + parameterNames[i]);
                        }
                    }
                }



//				method.invoke(controller, action.getMethod().getName(), methodParamList.toArray());
				method.invoke(controller, NULL_ARGS);
			} else {
				method.invoke(controller, NULL_ARGS);
			}
		}
	}

	public BaseController getController() {
		return controller;
	}
}
