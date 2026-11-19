package com.exemplo.ecommerce.chain;

import com.exemplo.ecommerce.model.PedidoRequest;

public class ValorHandler extends ValidacaoHandler {
    @Override
    protected void verificar(PedidoRequest pedido) {
        if (pedido.valor() <= 0) {
            throw new IllegalArgumentException("Valor do pedido deve ser maior que zero.");
        }
    }
}
