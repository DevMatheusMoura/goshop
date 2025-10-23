package com.Workspace.goshop.pedido;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoResponseDTO {
    private UUID pedidoId;
    private StatusPedido status;
    private String mensagem;
    private boolean sucesso;
}
