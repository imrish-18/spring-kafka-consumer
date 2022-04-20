package com.spring.kafka;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO: Auto-generated Javadoc
/**
 * The Class LibraryEvent.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class LibraryEvent {

	/** The library event id. */
	@Id
	private Integer libraryEventId;
	
	/** The book. */
	@NotNull
	private Book book;
	
	/**
	 * Gets the library event id.
	 *
	 * @return the library event id
	 */
	public Integer getLibraryEventId() {
		return libraryEventId;
	}

	/**
	 * Sets the library event id.
	 *
	 * @param libraryEventId the new library event id
	 */
	public void setLibraryEventId(Integer libraryEventId) {
		this.libraryEventId = libraryEventId;
	}

	/**
	 * Gets the book.
	 *
	 * @return the book
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * Sets the book.
	 *
	 * @param book the new book
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	
}
