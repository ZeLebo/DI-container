package team.config;

import team.service.ServiceB;
import team.service.impl.ServiceBAnotherImpl;
import team.Application;

import java.util.Map;

public class JavaConfiguration implements Configuration {
    @Override
    public String GetPackageToScan() {
        // get the root of the project
        return Application.class.getPackageName();
    }

    @Override
    public Map<Class, Class> getInterfaceToImplementetions() {
        return Map.of(ServiceB.class, ServiceBAnotherImpl.class);
    }
}
