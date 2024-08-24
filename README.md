Aqui está uma versão atualizada do README para refletir a inclusão do frontend em Vue.js:

---

# Magnetic Desafio

Este projeto é uma aplicação completa que inclui um backend desenvolvido com Spring Boot e um frontend desenvolvido com Vue.js.

## Backend (Spring Boot)

O backend fornece uma API para gerenciar funcionários e outras funcionalidades.

### Requisitos

Antes de iniciar o projeto, certifique-se de que você tem as seguintes ferramentas instaladas:

- [Java JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [MySQL](https://dev.mysql.com/downloads/)

### Configuração do Banco de Dados

As configurações do banco de dados estão localizadas no arquivo `src/main/resources/application.properties`. 

Aqui está um exemplo de como as configurações podem estar configuradas:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase
spring.datasource.username=root
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

- `spring.datasource.url`: URL de conexão do banco de dados.
- `spring.datasource.username`: Nome de usuário do banco de dados.
- `spring.datasource.password`: Senha do banco de dados.
- `spring.jpa.hibernate.ddl-auto`: Configura o comportamento do Hibernate para criar/atualizar as tabelas no banco de dados. `update` faz com que as tabelas sejam criadas ou atualizadas automaticamente ao iniciar o projeto.
- `spring.jpa.show-sql`: Configura o Spring Data JPA para mostrar as consultas SQL executadas.

### Como Rodar o Projeto

1. Clone o repositório:

    ```bash
    git clone https://github.com/seuusuario/myworkmanager_back.git
    cd myworkmanager_back
    ```

2. Compile e inicie o projeto usando Maven:

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

3. O aplicativo será iniciado na porta padrão `8081`. Você pode acessar a API via [http://localhost:8081](http://localhost:8081).

### Dependências

Este projeto usa as seguintes dependências:

- **Spring Boot Starter Data JDBC** e **Spring Boot Starter Data JPA** para acesso ao banco de dados.
- **Spring Boot Starter Web Services** para suporte a Web Services.
- **Spring Boot DevTools** para facilidades durante o desenvolvimento.
- **MySQL Connector** para conectar o MySQL ao Spring Boot.
- **Lombok** para reduzir o boilerplate de código.

## Frontend (Vue.js)

O frontend é desenvolvido usando Vue.js e Vuetify para criar uma interface de usuário rica e interativa.

### Requisitos

Certifique-se de que você tem o [Node.js](https://nodejs.org/) e o [npm](https://www.npmjs.com/) instalados.

### Instalação e Execução

1. Clone o repositório:

    ```bash
    git clone https://github.com/seuusuario/magnetic_desafio.git
    cd magnetic_desafio
    ```

2. Instale as dependências:

    ```bash
    npm install
    ```

3. Execute o servidor de desenvolvimento:

    ```bash
    npm run serve
    ```

4. O frontend será iniciado na porta padrão `8080`. Você pode acessar a aplicação via [http://localhost:8080](http://localhost:8080).

### Dependências

As principais dependências do frontend são:

- **Vue.js** para a criação da interface do usuário.
- **Vuetify** para componentes de UI baseados em Material Design.
- **axios** para comunicação com o backend.
- **vue-chartjs** para gráficos.
- **webfontloader** para carregar fontes externas.

### Scripts

- **serve**: Inicia o servidor de desenvolvimento.
- **build**: Compila o projeto para produção.
- **lint**: Executa o linting no código.

### Git Hooks

- **pre-commit**: Executa o lint-staged para garantir a qualidade do código antes do commit.

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).

---

Se precisar de mais alguma coisa, é só avisar!