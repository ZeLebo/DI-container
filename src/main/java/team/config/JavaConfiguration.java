package team.config;

import team.service.ServiceB;
import team.service.impl.ServiceBAnotherImpl;

import java.util.Map;

public class JavaConfiguration implements Configuration {
    @Override
    public String GetPackageToScan() {
        return "team.service.impl";
    }

    @Override
    public Map<Class, Class> getInterfaceToImplementetions() {
        return Map.of(ServiceB.class, ServiceBAnotherImpl.class);
    }
}
