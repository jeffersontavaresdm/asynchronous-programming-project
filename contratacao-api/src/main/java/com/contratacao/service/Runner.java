package com.contratacao.service;

import com.contratacao.factory.SnsClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

@Configuration
@Slf4j
public class Runner implements CommandLineRunner {

  private final SnsClientFactory snsClientFactory;

  public Runner(SnsClientFactory snsClientFactory) {
    this.snsClientFactory = snsClientFactory;
  }

  @Override
  public void run(String... args) {
    SubscribeRequest subscricaoCep = SubscribeRequest
      .builder()
      .protocol("sqs")
      .endpoint("arn:aws:sqs:us-east-1:000000000000:consulta-cep-queue")
      .topicArn("arn:aws:sns:us-east-1:000000000000:contratacao")
      .build();

    SubscribeRequest subscricaoCpf = SubscribeRequest
      .builder()
      .protocol("sqs")
      .endpoint("arn:aws:sqs:us-east-1:000000000000:consulta-cpf-queue")
      .topicArn("arn:aws:sns:us-east-1:000000000000:contratacao")
      .build();

    try (SnsClient snsClient = snsClientFactory.createSnsClient()) {
      snsClient.subscribe(subscricaoCep);
      snsClient.subscribe(subscricaoCpf);
    } catch (Exception exception) {
      log.error(exception.getMessage());
    }
  }
}
