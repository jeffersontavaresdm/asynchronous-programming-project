package com.client_consumer.service;

import com.client_consumer.entity.Cliente;
import com.client_consumer.exception.ClienteNotFoundException;
import com.client_consumer.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientConsumerService {

  private final ClienteRepository clienteRepository;

  public ClientConsumerService(ClienteRepository clienteRepository) {
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
