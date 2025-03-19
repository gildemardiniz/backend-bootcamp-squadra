# API de Cadastro de Pessoas - Projeto Final do Bootcamp Java

Este é o projeto final do **Bootcamp Java** da **Squadra Digital**, onde desenvolvi uma **API REST** para o cadastro de pessoas utilizando **Java com Spring Boot**. O objetivo do projeto foi construir uma API robusta, organizada e bem documentada, utilizando as melhores práticas do desenvolvimento back-end.

## Tecnologias Usadas

- **Back-End**:
  - Java 21
  - Spring Boot
  - Spring Data JPA
  - Spring Validation
  - Swagger (para documentação)
  
- **Banco de Dados**:
  - **Oracle Database** (configurado para persistência dos dados)

## Funcionalidades da API

A API permite realizar as seguintes operações:

- **Cadastro de Pessoas**: Cadastro de informações como nome, idade, e endereço.
- **Listagem de Pessoas**: Consulta a lista de pessoas cadastradas.
- **Atualização de Cadastro**: Atualização de dados de uma pessoa já cadastrada.
- **Exclusão de Cadastro**: Exclusão de um cadastro existente.

## Documentação da API

Para facilitar o uso e a compreensão da API, implementei **Swagger**. Após rodar a aplicação, você pode acessar a documentação interativa da API em:

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`

## Instalação

### Requisitos

- **Java 21** ou superior
- **Maven** 3.x ou superior
- **Banco de Dados Oracle** (configurado no `application.properties` ou `application.yml`)

### Passos para Instalar

1. Clone o repositório:
    ```bash
    git clone https://github.com/seu-usuario/cadastro-pessoas-api.git
    ```

2. Navegue até o diretório do projeto:
    ```bash
    cd cadastro-pessoas-api
    ```

3. Instale as dependências do back-end utilizando o Maven:
    ```bash
    mvn clean install
    ```

4. Configure as credenciais do banco de dados Oracle no arquivo `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    ```

5. Execute o back-end:
    ```bash
    mvn spring-boot:run
    ```

   A API estará rodando em `http://localhost:8080`.

## Como Usar

1. Acesse a documentação interativa da API em `http://localhost:8080/swagger-ui/index.html`.
2. Utilize os endpoints documentados para interagir com a API, realizar cadastros e consultar as pessoas.

## Contribuindo

Se você deseja contribuir com o projeto, siga os passos abaixo:

1. Faça um fork do repositório.
2. Crie uma branch com sua feature:
    ```bash
    git checkout -b minha-feature
    ```
3. Commit suas mudanças:
    ```bash
    git commit -m 'Adicionando minha feature'
    ```
4. Envie para o repositório remoto:
    ```bash
    git push origin minha-feature
    ```
5. Abra um Pull Request no GitHub.

## Agradecimentos

Agradeço à **Squadra Digital** pela oportunidade de aprender e crescer durante o bootcamp. Foi uma experiência que me desafiou e me permitiu desenvolver habilidades essenciais para minha jornada na tecnologia.

## Front-End

Você pode encontrar o código do **front-end** neste repositório: [frontend-bootcamp-squadra](https://github.com/gildemardiniz/frontend-bootcamp-squadra)

## Contato

[Gildemar Diniz](https://www.linkedin.com/in/gildemardiniz) | diniz.g.dev@gmail.com
