package team.context;

import lombok.Setter;
import team.factory.BeanFactory;

public class ApplicationContext {
    @Setter
    private BeanFactory beanFactory;

    public ApplicationContext(String packageToScan) {
        BeanFactory beanFactory = new BeanFactory(this, packageToScan);
        this.setBeanFactory(beanFactory);
    }

    // get the bean from map, if the bean doesn't exist -> create and return
    public <T> T getBean(Class<T> tClass) {
        return beanFactory.getBean(tClass);
    }
}



