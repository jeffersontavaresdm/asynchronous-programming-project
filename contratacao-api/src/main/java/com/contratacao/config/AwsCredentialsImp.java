package com.contratacao.config;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;

@Component
public class AwsCredentialsImp implements AwsCredentials {

  private final PropertiesConfig properties;

  public AwsCredentialsImp(PropertiesConfig properties) {
    this.properties = properties;
  }

  @Override
  public String accessKeyId() {
    return properties.getAccessKey();
  }

  @Override
  public String secretAccessKey() {
    return properties.getSecretAccessKey();
  }

  public Region region() {
    return Region.US_EAST_1;
  }
}
