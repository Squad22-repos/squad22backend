# Squad 22 - Backend
## Descrição

Este repositório contém o código do servidor desenvolvido pelo Squad 22.
## Instalação e Configuração

A aplicação foi desenvolvida no IntelliJ utilizando a linguagem Java versão 21, o framework Spring Boot, e é compilada com Maven. Para executá-la localmente, certifique-se de que tanto o JDK quanto o Maven estão instalados e configurados nas variáveis de ambiente. Verifique também se estão configurados corretamente na sua IDE.

Além disso, para executar a aplicação com um banco de dados local, será necessário utilizar um banco de dados PostgreSQL. Você pode utilizar os scripts SQL fornecidos na pasta database, ou utilizar o contêiner do banco de dados conforme descrito no README do repositório principal.

Para um guia mais completo da instalação do JDK e do Maven, veja este [vídeo](https://www.youtube.com/watch?v=WASIyomqarc).
### Instalação do Java

  Acesse [OpenJDK](https://jdk.java.net/archive/), baixe e instale o JDK versão 21 ou superior.
  Após a instalação, configure as variáveis de ambiente:
  
  - Abra o "Painel de Controle" > "Sistema e Segurança" > "Sistema" > "Configurações avançadas do sistema".
  - Clique em "Variáveis de Ambiente".
  - Em "Variáveis do sistema", clique em "Novo" e adicione JAVA_HOME apontando para o diretório de instalação do JDK (por exemplo, C:\Program Files\Java\jdk-21).
  - Encontre a variável Path, selecione-a e clique em "Editar". Adicione %JAVA_HOME%\bin ao caminho.

### Instalação do Maven

  Acesse o site oficial do [Maven](https://maven.apache.org/download.cgi) e baixe a versão mais recente.
  Extraia o conteúdo do arquivo baixado para um diretório de sua escolha (por exemplo, C:\Program Files\Maven).
  Configure as variáveis de ambiente:

  - Abra o "Painel de Controle" > "Sistema e Segurança" > "Sistema" > "Configurações avançadas do sistema".
  - Clique em "Variáveis de Ambiente". 
  - Em "Variáveis do sistema", clique em "Novo" e adicione M2_HOME apontando para o diretório de instalação do Maven (por exemplo, C:\Program Files\Maven\apache-maven-3.x.y).
  - Encontre a variável Path, selecione-a e clique em "Editar". Adicione %M2_HOME%\bin ao caminho.

## Configuração da Aplicação

Após instalar e configurar o Java e o Maven, clone este repositório em uma IDE dedicada à linguagem Java, preferencialmente o IntelliJ IDEA. Ao clonar o repositório, certifique-se de que a aplicação está configurada para usar o JDK 21. Além disso, você pode construir as imagens dos contêineres localmente utilizando a branch local e o comando ``docker compose up``.

## Configuração do Banco de Dados

Existem três opções para utilizar um banco de dados com a aplicação:

  1. Banco de Dados na Azure
     
      O código presente na branch main já está configurado para utilizar um banco de dados em um servidor online na Azure. Portanto, essa branch pode ser executada diretamente.

  3. Banco de Dados Contêinerizado

      Conforme citado no README principal, existe uma imagem do nosso banco de dados disponível neste [repositório Docker](https://hub.docker.com/repository/docker/anthonyyuriff579/22squad/general). Para utilizar a aplicação desta forma, faça o pull dessa imagem e execute um contêiner com ela. Depois, verifique nas propriedades da aplicação da branch local se a porta do banco de dados está configurada corretamente para corresponder à do contêiner iniciado.

  5. Recriação do Banco de Dados

      Os scripts utilizados para criar o banco de dados estão na pasta database, no arquivo sql_scripts.sql. Caso necessário, você pode utilizá-los para recriar o banco de dados PostgreSQL. Para isso, crie um servidor PostgreSQL localmente, de preferência usando a porta 5432 (porta padrão do PostgreSQL) e a senha 12345, que já está configurada nas propriedades da aplicação. Após isso, execute os scripts para recriar o banco de dados.

## Execução

Depois de configurar a aplicação, apenas clique no botão de executar no Intellij ou execute o arquivo Jar na pasta target, caso seja necessário utilize o comando ``mvn package -DskipTests`` para recompilar a aplicação.
