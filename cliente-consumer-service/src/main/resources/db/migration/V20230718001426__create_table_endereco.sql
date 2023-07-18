CREATE TABLE endereco
(
    id          BIGSERIAL PRIMARY KEY,
    cep         VARCHAR(10),
    logradouro  VARCHAR(255),
    complemento VARCHAR(255),
    bairro      VARCHAR(255),
    localidade  VARCHAR(255),
    uf          VARCHAR(2),
    ibge        VARCHAR(255),
    gia         VARCHAR(255),
    ddd         INTEGER,
    siafi       BIGINT
);
