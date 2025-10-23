package com.Workspace.goshop.relatorio;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaturamentoMensalDTO {
    private Integer ano;
    private Integer mes;
    private String mesNome;
    private Long totalPedidos;
    private BigDecimal valorTotalFaturado;
}
