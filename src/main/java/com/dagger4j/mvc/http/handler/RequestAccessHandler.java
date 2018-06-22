package com.dagger4j.mvc.http.handler;

import com.dagger4j.exception.MvcException;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;

/**
 * 请求访问处理器
 * Created by laotang on 2018/6/14.
 */
public class RequestAccessHandler{

    private static final Object[] NULL_ARGS = new Object[0];


    public static void doHandler(String target, IRequest request, IResponse response) throws MvcException {
        /*
//        Action action = InstanceFactory.getActionMapping().get(target);
        Route route = null;
//        if(null == action){
//            action = getRestfulActionMapping(request, target);
//            if(null == action) {
//                throw new DuangMvcException("action is null or access denied");
//            }
//        }

        Class<?> controllerClass = route.getControllerClass();
        Controller controller = null;
        //是否单例
        RequestMapping requestMapping = route.getRequestMapping();
        if(route.isSingleton()){
            controller = (Controller) BeanKit.getBean(controllerClass);
        } else {
            // 如果不是设置为单例模式的话就每次请求都创建一个新的Controller对象
            controller = ClassKit.newInstance(controllerClass);
            // 还要重新执行Ioc注入
            IocHelper.ioc(controller.getClass());
        }
        // 传入request, response到请求的Controller
        controller.init(request, response);
        // 取出方法对象
        Method method = action.getMethod();
        // 取消类型安全检测（可提高反射性能）
        method.setAccessible(true);
        // 反射执行方法
        method.invoke(controller, NULL_ARGS);
        // 返回结果
        controller.getRender().setContext(request, response).render();
        */
    }
}
