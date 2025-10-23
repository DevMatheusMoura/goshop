package com.Workspace.goshop.pedido;

import com.Workspace.goshop.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        pedido.setUsuario(usuario);
        Pedido novoPedido = pedidoService.salvarPedido(pedido);
        return new ResponseEntity<>(novoPedido, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/pagamento")
    public ResponseEntity<PagamentoResponseDTO> processarPagamento(
            @PathVariable UUID id,
            @RequestBody PagamentoRequestDTO pagamentoRequest) {
        
        // Verificar se o pedido pertence ao usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        if (!pedidoService.buscarPedidoPorIdEUsuario(id, usuario).isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        pagamentoRequest.setPedidoId(id);
        PagamentoResponseDTO response = pedidoService.processarPagamento(pagamentoRequest);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        List<Pedido> pedidos = pedidoService.listarPedidosPorUsuario(usuario);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        return pedidoService.buscarPedidoPorIdEUsuario(id, usuario)
                .map(pedido -> new ResponseEntity<>(pedido, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
