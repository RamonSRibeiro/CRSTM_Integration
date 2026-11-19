package com.exemplo.ecommerce.model;

/**
 * Dados que chegam no POST /pedidos.
 * frete:     NORMAL | EXPRESSO | RETIRADA
 * pagamento: PIX | CARTAO | BOLETO
 */
public record PedidoRequest(
        String cliente,
        String endereco,
        int quantidade,
        double valor,
        String frete,
        String pagamento) {
}
