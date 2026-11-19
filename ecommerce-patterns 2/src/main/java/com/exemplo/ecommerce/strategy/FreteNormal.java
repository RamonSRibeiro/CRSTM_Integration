package com.exemplo.ecommerce.strategy;

import org.springframework.stereotype.Component;

@Component("NORMAL")
public class FreteNormal implements FreteStrategy {
    @Override
    public double calcular(double valorPedido) {
        // frete grátis acima de 200
        return valorPedido >= 200 ? 0.0 : 15.0;
    }
}
