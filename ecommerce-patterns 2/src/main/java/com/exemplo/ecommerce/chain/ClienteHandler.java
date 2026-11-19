package com.exemplo.ecommerce.chain;

import com.exemplo.ecommerce.model.PedidoRequest;

public class ClienteHandler extends ValidacaoHandler {
    @Override
    protected void verificar(PedidoRequest pedido) {
        if (pedido.cliente() == null || pedido.cliente().isBlank()) {
            throw new IllegalArgumentException("Cliente é obrigatório.");
        }
    }
}
