# LibraryAPI

LibraryAPI é uma aplicação web desenvolvida para gerenciar o catálogo de uma biblioteca, com suporte para cadastro de livros e autores. O sistema possui autenticação e controle de acesso baseado em roles, garantindo segurança e restrições de acesso a determinadas funcionalidades.

---

## Funcionalidades

### Cadastro e Gerenciamento de Livros e Autores:
- Adicionar, editar, pesquisar, excluir Livros e Autores.
- Associar livros a autores.

### Autenticação e Autorização:
- **Servidor de Autenticação Próprio**:
  - Implementação personalizada para autenticação de usuários.
- **Controle de Acesso Baseado em Roles**:
  - `ROLE_GERENTE`: acesso a funcionalidades administrativas.
  - `ROLE_OPERADOR`: acesso restrito a funcionalidades específicas.
- **Credenciais Pré-configuradas**:
  - **Gerente**:
    - Username: `gerente`
    - Password: `gerente123`
  - **Operador**:
    - Username: `operador`
    - Password: `operador123`

### Validação de Dados:
- Uso do **Spring Validation** para garantir a consistência dos dados enviados.

### Documentação da API:
- Documentação interativa gerada com **Swagger**.  
  Acesse em: [Swagger UI](http://184.73.117.44:8080/swagger-ui/index.html)

---

## Tecnologias Utilizadas
- **Linguagem**: Java 21  
- **Framework Backend**: Spring Boot
- **Segurança**: Spring Security  
- **Autenticação**: OAuth2 e JWT
- **Banco de Dados**: PostgreSQL  
- **Containerização**: Docker  
- **Validação**: Spring Validation  
- **Documentação**: Swagger  
- **Hospedagem**: AWS EC2 e AWS RDS  

---

## Deploy na AWS
A aplicação está hospedada em uma instância **Amazon EC2**, com o banco de dados configurado no serviço **Amazon RDS** utilizando PostgreSQL.  

### Endereço da Aplicação:
- [http://184.73.117.44:8080](http://184.73.117.44:8080)

---

## Obter Token de Autenticação
Para acessar as funcionalidades protegidas, é necessário obter um token de autenticação via OAuth2.  

### Credenciais do Client
- **Client ID**: `client-production`
- **Client Secret**: `secret-production`
- **scope**: `OPERADOR`


