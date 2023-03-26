package team;

import team.context.ApplicationContext;
import team.service.*;

public class Application {

    public static void  main(String[] args) {
        ApplicationContext context = new ApplicationContext("team");

        ServiceB serviceB = context.getBean(ServiceB.class);
        serviceB.jobB();

        FrontService front = context.getBean(FrontService.class);
        front.siteLoading();
    }
}
