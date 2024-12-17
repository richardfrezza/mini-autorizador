# Projeto: Mini Autorizador

**_Etapa do processo seletivo - Teste t�cnico de programa��o_**

### Tecnologias Utilizadas

| **Tecnologia**      | **Descri��o**                                                                     |
|---------------------|-----------------------------------------------------------------------------------|
| **Java 17**         | Linguagem de programa��o para o desenvolvimento da aplica��o.                     |
| **Spring Boot**     | Framework para desenvolvimento de aplica��es em Spring.                           |
| **Spring Security** | Implementa��o de seguran�a para autentica��o de usu�rios e criptografia de senha. |
| **Spring Retry**    | Mecanismo para retry e controle de transa��es simult�neas.                        |
| **Flyway**          | Ferramenta para migra��o de banco.                                                |
| **Banco MySQL**     | Banco de dados relacional utilizado para armazenar dados relacionais.             |
| **Banco H2**        | Banco de dados utilizado para testes de integra��o.                               |
| **Junit**           | Framework para realiza��o de testes unit�rios.                                    |
| **Mockito**         | Ferramenta para cria��o de mocks e testes isolados.                               |
| **MockMvc**         | Ferramenta para testes de integra��o                                              |
| **Swagger**         | Documenta��o b�sica. [Acesse aqui](http://localhost:8080/swagger-ui/index.html).  |

## Implementa��es

### 1. Cria��o de cart�o

- **M�todo**: `POST`
- **URL**: `http://localhost:8080/cartoes`
- **Descri��o**: Endpoint para cria��o de um novo cart�o com saldo inicial de R$500.00.

#### Body:
```json
{
  "numeroCartao": "6549873025634501",
  "senha": "1234"
}
```

### 2. Consulta de saldos

- **M�todo**: `GET`
- **URL**: `http://localhost:8080/cartoes/{numeroCartao}`
- **Descri��o**: Endpoint para consulta de saldo atrav�s da numera��o do cart�o.

### 3. Realiza��o de transa��o

- **M�todo**: `POST`
- **URL**: `http://localhost:8080/transacoes`
- **Descri��o**: Endpoint para realizar transa��es de d�bito no valor do cart�o.

#### Body:
```json
{
  "numeroCartao": "6549873025634501",
  "senhaCartao": "1234",
  "valor": 10.00
}
```

### 4. **Autentica��o com Spring Security**
A aplica��o usa **Spring Security** para realizar o login de usu�rios utilizando **Bearer Basic**. Al�m disso, as senhas dos cart�es de cr�dito s�o armazenadas de forma segura no banco de dados utilizando criptografia.

### 5. **Controle de transa��es simult�neas**
O sistema usa o **Spring Retry** com **concorr�ncia otimista** para garantir o controle de transa��es simult�neas. A estrat�gia de controle de vers�o (`@Version`) � aplicada aos registros para evitar problemas de atualiza��o de dados durante acessos concorrentes.

### 6. **Design Pattern**
Foi utilizado o pattern  **Responsibility of Chain** para encadear as regras de transa��o, permitindo que a l�gica de neg�cio seja desacoplada e que as regras sejam processadas de forma sequencial e que novas regras 
possam ser inclu�das sem quebrar o c�digo existente.

### 7. **Centraliza��o do tratamento de exce��es**
As exce��es foram centralizadas utilizado o ControllerAdvice. Para facilitar a manunte��o e melhorar o controle de erros.  

### 8. **Documenta��o com Swagger**
A aplica��o utiliza o **Swagger** para gerar uma documenta��o simplificada da API, facilitando o entendimento e a utiliza��o da API pelos desenvolvedores e consumidores.
#### -------------------------------
_Obs: Saldo inicial do cart�o est� em placeholder de configura��o com default(R$500), com o prop�sito
de poder ser alterada em qualquer ambiente sem alterar o c�digo fonte da aplica��o._