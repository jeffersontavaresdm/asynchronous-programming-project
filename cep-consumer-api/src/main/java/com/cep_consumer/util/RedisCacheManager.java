package com.cep_consumer.util;

import com.cep_consumer.entity.Cliente;
import com.cep_consumer.entity.dto.EnderecoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheManager {

  private static final String ENDERECO_CACHE_KEY_PREFIX = "endereco:";
  private static final String CLIENTE_CACHE_KEY_PREFIX = "cliente:";

  private final RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public RedisCacheManager(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public EnderecoDTO getEnderecoByRedisCache(String cep) {
    String cacheKey = ENDERECO_CACHE_KEY_PREFIX + cep;
    return (EnderecoDTO) redisTemplate.opsForValue().get(cacheKey);
  }

  public void cacheAddress(String cep, EnderecoDTO enderecoDTO) {
    String cacheKey = ENDERECO_CACHE_KEY_PREFIX + cep;
    redisTemplate.opsForValue().set(cacheKey, enderecoDTO);
  }

  public Cliente getClienteFromCache(String cep) {
    String cacheKey = CLIENTE_CACHE_KEY_PREFIX + cep;
    return (Cliente) redisTemplate.opsForValue().get(cacheKey);
  }

  public void cacheCliente(Cliente cliente) {
    String cacheKey = CLIENTE_CACHE_KEY_PREFIX + cliente.getEndereco().getCep();
    redisTemplate.opsForValue().set(cacheKey, cliente);
  }
}