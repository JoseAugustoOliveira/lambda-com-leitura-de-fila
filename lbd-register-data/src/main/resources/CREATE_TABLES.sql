
-- tabela de endereços
CREATE TABLE IF NOT EXISTS invest.tb_endereco (
    id_pk BIGSERIAL PRIMARY KEY,
    rua VARCHAR(100),
    numero VARCHAR(20),
    cidade VARCHAR(80),
    estado VARCHAR(30),
    bairro VARCHAR(30)
);

-- tabela de movimentações
CREATE TABLE IF NOT EXISTS invest.tb_movimentacao (
    id_pk BIGSERIAL PRIMARY KEY,
    movementacao_id UUID NOT NULL,
    nome VARCHAR(100),
    email VARCHAR(100),
    descricao TEXT,
    nu_cpf VARCHAR(11),
    dt_criacao TIMESTAMP,
    dt_atualizacao TIMESTAMP,
    valor NUMERIC(19,2),
    status VARCHAR(50),
    dt_entrega DATE,
    endereco_id BIGINT,
    CONSTRAINT fk_mov_endereco FOREIGN KEY (endereco_id)
        REFERENCES invest.tb_endereco (id_pk)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- tabela de erros
CREATE TABLE IF NOT EXISTS invest.tb_movimentacao_erro (
    id_pk BIGSERIAL PRIMARY KEY,
    nu_cpf VARCHAR(11),
    campo VARCHAR(50),
    msg_erro TEXT,
    json_bruto TEXT,
    dt_erro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- índices úteis
CREATE INDEX IF NOT EXISTS idx_movimento_movementacao_id
    ON invest.tb_movimentacao (movementacao_id);

CREATE INDEX IF NOT EXISTS idx_movimento_nu_cpf
    ON invest.tb_movimentacao (nu_cpf);

CREATE INDEX IF NOT EXISTS idx_erro_nu_cpf
    ON invest.tb_movimentacao_erro (nu_cpf);


    -- criação de novas colunas
ALTER TABLE INVEST.TB_ENDERECO
ADD COLUMN COMPLEMENTO VARCHAR(50);

ALTER TABLE INVEST.TB_ENDERECO
ADD COLUMN CD_POSTAL VARCHAR(8);