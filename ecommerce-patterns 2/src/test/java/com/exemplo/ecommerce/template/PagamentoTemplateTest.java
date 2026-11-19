package com.exemplo.ecommerce.template;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoTemplateTest {

    private static final double DELTA = 0.001;

    @Test
    void pixNaoTemTaxa() {
        ResultadoPagamento r = new PagamentoPix().processar(100);
        assertEquals(0.0, r.taxa(), DELTA);
        assertEquals(100.0, r.totalCobrado(), DELTA);
        assertTrue(r.mensagem().contains("PIX"));
    }

    @Test
    void cartaoCobra3PorCentoDeTaxa() {
        ResultadoPagamento r = new PagamentoCartao().processar(100);
        assertEquals(3.0, r.taxa(), DELTA);
        assertEquals(103.0, r.totalCobrado(), DELTA);
        assertTrue(r.mensagem().contains("CARTÃO"));
    }

    @Test
    void boletoCobraTaxaFixaDe3_50() {
        ResultadoPagamento r = new PagamentoBoleto().processar(100);
        assertEquals(3.5, r.taxa(), DELTA);
        assertEquals(103.5, r.totalCobrado(), DELTA);
        assertTrue(r.mensagem().contains("BOLETO"));
    }

    @Test
    void esqueletoExecutaPassosNaOrdemFixa() {
        List<String> passos = new ArrayList<>();

        PagamentoTemplate espiao = new PagamentoTemplate() {
            @Override
            protected double calcularTaxa(double valor) {
                passos.add("taxa");
                return 1.0;
            }

            @Override
            protected String efetuarPagamento(double total) {
                passos.add("pagar:" + total);
                return "ok";
            }
        };

        ResultadoPagamento r = espiao.processar(10);

        assertEquals(List.of("taxa", "pagar:11.0"), passos);
        assertEquals(11.0, r.totalCobrado(), DELTA);
    }

    @Test
    void metodoTemplateNaoPodeSerSobrescrito() throws Exception {
        var metodo = PagamentoTemplate.class.getMethod("processar", double.class);
        assertTrue(java.lang.reflect.Modifier.isFinal(metodo.getModifiers()));
    }
}
