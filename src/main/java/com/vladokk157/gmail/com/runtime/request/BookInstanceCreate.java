package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.validation.Create;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import com.vladokk157.gmail.com.runtime.validation.Update;

/** Object Used to Create BookInstance */
@IdValid.List({
  @IdValid(
      field = "bookId",
      fieldType = Book.class,
      targetField = "book",
      groups = {Update.class, Create.class})
})
public class BookInstanceCreate extends BasicCreate {

  private Boolean blocked;

  @JsonIgnore private Book book;

  private String bookId;

  private String serialNumber;

  /**
   * @return blocked
   */
  public Boolean getBlocked() {
    return this.blocked;
  }

  /**
   * @param blocked blocked to set
   * @return BookInstanceCreate
   */
  public <T extends BookInstanceCreate> T setBlocked(Boolean blocked) {
    this.blocked = blocked;
    return (T) this;
  }

  /**
   * @return book
   */
  @JsonIgnore
  public Book getBook() {
    return this.book;
  }

  /**
   * @param book book to set
   * @return BookInstanceCreate
   */
  public <T extends BookInstanceCreate> T setBook(Book book) {
    this.book = book;
    return (T) this;
  }

  /**
   * @return bookId
   */
  public String getBookId() {
    return this.bookId;
  }

  /**
   * @param bookId bookId to set
   * @return BookInstanceCreate
   */
  public <T extends BookInstanceCreate> T setBookId(String bookId) {
    this.bookId = bookId;
    return (T) this;
  }

  /**
   * @return serialNumber
   */
  public String getSerialNumber() {
    return this.serialNumber;
  }

  /**
   * @param serialNumber serialNumber to set
   * @return BookInstanceCreate
   */
  public <T extends BookInstanceCreate> T setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
    return (T) this;
  }
}
