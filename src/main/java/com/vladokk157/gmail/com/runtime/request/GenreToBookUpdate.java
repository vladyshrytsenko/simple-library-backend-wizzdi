package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.GenreToBook;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import com.vladokk157.gmail.com.runtime.validation.Update;

/** Object Used to Update GenreToBook */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = GenreToBook.class,
      targetField = "genreToBook",
      groups = {Update.class})
})
public class GenreToBookUpdate extends GenreToBookCreate {

  @JsonIgnore private GenreToBook genreToBook;

  private String id;

  /**
   * @return genreToBook
   */
  @JsonIgnore
  public GenreToBook getGenreToBook() {
    return this.genreToBook;
  }

  /**
   * @param genreToBook genreToBook to set
   * @return GenreToBookUpdate
   */
  public <T extends GenreToBookUpdate> T setGenreToBook(GenreToBook genreToBook) {
    this.genreToBook = genreToBook;
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
   * @return GenreToBookUpdate
   */
  public <T extends GenreToBookUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
