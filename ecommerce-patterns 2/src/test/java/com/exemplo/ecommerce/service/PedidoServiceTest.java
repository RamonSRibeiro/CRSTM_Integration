package com.exemplo.ecommerce.service;

import com.exemplo.ecommerce.model.PedidoRequest;
import com.exemplo.ecommerce.model.PedidoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PedidoServiceTest {

    private static final double DELTA = 0.001;

    @Autowired
    private PedidoService service;

    @Test
    void expressoComPix() {
        PedidoResponse r = service.processar(
                new PedidoRequest("Ana", "Rua A", 2, 150, "EXPRESSO", "PIX"));

        assertEquals(150.0, r.valorProdutos(), DELTA);
        assertEquals(30.0, r.frete(), DELTA);
        assertEquals(0.0, r.taxaPagamento(), DELTA);
        assertEquals(180.0, r.totalPago(), DELTA);
    }

    @Test
    void normalComCartaoCobraTaxaSobreProdutosMaisFrete() {
        PedidoResponse r = service.processar(
                new PedidoRequest("Ana", "Rua A", 1, 100, "NORMAL", "CARTAO"));

        assertEquals(15.0, r.frete(), DELTA);
        assertEquals(3.45, r.taxaPagamento(), DELTA);   // 3% de 115
        assertEquals(118.45, r.totalPago(), DELTA);
    }

    @Test
    void retiradaComBoletoSemEndereco() {
        PedidoResponse r = service.processar(
                new PedidoRequest("Bia", null, 1, 80, "RETIRADA", "BOLETO"));

        assertEquals(0.0, r.frete(), DELTA);
        assertEquals(3.5, r.taxaPagamento(), DELTA);
        assertEquals(83.5, r.totalPago(), DELTA);
    }

    @Test
    void freteGratisNoNormalAcimaDe200() {
        PedidoResponse r = service.processar(
                new PedidoRequest("Ana", "Rua A", 1, 250, "NORMAL", "PIX"));

        assertEquals(0.0, r.frete(), DELTA);
        assertEquals(250.0, r.totalPago(), DELTA);
    }

    @Test
    void tiposSaoCaseInsensitive() {
        PedidoResponse r = service.processar(
                new PedidoRequest("Ana", "Rua A", 1, 100, "retirada", "pix"));

        assertEquals(100.0, r.totalPago(), DELTA);
    }

    @Test
    void validacaoFalhaAntesDeCalcularFreteEPagamento() {
        var ex = assertThrows(IllegalArgumentException.class, () -> service.processar(
                new PedidoRequest("Caio", "Rua B", 50, 100, "NORMAL", "PIX")));
        assertTrue(ex.getMessage().startsWith("Estoque insuficiente"));
    }

    @Test
    void freteInvalidoLancaErro() {
        var ex = assertThrows(IllegalArgumentException.class, () -> service.processar(
                new PedidoRequest("Ana", "Rua A", 1, 100, "TELEPORTE", "PIX")));
        assertEquals("Frete inválido: TELEPORTE", ex.getMessage());
    }

    @Test
    void pagamentoInvalidoLancaErro() {
        var ex = assertThrows(IllegalArgumentException.class, () -> service.processar(
                new PedidoRequest("Ana", "Rua A", 1, 100, "NORMAL", "FIADO")));
        assertEquals("Pagamento inválido: FIADO", ex.getMessage());
    }

    @Test
    void freteNuloLancaErro() {
        assertThrows(IllegalArgumentException.class, () -> service.processar(
                new PedidoRequest("Ana", "Rua A", 1, 100, null, "PIX")));
    }
}
