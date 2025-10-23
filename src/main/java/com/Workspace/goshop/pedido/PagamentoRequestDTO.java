package com.Workspace.goshop.pedido;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoRequestDTO {
    private UUID pedidoId;
    private String metodoPagamento; // "CARTAO", "PIX", "BOLETO"
    private String numeroCartao; // Para pagamento com cartão
    private String cvv; // Para pagamento com cartão
    private String dataVencimento; // Para pagamento com cartão
}
