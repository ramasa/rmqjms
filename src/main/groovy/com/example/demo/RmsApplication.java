package com.example.demo;

import javax.jms.ConnectionFactory;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

@SpringBootApplication
@EnableJms
@RestController
public class RmsApplication {

	@Autowired
	JmsMessagingTemplate t;

	public static void main(String[] args) {
		SpringApplication.run(RmsApplication.class, args);
	}

	@Bean
	ConnectionFactory cf() {
		RMQConnectionFactory cf = new RMQConnectionFactory();
		cf.setHost("localhost");
		return cf;
	}

	@Bean
	JmsTemplate jmsTemplate(ConnectionFactory cf) {
		JmsTemplate t = new JmsTemplate(cf);
		return t;
	}

	@GetMapping("/test")
	String test() {
		String resp = t.convertSendAndReceive("tq", "request", String.class);
		return resp;
	}

	@JmsListener(destination = "tq")
	String list(Message msg) {
		System.out.println("Received message" + msg);
		return "response";
	}

}
