package team;

import team.factory.BeanFactory;
import team.service.FrontService;
import team.service.MusicService;
import team.service.PictureService;
import team.service.ServiceB;

public class Main {

//    private static DIContext createContext() throws Exception {
//        String rootPackageName = Main.class.getPackage().getName();
//        return DIContext.createContextForPackage(rootPackageName);
//    }
//
//    private static void doSomething(DIContext context) {
//        try {
//            ServiceB serviceB = context.getServiceInstance(ServiceB.class);
//            FrontService front = context.getServiceInstance(FrontService.class);
//
//            serviceB.jobB();
//            front.siteLoading();
//        } catch (Exception e) {
//            System.out.println("Somewhere @service is missing");
//        }
//    }

    public static void main(String[] args) throws Exception {
        ServiceB serviceB = BeanFactory.getInstance().getBean(ServiceB.class);
        MusicService musicService = BeanFactory.getInstance().getBean(MusicService.class);
        PictureService pictureService = BeanFactory.getInstance().getBean(PictureService.class);
        FrontService frontService = BeanFactory.getInstance().getBean(FrontService.class);
//        DIContext context = createContext();
//        doSomething(context);
        serviceB.jobB();
        musicService.musicLoading();
        pictureService.pictureLoading();
        frontService.siteLoading();
    }
}
