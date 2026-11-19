package com.exemplo.ecommerce.chain;

import com.exemplo.ecommerce.model.PedidoRequest;

public class EnderecoHandler extends ValidacaoHandler {
    @Override
    protected void verificar(PedidoRequest pedido) {
        // Retirada na loja não precisa de endereço
        boolean retirada = "RETIRADA".equalsIgnoreCase(pedido.frete());
        if (!retirada && (pedido.endereco() == null || pedido.endereco().isBlank())) {
            throw new IllegalArgumentException("Endereço é obrigatório para entrega.");
        }
    }
}
