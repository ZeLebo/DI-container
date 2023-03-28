package team;

import lombok.SneakyThrows;
import team.context.ApplicationContext;
import team.service.FrontService;
import team.service.ServiceB;

public class Application {
//    private static final String packageToScan = "team";
    private static final String packageToScan = "src/test/beans.xml";

    volatile static ApplicationContext context = new ApplicationContext(packageToScan);

    @SneakyThrows
    public static void main(String[] args) {

        ServiceB serviceB = context.getBean(ServiceB.class);
        serviceB.jobB();
        serviceB.jobB();

        Thread thread = new Thread(() -> {
            ServiceB serviceB1 = context.getBean(ServiceB.class);
            serviceB1.jobB();
        });
        thread.start();

        FrontService frontService = context.getBean(FrontService.class);
        frontService.siteLoading();

    }
}
