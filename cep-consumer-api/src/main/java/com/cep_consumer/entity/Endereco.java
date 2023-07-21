package com.cep_consumer.entity;

import com.cep_consumer.entity.dto.EnderecoDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Endereco {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String cep;
  private String logradouro;
  private String complemento;
  private String bairro;
  private String localidade;
  private String uf;
  private String ibge;
  private String gia;
  private Integer ddd;
  private Long siafi;

  public EnderecoDTO toDTO() {
    return new EnderecoDTO(
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
