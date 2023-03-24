package team.config;

import java.util.Map;

public interface Configuration {

    String GetPackageToScan();

    Map<Class, Class> getInterfaceToImplementetions();
}
