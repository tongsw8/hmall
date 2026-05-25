package com.hmall.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ErrorMessageConfig {
    @Bean
    public DirectExchange errorMessageExchange() {
        return new DirectExchange("error.exchange");
    }

    @Bean
    public Queue errorMessageQueue() {
        return new Queue("error.queue");
    }
    @Bean
    public Binding errorMessageBinding() {
        return BindingBuilder.bind(errorMessageQueue()).to(errorMessageExchange()).with("error");
    }
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.exchange", "error");
    }
}
