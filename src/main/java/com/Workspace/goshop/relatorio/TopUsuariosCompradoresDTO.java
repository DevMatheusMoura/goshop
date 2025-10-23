package com.Workspace.goshop.relatorio;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopUsuariosCompradoresDTO {
    private Long usuarioId;
    private String nomeCompleto;
    private String email;
    private Long totalPedidos;
    private BigDecimal valorTotalCompras;
}
