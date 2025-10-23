[_DATABASE_GOSHOP_.sql](https://github.com/user-attachments/files/23105741/_DATABASE_GOSHOP_.sql)# GoShop - Sistema de E-commerce

Sistema de gerenciamento de produtos e pedidos com autenticação JWT e controle de acesso baseado em perfis.

## 📋 Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **MySQL 8.0+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Como Executar o Projeto

### 1. Clone o Repositório
```bash
git clone <url-do-repositorio>
cd goshop
```

### 2. Configure o Banco de Dados

#### 2.1. Instale e configure o MySQL
- Instale o MySQL 8.0+
- Importe o banco de dados através
- Configure um usuário com acesso ao banco

#### 2.2. Importe o MySQL
- Utilize o dump do banco de dados
  -  [Uploa-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Versão do servidor: 9.1.0
-- Versão do PHP: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `goshop`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `itens_pedido`
--

DROP TABLE IF EXISTS `itens_pedido`;
CREATE TABLE IF NOT EXISTS `itens_pedido` (
  `id` binary(16) NOT NULL,
  `preco_unitario` decimal(38,2) DEFAULT NULL,
  `quantidade` int DEFAULT NULL,
  `pedido_id` binary(16) NOT NULL,
  `produto_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK42mycompce3b7yt3l6ukdwsxy` (`pedido_id`),
  KEY `FKxytdlekpdaobqphujy9bmuhl` (`produto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
CREATE TABLE IF NOT EXISTS `pedidos` (
  `id` binary(16) NOT NULL,
  `data_de_atualizacao` datetime(6) DEFAULT NULL,
  `data_de_criacao` datetime(6) DEFAULT NULL,
  `status` enum('CANCELADO','PAGO','PENDENTE') DEFAULT NULL,
  `valor_total` decimal(38,2) DEFAULT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5g0es69v35nmkmpi8uewbphs2` (`usuario_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `produtos`
--

DROP TABLE IF EXISTS `produtos`;
CREATE TABLE IF NOT EXISTS `produtos` (
  `id` binary(16) NOT NULL,
  `categoria` varchar(255) NOT NULL,
  `data_de_atualizacao` datetime(6) DEFAULT NULL,
  `data_de_criacao` datetime(6) DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `nome` varchar(255) NOT NULL,
  `preco` decimal(38,2) NOT NULL,
  `quantidade_em_estoque` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cpf` varchar(255) DEFAULT NULL,
  `data_nascimento` date NOT NULL,
  `email` varchar(255) NOT NULL,
  `nome_completo` varchar(255) NOT NULL,
  `perfil` enum('ADMIN','USER') DEFAULT NULL,
  `senha` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `itens_pedido`
--
ALTER TABLE `itens_pedido`
  ADD CONSTRAINT `FK42mycompce3b7yt3l6ukdwsxy` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`),
  ADD CONSTRAINT `FKxytdlekpdaobqphujy9bmuhl` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`id`);

--
-- Restrições para tabelas `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `FK5g0es69v35nmkmpi8uewbphs2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
ding _DATABASE_GOSHOP_.sql…]()

#### 2.3. Configure as credenciais no `application.yml`
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/goshop?createDatabaseIfNotExist=true
    username: seu_usuario_mysql
    password: sua_senha_mysql
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3. Execute o Projeto

#### Opção 1: Via IDE
1. Abra o projeto na sua IDE
2. Execute a classe `GoshopApplication.java`
3. O servidor iniciará na porta `8080`

#### Opção 2: Via Maven
```bash
# Compilar o projeto
mvn clean compile

# Executar o projeto
mvn spring-boot:run
```

#### Opção 3: Via JAR
```bash
# Gerar o JAR
mvn clean package

# Executar o JAR
java -jar target/goshop-0.0.1-SNAPSHOT.jar
```

### 4. Verificar se está funcionando
Acesse: `http://localhost:8080/goshop`

## 🔧 Configurações Adicionais

### Configuração do JWT
As configurações do JWT estão no arquivo `application.yml`:
```yaml
jwt:
  secret: seu-segredo-jwt-aqui-deve-ser-longo-e-seguro
  expiration: 86400000 # 24 horas em milissegundos
```

### Otimização de Performance (Opcional)
Para melhorar a performance das consultas, execute o script SQL:
```bash
mysql -u seu_usuario -p goshop < src/main/resources/sql/indices_otimizacao.sql
```

## 📚 Como Usar a API

### 1. Criar um Usuário
```bash
curl -X POST http://localhost:8080/goshop/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "João Silva",
    "dataNascimento": "1990-01-01",
    "cpf": "12345678901",
    "email": "joao@email.com",
    "senha": "123456"
  }'
```

### 2. Fazer Login
```bash
curl -X POST http://localhost:8080/goshop/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "123456"
  }'
```

### 3. Usar o Token nas Requisições
```bash
curl -X GET http://localhost:8080/goshop/produtos \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

## 🛠️ Endpoints Disponíveis

### Autenticação
- `POST /goshop/auth/login` - Login e obtenção do token JWT

### Usuários
- `POST /goshop/usuarios` - Criar usuário (padrão: USER)
- `POST /goshop/usuarios/admin` - Criar usuário ADMIN (requer autenticação ADMIN)
- `GET /goshop/usuarios` - Listar usuários (requer autenticação ADMIN)

### Produtos
- `POST /goshop/produtos` - Criar produto (requer autenticação ADMIN)
- `GET /goshop/produtos` - Listar produtos (requer autenticação ADMIN ou USER)
- `GET /goshop/produtos/{id}` - Buscar produto por ID (requer autenticação ADMIN ou USER)
- `PUT /goshop/produtos/{id}` - Atualizar produto (requer autenticação ADMIN)
- `DELETE /goshop/produtos/{id}` - Deletar produto (requer autenticação ADMIN)

### Pedidos
- `POST /goshop/pedidos` - Criar pedido (requer autenticação USER ou ADMIN)
- `POST /goshop/pedidos/{id}/pagamento` - Processar pagamento do pedido (requer autenticação)
- `GET /goshop/pedidos` - Listar pedidos do usuário logado (requer autenticação)
- `GET /goshop/pedidos/{id}` - Buscar pedido por ID (requer autenticação, apenas seus próprios pedidos)

### Relatórios Administrativos (Apenas ADMIN)
- `GET /goshop/relatorios/top-usuarios-compradores` - Top 5 usuários que mais compraram
- `GET /goshop/relatorios/ticket-medio-usuarios` - Ticket médio dos pedidos de cada usuário
- `GET /goshop/relatorios/faturamento-mensal` - Valor total faturado no mês

### Consultas Otimizadas MySQL (Apenas ADMIN)
- `GET /goshop/consultas-otimizadas/top-usuarios-compradores` - Top 5 usuários que mais compraram
- `GET /goshop/consultas-otimizadas/ticket-medio-usuarios` - Ticket médio dos pedidos de cada usuário
- `GET /goshop/consultas-otimizadas/faturamento-mensal` - Valor total faturado no mês

## 🔐 Perfis de Usuário

### ADMIN
- Pode criar, atualizar e deletar produtos
- Pode criar usuários com perfil ADMIN
- Pode acessar relatórios administrativos
- Pode acessar consultas otimizadas

### USER
- Pode criar pedidos
- Pode visualizar produtos
- Pode processar pagamentos
- Pode listar seus próprios pedidos

## 🏗️ Estrutura do Projeto

```
src/main/java/com/Workspace/goshop/
├── GoshopApplication.java          # Classe principal
├── pedido/                         # Entidades e serviços de pedidos
│   ├── Pedido.java
│   ├── ItemPedido.java
│   ├── StatusPedido.java
│   ├── PedidoController.java
│   ├── PedidoService.java
│   ├── PedidoRepository.java
│   ├── PagamentoRequestDTO.java
│   └── PagamentoResponseDTO.java
├── produto/                        # Entidades e serviços de produtos
│   ├── Produto.java
│   ├── ProdutoController.java
│   ├── ProdutoService.java
│   └── ProdutoRepository.java
├── usuario/                        # Entidades e serviços de usuários
│   ├── Usuario.java
│   ├── Perfil.java
│   ├── UsuarioController.java
│   ├── UsuarioService.java
│   ├── UsuarioRepository.java
│   └── UserDetailsServiceImple.java
├── security/                       # Configurações de segurança e JWT
│   ├── SecurityConfig.java
│   ├── JwtUtil.java
│   ├── JwtRequestFilter.java
│   ├── AuthenticationController.java
│   ├── AuthenticationRequest.java
│   └── AuthenticationResponse.java
└── consultas/                      # Consultas otimizadas MySQL
    ├── ConsultasOtimizadasController.java
    ├── ConsultasOtimizadasService.java
    ├── ConsultasOtimizadasRepository.java
    ├── TopUsuariosCompradoresDTO.java
    ├── TicketMedioUsuarioDTO.java
    ├── FaturamentoMensalDTO.java
    └── CONSULTAS_SQL_OTIMIZADAS.md  # Documentação das consultas SQL
```

## 🧪 Testando a API

### Exemplo Completo de Uso

1. **Criar um usuário:**
```bash
curl -X POST http://localhost:8080/goshop/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "João Silva",
    "dataNascimento": "1990-01-01",
    "cpf": "12345678901",
    "email": "joao@email.com",
    "senha": "123456"
  }'
```

2. **Fazer login:**
```bash
curl -X POST http://localhost:8080/goshop/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "123456"
  }'
```

3. **Criar um produto (como ADMIN):**
```bash
curl -X POST http://localhost:8080/goshop/produtos \
  -H "Authorization: Bearer SEU_TOKEN_ADMIN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Produto Teste",
    "descricao": "Descrição do produto",
    "preco": 100.00,
    "categoria": "Categoria Teste",
    "quantidadeEmEstoque": 10
  }'
```

4. **Criar um pedido:**
```bash
curl -X POST http://localhost:8080/goshop/pedidos \
  -H "Authorization: Bearer SEU_TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{
    "itens": [
      {
        "produto": {"id": "produto-uuid-1"},
        "quantidade": 2
      }
    ]
  }'
```

5. **Processar pagamento:**
```bash
curl -X POST http://localhost:8080/goshop/pedidos/pedido-uuid/pagamento \
  -H "Authorization: Bearer SEU_TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{
    "metodoPagamento": "CARTAO",
    "numeroCartao": "1234567890123456",
    "cvv": "123",
    "dataVencimento": "12/25"
  }'
```

## 🐛 Solução de Problemas

### Erro de Conexão com MySQL
- Verifique se o MySQL está rodando
- Confirme as credenciais no `application.yml`
- Certifique-se de que o banco `goshop` existe

### Erro de Porta em Uso
- Altere a porta no `application.yml`:
```yaml
server:
  port: 8081
```

### Erro de Token JWT
- Verifique se o token está sendo enviado no header `Authorization: Bearer TOKEN`
- Confirme se o token não expirou (24 horas)

## 📝 Tecnologias Utilizadas

- **Spring Boot 3.5.6**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **JWT (jjwt)**
- **Lombok**
- **Maven**

## 📄 Licença

Este projeto está sob a licença MIT.

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📞 Suporte

Para dúvidas ou problemas, abra uma issue no repositório.
