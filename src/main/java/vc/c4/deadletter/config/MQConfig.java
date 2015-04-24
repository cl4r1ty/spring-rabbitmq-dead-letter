package vc.c4.deadletter.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfig {

  public static final String OUTGOING_QUEUE = "outgoing.example";

  public static final String INCOMING_QUEUE = "incoming.example";

  @Autowired
  private ConnectionFactory cachingConnectionFactory;

  // Setting the annotation listeners to use the jackson2JsonMessageConverter
  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(cachingConnectionFactory);
    factory.setMessageConverter(jackson2JsonMessageConverter());
    return factory;
  }

  // Standardize on a single objectMapper for all message queue items
  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public Queue outgoingQueue() {
    Map<String, Object> args = new HashMap<String, Object>();
    // The default exchange
    args.put("x-dead-letter-exchange", "");
    // Route to the incoming queue when the TTL occurs
    args.put("x-dead-letter-routing-key", INCOMING_QUEUE);
    // TTL 5 seconds
    args.put("x-message-ttl", 5000);
    return new Queue(OUTGOING_QUEUE, false, false, false, args);
  }

  @Bean
  public RabbitTemplate outgoingSender() {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
    rabbitTemplate.setQueue(outgoingQueue().getName());
    rabbitTemplate.setRoutingKey(outgoingQueue().getName());
    rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
    return rabbitTemplate;
  }

  @Bean
  public Queue incomingQueue() {
    return new Queue(INCOMING_QUEUE);
  }

}
