package com.Workspace.goshop.pedido;

import com.Workspace.goshop.produto.Produto;
import com.Workspace.goshop.produto.ProdutoService;
import com.Workspace.goshop.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Transactional
    public Pedido salvarPedido(Pedido pedido) {
        // Definir status inicial como PENDENTE
        pedido.setStatus(StatusPedido.PENDENTE);
        
        // Calcular valor total dinamicamente
        calcularValorTotal(pedido);
        
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public PagamentoResponseDTO processarPagamento(PagamentoRequestDTO pagamentoRequest) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(pagamentoRequest.getPedidoId());
        
        if (pedidoOpt.isEmpty()) {
            return new PagamentoResponseDTO(
                pagamentoRequest.getPedidoId(),
                null,
                "Pedido não encontrado",
                false
            );
        }

        Pedido pedido = pedidoOpt.get();
        
        // Verificar se o pedido está pendente
        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            return new PagamentoResponseDTO(
                pedido.getId(),
                pedido.getStatus(),
                "Pedido já foi processado",
                false
            );
        }

        // Verificar estoque disponível
        if (!verificarEstoqueDisponivel(pedido)) {
            pedido.setStatus(StatusPedido.CANCELADO);
            pedidoRepository.save(pedido);
            
            return new PagamentoResponseDTO(
                pedido.getId(),
                StatusPedido.CANCELADO,
                "Pedido cancelado: estoque insuficiente para alguns produtos",
                false
            );
        }

        // Processar pagamento (simulação)
        boolean pagamentoProcessado = processarPagamentoSimulado(pagamentoRequest);
        
        if (pagamentoProcessado) {
            // Atualizar estoque dos produtos
            atualizarEstoqueProdutos(pedido);
            
            // Atualizar status do pedido
            pedido.setStatus(StatusPedido.PAGO);
            pedidoRepository.save(pedido);
            
            return new PagamentoResponseDTO(
                pedido.getId(),
                StatusPedido.PAGO,
                "Pagamento processado com sucesso",
                true
            );
        } else {
            pedido.setStatus(StatusPedido.CANCELADO);
            pedidoRepository.save(pedido);
            
            return new PagamentoResponseDTO(
                pedido.getId(),
                StatusPedido.CANCELADO,
                "Pagamento recusado",
                false
            );
        }
    }

    private void calcularValorTotal(Pedido pedido) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        
        if (pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                // Buscar preço atual do produto
                Optional<Produto> produtoOpt = produtoService.buscarProdutoPorId(item.getProduto().getId());
                if (produtoOpt.isPresent()) {
                    Produto produto = produtoOpt.get();
                    item.setPrecoUnitario(produto.getPreco());
                    BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
                    valorTotal = valorTotal.add(subtotal);
                }
            }
        }
        
        pedido.setValorTotal(valorTotal);
    }

    private boolean verificarEstoqueDisponivel(Pedido pedido) {
        if (pedido.getItens() == null) {
            return false;
        }

        for (ItemPedido item : pedido.getItens()) {
            Optional<Produto> produtoOpt = produtoService.buscarProdutoPorId(item.getProduto().getId());
            if (produtoOpt.isEmpty()) {
                return false;
            }

            Produto produto = produtoOpt.get();
            if (produto.getQuantidadeEmEstoque() < item.getQuantidade()) {
                return false;
            }
        }

        return true;
    }

    private void atualizarEstoqueProdutos(Pedido pedido) {
        if (pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                Optional<Produto> produtoOpt = produtoService.buscarProdutoPorId(item.getProduto().getId());
                if (produtoOpt.isPresent()) {
                    Produto produto = produtoOpt.get();
                    int novaQuantidade = produto.getQuantidadeEmEstoque() - item.getQuantidade();
                    produto.setQuantidadeEmEstoque(novaQuantidade);
                    produtoService.salvarProduto(produto);
                }
            }
        }
    }

    private boolean processarPagamentoSimulado(PagamentoRequestDTO pagamentoRequest) {
        // Simulação de processamento de pagamento
        // Em um sistema real, aqui seria feita a integração com gateway de pagamento
        return true; // Simular pagamento aprovado
    }

    public List<Pedido> listarPedidosPorUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuario(usuario);
    }

    public Optional<Pedido> buscarPedidoPorIdEUsuario(UUID id, Usuario usuario) {
        return pedidoRepository.findById(id)
                .filter(pedido -> pedido.getUsuario().equals(usuario));
    }

    public Optional<Pedido> buscarPedidoPorId(UUID id) {
        return pedidoRepository.findById(id);
    }
}
