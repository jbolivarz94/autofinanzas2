package com.jbolivarz.autofinanzas2.services;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;
import com.jbolivarz.autofinanzas2.models.Usuario;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioSQSService {

    private AmazonSQS clientSQS;

    public UsuarioSQSService(AmazonSQS clientSQS) {this.clientSQS = clientSQS;}

    public String createQueue(String queueName){
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        return clientSQS.createQueue(createQueueRequest).getQueueUrl();
    }

    public String publishStandarQueueMessage(String urlQueue, Integer delaySeconds, Usuario usuario){
        Map<String, MessageAttributeValue> atributosMensaje = new HashMap<>();

        atributosMensaje.put("id", new MessageAttributeValue().withStringValue(usuario.getId().toString()).withDataType("Number"));
        atributosMensaje.put("identificacion", new MessageAttributeValue().withStringValue(usuario.getIdentificacion()).withDataType("String"));
        atributosMensaje.put("nombre", new MessageAttributeValue().withStringValue(usuario.getNombre()).withDataType("String"));
        atributosMensaje.put("apellido", new MessageAttributeValue().withStringValue(usuario.getApellido()).withDataType("String"));
        atributosMensaje.put("email", new MessageAttributeValue().withStringValue(usuario.getEmail()).withDataType("String"));
        atributosMensaje.put("telefono", new MessageAttributeValue().withStringValue(usuario.getTelefono()).withDataType("String"));

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(urlQueue)
                .withMessageBody(usuario.getIdentificacion())
                .withDelaySeconds(delaySeconds)
                .withMessageAttributes(atributosMensaje);

        return  clientSQS.sendMessage(sendMessageRequest).getMessageId();
    }

    public void publishStandarQueueMessage(String urlQueue, Integer delaySeconds, List<Usuario> usuarios){
        for(Usuario usuario: usuarios){
            publishStandarQueueMessage(urlQueue, delaySeconds, usuario);
        }
    }

    public List<Message> receiveMessageFromQueue(String urlQueue, Integer maxNumberMessages, Integer waitTimeSeconds){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(urlQueue)
                .withMaxNumberOfMessages(maxNumberMessages)
                .withWaitTimeSeconds(waitTimeSeconds);

        return clientSQS.receiveMessage(receiveMessageRequest).getMessages();
    }

    private String getQueueUrl(String queueName){
        return clientSQS.getQueueUrl(queueName).getQueueUrl();
    }

    private List<Message> receiveMessagesFromQueue(String queueName, Integer maxNumberMessages, Integer waitTimeSeconds){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(this.getQueueUrl(queueName))
                .withMaxNumberOfMessages(maxNumberMessages)
                .withMessageAttributeNames(List.of("All"))
                .withWaitTimeSeconds(waitTimeSeconds);
        return clientSQS.receiveMessage(receiveMessageRequest).getMessages();
    }

    public Mono<Usuario> deleteUsuarioMessageInQueue(String queueName, Integer maxNumberMessages,
                                                     Integer waitTimeSeconds, String descripcionCredito){
        List<Message> usuarioMessages = receiveMessagesFromQueue(queueName, maxNumberMessages, waitTimeSeconds);
        for(Message message : usuarioMessages){
            if(!message.getMessageAttributes().isEmpty()) {
                if (message.getMessageAttributes().get("identificacion").getStringValue().equals(descripcionCredito)) {
                    Usuario usuario = new Usuario(Integer.valueOf(message.getMessageAttributes().get("id").getStringValue()),
                            message.getMessageAttributes().get("identificacion").getStringValue(),
                            message.getMessageAttributes().get("nombre").getStringValue(),
                            message.getMessageAttributes().get("apellido").getStringValue(),
                            message.getMessageAttributes().get("email").getStringValue(),
                            message.getMessageAttributes().get("telefono").getStringValue());
                    clientSQS.deleteMessage(this.getQueueUrl(queueName), message.getReceiptHandle());
                    return Mono.just(usuario);
                }
            }
        }
        return Mono.empty();
    }
}
