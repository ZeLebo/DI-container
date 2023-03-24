package team;

import lombok.SneakyThrows;
import team.context.ApplicationContext;
import team.factory.BeanFactory;
import team.service.*;

public class Application {
    public ApplicationContext run() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);

        return applicationContext;
    }

    public static void  main(String[] args) {
        Application application = new Application();
        ApplicationContext context = application.run();

        ServiceB serviceB = context.getBean(ServiceB.class);
        FrontService frontService = context.getBean(FrontService.class);
        serviceB.jobB();
        frontService.siteLoading();

        SomeService someService = context.getBean(SomeService.class);
        someService.prepare();
    }
}
