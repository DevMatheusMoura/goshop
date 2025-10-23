package com.Workspace.goshop.relatorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/top-usuarios-compradores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopUsuariosCompradoresDTO>> buscarTop5UsuariosCompradores() {
        List<TopUsuariosCompradoresDTO> topUsuarios = relatorioService.buscarTop5UsuariosCompradores();
        return new ResponseEntity<>(topUsuarios, HttpStatus.OK);
    }

    @GetMapping("/ticket-medio-usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketMedioUsuarioDTO>> buscarTicketMedioUsuarios() {
        List<TicketMedioUsuarioDTO> ticketsMedios = relatorioService.buscarTicketMedioUsuarios();
        return new ResponseEntity<>(ticketsMedios, HttpStatus.OK);
    }

    @GetMapping("/faturamento-mensal")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FaturamentoMensalDTO>> buscarFaturamentoMensal(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer mes) {
        List<FaturamentoMensalDTO> faturamentos = relatorioService.buscarFaturamentoMensal(ano, mes);
        return new ResponseEntity<>(faturamentos, HttpStatus.OK);
    }
}
