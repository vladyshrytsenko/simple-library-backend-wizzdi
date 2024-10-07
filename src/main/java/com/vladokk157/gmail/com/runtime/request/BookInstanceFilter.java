package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List BookInstance */
@IdValid.List({@IdValid(field = "bookIds", fieldType = Book.class, targetField = "books")})
public class BookInstanceFilter extends BasicFilter {

  private Set<Boolean> blocked;

  private Set<String> bookIds;

  @JsonIgnore private List<Book> books;

  private Set<String> serialNumber;

  /**
   * @return blocked
   */
  public Set<Boolean> getBlocked() {
    return this.blocked;
  }

  /**
   * @param blocked blocked to set
   * @return BookInstanceFilter
   */
  public <T extends BookInstanceFilter> T setBlocked(Set<Boolean> blocked) {
    this.blocked = blocked;
    return (T) this;
  }

  /**
   * @return bookIds
   */
  public Set<String> getBookIds() {
    return this.bookIds;
  }

  /**
   * @param bookIds bookIds to set
   * @return BookInstanceFilter
   */
  public <T extends BookInstanceFilter> T setBookIds(Set<String> bookIds) {
    this.bookIds = bookIds;
    return (T) this;
  }

  /**
   * @return books
   */
  @JsonIgnore
  public List<Book> getBooks() {
    return this.books;
  }

  /**
   * @param books books to set
   * @return BookInstanceFilter
   */
  public <T extends BookInstanceFilter> T setBooks(List<Book> books) {
    this.books = books;
    return (T) this;
  }

  /**
   * @return serialNumber
   */
  public Set<String> getSerialNumber() {
    return this.serialNumber;
  }

  /**
   * @param serialNumber serialNumber to set
   * @return BookInstanceFilter
   */
  public <T extends BookInstanceFilter> T setSerialNumber(Set<String> serialNumber) {
    this.serialNumber = serialNumber;
    return (T) this;
  }
}
