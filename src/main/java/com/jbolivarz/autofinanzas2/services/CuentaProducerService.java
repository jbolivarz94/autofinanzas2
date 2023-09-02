package com.jbolivarz.autofinanzas2.services;

import com.jbolivarz.autofinanzas2.models.Cuenta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CuentaProducerService {

    private final Logger LOGGER = LoggerFactory.getLogger(CuentaProducerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CuentaProducerService(KafkaTemplate<String, String> kafkaTemplate){ this.kafkaTemplate = kafkaTemplate; }

    public void sendKey(String topico, Integer key, Cuenta cuenta){
        var future = kafkaTemplate.send(topico, key.toString(), cuenta.toString());

        future.whenComplete((resultadoEnvio, excepcion) -> {
           if(excepcion != null){
               LOGGER.error(excepcion.getMessage());
               future.completeExceptionally(excepcion);
           }else{
               future.complete(resultadoEnvio);
           }
            LOGGER.info("Cuenta enviada al topico de Kafka con id "+cuenta);
        });
    }
}
