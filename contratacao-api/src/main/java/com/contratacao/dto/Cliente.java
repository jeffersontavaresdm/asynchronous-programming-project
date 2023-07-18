package com.contratacao.dto;

import jakarta.validation.constraints.Pattern;

public record Cliente(
  String cliente,
  @Pattern(regexp = "\\d{8}", message = "O CEP deve ter 8 dígitos")
  String cep
) { }
