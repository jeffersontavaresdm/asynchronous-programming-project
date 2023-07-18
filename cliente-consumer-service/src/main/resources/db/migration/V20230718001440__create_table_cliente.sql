CREATE TABLE cliente
(
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(255) NOT NULL,
    endereco_id BIGINT UNIQUE,
    FOREIGN KEY (endereco_id) REFERENCES endereco (id)
);