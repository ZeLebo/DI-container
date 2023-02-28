package team.config;

import team.service.ServiceB;
import team.service.impl.ServiceBAnotherImpl;

import java.util.Map;

public interface Configuration {

    String GetPackageToScan();

    Map<Class, Class> getInterfaceToImplementetions();
}
