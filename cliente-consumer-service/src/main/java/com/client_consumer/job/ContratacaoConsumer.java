package com.client_consumer.job;

import com.client_consumer.entity.dto.ContratacaoMessage;
import com.client_consumer.entity.dto.SnsTopicMessage;
import com.client_consumer.repository.ClienteRepository;
import com.client_consumer.repository.EnderecoRepository;
import com.client_consumer.util.CPFValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContratacaoConsumer {

  private final String API_URL = "http://...";
  private final ClienteRepository clienteRepository;
  private final EnderecoRepository enderecoRepository;

  public ContratacaoConsumer(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
    this.clienteRepository = clienteRepository;
    this.enderecoRepository = enderecoRepository;
  }

  @SqsListener("consulta-cpf-queue")
  public void listen(SnsTopicMessage snsTopicMessage) {
    ObjectMapper mapper = new ObjectMapper();

    ContratacaoMessage message;
    try {
      message = mapper.readValue(snsTopicMessage.Message(), ContratacaoMessage.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    // Validar o CPF
    boolean cpfValidator = CPFValidator.isValid(message.cpf());

    if (cpfValidator) {
      System.out.println("CPF válido");
    } else {
      System.out.println("CPF inválido");
    }

    //    String url = UriComponentsBuilder
    //      .fromUriString("https://.../{cpf}")
    //      .buildAndExpand(message.cpf())
    //      .toUriString();
    //
    //    RestTemplate restTemplate = new RestTemplate();
    //    Dados dados = restTemplate.getForObject(url, Dados.class);
    //
    //    if (Objects.nonNull(dados)) {
    //      dadosRepository.save(dados);
    //
    //      log.info("Dados: {}", dados);
    //    } else {
    //      log.error("Endereço não encontrado...");
    //    }
    //
    // salvar no banco...
  }
}
