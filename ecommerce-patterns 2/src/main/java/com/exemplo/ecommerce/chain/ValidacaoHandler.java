package com.exemplo.ecommerce.chain;

import com.exemplo.ecommerce.model.PedidoRequest;

/**
 * CHAIN OF RESPONSIBILITY
 * Cada handler faz a sua verificação e passa o pedido para o próximo elo.
 * Se algo estiver errado, lança exceção e a cadeia é interrompida.
 */
public abstract class ValidacaoHandler {

    private ValidacaoHandler proximo;

    public ValidacaoHandler setProximo(ValidacaoHandler proximo) {
        this.proximo = proximo;
        return proximo; // permite encadear: a.setProximo(b).setProximo(c)
    }

    public void validar(PedidoRequest pedido) {
        verificar(pedido);
        if (proximo != null) {
            proximo.validar(pedido);
        }
    }

    protected abstract void verificar(PedidoRequest pedido);
}
