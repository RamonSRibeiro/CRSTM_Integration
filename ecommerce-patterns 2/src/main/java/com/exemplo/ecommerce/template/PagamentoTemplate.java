package com.exemplo.ecommerce.template;

/**
 * TEMPLATE METHOD
 * processar() é o "esqueleto" do algoritmo: a ordem dos passos é fixa (final).
 * As subclasses só preenchem os passos que variam (taxa e execução).
 */
public abstract class PagamentoTemplate {

    public final ResultadoPagamento processar(double valor) {
        System.out.println("[Pagamento] Iniciando pagamento de R$ " + valor);   // passo fixo
        double taxa = calcularTaxa(valor);                                        // passo variável
        double total = valor + taxa;
        String mensagem = efetuarPagamento(total);                                // passo variável
        System.out.println("[Pagamento] Confirmação enviada ao cliente.");        // passo fixo
        return new ResultadoPagamento(taxa, total, mensagem);
    }

    protected abstract double calcularTaxa(double valor);

    protected abstract String efetuarPagamento(double total);
}
