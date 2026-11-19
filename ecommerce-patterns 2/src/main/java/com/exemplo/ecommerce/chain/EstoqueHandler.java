package com.exemplo.ecommerce.chain;

import com.exemplo.ecommerce.model.PedidoRequest;

public class EstoqueHandler extends ValidacaoHandler {

    private static final int ESTOQUE_SIMULADO = 10;

    @Override
    protected void verificar(PedidoRequest pedido) {
        if (pedido.quantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        if (pedido.quantidade() > ESTOQUE_SIMULADO) {
            throw new IllegalArgumentException(
                    "Estoque insuficiente. Disponível: " + ESTOQUE_SIMULADO);
        }
    }
}
