package com.cep_consumer.controller;

import com.cep_consumer.entity.Cliente;
import com.cep_consumer.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cep-consumer")
public class CepConsumerController {

  private final ClienteRepository clienteRepository;

  public CepConsumerController(ClienteRepository clienteRepository) {
    this.clienteRepository = clienteRepository;
  }

  @GetMapping("/cliente")
  public ResponseEntity<List<Cliente>> listar() {
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(clienteRepository.findAll());
  }
}
