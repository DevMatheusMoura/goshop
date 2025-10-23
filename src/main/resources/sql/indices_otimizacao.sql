-- Script para criar índices otimizados para consultas de relatórios
-- Execute este script no MySQL para melhorar a performance das consultas

-- Índice para otimizar consultas por status de pedido
CREATE INDEX idx_pedidos_status ON pedidos(status);

-- Índice para otimizar consultas por data de criação
CREATE INDEX idx_pedidos_data_criacao ON pedidos(data_de_criacao);

-- Índice composto para otimizar consultas por status e data
CREATE INDEX idx_pedidos_status_data ON pedidos(status, data_de_criacao);

-- Índice para otimizar JOINs por usuário
CREATE INDEX idx_pedidos_usuario_id ON pedidos(usuario_id);

-- Índice composto para otimizar consultas de relatórios
CREATE INDEX idx_pedidos_usuario_status ON pedidos(usuario_id, status);

-- Índice para otimizar consultas por valor total
CREATE INDEX idx_pedidos_valor_total ON pedidos(valor_total);

-- Índice para otimizar consultas por email de usuário
CREATE INDEX idx_usuarios_email ON usuarios(email);

-- Índice para otimizar consultas por nome completo
CREATE INDEX idx_usuarios_nome ON usuarios(nome_completo);
