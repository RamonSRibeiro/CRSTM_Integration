package com.exemplo.ecommerce.strategy;

/**
 * STRATEGY
 * Define a família de algoritmos de cálculo de frete.
 * Cada implementação é intercambiável em tempo de execução.
 */
public interface FreteStrategy {
    double calcular(double valorPedido);
}
