package team.tst;

import team.tst.service.FrontService;
import team.tst.service.ServiceB;

public class Main {

    private static DIContext createContext() throws Exception {
        String rootPackageName = Main.class.getPackage().getName();
        return DIContext.createContextForPackage(rootPackageName);
    }

    private static void doSomething(DIContext context) {
        try {
            ServiceB serviceB = context.getServiceInstance(ServiceB.class);
            FrontService front = context.getServiceInstance(FrontService.class);

            serviceB.jobB();
            front.siteLoading();
        } catch (Exception e) {
            System.out.println("Somewhere @service is missing");
        }
    }

    public static void main(String[] args) throws Exception {
        DIContext context = createContext();
        doSomething(context);
    }
}
