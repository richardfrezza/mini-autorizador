# Projeto: Mini Autorizador

**_Etapa do processo seletivo - Teste técnico de programação_**

### Tecnologias Utilizadas

| **Tecnologia**      | **Descrição**                                                                     |
|---------------------|-----------------------------------------------------------------------------------|
| **Java 17**         | Linguagem de programação para o desenvolvimento da aplicação.                     |
| **Spring Boot**     | Framework para desenvolvimento de aplicações em Spring.                           |
| **Spring Security** | Implementação de segurança para autenticação de usuários e criptografia de senha. |
| **Spring Retry**    | Mecanismo para retry e controle de transações simultâneas.                        |
| **Flyway**          | Ferramenta para migração de banco.                                                |
| **Banco MySQL**     | Banco de dados relacional utilizado para armazenar dados relacionais.             |
| **Banco H2**        | Banco de dados utilizado para testes de integração.                               |
| **Junit**           | Framework para realização de testes unitários.                                    |
| **Mockito**         | Ferramenta para criação de mocks e testes isolados.                               |
| **MockMvc**         | Ferramenta para testes de integração                                              |
| **Swagger**         | Documentação básica. [Acesse aqui](http://localhost:8080/swagger-ui/index.html).  |

## Implementações

### 1. Criação de cartão

- **Método**: `POST`
- **URL**: `http://localhost:8080/cartoes`
- **Descrição**: Endpoint para criação de um novo cartão com saldo inicial de R$500.00.

#### Body:
```json
{
  "numeroCartao": "6549873025634501",
  "senha": "1234"
}
```

### 2. Consulta de saldos

- **Método**: `GET`
- **URL**: `http://localhost:8080/cartoes/{numeroCartao}`
- **Descrição**: Endpoint para consulta de saldo através da numeração do cartão.

### 3. Realização de transação

- **Método**: `POST`
- **URL**: `http://localhost:8080/transacoes`
- **Descrição**: Endpoint para realizar transações de débito no valor do cartão.

#### Body:
```json
{
  "numeroCartao": "6549873025634501",
  "senhaCartao": "1234",
  "valor": 10.00
}
```

### 4. **Autenticação com Spring Security**
A aplicação usa **Spring Security** para realizar o login de usuários utilizando **Bearer Basic**. Além disso, as senhas dos cartões de crédito são armazenadas de forma segura no banco de dados utilizando criptografia.

### 5. **Controle de transações simultâneas**
O sistema usa o **Spring Retry** com **concorrência otimista** para garantir o controle de transações simultâneas. A estratégia de controle de versão (`@Version`) é aplicada aos registros para evitar problemas de atualização de dados durante acessos concorrentes.

### 6. **Design Pattern**
Foi utilizado o pattern  **Responsibility of Chain** para encadear as regras de transação, permitindo que a lógica de negócio seja desacoplada e que as regras sejam processadas de forma sequencial e que novas regras 
possam ser incluídas sem quebrar o código existente.

### 7. **Centralização do tratamento de exceções**
As exceções foram centralizadas utilizado o ControllerAdvice. Para facilitar a manunteção e melhorar o controle de erros.  

### 8. **Documentação com Swagger**
A aplicação utiliza o **Swagger** para gerar uma documentação simplificada da API, facilitando o entendimento e a utilização da API pelos desenvolvedores e consumidores.
#### -------------------------------
_Obs: Saldo inicial do cartão está em placeholder de configuração com default(R$500), com o propósito
de poder ser alterada em qualquer ambiente sem alterar o código fonte da aplicação._