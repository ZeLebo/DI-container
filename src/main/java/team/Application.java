package team;

import lombok.SneakyThrows;
import team.context.ApplicationContext;
import team.service.*;

public class Application {

    @SneakyThrows
    public static void main(String[] args) {
         ApplicationContext context = new ApplicationContext("src/test/beans.xml");
        // ApplicationContext context = new ApplicationContext("team");

         ServiceB serviceB = context.getBean(ServiceB.class);
         serviceB.jobB();

         MusicService music = context.getBean(MusicService.class);
         music.musicLoading();


/*
        ServiceB serviceB1 = context.getBean(ServiceB.class);
        serviceB1.jobB();
        FrontService front = context.getBean(FrontService.class);
        front.siteLoading();
*/

    }
}
