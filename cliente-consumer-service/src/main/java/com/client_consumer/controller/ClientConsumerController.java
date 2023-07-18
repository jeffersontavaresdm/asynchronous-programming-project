package com.client_consumer.controller;

import com.client_consumer.entity.Cliente;
import com.client_consumer.service.ClientConsumerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClientConsumerController {

  private final ClientConsumerService clientConsumerService;

  public ClientConsumerController(ClientConsumerService clientConsumerService) {
    this.clientConsumerService = clientConsumerService;
  }

  @GetMapping
  public ResponseEntity<List<Cliente>> buscarClientes() {
    List<Cliente> clientes = clientConsumerService.buscarClientes();
    return ResponseEntity.status(HttpStatus.OK).body(clientes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
    Cliente cliente = clientConsumerService.buscarClientePorId(id);
    return ResponseEntity.status(HttpStatus.OK).body(cliente);
  }
}
