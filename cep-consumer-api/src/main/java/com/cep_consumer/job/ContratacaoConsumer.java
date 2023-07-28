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
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
public class ContratacaoConsumer {

  private final ClienteRepository clienteRepository;
  private final EnderecoRepository enderecoRepository;
  private final RedisCacheManager redisCacheManager;
  private final MeterRegistry meterRegistry;

  public ContratacaoConsumer(
    ClienteRepository clienteRepository,
    EnderecoRepository enderecoRepository,
    RedisCacheManager redisCacheManager,
    MeterRegistry meterRegistry
  ) {
    this.clienteRepository = clienteRepository;
    this.enderecoRepository = enderecoRepository;
    this.redisCacheManager = redisCacheManager;
    this.meterRegistry = meterRegistry;
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
      // quantidade de sucessos de enderecos buscados
      meterRegistry.counter("consulta_cep_successful").increment();

      Endereco endereco = enderecoRepository.save(enderecoDTO.toEntity());
      Cliente cliente = clienteRepository.save(new Cliente(null, message.cliente(), endereco));

      redisCacheManager.cacheCliente(cliente);

      log.info("Cliente salvo no banco de dados. Cliente: {}", cliente.getNome());
    } else {
      log.info("Endereço não encontrado!");
    }
  }

  public EnderecoDTO getEndereco(String cep) {
    // quantidade de buscas por endereco (viacep ou redis)
    meterRegistry.counter("consulta_cep").increment();

    long start = System.currentTimeMillis();

    EnderecoDTO enderecoDTO = null;
    try {
      enderecoDTO = redisCacheManager.getEnderecoByRedisCache(cep);
    } catch (Exception exception) {
      // quantidade de consultas ao redis que deu erro
      meterRegistry.counter("consulta_perdida_redis").increment();
    }

    // +- quanto tempo leva para buscar um endereco no redis
    meterRegistry
      .timer("redis_consulta_cep")
      .record(Duration.of(System.currentTimeMillis() - start, ChronoUnit.MILLIS));

    if (enderecoDTO == null) {
      RestTemplate restTemplate = new RestTemplate();

      String url = UriComponentsBuilder
        .fromUriString("https://viacep.com.br/ws/{cep}/json/")
        .buildAndExpand(cep)
        .toUriString();

      start = System.currentTimeMillis();

      try {
        enderecoDTO = restTemplate.getForObject(url, EnderecoDTO.class);
      } catch (Exception exception) {
        // quantidade de consultas a api do viacep que deu erro
        meterRegistry.counter("consulta_perdida_viacep").increment();
      }

      // +- quanto tempo leva para buscar um endereco na api do viacep
      meterRegistry
        .timer("viacep_consulta_cep")
        .record(Duration.of(System.currentTimeMillis() - start, ChronoUnit.MILLIS));

      if (enderecoDTO != null && enderecoDTO.cep() != null) {
        redisCacheManager.cacheAddress(cep, enderecoDTO);
        log.info("Endereço encontrado na api VIA CEP!");
      }
    } else {
      log.info("Endereço encontrado no cache do Redis!");
    }

    return enderecoDTO;
  }
}
