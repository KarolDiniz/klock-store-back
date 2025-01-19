# Klock Store - Backend

Este é o backend da aplicação **Klock Store** lembrando que tem o frontend. Este é um sistema de gestão de pedidos, clientes e itens. Este projeto utiliza **Java 17** e o framework **Spring Boot** para implementar os endpoints RESTful.

## Funcionalidades

A API oferece CRUD completo para:

- **Clientes**: Cadastrar, listar, atualizar e excluir clientes.
- **Itens**: Cadastrar, listar, atualizar e excluir itens.
- **Pedidos**: Criar, listar, atualizar e excluir pedidos.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot** (version 2.x)
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger/OpenAPI** para documentação da API

## Requisitos

- **Java 17**
- **Banco de Dados PostgreSQL** 
- **Maven** para gerenciamento de dependências.

## Como Rodar o Projeto

### Passo 1: Clonar o Repositório

Clone este repositório para a sua máquina local:

```bash
git clone https://github.com/KarolDiniz/klock-store-backend.git
cd klock-store-backend
```
Ou faça o download do projeto em formato zip e extraia o conteúdo.

### Passo 2: Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL com o nome **klockstore**.

2. Configure as credenciais do banco no arquivo src/main/resources/application.properties:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/klockstore
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```
Substitua seu_usuario e sua_senha pelas credenciais corretas do seu banco de dados.

### Passo 3:  Instalar Dependências

Com o Maven instalado, rode o seguinte comando para baixar as dependências:

```bash
mvn clean install
```

### Passo 4: Rodar o Projeto

Execute (rode) o projeto na sua máquina.

### Passo 5: Testando da API

A API possui os seguintes endpoints:

- **Clientes**: 
  - GET /api/clientes
  - GET /api/clientes/{id}
  - POST /api/clientes
  - PUT /api/clientes/{id}
  - DELETE /api/clientes/{id}

Exemplo do corpo da requisição (POST):
```json
{
  "email": "cliente@gmail.com",
  "vip": true
}
```

- **Itens**:
  - GET /api/itens
  - GET /api/itens/{id}
  - POST /api/itens
  - PUT /api/itens/{id}
  - DELETE /api/itens/{id}

Exemplo do corpo da requisição (POST):
```json
{
  "nome": "nome do item",
  "preco": 10.0,
  "quantidade": 1,
  "estoque": 10
}
```

- **Pedidos**:
  - GET /api/pedidos
  - GET /api/pedidos/{id}
  - POST /api/pedidos
  - PUT /api/pedidos/{id}
  - DELETE /api/pedidos/{id}

Exemplo do corpo da requisição (POST):
```json
{
  "cliente": {
    "id": 1
  },
  "items": [
    {
      "id": 1
    },
    {
      "id": 2
    }
  ]
}
```

### Rodando com o front-end:

Execute o backend junto com o frontend, você pode clonar o repositório do frontend e seguir os passos abaixo:

**Repositório do Frontend:** https://github.com/KarolDiniz/klock-store-front

### Documentação com o swagger:
documentação da API no Swagger em http://localhost:8080/swagger-ui/index.html


