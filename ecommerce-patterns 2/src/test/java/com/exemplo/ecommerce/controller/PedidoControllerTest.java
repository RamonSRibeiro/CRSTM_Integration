package com.exemplo.ecommerce.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerTest {

    @Autowired
    private MockMvc mvc;

    private org.springframework.test.web.servlet.ResultActions enviar(String json) throws Exception {
        return mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @Test
    void pedidoValidoRetorna200() throws Exception {
        enviar("""
                {"cliente":"Ana","endereco":"Rua A, 10","quantidade":2,"valor":150.0,
                 "frete":"EXPRESSO","pagamento":"PIX"}""")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorProdutos").value(150.0))
                .andExpect(jsonPath("$.frete").value(30.0))
                .andExpect(jsonPath("$.totalPago").value(180.0));
    }

    @Test
    void retiradaSemEnderecoRetorna200() throws Exception {
        enviar("""
                {"cliente":"Bia","quantidade":1,"valor":80.0,"frete":"RETIRADA","pagamento":"PIX"}""")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.frete").value(0.0))
                .andExpect(jsonPath("$.totalPago").value(80.0));
    }

    @Test
    void estoqueInsuficienteRetorna400ComMensagem() throws Exception {
        enviar("""
                {"cliente":"Caio","endereco":"Rua B","quantidade":50,"valor":100.0,
                 "frete":"NORMAL","pagamento":"BOLETO"}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Estoque insuficiente. Disponível: 10"));
    }

    @Test
    void clienteAusenteRetorna400() throws Exception {
        enviar("""
                {"endereco":"Rua B","quantidade":1,"valor":100.0,"frete":"NORMAL","pagamento":"PIX"}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Cliente é obrigatório."));
    }

    @Test
    void enderecoAusenteComEntregaRetorna400() throws Exception {
        enviar("""
                {"cliente":"Ana","quantidade":1,"valor":100.0,"frete":"NORMAL","pagamento":"PIX"}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Endereço é obrigatório para entrega."));
    }

    @Test
    void freteInvalidoRetorna400() throws Exception {
        enviar("""
                {"cliente":"Ana","endereco":"Rua A","quantidade":1,"valor":100.0,
                 "frete":"TELEPORTE","pagamento":"PIX"}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Frete inválido: TELEPORTE"));
    }

    @Test
    void pagamentoInvalidoRetorna400() throws Exception {
        enviar("""
                {"cliente":"Ana","endereco":"Rua A","quantidade":1,"valor":100.0,
                 "frete":"NORMAL","pagamento":"FIADO"}""")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Pagamento inválido: FIADO"));
    }
}
