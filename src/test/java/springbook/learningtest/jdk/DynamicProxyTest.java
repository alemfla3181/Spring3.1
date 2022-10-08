package springbook.learningtest.jdk;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.learningtest.dao.Hello;
import springbook.learningtest.dao.HelloTarget;
import springbook.learningtest.dao.UppercaseHandler;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DynamicProxyTest {
    @Test
    public void simpleProxy(){
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[] {Hello.class},
            new UppercaseHandler(new HelloTarget()));
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    public void proxyFactoryBean(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        // 타깃 설정
        pfBean.setTarget(new HelloTarget());
        // 부가기능을 담은 어드바이스를 추가한다. 여러개를 추가할 수도 있다.
        pfBean.addAdvice(new UppercaseAdvice());

        // FactoryBean이므로 getObject()로 생성된 프록시를 가져옴
        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    public void pointcutAdvisor(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        // 메소드 이름을 비교해서 대상을 선정하는 알고리즘을 제공하는 포인트컷 생성
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        // 이름 비교조건 설정
        // sayH로 시작하는 모든 메소드를 선택함
        pointcut.setMappedName("sayH*");

        // 포인트컷과 어드바이스를 Advisor로 묶어서 한 번에 추가
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        // 메소드 이름이 포인트컷의 선정조건에 맞지 않으므로, 부가기능이 적용되지 않음
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // 리플렉션의 Method와 달리 메소드 실행 시 타깃 오브젝트를 전달할 필요가 없다.
            // Methodinvocation은 메소드 정보와 함께 타깃 오브젝트를 알고 있기 때문이다.
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }
    }
//
//    static interface Hello {
//        String sayHello(String name);
//        String sayHi(String name);
//        String sayThankYou(String name);
//    }
//
//    static class HelloTarget implements Hello {
//        @Override
//        public String sayHello(String name) { return "Hello " + name; }
//        @Override
//        public String sayHi(String name) { return "Hi " +name; }
//        @Override
//        public String sayThankYou(String name) { return "Thank You " + name; }
//    }

//    @Test
//    public void dynamicProxy() {
//        // params 1. 클래스 로더 2. 다이내믹 프록시가 구현할 인터페이스 3. 부가기능과 위임 관련 코드를 담고있는 InvocationHandler 구현 오브젝트
//        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
//            getClass().getClassLoader(),
//            new Class[] {Hello.class},
//            new UppercaseHandler(new HelloTarget()));
//    }
//
//    @Test
//    public void invokeMethod() throws  Exception{
//        String name = "Spring";
//
//        // length()
//        assertThat(name.length()).isEqualTo(6);
//
//        Method lengthMethod = String.class.getMethod("length");
//        assertThat((Integer)lengthMethod.invoke(name)).isEqualTo(6);
//
//        // charAt()
//        assertThat(name.charAt(0)).isEqualTo('S');
//
//        Method charAtMethod = String.class.getMethod("charAt", int.class);
//        assertThat((Character)charAtMethod.invoke(name, 0)).isEqualTo('S');
//    }
}
