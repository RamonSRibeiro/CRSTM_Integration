# Exercício: Chain of Responsibility + Strategy + Template Method
Tema: **Pedido de E-commerce** (Java 17 + Spring Boot 3)

## Fluxo
```
POST /pedidos
   │
   ▼
[1] CHAIN OF RESPONSIBILITY  → valida: Cliente → Endereço → Estoque → Valor
   │
   ▼
[2] STRATEGY                 → calcula frete: NORMAL | EXPRESSO | RETIRADA
   │
   ▼
[3] TEMPLATE METHOD          → processa pagamento: PIX | CARTAO | BOLETO
   │
   ▼
Resposta JSON
```

## Onde está cada padrão
| Padrão | Pasta | Ideia |
|---|---|---|
| Chain of Responsibility | `chain/` | Cada handler valida uma regra e chama o próximo |
| Strategy | `strategy/` | Algoritmos de frete intercambiáveis |
| Template Method | `template/` | `processar()` é fixo; taxa e execução variam por subclasse |
| Integração | `service/PedidoService` | Orquestra os três |

## Executar
```
mvn spring-boot:run
```

## Testes automatizados
```
mvn test
```
| Classe de teste | Tipo | O que cobre |
|---|---|---|
| `chain/ValidacaoChainTest` | unitário | cada handler, a ordem dos elos e a parada no primeiro erro |
| `strategy/FreteStrategyTest` | unitário | regras de cada frete (limites de 200 e mínimo de 30) |
| `template/PagamentoTemplateTest` | unitário | taxa de cada pagamento, ordem fixa dos passos e `processar()` ser `final` |
| `service/PedidoServiceTest` | integração (Spring) | os 3 padrões juntos, tipos inválidos, case-insensitive |
| `controller/PedidoControllerTest` | integração (MockMvc) | respostas HTTP 200 e 400 com mensagem de erro |

## Testes manuais (curl)
Pedido OK (frete expresso + cartão):
```
curl -X POST localhost:8080/pedidos -H "Content-Type: application/json" -d '{
  "cliente":"Ana","endereco":"Rua A, 10","quantidade":2,"valor":150.0,
  "frete":"EXPRESSO","pagamento":"CARTAO"}'
```
Retirada + PIX (sem endereço):
```
curl -X POST localhost:8080/pedidos -H "Content-Type: application/json" -d '{
  "cliente":"Bia","quantidade":1,"valor":80.0,"frete":"RETIRADA","pagamento":"PIX"}'
```
Falha na cadeia (estoque):
```
curl -X POST localhost:8080/pedidos -H "Content-Type: application/json" -d '{
  "cliente":"Caio","endereco":"Rua B","quantidade":50,"valor":100.0,
  "frete":"NORMAL","pagamento":"BOLETO"}'
```

## Desafios para praticar
1. Adicione um handler `CupomHandler` na cadeia.
2. Crie uma nova estratégia `FreteInternacional`.
3. Crie `PagamentoCripto` estendendo `PagamentoTemplate`.
4. Adicione um passo-gancho (hook) opcional no template, como `antiFraude()`.
