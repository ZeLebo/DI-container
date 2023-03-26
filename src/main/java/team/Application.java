package team;

import team.context.ApplicationContext;
import team.service.*;

public class Application {

    public static void  main(String[] args) {
        ApplicationContext context = new ApplicationContext();

        ServiceB serviceB = context.getBean(ServiceB.class);
        FrontService frontService = context.getBean(FrontService.class);
        serviceB.jobB();
        frontService.siteLoading();

        SomeService someService = context.getBean(SomeService.class);
        someService.prepare();
    }
}
