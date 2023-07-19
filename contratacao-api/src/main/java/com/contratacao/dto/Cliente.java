package com.contratacao.dto;

import jakarta.validation.constraints.Pattern;

public record Cliente(
  String cliente,
  @Pattern(regexp = "\\d{8}", message = "O CEP deve ter 8 dígitos")
  String cep,

  @Pattern(regexp = "\\d{11}", message = "O CPF deve ter 11 dígitos")
  String cpf
) { }
