package team.config;

public class JavaConfiguration implements Configuration {
    @Override
    public String GetPackageToScan() {
        return "team.service.impl";
    }
}
