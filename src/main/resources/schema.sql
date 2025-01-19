CREATE TABLE customer (
                         id SERIAL PRIMARY KEY,
                         email VARCHAR(255) NOT NULL,
                         is_vip BOOLEAN NOT NULL
);

CREATE TABLE item (
                      id SERIAL PRIMARY KEY,
                      nome VARCHAR(255) NOT NULL,
                      preco DOUBLE PRECISION NOT NULL,
                      quantidade INT NOT NULL,
                      estoque INT NOT NULL
);

CREATE TABLE order_table (
                        id SERIAL PRIMARY KEY,
                        cliente_id BIGINT REFERENCES cliente(id),
                        total DOUBLE PRECISION,
                        total_com_desconto DOUBLE PRECISION,
                        em_estoque BOOLEAN,
                        data_entrega DATE
);

CREATE TABLE order_items (
                              pedido_id BIGINT REFERENCES pedido(id),
                              item_id BIGINT REFERENCES item(id),
                              PRIMARY KEY (pedido_id, item_id)
);
