package com.spring.kafka;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO: Auto-generated Javadoc
/**
 * The Class Book.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Book {

	
	/** The book id. */
	@NotNull
	@Id
	private Integer book_Id;
	
	/** The book name. */
	 @NotBlank
	private String book_Name;
	
	/** The book author. */
	 @NotBlank
	private String book_Author;
	
	/**
	 * Gets the book id.
	 *
	 * @return the book id
	 */
	public Integer getBook_Id() {
		return book_Id;
	}
	
	/**
	 * Sets the book id.
	 *
	 * @param book_Id the new book id
	 */
	public void setBook_Id(Integer book_Id) {
		this.book_Id = book_Id;
	}
	
	/**
	 * Gets the book name.
	 *
	 * @return the book name
	 */
	public String getBook_Name() {
		return book_Name;
	}
	
	/**
	 * Sets the book name.
	 *
	 * @param book_Name the new book name
	 */
	public void setBook_Name(String book_Name) {
		this.book_Name = book_Name;
	}
	
	/**
	 * Gets the book author.
	 *
	 * @return the book author
	 */
	public String getBook_Author() {
		return book_Author;
	}
	
	/**
	 * Sets the book author.
	 *
	 * @param book_Author the new book author
	 */
	public void setBook_Author(String book_Author) {
		this.book_Author = book_Author;
	}
	
}
