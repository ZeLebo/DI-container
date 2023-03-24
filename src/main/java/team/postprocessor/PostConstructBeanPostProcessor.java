package team.postprocessor;

import lombok.SneakyThrows;
import team.annotations.PostConstruct;

import java.lang.reflect.Method;

public class PostConstructBeanPostProcessor implements BeanPostProcessor {

    @Override
    @SneakyThrows
    public void process(Object bean) {
        for(Method method : bean.getClass().getDeclaredMethods()) {
            if(method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(bean);
            }
        }
    }
}
