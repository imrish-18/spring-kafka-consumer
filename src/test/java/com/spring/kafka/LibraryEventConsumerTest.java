package com.spring.kafka;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@EmbeddedKafka(topics={"library-events"},partitions=3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"
, "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class LibraryEventConsumerTest {

	@Autowired
	EmbeddedKafkaBroker broker;
	
	/** The template. */
	@Autowired
	KafkaTemplate<Integer, String> template;
	
	@SpyBean
	LibraryEventsConsumer consumerSpy;
	
	 @Autowired
	    ObjectMapper objectMapper;

	
	@SpyBean
	LibraryService serviceSpy;
	@Autowired
	KafkaListenerEndpointRegistry listener;
	
	@Autowired
	LibraryEventRepo repo;
	@BeforeEach
	void setUp()
	{
		for(org.springframework.kafka.listener.MessageListenerContainer container:listener.getListenerContainers())
		{
			ContainerTestUtils.waitForAssignment(container, broker.getPartitionsPerTopic());
		}
	}
	
	 /**
 	 * Publish new library event.
 	 *
 	 * @throws InterruptedException the interrupted exception
 	 * @throws ExecutionException the execution exception
 	 * @throws JsonMappingException the json mapping exception
 	 * @throws JsonProcessingException the json processing exception
 	 */
 	@Test
	 void publishNewLibraryEvent() throws InterruptedException, ExecutionException, JsonMappingException, JsonProcessingException
	 {
		 String json=" {\"libraryEventId\":null,\"book\":{\"bookId\":456,\"bookName\":\"Kafka	 Using Spring Boot\",\"bookAuthor\":\"rish\"}}";
		 template.sendDefault(json).get();
		 
		 CountDownLatch latch=new CountDownLatch(1);
		 
		 latch.await(3,TimeUnit.SECONDS);
		 verify(consumerSpy,times(1)).onMessage((ConsumerRecord<Integer, String>) isA(ConsumerRecord.class));
		 List<LibraryEvent> libraryEventList = (List<LibraryEvent>)repo.findAll();
	        assert libraryEventList.size() ==1;
	        libraryEventList.forEach(libraryEvent -> {
	            assert libraryEvent.getLibraryEventId()!=null;
	            assertEquals(456, libraryEvent.getBook().getBook_Id());
	        });
		
  }
	 
	 /**
 	 * Publish update library event.
 	 *
 	 * @throws JsonProcessingException the json processing exception
 	 * @throws ExecutionException the execution exception
 	 * @throws InterruptedException the interrupted exception
 	 */
 	@Test
	    void publishUpdateLibraryEvent() throws JsonProcessingException, ExecutionException, InterruptedException {
	        //given
	        String json = " {\\\"libraryEventId\\\":null,\\\"book\\\":{\\\"bookId\\\":456,\\\"bookName\\\":\\\"Kafka	 Using Spring Boot\\\",\\\"bookAuthor\\\":\\\"rish\\\"}}";
	        LibraryEvent libraryEvent = objectMapper.readValue(json, LibraryEvent.class);
	        
	        repo.save(libraryEvent);
	        //publish the update LibraryEvent

	       Book updatedBook = new Book();
	      
	            
	      
	        libraryEvent.setBook(updatedBook);
	        String updatedJson = objectMapper.writeValueAsString(libraryEvent);
	        template.sendDefault(libraryEvent.getLibraryEventId(), updatedJson).get();

	        //when
	        CountDownLatch latch = new CountDownLatch(1);
	        latch.await(3, TimeUnit.SECONDS);

	        //then
	        verify(consumerSpy, times(1)).onMessage((ConsumerRecord<Integer, String>) isA(ConsumerRecord.class));
	        verify(serviceSpy, times(1)).processLibraryEvent((ConsumerRecord<Integer, String>) isA(ConsumerRecord.class));
	        LibraryEvent persistedLibraryEvent = repo.findById(libraryEvent.getLibraryEventId()).get();
	        assertEquals("Kafka Using Spring Boot 2.x", persistedLibraryEvent.getBook().getBook_Name());
	    }

	    /**
    	 * Publish update library event null library event.
    	 *
    	 * @throws JsonProcessingException the json processing exception
    	 * @throws ExecutionException the execution exception
    	 * @throws InterruptedException the interrupted exception
    	 */
    	@Test
	    void publishUpdateLibraryEvent_null_LibraryEvent() throws JsonProcessingException, ExecutionException, InterruptedException {
	        //given

	        String json = " {\\\"libraryEventId\\\":null,\\\"book\\\":{\\\"bookId\\\":456,\\\"bookName\\\":\\\"Kafka Using Spring Boot\\\",\\\"bookAuthor\\\":\\\"rish\\\"}}";
	        template.sendDefault(json).get();

	        //when
	        CountDownLatch latch = new CountDownLatch(1);
	        latch.await(5, TimeUnit.SECONDS);

	        //then
	        verify(consumerSpy, times(10)).onMessage((ConsumerRecord<Integer, String>) isA(ConsumerRecord.class));
	        verify(serviceSpy, times(10)).processLibraryEvent((ConsumerRecord<Integer, String>) isA(ConsumerRecord.class));
	    }

}
