package com.exemplo.ecommerce.service;

import com.exemplo.ecommerce.chain.*;
import com.exemplo.ecommerce.model.PedidoRequest;
import com.exemplo.ecommerce.model.PedidoResponse;
import com.exemplo.ecommerce.strategy.FreteStrategy;
import com.exemplo.ecommerce.template.PagamentoTemplate;
import com.exemplo.ecommerce.template.ResultadoPagamento;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PedidoService {

    private final ValidacaoHandler cadeiaDeValidacao;
    private final Map<String, FreteStrategy> estrategiasFrete;      // Spring injeta pelo nome do bean
    private final Map<String, PagamentoTemplate> processadoresPagamento;

    public PedidoService(Map<String, FreteStrategy> estrategiasFrete,
                         Map<String, PagamentoTemplate> processadoresPagamento) {
        this.estrategiasFrete = estrategiasFrete;
        this.processadoresPagamento = processadoresPagamento;

        // Monta a CHAIN: Cliente -> Endereço -> Estoque -> Valor
        ValidacaoHandler cliente = new ClienteHandler();
        cliente.setProximo(new EnderecoHandler())
               .setProximo(new EstoqueHandler())
               .setProximo(new ValorHandler());
        this.cadeiaDeValidacao = cliente;
    }

    public PedidoResponse processar(PedidoRequest pedido) {
        // 1) CHAIN OF RESPONSIBILITY: valida o pedido
        cadeiaDeValidacao.validar(pedido);

        // 2) STRATEGY: escolhe o algoritmo de frete
        FreteStrategy freteStrategy = buscar(estrategiasFrete, pedido.frete(), "Frete");
        double frete = freteStrategy.calcular(pedido.valor());

        // 3) TEMPLATE METHOD: executa o fluxo de pagamento
        PagamentoTemplate pagamento = buscar(processadoresPagamento, pedido.pagamento(), "Pagamento");
        ResultadoPagamento resultado = pagamento.processar(pedido.valor() + frete);

        return new PedidoResponse(
                pedido.valor(), frete, resultado.taxa(), resultado.totalCobrado(), resultado.mensagem());
    }

    private <T> T buscar(Map<String, T> mapa, String chave, String tipo) {
        T item = chave == null ? null : mapa.get(chave.toUpperCase());
        if (item == null) {
            throw new IllegalArgumentException(tipo + " inválido: " + chave);
        }
        return item;
    }
}
