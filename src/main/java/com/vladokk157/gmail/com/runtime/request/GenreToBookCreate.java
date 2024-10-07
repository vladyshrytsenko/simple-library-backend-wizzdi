package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.model.Genre;
import com.vladokk157.gmail.com.runtime.validation.Create;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import com.vladokk157.gmail.com.runtime.validation.Update;

/** Object Used to Create GenreToBook */
@IdValid.List({
  @IdValid(
      field = "bookId",
      fieldType = Book.class,
      targetField = "book",
      groups = {Update.class, Create.class}),
  @IdValid(
      field = "genreId",
      fieldType = Genre.class,
      targetField = "genre",
      groups = {Update.class, Create.class})
})
public class GenreToBookCreate extends BasicCreate {

  @JsonIgnore private Book book;

  private String bookId;

  @JsonIgnore private Genre genre;

  private String genreId;

  /**
   * @return book
   */
  @JsonIgnore
  public Book getBook() {
    return this.book;
  }

  /**
   * @param book book to set
   * @return GenreToBookCreate
   */
  public <T extends GenreToBookCreate> T setBook(Book book) {
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
   * @return GenreToBookCreate
   */
  public <T extends GenreToBookCreate> T setBookId(String bookId) {
    this.bookId = bookId;
    return (T) this;
  }

  /**
   * @return genre
   */
  @JsonIgnore
  public Genre getGenre() {
    return this.genre;
  }

  /**
   * @param genre genre to set
   * @return GenreToBookCreate
   */
  public <T extends GenreToBookCreate> T setGenre(Genre genre) {
    this.genre = genre;
    return (T) this;
  }

  /**
   * @return genreId
   */
  public String getGenreId() {
    return this.genreId;
  }

  /**
   * @param genreId genreId to set
   * @return GenreToBookCreate
   */
  public <T extends GenreToBookCreate> T setGenreId(String genreId) {
    this.genreId = genreId;
    return (T) this;
  }
}
