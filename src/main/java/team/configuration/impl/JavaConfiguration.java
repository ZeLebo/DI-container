package team.configuration.impl;

import team.configuration.Configuration;

public class JavaConfiguration implements Configuration {
    @Override
    public String getPackageToScan() {
        return "team";
    }
}
