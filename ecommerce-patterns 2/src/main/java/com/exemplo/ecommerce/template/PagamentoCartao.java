package com.exemplo.ecommerce.template;

import org.springframework.stereotype.Component;

@Component("CARTAO")
public class PagamentoCartao extends PagamentoTemplate {
    @Override
    protected double calcularTaxa(double valor) {
        return valor * 0.03; // 3% de taxa
    }

    @Override
    protected String efetuarPagamento(double total) {
        return "Pago no CARTÃO. Total: R$ " + total;
    }
}
