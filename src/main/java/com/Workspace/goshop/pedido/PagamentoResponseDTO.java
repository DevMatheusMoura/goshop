package com.Workspace.goshop.pedido;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoResponseDTO {
    private UUID pedidoId;
    private StatusPedido status;
    private String mensagem;
    private boolean sucesso;
}
