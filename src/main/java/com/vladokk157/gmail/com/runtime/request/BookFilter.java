package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.AuthorToBook;
import com.vladokk157.gmail.com.runtime.model.BookInstance;
import com.vladokk157.gmail.com.runtime.model.GenreToBook;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List Book */
@IdValid.List({
  @IdValid(
      field = "bookAuthorToBooksIds",
      fieldType = AuthorToBook.class,
      targetField = "bookAuthorToBookses"),
  @IdValid(
      field = "bookGenreToBooksIds",
      fieldType = GenreToBook.class,
      targetField = "bookGenreToBookses"),
  @IdValid(
      field = "bookBookInstancesIds",
      fieldType = BookInstance.class,
      targetField = "bookBookInstanceses"),
  @IdValid(field = "bookInstanceIds", fieldType = BookInstance.class, targetField = "bookInstances")
})
public class BookFilter extends BasicFilter {

  private Set<String> bookAuthorToBooksIds;

  @JsonIgnore private List<AuthorToBook> bookAuthorToBookses;

  private Set<String> bookBookInstancesIds;

  @JsonIgnore private List<BookInstance> bookBookInstanceses;

  private Set<String> bookGenreToBooksIds;

  @JsonIgnore private List<GenreToBook> bookGenreToBookses;

  private Set<String> bookInstanceIds;

  @JsonIgnore private List<BookInstance> bookInstances;

  /**
   * @return bookAuthorToBooksIds
   */
  public Set<String> getBookAuthorToBooksIds() {
    return this.bookAuthorToBooksIds;
  }

  /**
   * @param bookAuthorToBooksIds bookAuthorToBooksIds to set
   * @return BookFilter
   */
  public <T extends BookFilter> T setBookAuthorToBooksIds(Set<String> bookAuthorToBooksIds) {
    this.bookAuthorToBooksIds = bookAuthorToBooksIds;
    return (T) this;
  }

  /**
   * @return bookAuthorToBookses
   */
  @JsonIgnore
  public List<AuthorToBook> getBookAuthorToBookses() {
    return this.bookAuthorToBookses;
  }

  /**
   * @param bookAuthorToBookses bookAuthorToBookses to set
   * @return BookFilter
   */
  public <T extends BookFilter> T setBookAuthorToBookses(List<AuthorToBook> bookAuthorToBookses) {
    this.bookAuthorToBookses = bookAuthorToBookses;
    return (T) this;
  }

  /**
   * @return bookBookInstancesIds
   */
  public Set<String> getBookBookInstancesIds() {
    return this.bookBookInstancesIds;
  }

  /**
   * @param bookBookInstancesIds bookBookInstancesIds to set
   * @return BookFilter
   */
  public <T extends BookFilter> T setBookBookInstancesIds(Set<String> bookBookInstancesIds) {
    this.bookBookInstancesIds = bookBookInstancesIds;
    return (T) this;
  }

  /**
   * @return bookBookInstanceses
   */
  @JsonIgnore
  public List<BookInstance> getBookBookInstanceses() {
    return this.bookBookInstanceses;
  }

  /**
   * @param bookBookInstanceses bookBookInstanceses to set
   * @return BookFilter
   */
  public <T extends BookFilter> T setBookBookInstanceses(List<BookInstance> bookBookInstanceses) {
    this.bookBookInstanceses = bookBookInstanceses;
    return (T) this;
  }

  /**
   * @return bookGenreToBooksIds
   */
  public Set<String> getBookGenreToBooksIds() {
    return this.bookGenreToBooksIds;
  }

  /**
   * @param bookGenreToBooksIds bookGenreToBooksIds to set
   * @return BookFilter
   */
  public <T extends BookFilter> T setBookGenreToBooksIds(Set<String> bookGenreToBooksIds) {
    this.bookGenreToBooksIds = bookGenreToBooksIds;
    return (T) this;
  }

  /**
   * @return bookGenreToBookses
   */
  @JsonIgnore
  public List<GenreToBook> getBookGenreToBookses() {
    return this.bookGenreToBookses;
  }

  /**
   * @param bookGenreToBookses bookGenreToBookses to set
   * @return BookFilter
   */
  public <T extends BookFilter> T setBookGenreToBookses(List<GenreToBook> bookGenreToBookses) {
    this.bookGenreToBookses = bookGenreToBookses;
    return (T) this;
  }

  /**
   * @return bookInstanceIds
   */
  public Set<String> getBookInstanceIds() {
    return this.bookInstanceIds;
  }

  /**
   * @param bookInstanceIds bookInstanceIds to set
   * @return BookFilter
   */
  public <T extends BookFilter> T setBookInstanceIds(Set<String> bookInstanceIds) {
    this.bookInstanceIds = bookInstanceIds;
    return (T) this;
  }

  /**
   * @return bookInstances
   */
  @JsonIgnore
  public List<BookInstance> getBookInstances() {
    return this.bookInstances;
  }

  /**
   * @param bookInstances bookInstances to set
   * @return BookFilter
   */
  public <T extends BookFilter> T setBookInstances(List<BookInstance> bookInstances) {
    this.bookInstances = bookInstances;
    return (T) this;
  }
}
