package com.spring.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

// TODO: Auto-generated Javadoc
@Component

/**
 * The Class LibraryEventsConsumer.
 */
public class LibraryEventsConsumer {

	/** The logger. */
	Logger logger = LoggerFactory.getLogger(LibraryEventsConsumer.class);
	
	@Autowired
	private LibraryService service;
	
	/**
	 * On message.
	 *
	 * @param record the record
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 */
	@KafkaListener(topics= {"library-events"},groupId = "library-events-listener-group")
	public void onMessage(ConsumerRecord<Integer, String> record) throws JsonMappingException, JsonProcessingException
	{
		logger.info("consumer record is :{}",record.toString());
		service.processLibraryEvent(record);
		logger.info("hello rish");
	}
}
