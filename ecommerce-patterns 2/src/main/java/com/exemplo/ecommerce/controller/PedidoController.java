package com.exemplo.ecommerce.controller;

import com.exemplo.ecommerce.model.PedidoRequest;
import com.exemplo.ecommerce.model.PedidoResponse;
import com.exemplo.ecommerce.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public PedidoResponse criar(@RequestBody PedidoRequest pedido) {
        return service.processar(pedido);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> erro(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }
}
