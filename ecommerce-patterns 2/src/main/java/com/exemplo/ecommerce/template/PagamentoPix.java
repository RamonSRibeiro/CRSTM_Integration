package com.exemplo.ecommerce.template;

import org.springframework.stereotype.Component;

@Component("PIX")
public class PagamentoPix extends PagamentoTemplate {
    @Override
    protected double calcularTaxa(double valor) {
        return 0.0; // sem taxa
    }

    @Override
    protected String efetuarPagamento(double total) {
        return "Pago via PIX. Total: R$ " + total;
    }
}
