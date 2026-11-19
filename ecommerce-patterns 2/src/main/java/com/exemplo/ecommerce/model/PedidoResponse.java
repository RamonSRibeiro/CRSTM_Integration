package com.exemplo.ecommerce.model;

public record PedidoResponse(
        double valorProdutos,
        double frete,
        double taxaPagamento,
        double totalPago,
        String mensagem) {
}
