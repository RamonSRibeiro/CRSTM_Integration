package com.exemplo.ecommerce.template;

import org.springframework.stereotype.Component;

@Component("BOLETO")
public class PagamentoBoleto extends PagamentoTemplate {
    @Override
    protected double calcularTaxa(double valor) {
        return 3.50; // taxa fixa de emissão
    }

    @Override
    protected String efetuarPagamento(double total) {
        return "BOLETO gerado. Total: R$ " + total;
    }
}
