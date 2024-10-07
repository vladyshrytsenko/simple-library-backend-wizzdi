package com.vladokk157.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Book extends Basic {

  @OneToMany(targetEntity = AuthorToBook.class, mappedBy = "book")
  @JsonIgnore
  private List<AuthorToBook> bookAuthorToBooks;

  @OneToMany(targetEntity = BookInstance.class, mappedBy = "book")
  @JsonIgnore
  private List<BookInstance> bookBookInstances;

  @OneToMany(targetEntity = GenreToBook.class, mappedBy = "book")
  @JsonIgnore
  private List<GenreToBook> bookGenreToBooks;

  @ManyToOne(targetEntity = BookInstance.class)
  private BookInstance bookInstance;

  /**
   * @return bookAuthorToBooks
   */
  @OneToMany(targetEntity = AuthorToBook.class, mappedBy = "book")
  @JsonIgnore
  public List<AuthorToBook> getBookAuthorToBooks() {
    return this.bookAuthorToBooks;
  }

  /**
   * @param bookAuthorToBooks bookAuthorToBooks to set
   * @return Book
   */
  public <T extends Book> T setBookAuthorToBooks(List<AuthorToBook> bookAuthorToBooks) {
    this.bookAuthorToBooks = bookAuthorToBooks;
    return (T) this;
  }

  /**
   * @return bookBookInstances
   */
  @OneToMany(targetEntity = BookInstance.class, mappedBy = "book")
  @JsonIgnore
  public List<BookInstance> getBookBookInstances() {
    return this.bookBookInstances;
  }

  /**
   * @param bookBookInstances bookBookInstances to set
   * @return Book
   */
  public <T extends Book> T setBookBookInstances(List<BookInstance> bookBookInstances) {
    this.bookBookInstances = bookBookInstances;
    return (T) this;
  }

  /**
   * @return bookGenreToBooks
   */
  @OneToMany(targetEntity = GenreToBook.class, mappedBy = "book")
  @JsonIgnore
  public List<GenreToBook> getBookGenreToBooks() {
    return this.bookGenreToBooks;
  }

  /**
   * @param bookGenreToBooks bookGenreToBooks to set
   * @return Book
   */
  public <T extends Book> T setBookGenreToBooks(List<GenreToBook> bookGenreToBooks) {
    this.bookGenreToBooks = bookGenreToBooks;
    return (T) this;
  }

  /**
   * @return bookInstance
   */
  @ManyToOne(targetEntity = BookInstance.class)
  public BookInstance getBookInstance() {
    return this.bookInstance;
  }

  /**
   * @param bookInstance bookInstance to set
   * @return Book
   */
  public <T extends Book> T setBookInstance(BookInstance bookInstance) {
    this.bookInstance = bookInstance;
    return (T) this;
  }
}
