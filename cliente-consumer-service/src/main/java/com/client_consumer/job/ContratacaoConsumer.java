package com.client_consumer.job;

import com.client_consumer.entity.Cliente;
import com.client_consumer.entity.Endereco;
import com.client_consumer.entity.dto.ContratacaoMessage;
import com.client_consumer.entity.dto.EnderecoDTO;
import com.client_consumer.entity.dto.SnsTopicMessage;
import com.client_consumer.repository.ClienteRepository;
import com.client_consumer.repository.EnderecoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class ContratacaoConsumer {

  private final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";
  private final ClienteRepository clienteRepository;
  private final EnderecoRepository enderecoRepository;

  public ContratacaoConsumer(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
    this.clienteRepository = clienteRepository;
    this.enderecoRepository = enderecoRepository;
  }

  @SqsListener("consulta-cep-queue")
  public void listen(SnsTopicMessage snsTopicMessage) {
    ObjectMapper mapper = new ObjectMapper();

    ContratacaoMessage message;
    try {
      message = mapper.readValue(snsTopicMessage.Message(), ContratacaoMessage.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    String url = UriComponentsBuilder
      .fromUriString(VIA_CEP_URL)
      .buildAndExpand(message.cep())
      .toUriString();

    RestTemplate restTemplate = new RestTemplate();
    EnderecoDTO enderecoDTO = restTemplate.getForObject(url, EnderecoDTO.class);

    if (enderecoDTO != null && enderecoDTO.cep() != null) {
      Endereco endereco = enderecoRepository.saveAndFlush(enderecoDTO.toEntity());
      Cliente cliente = clienteRepository.save(new Cliente(null, message.cliente(), endereco));

      log.info("Cliente: {}", cliente);
    } else {
      log.error("Endereço não encontrado...");
    }
  }
}
