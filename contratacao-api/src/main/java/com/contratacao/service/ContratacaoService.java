package com.contratacao.service;

import com.contratacao.config.PropertiesConfig;
import com.contratacao.dto.Cliente;
import com.contratacao.factory.SqsClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@Slf4j
public class ContratacaoService {

  private final PropertiesConfig propertiesConfig;
  private final SqsClientFactory sqsClientFactory;
  private final ObjectMapper objectMapper;

  public ContratacaoService(
    PropertiesConfig propertiesConfig,
    SqsClientFactory sqsClientFactory,
    ObjectMapper objectMapper
  ) {
    this.propertiesConfig = propertiesConfig;
    this.sqsClientFactory = sqsClientFactory;
    this.objectMapper = objectMapper;
  }

  public void enviarSolicitacaoCliente(Cliente cliente) {
    try (SqsClient sqsClient = sqsClientFactory.createSqsClient()) {
      String messageBody = objectMapper.writeValueAsString(cliente);

      SendMessageRequest request = SendMessageRequest
        .builder()
        .queueUrl(propertiesConfig.getSqsUrl())
        .messageBody(messageBody)
        .build();

      sqsClient.sendMessage(request);
      log.info("Mensagem enviada com sucesso!");
    } catch (Exception exception) {
      log.error(exception.getMessage());
    }
  }
}
