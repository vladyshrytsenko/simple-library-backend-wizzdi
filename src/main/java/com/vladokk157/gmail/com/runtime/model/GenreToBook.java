package com.vladokk157.gmail.com.runtime.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class GenreToBook extends Basic {

  @ManyToOne(targetEntity = Genre.class)
  private Genre genre;

  @ManyToOne(targetEntity = Book.class)
  private Book book;

  /**
   * @return genre
   */
  @ManyToOne(targetEntity = Genre.class)
  public Genre getGenre() {
    return this.genre;
  }

  /**
   * @param genre genre to set
   * @return GenreToBook
   */
  public <T extends GenreToBook> T setGenre(Genre genre) {
    this.genre = genre;
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
   * @return GenreToBook
   */
  public <T extends GenreToBook> T setBook(Book book) {
    this.book = book;
    return (T) this;
  }
}
