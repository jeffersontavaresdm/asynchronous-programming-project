package com.contratacao.service;

import com.contratacao.dto.Cliente;
import com.contratacao.factory.SnsClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
@Slf4j
public class ContratacaoService {

  private final SnsClientFactory snsClientFactory;
  private final ObjectMapper objectMapper;

  public ContratacaoService(SnsClientFactory snsClientFactory, ObjectMapper objectMapper) {
    this.snsClientFactory = snsClientFactory;
    this.objectMapper = objectMapper;
  }

  public void enviarSolicitacaoCliente(Cliente cliente) {
    try (SnsClient snsClient = snsClientFactory.createSnsClient()) {
      String messageBody = objectMapper.writeValueAsString(cliente);

      PublishRequest request = PublishRequest
        .builder()
        .topicArn("arn:aws:sns:us-east-1:000000000000:contratacao")
        .message(messageBody)
        .build();

      snsClient.publish(request);
      log.info("Mensagem enviada com sucesso!");
    } catch (Exception exception) {
      log.error(exception.getMessage());
    }
  }
}
