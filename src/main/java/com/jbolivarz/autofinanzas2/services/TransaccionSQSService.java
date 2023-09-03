package com.jbolivarz.autofinanzas2.services;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.jbolivarz.autofinanzas2.models.Cuenta;
import com.jbolivarz.autofinanzas2.models.Transaccion;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransaccionSQSService {
    private AmazonSQS clientSQS;

    public String createQueue(String queueName){
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        return clientSQS.createQueue(createQueueRequest).getQueueUrl();
    }

    public void publishStandarQueueMessage(String urlQueue, Integer delaySeconds, Transaccion transaccion){
        Map<String, MessageAttributeValue> atributosMensaje = new HashMap<>();

        atributosMensaje.put("id", new MessageAttributeValue().withStringValue(transaccion.getId().toString()).withDataType("Number"));
        atributosMensaje.put("idCuenta", new MessageAttributeValue().withStringValue(transaccion.getIdCuenta().toString()).withDataType("Number"));
        atributosMensaje.put("idCategoria", new MessageAttributeValue().withStringValue(transaccion.getIdCategoria().toString()).withDataType("Number"));
        atributosMensaje.put("fecha", new MessageAttributeValue().withStringValue(transaccion.getFecha().toString()).withDataType("String"));
        atributosMensaje.put("descripcion", new MessageAttributeValue().withStringValue(transaccion.getDescripcion()).withDataType("String"));
        atributosMensaje.put("monto", new MessageAttributeValue().withStringValue(transaccion.getMonto().toString()).withDataType("Number"));

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(urlQueue)
                .withMessageBody(transaccion.getDescripcion())
                .withDelaySeconds(delaySeconds)
                .withMessageAttributes(atributosMensaje);

        clientSQS.sendMessage(sendMessageRequest);
    }

    public void publishStandarQueueMessage(String urlQueue, Integer delaySeconds, List<Transaccion> transaccions){
        for(Transaccion transaccion: transaccions){
            publishStandarQueueMessage(urlQueue, delaySeconds, transaccion);
        }
    }

    public List<Message> receiveMessageFromQueue(String urlQueue, Integer maxNumberMessages, Integer waitTimeSeconds){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(urlQueue)
                .withMaxNumberOfMessages(maxNumberMessages)
                .withWaitTimeSeconds(waitTimeSeconds);

        return clientSQS.receiveMessage(receiveMessageRequest).getMessages();
    }
}

