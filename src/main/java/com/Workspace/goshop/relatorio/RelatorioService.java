package com.Workspace.goshop.relatorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    public List<TopUsuariosCompradoresDTO> buscarTop5UsuariosCompradores() {
        List<Object[]> resultados = relatorioRepository.buscarTop5UsuariosCompradores();
        List<TopUsuariosCompradoresDTO> topUsuarios = new ArrayList<>();

        for (Object[] resultado : resultados) {
            TopUsuariosCompradoresDTO dto = new TopUsuariosCompradoresDTO();
            dto.setUsuarioId(((Number) resultado[0]).longValue());
            dto.setNomeCompleto((String) resultado[1]);
            dto.setEmail((String) resultado[2]);
            dto.setTotalPedidos(((Number) resultado[3]).longValue());
            dto.setValorTotalCompras((BigDecimal) resultado[4]);
            topUsuarios.add(dto);
        }

        return topUsuarios;
    }

    public List<TicketMedioUsuarioDTO> buscarTicketMedioUsuarios() {
        List<Object[]> resultados = relatorioRepository.buscarTicketMedioUsuarios();
        List<TicketMedioUsuarioDTO> ticketsMedios = new ArrayList<>();

        for (Object[] resultado : resultados) {
            TicketMedioUsuarioDTO dto = new TicketMedioUsuarioDTO();
            dto.setUsuarioId(((Number) resultado[0]).longValue());
            dto.setNomeCompleto((String) resultado[1]);
            dto.setEmail((String) resultado[2]);
            dto.setTotalPedidos(((Number) resultado[3]).longValue());
            dto.setValorTotalCompras((BigDecimal) resultado[4]);
            dto.setTicketMedio((BigDecimal) resultado[5]);
            ticketsMedios.add(dto);
        }

        return ticketsMedios;
    }

    public List<FaturamentoMensalDTO> buscarFaturamentoMensal(Integer ano, Integer mes) {
        LocalDateTime dataInicio;
        LocalDateTime dataFim;

        if (ano != null && mes != null) {
            // Buscar faturamento de um mês específico
            YearMonth yearMonth = YearMonth.of(ano, mes);
            dataInicio = yearMonth.atDay(1).atStartOfDay();
            dataFim = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        } else if (ano != null) {
            // Buscar faturamento de um ano específico
            dataInicio = LocalDateTime.of(ano, 1, 1, 0, 0, 0);
            dataFim = LocalDateTime.of(ano, 12, 31, 23, 59, 59);
        } else {
            // Buscar faturamento do mês atual
            YearMonth mesAtual = YearMonth.now();
            dataInicio = mesAtual.atDay(1).atStartOfDay();
            dataFim = mesAtual.atEndOfMonth().atTime(23, 59, 59);
        }

        List<Object[]> resultados = relatorioRepository.buscarFaturamentoMensal(dataInicio, dataFim);
        List<FaturamentoMensalDTO> faturamentos = new ArrayList<>();

        for (Object[] resultado : resultados) {
            FaturamentoMensalDTO dto = new FaturamentoMensalDTO();
            dto.setAno(((Number) resultado[0]).intValue());
            dto.setMes(((Number) resultado[1]).intValue());
            dto.setMesNome((String) resultado[2]);
            dto.setTotalPedidos(((Number) resultado[3]).longValue());
            dto.setValorTotalFaturado((BigDecimal) resultado[4]);
            faturamentos.add(dto);
        }

        return faturamentos;
    }
}
