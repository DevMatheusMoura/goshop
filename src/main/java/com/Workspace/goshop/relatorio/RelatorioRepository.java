package com.Workspace.goshop.relatorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RelatorioRepository extends JpaRepository<Object, Long> {

    @Query(value = """
        SELECT 
            u.id as usuarioId,
            u.nome_completo as nomeCompleto,
            u.email as email,
            COUNT(p.id) as totalPedidos,
            COALESCE(SUM(p.valor_total), 0) as valorTotalCompras
        FROM usuarios u
        INNER JOIN pedidos p ON u.id = p.usuario_id
        WHERE p.status = 'FINALIZADO'
        GROUP BY u.id, u.nome_completo, u.email
        ORDER BY valorTotalCompras DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> buscarTop5UsuariosCompradores();

    @Query(value = """
        SELECT 
            u.id as usuarioId,
            u.nome_completo as nomeCompleto,
            u.email as email,
            COUNT(p.id) as totalPedidos,
            COALESCE(SUM(p.valor_total), 0) as valorTotalCompras,
            CASE 
                WHEN COUNT(p.id) > 0 THEN COALESCE(SUM(p.valor_total), 0) / COUNT(p.id)
                ELSE 0 
            END as ticketMedio
        FROM usuarios u
        INNER JOIN pedidos p ON u.id = p.usuario_id
        WHERE p.status = 'FINALIZADO'
        GROUP BY u.id, u.nome_completo, u.email
        HAVING COUNT(p.id) > 0
        ORDER BY ticketMedio DESC
        """, nativeQuery = true)
    List<Object[]> buscarTicketMedioUsuarios();

    @Query(value = """
        SELECT 
            YEAR(p.data_de_criacao) as ano,
            MONTH(p.data_de_criacao) as mes,
            CASE MONTH(p.data_de_criacao)
                WHEN 1 THEN 'Janeiro'
                WHEN 2 THEN 'Fevereiro'
                WHEN 3 THEN 'Março'
                WHEN 4 THEN 'Abril'
                WHEN 5 THEN 'Maio'
                WHEN 6 THEN 'Junho'
                WHEN 7 THEN 'Julho'
                WHEN 8 THEN 'Agosto'
                WHEN 9 THEN 'Setembro'
                WHEN 10 THEN 'Outubro'
                WHEN 11 THEN 'Novembro'
                WHEN 12 THEN 'Dezembro'
            END as mesNome,
            COUNT(p.id) as totalPedidos,
            COALESCE(SUM(p.valor_total), 0) as valorTotalFaturado
        FROM pedidos p
        WHERE p.status = 'FINALIZADO'
            AND p.data_de_criacao >= :dataInicio
            AND p.data_de_criacao <= :dataFim
        GROUP BY YEAR(p.data_de_criacao), MONTH(p.data_de_criacao)
        ORDER BY ano DESC, mes DESC
        """, nativeQuery = true)
    List<Object[]> buscarFaturamentoMensal(@Param("dataInicio") LocalDateTime dataInicio, 
                                          @Param("dataFim") LocalDateTime dataFim);
}
