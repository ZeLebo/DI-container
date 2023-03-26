package team.config;

import team.service.ServiceB;
import team.service.impl.ServiceBAnotherImpl;
import team.Application;
import team.service.impl.ServiceBImpl;

import java.util.Map;

public class JavaConfiguration implements Configuration {
    @Override
    public String GetPackageToScan() {
        // get the root of the project
        return Application.class.getPackageName();
    }

    // this map has to be collected in xml
    @Override
    public Map<Class, Class> getInterfaceToImplementetions() {
        return Map.of(ServiceB.class, ServiceBImpl.class);
    }
}
