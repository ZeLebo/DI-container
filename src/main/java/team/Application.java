package team;

import lombok.SneakyThrows;
import team.context.ApplicationContext;
import team.service.*;

public class Application {

//    volatile static ApplicationContext context = new ApplicationContext("team");

    @SneakyThrows
    public static void main(String[] args) {
//         ApplicationContext context = new ApplicationContext("src/test/beans.xml");
        ApplicationContext context = new ApplicationContext("team");

        ServiceB serviceB = context.getBean(ServiceB.class);
        serviceB.jobB();
        serviceB.jobB();

//        // make thread where new instance of ServiceB will be created and called jobB()
        final Thread thread = new Thread(() -> {
            ServiceB serviceB1 = context.getBean(ServiceB.class);
            serviceB1.jobB();

        });
        System.out.println(thread == Thread.currentThread());
        thread.start();
        thread.join();

//        TestThread thread = new TestThread(context);
//        thread.Run();
    }


/*
        ServiceB serviceB1 = context.getBean(ServiceB.class);
        serviceB1.jobB();
        FrontService front = context.getBean(FrontService.class);
        front.siteLoading();
*/
}
