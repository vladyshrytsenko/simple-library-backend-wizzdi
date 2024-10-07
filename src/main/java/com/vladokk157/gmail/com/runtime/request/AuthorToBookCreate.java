package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.Author;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.validation.Create;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import com.vladokk157.gmail.com.runtime.validation.Update;

/** Object Used to Create AuthorToBook */
@IdValid.List({
  @IdValid(
      field = "authorId",
      fieldType = Author.class,
      targetField = "author",
      groups = {Create.class, Update.class}),
  @IdValid(
      field = "bookId",
      fieldType = Book.class,
      targetField = "book",
      groups = {Create.class, Update.class})
})
public class AuthorToBookCreate extends BasicCreate {

  @JsonIgnore private Author author;

  private String authorId;

  @JsonIgnore private Book book;

  private String bookId;

  /**
   * @return author
   */
  @JsonIgnore
  public Author getAuthor() {
    return this.author;
  }

  /**
   * @param author author to set
   * @return AuthorToBookCreate
   */
  public <T extends AuthorToBookCreate> T setAuthor(Author author) {
    this.author = author;
    return (T) this;
  }

  /**
   * @return authorId
   */
  public String getAuthorId() {
    return this.authorId;
  }

  /**
   * @param authorId authorId to set
   * @return AuthorToBookCreate
   */
  public <T extends AuthorToBookCreate> T setAuthorId(String authorId) {
    this.authorId = authorId;
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
   * @return AuthorToBookCreate
   */
  public <T extends AuthorToBookCreate> T setBook(Book book) {
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
   * @return AuthorToBookCreate
   */
  public <T extends AuthorToBookCreate> T setBookId(String bookId) {
    this.bookId = bookId;
    return (T) this;
  }
}
