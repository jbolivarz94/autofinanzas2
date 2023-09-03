package com.jbolivarz.autofinanzas2.services;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.jbolivarz.autofinanzas2.models.Cuenta;
import com.jbolivarz.autofinanzas2.models.Usuario;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CuentaSQSService {
    private AmazonSQS clientSQS;

    public String createQueue(String queueName){
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        return clientSQS.createQueue(createQueueRequest).getQueueUrl();
    }

    public void publishStandarQueueMessage(String urlQueue, Integer delaySeconds, Cuenta cuenta){
        Map<String, MessageAttributeValue> atributosMensaje = new HashMap<>();

        atributosMensaje.put("id", new MessageAttributeValue().withStringValue(cuenta.getId().toString()).withDataType("Number"));
        atributosMensaje.put("idUsuario", new MessageAttributeValue().withStringValue(cuenta.getIdUsuario().toString()).withDataType("Number"));
        atributosMensaje.put("numero", new MessageAttributeValue().withStringValue(cuenta.getNumero()).withDataType("String"));
        atributosMensaje.put("tipo", new MessageAttributeValue().withStringValue(cuenta.getTipo()).withDataType("String"));
        atributosMensaje.put("montoInicial", new MessageAttributeValue().withStringValue(cuenta.getMontoInicial().toString()).withDataType("String"));

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(urlQueue)
                .withMessageBody(cuenta.getNumero())
                .withDelaySeconds(delaySeconds)
                .withMessageAttributes(atributosMensaje);

        clientSQS.sendMessage(sendMessageRequest);
    }

    public void publishStandarQueueMessage(String urlQueue, Integer delaySeconds, List<Cuenta> cuentas){
        for(Cuenta cuenta: cuentas){
            publishStandarQueueMessage(urlQueue, delaySeconds, cuenta);
        }
    }

    public List<Message> receiveMessageFromQueue(String urlQueue, Integer maxNumberMessages, Integer waitTimeSeconds){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(urlQueue)
                .withMaxNumberOfMessages(maxNumberMessages)
                .withWaitTimeSeconds(waitTimeSeconds);

        return clientSQS.receiveMessage(receiveMessageRequest).getMessages();
    }
}
