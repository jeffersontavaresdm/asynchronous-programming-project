package com.contratacao.factory;

import com.contratacao.config.PropertiesConfig;
import com.contratacao.config.AwsCredentialsImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Slf4j
@Component
public class SqsClientFactory {
  private final PropertiesConfig properties;
  private final AwsCredentialsImp credentials;

  public SqsClientFactory(PropertiesConfig properties, AwsCredentialsImp credentials) {
    this.properties = properties;
    this.credentials = credentials;
  }

  public SqsClient createSqsClient() {
    return SqsClient
      .builder()
      .endpointOverride(URI.create(properties.getLocalUrl()))
      .region(credentials.region())
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .build();
  }
}
