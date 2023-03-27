package team;

import lombok.SneakyThrows;
import team.context.ApplicationContext;
import team.service.*;
import team.service.impl.FrontServiceImpl;

import java.lang.reflect.Field;

public class Application {
//    private static final String packageToScan = "team";
    private static final String packageToScan = "src/test/beans.xml";

    volatile static ApplicationContext context = new ApplicationContext(packageToScan);

    @SneakyThrows
    public static void main(String[] args) {
        ServiceB serviceB = context.getBean(ServiceB.class);
        serviceB.jobB();
        serviceB.jobB();

        // create a thread where will call the method jobB() of ServiceB
        Thread thread = new Thread(() -> {
            ServiceB serviceB1 = context.getBean(ServiceB.class);
            serviceB1.jobB();
        });
        thread.start();
//        MusicService music = context.getBean(MusicService.class);
//        music.musicLoading();
//
//        PictureService pictureService = context.getBean(PictureService.class);
//        pictureService.pictureLoading();

        FrontService frontService = context.getBean(FrontService.class);
        frontService.siteLoading();

    }
}
