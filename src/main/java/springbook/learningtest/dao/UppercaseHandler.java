package springbook.learningtest.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

    private final Object target;  // 타깃의 종류와 상관없이 적용이 가능

    public UppercaseHandler(Hello target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if(ret instanceof String && method.getName().startsWith("say")) {   // 리턴 타입 및 메소드 이름이 일치하는 경우 프록시 적용
            return ((String)ret).toUpperCase();
        } else {    // 미 일치시 타겟 메소드 그대로 호출
            return ret;
        }
    }
}
