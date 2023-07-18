package com.client_consumer.entity.dto;

import com.client_consumer.entity.Endereco;

public record EnderecoDTO(
  String cep,
  String logradouro,
  String complemento,
  String bairro,
  String localidade,
  String uf,
  String ibge,
  String gia,
  Integer ddd,
  Long siafi
) {
  public Endereco toEntity() {
    return new Endereco(
      null,
      this.cep,
      this.logradouro,
      this.complemento,
      this.bairro,
      this.localidade,
      this.uf,
      this.ibge,
      this.gia,
      this.ddd,
      this.siafi
    );
  }
}