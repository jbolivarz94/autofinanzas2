package com.jbolivarz.autofinanzas2.services;

import com.jbolivarz.autofinanzas2.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UsuarioProducerService {
    private final Logger LOGGER = LoggerFactory.getLogger(UsuarioProducerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UsuarioProducerService(KafkaTemplate<String, String> kafkaTemplate){ this.kafkaTemplate = kafkaTemplate; }

    public void sendKey(String topico, Integer key, Usuario usuario){
        var future = kafkaTemplate.send(topico, key.toString(), usuario.toString());

        future.whenComplete((resultadoEnvio, excepcion) -> {
            if(excepcion != null){
                LOGGER.error(excepcion.getMessage());
                future.completeExceptionally(excepcion);
            }else{
                future.complete(resultadoEnvio);
            }
            LOGGER.info("Usuario enviado al topico de Kafka con id "+usuario);
        });
    }
}
