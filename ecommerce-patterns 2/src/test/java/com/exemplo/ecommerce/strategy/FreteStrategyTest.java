package com.exemplo.ecommerce.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FreteStrategyTest {

    private static final double DELTA = 0.001;

    @Test
    void freteNormalCobra15ParaPedidoPequeno() {
        assertEquals(15.0, new FreteNormal().calcular(100), DELTA);
    }

    @Test
    void freteNormalEhGratisAPartirDe200() {
        assertEquals(0.0, new FreteNormal().calcular(200), DELTA);
        assertEquals(0.0, new FreteNormal().calcular(350), DELTA);
    }

    @Test
    void freteExpressoTemMinimoDe30() {
        assertEquals(30.0, new FreteExpresso().calcular(100), DELTA); // 10% = 10 -> mínimo 30
    }

    @Test
    void freteExpressoCobra10PorCentoAcimaDoMinimo() {
        assertEquals(50.0, new FreteExpresso().calcular(500), DELTA);
    }

    @Test
    void retiradaNaLojaNaoTemFrete() {
        assertEquals(0.0, new FreteRetirada().calcular(999), DELTA);
    }

    @Test
    void estrategiasSaoIntercambiaveisPelaInterface() {
        FreteStrategy s = new FreteExpresso();
        double expresso = s.calcular(100);
        s = new FreteRetirada();
        double retirada = s.calcular(100);
        assertEquals(30.0, expresso, DELTA);
        assertEquals(0.0, retirada, DELTA);
    }
}
