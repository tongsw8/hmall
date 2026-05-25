package com.hmall.pay;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PayApplicationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void testConfirmSuccess() throws InterruptedException {
        CorrelationData cd = new CorrelationData("test-ok-1");
        rabbitTemplate.convertAndSend("pay.direct", "queue", 123L, cd);
        Thread.sleep(4000);
    }
}