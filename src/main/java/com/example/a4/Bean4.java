package com.example.a4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "java")
@Slf4j
public class Bean4 {
    private String home;
    private String version;

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
      log.debug("home"+home);
        this.home = home;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        log.debug("version:"+version);
        this.version = version;
    }
}
