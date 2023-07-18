# Asynchronous Programming Project

Este guia fornece instruções sobre como usar os comandos do LocalStack para interagir com a fila SQS localmente. O LocalStack é uma ferramenta que simula serviços da AWS em um ambiente local para fins de desenvolvimento e testes.

## Pré-requisitos

Certifique-se de ter o Docker instalado em sua máquina antes de começar. Você pode baixar o Docker em: [https://www.docker.com/](https://www.docker.com/)

## Configuração do LocalStack

Antes de usar os comandos do LocalStack, é necessário configurar o ambiente local. Siga as etapas abaixo:

1. Inicie o LocalStack usando o Docker Compose. Abra um terminal e navegue até o diretório onde está localizado o arquivo `docker-compose.yaml`. Em seguida, execute o seguinte comando:
> _docker-compose up_


2. Aguarde até que o LocalStack esteja pronto para uso. Você pode verificar o status dos contêineres do LocalStack usando o seguinte comando:
> _docker-compose ps_


3. Configure as credenciais da AWS para o perfil `localstack` usando o seguinte comando:
> _aws configure --profile localstack_

Quando solicitado, insira as seguintes informações:
- AWS Access Key ID:        `<valor qualquer>`
- AWS Secret Access Key:    `<valor qualquer>`
- Default region name:      `us-east-1`
- Default output format:    `json`

4. A configuração do LocalStack está concluída. Agora você pode usar os comandos abaixo para interagir e testar a fila SQS localmente.

## Comandos do LocalStack

Aqui estão os principais comandos do LocalStack para interagir com a fila SQS:

- **Criar uma fila**:
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name fila-contratacao --profile localstack

- **Enviar uma mensagem para a fila**:
aws --endpoint-url=http://localhost:4566 sqs send-message --queue-url http://localhost:4566/000000000000/fila-contratacao --message-body "minha mensagem" --profile localstack --region us-east-1


- **Receber uma mensagem da fila**:
aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/fila-contratacao --profile localstack --region us-east-1


- **Excluir uma mensagem da fila**:
aws --endpoint-url=http://localhost:4566 sqs delete-message --queue-url http://localhost:4566/000000000000/fila-contratacao --receipt-handle <ReceiptHandle> --profile localstack --region us-east-1


Certifique-se de substituir `<ReceiptHandle>` pelo valor real do "ReceiptHandle" retornado ao receber uma mensagem da fila.

### Suba o banco de dados que armazenará os dados:

- Entre no diretório _cliente-consumer-service_ e use o comando:
> _docker compose up_

- Suba o serviço com o comando: mvn clean package && mvn spring-boot:run
- Entre no diretório do serviço _contratacao-api_
- Suba o serviço com o comando: mvn clean package && mvn spring-boot:run

### Endpoints
- Você pode enviar o nome do cliente e o cep em uma requisição do tipo **POST** para:
> **http://127.0.0.1:8080/contratacao**
- Você pode receber os dados de um cliente com uma requisição do tipo GET para:
> **http://localhost:8081/cliente/1**
- Vocêw pode receber os dados de todos os cliente com uma requisição do tipo GET para:
> **http://localhost:8081/cliente**

## Conclusão
Agora você está pronto para começar a usar o projeto de programação assíncrona com o LocalStack e a fila SQS localmente.

Você configurou o ambiente do LocalStack, criou a fila SQS, e aprendeu como enviar, receber e excluir mensagens da fila usando os comandos fornecidos.

Além disso, você subiu o banco de dados necessário para armazenar os dados e iniciou os serviços contratacao-api e cliente-consumer-service.

Agora você pode explorar e testar o projeto, enviando mensagens para a fila e verificando como o serviço cliente-consumer-service consome essas mensagens e armazena os dados no banco de dados.

valeus!
