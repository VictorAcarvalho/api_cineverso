# Cineverso Api 🎥

### Essa é a documentação da api do cineverso, uma api para amantes da sétima arte.

## Arquitetura 

Foi escolhida a arquitetura hexagonal com repository e services pattern, afim de modularizar
a aplicação, deixando as responsabilidades para cada camada, como por exemplo a camada de repository
está responsável por fazer a persistência de dados no banco. Além disso foi utilizado também o padrão adapter,
para transformar dados que a API recebe nas requisições, em entidades ou outros objetos. Para ter uma
maior segurança, foi implementado o esquema de autenticação e autorização com o JWT(Json Web Token), que de maneira stateless consegue fazer a validação de
um token entre as aplicações. Dessa forma podemos utilizar de boas práticas e passarmos como por exemplo
o identificador único e a credencial do usuário dentro do JWT. Com essa implementação, alguns endpoints como update de usuário, criação de filmes e votação de filmes, 
fossem identificados por credencial(role) e usuário, evitando a alteração de dados de outras pessoas ou sem a autorização necessária. 

- Pontos de melhoria:

  - Melhoria na trativa de erro, utilizando um padrão factory para definir alguns casos.
  - Melhoria no adapter para verificação de dados
  - Implementação de padrão strategy case para validação de dados


## Milestones
- [x] Endpoints de usuário (criação, soft delete, update, busca)
- [x] Autenticação e autorização
- [x] Endpoints de filmes
- [ ] Ativação de usuário por email
- [ ] Cofiguração orquestração de container
- [ ] Observabilidade e melhoria de logs (Prometheus e Grafana)
- [ ] Implementação de Linter (CheckStyle)
- [ ] CD/CD (Cobertura de testes, padronização de código, vulnerabilidade(*trivy*), deploy )
- [ ] Deploy em ambiente cloud
- [ ] Documentação Swagger

## Tecnologias

#### A api foi desenvolvida em Java 21 com Springboot, utilizando pacotes como SpringSecurity,Lombok , SpringData-JPA, Actuator e Oauth2.
#### Além disso a API tem alguns serviços para desenvolvimento local com Docker e o compose. Essas escolhas de tecnologia, foram feitas para termos além de um ambiente de teste local, escalabilidade ao longo do desenvolvimento do produto,dado tanto pela orientação a objetos do java, quanto pela injeção de controle e inverssão de dependência do Spring.

## Como rodar:

### O que você precisa ter em seu ambiente 💻 :

- Antes de mais nada verifique se você tenha o Java 21 instalado em seu ambiente de trabalho, caso não tenha
você pode seguir esse [tutorial](https://www.gasparbarancelli.com/post/como-baixar-e-instalar-o-java-(jvm)-passo-a-passo-para-windows,-linux-e-mac-com-sdkman)

- Após fazer toda a instalação verifique também se você tem o gradle instalado. 
 Caso também não tenha você pode seguir esse [passo a passo](https://docs.gradle.org/current/userguide/installation.html).


- Depois de fazer os passos acima, verifique se sua IDE tem suporte para a linguagem.


- Além disso, para subir o banco de dados e o PGadmin, você precisará instalar o docker, então você 
pode seguir esse [tutorial](https://gestaoderedessociais.com.br/como-instalar-o-docker-no-linux-no-windows-e-no-macos/) 

### Configurações:

Antes de qualquer coisa, precisamos definir nossas chaves públicas e privadas para o encode 
e decode do JWT. Para isso podemos utilizar algumas ferramentas, porém como exemplo irei utilizar o [oppenssl](https://www.openssl.org).

- Dentro da pasta resources do projeto  rode o comando :

````
openssl genrsa > app.key  
````
Assim ele irá gerar uma nova chave no arquivo app.key. Após a criação da chave privada vamos criar a chave
 pública com o comando:

````
openssl  rsa -in app.key -pubout -out app.pub 
````

Após isso podemos rodar o comando do docker compose para subirmos nosso banco de dados e o pgAdmin:
````
docker compose up 
````

E assim podemos então rodar a nossa aplicação no _ApiCineversoApplication.java_. 


````
É recomendado que o primeiro usuário seja criado na rota /users/admin com o campo role:ADMIN
````


### Acessando pgAdmin 

- Para acessar o pgAdmin, após subir o container docker, vá para http://localhost:5050/, e coloque as credenciais:

_user: admin@admin.com_

_password: admin_

Após entrar no pgAdmin, você deve conectar o banco de dados que tem os seguintes configurações:

- Nome do serviço(banco) : db
- porta: 5432
- user: myuser
- password: secret


### Rotas e endpoints:
 
- É possível conferir as rotas e endpoints com alguns exemplos de requisições e respostas nesse [link](https://www.apidog.com/apidoc/shared-41640e3c-e716-41d7-82c1-26a98307db38/api-15054907)