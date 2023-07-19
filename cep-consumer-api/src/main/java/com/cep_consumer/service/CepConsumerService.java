package com.cep_consumer.service;

import com.cep_consumer.entity.Cliente;
import com.cep_consumer.exception.ClienteNotFoundException;
import com.cep_consumer.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CepConsumerService {

  private final ClienteRepository clienteRepository;

  public CepConsumerService(ClienteRepository clienteRepository) {
    this.clienteRepository = clienteRepository;
  }

  public List<Cliente> buscarClientes() {
    return clienteRepository.findAll();
  }

  public Cliente buscarClientePorId(Long id) {
    Optional<Cliente> optionalCliente = clienteRepository.findById(id);

    if (optionalCliente.isPresent()) {
      return optionalCliente.get();
    }

    throw new ClienteNotFoundException();
  }
}
