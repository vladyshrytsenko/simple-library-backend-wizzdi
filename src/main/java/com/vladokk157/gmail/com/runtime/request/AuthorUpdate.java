package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.Author;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import com.vladokk157.gmail.com.runtime.validation.Update;

/** Object Used to Update Author */
@IdValid.List({
  @IdValid(
      field = "id",
      fieldType = Author.class,
      targetField = "author",
      groups = {Update.class})
})
public class AuthorUpdate extends AuthorCreate {

  @JsonIgnore private Author author;

  private String id;

  /**
   * @return author
   */
  @JsonIgnore
  public Author getAuthor() {
    return this.author;
  }

  /**
   * @param author author to set
   * @return AuthorUpdate
   */
  public <T extends AuthorUpdate> T setAuthor(Author author) {
    this.author = author;
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
   * @return AuthorUpdate
   */
  public <T extends AuthorUpdate> T setId(String id) {
    this.id = id;
    return (T) this;
  }
}
