package com.spring.kafka;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LibraryService {
	
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	LibraryEventRepo repo;
	/** The logger. */
	Logger logger = LoggerFactory.getLogger(LibraryService.class);
	
	public void processLibraryEvent(ConsumerRecord<Integer, String> record) throws JsonMappingException, JsonProcessingException
	{
		LibraryEvent event=mapper.readValue(record.value(), LibraryEvent.class);
		
		logger.info("libraryEvent : {}",event);
		
		repo.save(event);
	}
	
	
	 /**
 	 * Validate.
 	 *
 	 * @param libraryEvent the library event
 	 */
 	private void validate(LibraryEvent libraryEvent) {
	        if(libraryEvent.getLibraryEventId()==null){
	            throw new IllegalArgumentException("Library Event Id is missing");
	        }

	        Optional<LibraryEvent> libraryEventOptional = repo.findById(libraryEvent.getLibraryEventId());
	        if(!libraryEventOptional.isPresent()){
	            throw new IllegalArgumentException("Not a valid library Event");
	        }
	        logger.info("Validation is successful for the library Event : {} ", libraryEventOptional.get());
	    }

	    /**
    	 * Save.
    	 *
    	 * @param libraryEvent the library event
    	 */
    	private void save(LibraryEvent libraryEvent) {
	        libraryEvent.getBook();
	       repo.save(libraryEvent);
	        logger.info("Successfully Persisted the libary Event {} ", libraryEvent);
	    }

}
