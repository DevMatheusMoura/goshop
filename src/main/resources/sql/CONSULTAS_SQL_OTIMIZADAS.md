# Consultas SQL Otimizadas - GoShop

Este arquivo contém as consultas SQL otimizadas para relatórios administrativos do sistema GoShop.

## Índices Recomendados

Execute os seguintes índices no MySQL para otimizar as consultas:

```sql
-- Índices otimizados para consultas de relatórios
CREATE INDEX idx_pedidos_status ON pedidos(status);
CREATE INDEX idx_pedidos_data_criacao ON pedidos(data_de_criacao);
CREATE INDEX idx_pedidos_status_data ON pedidos(status, data_de_criacao);
CREATE INDEX idx_pedidos_usuario_id ON pedidos(usuario_id);
CREATE INDEX idx_pedidos_usuario_status ON pedidos(usuario_id, status);
CREATE INDEX idx_pedidos_valor_total ON pedidos(valor_total);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_nome ON usuarios(nome_completo);
```

## Consultas SQL

### 1. Top 5 Usuários que Mais Compraram

```sql
SELECT 
    u.id as usuarioId,
    u.nome_completo as nomeCompleto,
    u.email as email,
    COUNT(p.id) as totalPedidos,
    COALESCE(SUM(p.valor_total), 0) as valorTotalCompras
FROM usuarios u
INNER JOIN pedidos p ON u.id = p.usuario_id
WHERE p.status = 'FINALIZADO'
GROUP BY u.id, u.nome_completo, u.email
ORDER BY valorTotalCompras DESC
LIMIT 5;
```

**Explicação:**
- Usa INNER JOIN para garantir que apenas usuários com pedidos sejam retornados
- Filtra apenas pedidos com status 'FINALIZADO'
- Agrupa por usuário e calcula totais
- Ordena por valor total decrescente
- Limita a 5 resultados

### 2. Ticket Médio dos Pedidos por Usuário

```sql
SELECT 
    u.id as usuarioId,
    u.nome_completo as nomeCompleto,
    u.email as email,
    COUNT(p.id) as totalPedidos,
    COALESCE(SUM(p.valor_total), 0) as valorTotalCompras,
    CASE 
        WHEN COUNT(p.id) > 0 THEN COALESCE(SUM(p.valor_total), 0) / COUNT(p.id)
        ELSE 0 
    END as ticketMedio
FROM usuarios u
INNER JOIN pedidos p ON u.id = p.usuario_id
WHERE p.status = 'FINALIZADO'
GROUP BY u.id, u.nome_completo, u.email
HAVING COUNT(p.id) > 0
ORDER BY ticketMedio DESC;
```

**Explicação:**
- Calcula o ticket médio dividindo o valor total pelo número de pedidos
- Usa HAVING para filtrar apenas usuários com pedidos
- Ordena por ticket médio decrescente
- Evita divisão por zero com CASE

### 3. Faturamento Mensal

```sql
SELECT 
    YEAR(p.data_de_criacao) as ano,
    MONTH(p.data_de_criacao) as mes,
    CASE MONTH(p.data_de_criacao)
        WHEN 1 THEN 'Janeiro'
        WHEN 2 THEN 'Fevereiro'
        WHEN 3 THEN 'Março'
        WHEN 4 THEN 'Abril'
        WHEN 5 THEN 'Maio'
        WHEN 6 THEN 'Junho'
        WHEN 7 THEN 'Julho'
        WHEN 8 THEN 'Agosto'
        WHEN 9 THEN 'Setembro'
        WHEN 10 THEN 'Outubro'
        WHEN 11 THEN 'Novembro'
        WHEN 12 THEN 'Dezembro'
    END as mesNome,
    COUNT(p.id) as totalPedidos,
    COALESCE(SUM(p.valor_total), 0) as valorTotalFaturado
FROM pedidos p
WHERE p.status = 'FINALIZADO'
    AND p.data_de_criacao >= :dataInicio
    AND p.data_de_criacao <= :dataFim
GROUP BY YEAR(p.data_de_criacao), MONTH(p.data_de_criacao)
ORDER BY ano DESC, mes DESC;
```

**Explicação:**
- Extrai ano e mês da data de criação
- Converte número do mês para nome em português
- Filtra por período usando parâmetros
- Agrupa por ano e mês
- Ordena por ano e mês decrescente

## Parâmetros das Consultas

### Faturamento Mensal
- `dataInicio`: Data/hora de início do período (LocalDateTime)
- `dataFim`: Data/hora de fim do período (LocalDateTime)

## Otimizações Implementadas

1. **INNER JOINs**: Mais eficientes que LEFT JOINs quando não precisamos de registros nulos
2. **Índices compostos**: Para consultas que filtram por múltiplas colunas
3. **Agregações no banco**: COUNT, SUM, AVG executados no MySQL
4. **LIMIT**: Para consultas que retornam apenas os top resultados
5. **HAVING**: Para filtros após agrupamento
6. **COALESCE**: Para tratar valores nulos

## Performance

Com os índices recomendados, as consultas devem executar em:
- **Top 5 usuários**: < 100ms para até 100k pedidos
- **Ticket médio**: < 200ms para até 100k pedidos  
- **Faturamento mensal**: < 150ms para até 100k pedidos

## Uso das Consultas

Estas consultas podem ser executadas diretamente no MySQL ou integradas em aplicações que precisem de relatórios de performance otimizada.

Para integração com Spring Boot, use `@Query` com `nativeQuery = true` no repository.
