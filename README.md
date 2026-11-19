# Exercício: Chain of Responsibility + Strategy + Template Method

**Tema:** Pedido de E-commerce
**Tecnologias:** Java 17, Spring Boot 3, Maven, JUnit 5

## Objetivo

Praticar a integração de três padrões de projeto em uma API simples que processa pedidos de uma loja virtual.

## Como funciona

O endpoint `POST /pedidos` recebe um pedido e executa três etapas em sequência:

```
Pedido → [1] Validação → [2] Cálculo de frete → [3] Pagamento → Resposta
```

| Etapa | Padrão | O que faz |
|---|---|---|
| 1. Validação | **Chain of Responsibility** | Uma cadeia de handlers (Cliente → Endereço → Estoque → Valor). Cada um valida uma regra e passa para o próximo. Se algum falhar, a cadeia para e a API retorna erro 400. |
| 2. Frete | **Strategy** | Algoritmos de frete intercambiáveis: `NORMAL`, `EXPRESSO` e `RETIRADA`. A estratégia é escolhida pelo nome informado no pedido. |
| 3. Pagamento | **Template Method** | `PagamentoTemplate.processar()` define o fluxo fixo. `PIX`, `CARTAO` e `BOLETO` só implementam a taxa e a forma de pagamento. |

O `PedidoService` orquestra os três padrões.

## Regras de negócio

- **Estoque:** simulado em 10 unidades.
- **Endereço:** obrigatório, exceto na retirada na loja.
- **Frete normal:** R$ 15,00, grátis a partir de R$ 200,00.
- **Frete expresso:** 10% do valor, mínimo de R$ 30,00.
- **Retirada:** sem custo.
- **PIX:** sem taxa.
- **Cartão:** 3% de taxa, calculada sobre produtos + frete.
- **Boleto:** taxa fixa de R$ 3,50.

## Estrutura

```
src/main/java/com/exemplo/ecommerce
├── chain/        → ValidacaoHandler + 4 handlers
├── strategy/     → FreteStrategy + 3 implementações
├── template/     → PagamentoTemplate + 3 implementações
├── service/      → PedidoService (integra os padrões)
├── controller/   → PedidoController
└── model/        → PedidoRequest e PedidoResponse
```

## Como executar

```
mvn spring-boot:run
```

## Exemplo de requisição

```
curl -X POST localhost:8080/pedidos -H "Content-Type: application/json" -d '{
  "cliente": "Ana",
  "endereco": "Rua A, 10",
  "quantidade": 2,
  "valor": 150.0,
  "frete": "EXPRESSO",
  "pagamento": "PIX"
}'
```

Resposta esperada:

```json
{
  "valorProdutos": 150.0,
  "frete": 30.0,
  "taxaPagamento": 0.0,
  "totalPago": 180.0,
  "mensagem": "Pago via PIX. Total: R$ 180.0"
}
```

## Como executar os testes

```
mvn test
```

Os testes unitários cobrem a cadeia, as estratégias e o template. Os testes de integração cobrem o serviço e o controller (com MockMvc).
