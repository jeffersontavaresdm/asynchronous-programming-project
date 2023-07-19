package com.cep_consumer.controller;


import com.cep_consumer.entity.Cliente;
import com.cep_consumer.service.CepConsumerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cep-consumer")
public class CepConsumerController {

  private final CepConsumerService cepConsumerService;

  public CepConsumerController(CepConsumerService cepConsumerService) {
    this.cepConsumerService = cepConsumerService;
  }

  @GetMapping
  public ResponseEntity<List<Cliente>> buscarClientes() {
    List<Cliente> clientes = cepConsumerService.buscarClientes();
    return ResponseEntity.status(HttpStatus.OK).body(clientes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
    Cliente cliente = cepConsumerService.buscarClientePorId(id);
    return ResponseEntity.status(HttpStatus.OK).body(cliente);
  }
}