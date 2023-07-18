package com.contratacao.controller;

import com.contratacao.dto.Cliente;
import com.contratacao.service.ContratacaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contratacao")
public class ContratacaoController {

  private final ContratacaoService contratacaoService;

  public ContratacaoController(ContratacaoService contratacaoService) {
    this.contratacaoService = contratacaoService;
  }

  @PostMapping
  public void enviarContratacao(@RequestBody @Valid Cliente cliente) {
    contratacaoService.enviarSolicitacaoCliente(cliente);
  }
}
