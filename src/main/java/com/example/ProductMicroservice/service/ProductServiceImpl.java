package com.example.ProductMicroservice.service;


import com.example.core.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import com.example.ProductMicroservice.rest.CreateProductRestModel;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

	KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
	private final Logger LOGGER  = LoggerFactory.getLogger(this.getClass());

	public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public String createProduct(CreateProductRestModel productRestModel) throws Exception {

		String productId = UUID.randomUUID().toString();

		// assuming data stored in db before publishing an Event




		ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,
				productRestModel.getTitle(), productRestModel.getPrice(),
				productRestModel.getQuantity());


//1. send message asynchronously


//		CompletableFuture<SendResult<String,ProductCreatedEvent>> future = kafkaTemplate.send("product-created-events-topic",productId,productCreatedEvent);
//
//		future.whenComplete((result,exception)-> {
//					if (exception !=null){
//						LOGGER.error("**********failed to send message"+ exception.getMessage());
//					}
//					else{
//						LOGGER.info("***********message sent successfully" +result.getRecordMetadata());
//					}
//				}
//				);


		// 2. sychronous sending

		LOGGER.info("Before publishing a ProductCreatedEvent");

		SendResult<String, ProductCreatedEvent> result =
				kafkaTemplate.send("product-created-events-topic",productId, productCreatedEvent).get();

		LOGGER.info("Partition: " + result.getRecordMetadata().partition());
		LOGGER.info("Topic: " + result.getRecordMetadata().topic());
		LOGGER.info("Offset: " + result.getRecordMetadata().offset());

		LOGGER.info("***** Returning product id");

		return productId;
	}

}
