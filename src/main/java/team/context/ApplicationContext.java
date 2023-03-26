package team.context;

import lombok.Setter;
import team.config.Configuration;
import team.factory.BeanFactory;

public class ApplicationContext {
    @Setter
    private BeanFactory beanFactory;
    public ApplicationContext() {
        BeanFactory beanFactory = new BeanFactory(this);
        this.setBeanFactory(beanFactory);
    }

    public ApplicationContext(Configuration configuration) {
        BeanFactory beanFactory = new BeanFactory(this, configuration);
        this.setBeanFactory(beanFactory);
    }

    // get the bean from map, if the bean doesn't exist -> create and return
    public <T> T getBean(Class<T> tClass) {
        return beanFactory.getBean(tClass);
    }
}



