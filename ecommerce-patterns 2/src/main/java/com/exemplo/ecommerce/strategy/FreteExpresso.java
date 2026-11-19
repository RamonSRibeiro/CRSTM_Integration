package com.exemplo.ecommerce.strategy;

import org.springframework.stereotype.Component;

@Component("EXPRESSO")
public class FreteExpresso implements FreteStrategy {
    @Override
    public double calcular(double valorPedido) {
        // 10% do pedido, mínimo de 30
        return Math.max(30.0, valorPedido * 0.10);
    }
}
