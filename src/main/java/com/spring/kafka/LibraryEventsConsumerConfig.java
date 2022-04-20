
package com.spring.kafka; 

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import  org.springframework.context.annotation.Configuration; 
  import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import org.springframework.kafka.listener.ContainerProperties; 
  // TODO: Auto-generated Javadoc
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

  /**
   * The Class LibraryEventsConsumerConfig.
   */
  @Configuration
 @EnableKafka
 public class LibraryEventsConsumerConfig 
 { 
	
	  
	  /** The logger. */
		Logger logger = LoggerFactory.getLogger(LibraryEventsConsumerConfig.class);
	  
	  /**
  	 * Error handler.
  	 *
  	 * @return the default error handler
  	 */
  	public DefaultErrorHandler errorHandler()
	  {
		 var FixedBackOff =new FixedBackOff(1000L,2);
		 var exceptionToIgnoreList=List.of(IllegalArgumentException.class);
		 
		 
		 DefaultErrorHandler errorhandler= new DefaultErrorHandler(FixedBackOff);
		 
		 exceptionToIgnoreList.forEach(errorhandler::addNotRetryableExceptions);
		 
		 errorhandler.
		 setRetryListeners(((recore,ex,deliveryattempt)->{
			 
			 logger.info("failed record in retry listener exception : {}, deliverAttempt : {}",ex.getMessage(),deliveryattempt);
		 }));
		 
		 
		 return null;
		 
		 
	  }
	  
	  
	  /**
  	 * Kafka listener container factory.
  	 *
  	 * @param configurer the configurer
  	 * @param kafkaConsumerFactory the kafka consumer factory
  	 * @return the concurrent kafka listener container factory
  	 */
  	@Bean
		@ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
		ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
				ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
				ConsumerFactory<Object, Object> kafkaConsumerFactory) {
			ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
			configurer.configure(factory, kafkaConsumerFactory);
			factory.setConcurrency(3);
			factory.setCommonErrorHandler(errorHandler());
			//factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
			
			return factory;
		}

	  
 }
 
 