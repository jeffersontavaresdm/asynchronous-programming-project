package com.contratacao.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
  @PropertySource("classpath:queue-config.properties"),
  @PropertySource("classpath:credentials.properties")
})
@Getter
public class PropertiesConfig {

  @Value("${sqs-url}")
  private String sqsUrl;

  @Value("${local-url}")
  private String localUrl;

  @Value("${access_key}")
  private String accessKey;

  @Value("${secret_access_key}")
  private String secretAccessKey;
}