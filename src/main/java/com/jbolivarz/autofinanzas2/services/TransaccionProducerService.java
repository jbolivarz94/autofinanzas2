package com.jbolivarz.autofinanzas2.services;

import com.jbolivarz.autofinanzas2.models.Transaccion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransaccionProducerService {
    private final Logger LOGGER = LoggerFactory.getLogger(TransaccionProducerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransaccionProducerService(KafkaTemplate<String, String> kafkaTemplate){ this.kafkaTemplate = kafkaTemplate; }

    public void sendKey(String topico, Integer key, Transaccion transaccion){
        var future = kafkaTemplate.send(topico, key.toString(), transaccion.toString());

        future.whenComplete((resultadoEnvio, excepcion) -> {
            if(excepcion != null){
                LOGGER.error(excepcion.getMessage());
                future.completeExceptionally(excepcion);
            }else{
                future.complete(resultadoEnvio);
            }
            LOGGER.info("Transaccion enviada al topico de Kafka con id "+transaccion);
        });
    }
}
