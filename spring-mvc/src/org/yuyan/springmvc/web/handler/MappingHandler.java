package org.yuyan.springmvc.web.handler;

import org.yuyan.springmvc.beans.BeanFactory;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.yuyan.springmvc.beans.TypeServletRequest;
import org.yuyan.springmvc.beans.TypeServletResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MappingHandler {
    private String uri;
    private Method method;
    private Class<?> controller;
    private String[] args;

    public MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }

    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest)req).getRequestURI();
        if (uri == null || !uri.equals(requestUri)) {
            return false;
        }

        Object[] params = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            params[i] = req.getParameter(args[i]);
        }

        Object ctl = BeanFactory.getBean(controller);
        checkServletAttributes(ctl, req, res);
        Object response = method.invoke(ctl, params);
        res.getWriter().println(response.toString());
        return true;
    }

    private void checkServletAttributes(Object object, ServletRequest req, ServletResponse res) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(TypeServletRequest.class)) {
                field.setAccessible(true);
                field.set(object, req);
            }
            if (field.isAnnotationPresent(TypeServletResponse.class)) {
                field.setAccessible(true);
                field.set(object, res);
            }
        }
    }
}
