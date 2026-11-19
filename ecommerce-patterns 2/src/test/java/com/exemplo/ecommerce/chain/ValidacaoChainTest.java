package com.exemplo.ecommerce.chain;

import com.exemplo.ecommerce.model.PedidoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidacaoChainTest {

    private ValidacaoHandler cadeia;

    @BeforeEach
    void montarCadeia() {
        ValidacaoHandler cliente = new ClienteHandler();
        cliente.setProximo(new EnderecoHandler())
               .setProximo(new EstoqueHandler())
               .setProximo(new ValorHandler());
        cadeia = cliente;
    }

    private PedidoRequest pedido(String cliente, String endereco, int qtd, double valor, String frete) {
        return new PedidoRequest(cliente, endereco, qtd, valor, frete, "PIX");
    }

    private String erro(PedidoRequest p) {
        return assertThrows(IllegalArgumentException.class, () -> cadeia.validar(p)).getMessage();
    }

    @Test
    void pedidoValidoPassaPorTodaACadeia() {
        assertDoesNotThrow(() -> cadeia.validar(pedido("Ana", "Rua A", 2, 100, "NORMAL")));
    }

    @Test
    void clienteVazioEhRejeitado() {
        assertEquals("Cliente é obrigatório.", erro(pedido("  ", "Rua A", 1, 100, "NORMAL")));
    }

    @Test
    void clienteNuloEhRejeitado() {
        assertEquals("Cliente é obrigatório.", erro(pedido(null, "Rua A", 1, 100, "NORMAL")));
    }

    @Test
    void enderecoVazioEhRejeitadoQuandoHaEntrega() {
        assertEquals("Endereço é obrigatório para entrega.", erro(pedido("Ana", "", 1, 100, "EXPRESSO")));
    }

    @Test
    void enderecoNaoEhExigidoNaRetiradaNaLoja() {
        assertDoesNotThrow(() -> cadeia.validar(pedido("Ana", null, 1, 100, "RETIRADA")));
    }

    @Test
    void quantidadeZeroEhRejeitada() {
        assertEquals("Quantidade deve ser maior que zero.", erro(pedido("Ana", "Rua A", 0, 100, "NORMAL")));
    }

    @Test
    void quantidadeAcimaDoEstoqueEhRejeitada() {
        assertTrue(erro(pedido("Ana", "Rua A", 11, 100, "NORMAL")).startsWith("Estoque insuficiente"));
    }

    @Test
    void quantidadeNoLimiteDoEstoqueEhAceita() {
        assertDoesNotThrow(() -> cadeia.validar(pedido("Ana", "Rua A", 10, 100, "NORMAL")));
    }

    @Test
    void valorZeroOuNegativoEhRejeitado() {
        assertEquals("Valor do pedido deve ser maior que zero.", erro(pedido("Ana", "Rua A", 1, 0, "NORMAL")));
        assertEquals("Valor do pedido deve ser maior que zero.", erro(pedido("Ana", "Rua A", 1, -5, "NORMAL")));
    }

    @Test
    void cadeiaParaNoPrimeiroErroNaOrdemDosElos() {
        // cliente vazio E quantidade inválida: quem fala primeiro é o ClienteHandler
        assertEquals("Cliente é obrigatório.", erro(pedido("", "Rua A", 0, 100, "NORMAL")));
    }

    @Test
    void setProximoRetornaOProximoElo() {
        ValidacaoHandler a = new ClienteHandler();
        ValidacaoHandler b = new ValorHandler();
        assertSame(b, a.setProximo(b));
    }
}
