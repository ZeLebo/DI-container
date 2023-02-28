package team;

import team.factory.BeanFactory;
import team.service.FrontService;
import team.service.MusicService;
import team.service.PictureService;
import team.service.ServiceB;

public class Main {

    public static void main(String[] args) throws Exception {
        ServiceB serviceB = BeanFactory.getInstance().getBean(ServiceB.class);
        MusicService musicService = BeanFactory.getInstance().getBean(MusicService.class);
        PictureService pictureService = BeanFactory.getInstance().getBean(PictureService.class);
        FrontService frontService = BeanFactory.getInstance().getBean(FrontService.class);
        serviceB.jobB();
        frontService.siteLoading();
    }
}
