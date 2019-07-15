package com.neonex.mcp.mcm;

import com.neonex.mcp.mcm.server.McmServer;
import com.neonex.mcp.mcm.server.ServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;


@RequiredArgsConstructor
@SpringBootApplication
@EnableConfigurationProperties( {ServerProperties.class})
public class McmApplication {

    private final McmServer mcmServer;

    public static void main(String[] args) {
        SpringApplication.run(McmApplication.class, args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
        return new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
                mcmServer.start();
            }
        };
    }
}
