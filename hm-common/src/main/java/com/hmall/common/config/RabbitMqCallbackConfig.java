package com.hmall.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitMqCallbackConfig {

    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        // 1. 确认消息是否到达交换机
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String messageId = correlationData == null ? null : correlationData.getId();

            if (ack) {
                log.info("消息发送到交换机成功，messageId={}", messageId);
                return;
            }

            log.error("消息发送到交换机失败，messageId={}, cause={}", messageId, cause);
            // 后续一般在这里：记录失败日志、更新消息表状态、触发重试或告警
        });

        // 2. 消息到交换机了，但没有路由到队列
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("消息路由到队列失败，exchange={}, routingKey={}, replyCode={}, replyText={}, message={}",
                    returned.getExchange(),
                    returned.getRoutingKey(),
                    returned.getReplyCode(),
                    returned.getReplyText(),
                    returned.getMessage());
        });
    }
}
