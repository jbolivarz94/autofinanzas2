package com.jbolivarz.autofinanzas2.controller;

import com.jbolivarz.autofinanzas2.services.UsuarioConsumerService;
import com.jbolivarz.autofinanzas2.services.UsuarioSQSService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    UsuarioSQSService usuarioSQSService;
    UsuarioConsumerService usuarioConsumerService;

    public UsuarioController(UsuarioSQSService usuarioSQSService, UsuarioConsumerService usuarioConsumerService) {
        this.usuarioSQSService = usuarioSQSService;
        this.usuarioConsumerService = usuarioConsumerService;
    }

    @GetMapping("/topico-kafka/{topico}")
    public Mono<String> getUsuarioFromTopicoKafka(@PathVariable String topico){
        return  Mono.just(usuarioConsumerService.obtenerNombreUltimoUsuario(topico));
    }

    @PostMapping("/aws/createQueue")
    public Mono<String> postCreateQueue(@RequestBody Map<String, Object> requestBody){
        return Mono.just(usuarioSQSService.createQueue((String) requestBody.get("queueName")));
    }
}
