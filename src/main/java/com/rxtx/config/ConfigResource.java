package com.rxtx.config;

import com.rxtx.entity.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "spring.static")
public class ConfigResource {
    List<Resource> resources;
}

