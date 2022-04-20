package com.spring.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class LibraryEventsConsumerManaulOffSet.
 */
@Component

public class LibraryEventsConsumerManaulOffSet implements AcknowledgingMessageListener<Integer, String>{

	/** The logger. */
	Logger logger = LoggerFactory.getLogger(LibraryEventsConsumerManaulOffSet.class);
	
	/**
	 * On message.
	 *
	 * @param record the record
	 * @param acknowledgment the acknowledgment
	 */
	@KafkaListener(topics= {"library-events"},groupId = "library-events-listener-group")
	@Override
	public void onMessage(ConsumerRecord<Integer, String> record, Acknowledgment acknowledgment) {
		// TODO Auto-generated method stub
		logger.info("consumer record is :{}",record.toString());
		acknowledgment.acknowledge();
	
		
	}
}
