package com.exemplo.ecommerce.strategy;

import org.springframework.stereotype.Component;

@Component("RETIRADA")
public class FreteRetirada implements FreteStrategy {
    @Override
    public double calcular(double valorPedido) {
        return 0.0;
    }
}
