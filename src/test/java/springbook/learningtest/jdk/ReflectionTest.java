package springbook.learningtest.jdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.learningtest.dao.Hello;
import springbook.learningtest.dao.HelloTarget;
import springbook.learningtest.dao.UppercaseHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ReflectionTest {

    @Test
    public void
    invokeMethod() throws  Exception{
        String name = "Spring";

        // length()
        assertThat(name.length()).isEqualTo(6);

        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer)lengthMethod.invoke(name)).isEqualTo(6);

        // charAt()
        assertThat(name.charAt(0)).isEqualTo('S');

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character)charAtMethod.invoke(name, 0)).isEqualTo('S');
    }

    @Test
    public void simpleProxy(){
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");

//        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
//            getClass().getClassLoader(),
//            new Class[] {Hello.class},
//            new UppercaseHandler(new HelloTarget()));
//        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
//        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
//        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    public void dynamicProxy() {
        // params 1. 클래스 로더 2. 다이내믹 프록시가 구현할 인터페이스 3. 부가기능과 위임 관련 코드를 담고있는 InvocationHandler 구현 오브젝트
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[] {Hello.class},
            new UppercaseHandler(new HelloTarget()));
    }
}
