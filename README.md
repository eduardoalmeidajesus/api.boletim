# Sistema de Gerenciamento de Boletins de Ocorrência de Furto de Veículos

Projeto desenvolvido em **Java** para a disciplina **APIs e Web Services**, com o objetivo de implementar uma **Web API REST** para o gerenciamento de **boletins de ocorrência de furto de veículos**, integrada a uma **aplicação web**.

---

## 📚 Descrição do Projeto

Este sistema tem como finalidade o **gerenciamento do ciclo de vida de boletins de ocorrência de furto de veículos**, permitindo tanto o **cadastro manual dos registros** quanto o **carregamento de dados reais disponibilizados pela Secretaria de Segurança Pública do Estado de São Paulo (SSP-SP)**.

A aplicação foi desenvolvida como uma **Web API REST utilizando Spring Boot e JAX-RS**, seguindo os conceitos e boas práticas abordados em sala de aula.  

Todas as informações são **armazenadas em memória**.

Para atender às diretrizes da **LGPD (Lei Geral de Proteção de Dados)**, os dados sensíveis das pessoas envolvidas (vítimas) são **omitidos nas respostas da API**.

---

### Funcionalidades do sistema

- ✅ Cadastro de boletins de ocorrência de furto de veículos  
- ✅ Atualização de boletins previamente cadastrados  
- ✅ Exclusão de boletins de ocorrência  
- ✅ Listagem de boletins de ocorrência com filtros:
  - Identificador do boletim  
  - Cidade onde ocorreu o furto  
  - Período da ocorrência (Manhã, Tarde, Noite, Madrugada, etc.)  
- ✅ Listagem de veículos furtados com filtros:
  - Placa  
  - Cor  
  - Tipo do veículo (Carro, Motocicleta, Caminhão, etc.)  
- ✅ Validação de regras de negócio (campos obrigatórios, formatos válidos, datas, etc.)  
- ✅ Carregamento de dados reais a partir de arquivos CSV/XLS disponibilizados pela SSP-SP  
- ✅ Interface web integrada à Web API para consumo das funcionalidades  

---

O sistema permite a **importação de registros reais de boletins de ocorrência** a partir de **arquivos CSV** disponibilizados pela **Secretaria de Segurança Pública do Estado de São Paulo (SSP-SP)**.

Esses arquivos são obtidos por meio do portal da transparência da SSP e contêm dados públicos sobre ocorrências de furto de veículos. O sistema realiza a **leitura, processamento e incorporação automática desses registros ao conjunto de dados mantidos em memória**.

O carregamento dos dados pelo usuário é feito de forma simples por meio da **interface web**, bastando clicar na opção **“Carregar Dados CSV”**, que importa os registros para a aplicação.

🔗 Fonte dos dados:  
http://www.ssp.sp.gov.br/transparenciassp/Consulta.aspx

---

## 🛠️ Tecnologias Utilizadas

- Java 17  
- Spring Boot  
- JAX-RS (Jersey)  
- Maven  
- HTML5  
- CSS3  
- JavaScript (Fetch API)  

---

## ▶️ Como Executar o Projeto

### Pré-requisitos

- Java 17 ou superior  
- Maven  

### Passos para execução

1. Clonar o repositório:
   ```bash
   git clone https://github.com/eduardoalmeidajesus/api.boletim

2. Acessar a pasta do projeto:
   ```bash
   cd api.boletins

3. Executar a aplicação:
   ```bash
   mvn spring-boot:run

4. Acessar no navegador:
   ```bash
   (http://localhost:8080)

