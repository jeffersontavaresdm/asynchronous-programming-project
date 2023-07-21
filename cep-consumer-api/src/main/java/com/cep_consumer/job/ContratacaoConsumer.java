package com.cep_consumer.job;

import com.cep_consumer.entity.Cliente;
import com.cep_consumer.entity.Endereco;
import com.cep_consumer.entity.dto.ContratacaoMessage;
import com.cep_consumer.entity.dto.EnderecoDTO;
import com.cep_consumer.entity.dto.SnsTopicMessage;
import com.cep_consumer.repository.ClienteRepository;
import com.cep_consumer.repository.EnderecoRepository;
import com.cep_consumer.util.RedisCacheManager;
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

  private final ClienteRepository clienteRepository;
  private final EnderecoRepository enderecoRepository;
  private final RedisCacheManager redisCacheManager;

  public ContratacaoConsumer(
    ClienteRepository clienteRepository,
    EnderecoRepository enderecoRepository,
    RedisCacheManager redisCacheManager
  ) {
    this.clienteRepository = clienteRepository;
    this.enderecoRepository = enderecoRepository;
    this.redisCacheManager = redisCacheManager;
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

    EnderecoDTO enderecoDTO = getEndereco(message.cep());

    if (enderecoDTO != null && enderecoDTO.cep() != null) {
      Endereco endereco = enderecoRepository.save(enderecoDTO.toEntity());
      Cliente cliente = clienteRepository.save(new Cliente(null, message.cliente(), endereco));

      redisCacheManager.cacheCliente(cliente);

      log.info("Cliente salvo no banco de dados. Cliente: {}", cliente.getNome());
    } else {
      log.info("Endereço não encontrado!");
    }
  }

  public EnderecoDTO getEndereco(String cep) {
    EnderecoDTO enderecoDTO = redisCacheManager.getEnderecoByRedisCache(cep);

    if (enderecoDTO == null) {
      RestTemplate restTemplate = new RestTemplate();

      String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";
      String url = UriComponentsBuilder
        .fromUriString(VIA_CEP_URL)
        .buildAndExpand(cep)
        .toUriString();

      enderecoDTO = restTemplate.getForObject(url, EnderecoDTO.class);

      if (enderecoDTO != null && enderecoDTO.cep() != null) {
        redisCacheManager.cacheAddress(cep, enderecoDTO);
      }

      log.info("Endereço encontrado na api VIA CEP!");
    } else {
      log.info("Endereço encontrado no cache do Redis!");
    }

    return enderecoDTO;
  }
}
