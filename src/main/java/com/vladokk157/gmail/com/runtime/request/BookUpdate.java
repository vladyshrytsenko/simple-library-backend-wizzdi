package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import com.vladokk157.gmail.com.runtime.validation.Update;

/** Object Used to Update Book */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Book.class,
      targetField = "book",
      groups = {Update.class})
})
public class BookUpdate extends BookCreate {

  @JsonIgnore private Book book;

  private String id;

  /**
   * @return book
   */
  @JsonIgnore
  public Book getBook() {
    return this.book;
  }

  /**
   * @param book book to set
   * @return BookUpdate
   */
  public <T extends BookUpdate> T setBook(Book book) {
    this.book = book;
    return (T) this;
  }

  /**
   * @return id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id id to set
   * @return BookUpdate
   */
  public <T extends BookUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
