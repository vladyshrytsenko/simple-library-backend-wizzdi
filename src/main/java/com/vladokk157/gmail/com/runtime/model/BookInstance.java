package com.vladokk157.gmail.com.runtime.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class BookInstance extends Basic {

  private String serialNumber;

  private boolean blocked;

  @ManyToOne(targetEntity = Book.class)
  private Book book;

  /**
   * @return serialNumber
   */
  public String getSerialNumber() {
    return this.serialNumber;
  }

  /**
   * @param serialNumber serialNumber to set
   * @return BookInstance
   */
  public <T extends BookInstance> T setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
    return (T) this;
  }

  /**
   * @return blocked
   */
  public boolean isBlocked() {
    return this.blocked;
  }

  /**
   * @param blocked blocked to set
   * @return BookInstance
   */
  public <T extends BookInstance> T setBlocked(boolean blocked) {
    this.blocked = blocked;
    return (T) this;
  }

  /**
   * @return book
   */
  @ManyToOne(targetEntity = Book.class)
  public Book getBook() {
    return this.book;
  }

  /**
   * @param book book to set
   * @return BookInstance
   */
  public <T extends BookInstance> T setBook(Book book) {
    this.book = book;
    return (T) this;
  }
}
