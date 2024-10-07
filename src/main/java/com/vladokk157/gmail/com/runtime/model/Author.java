package com.vladokk157.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Author extends Person {

  @OneToMany(targetEntity = AuthorToBook.class, mappedBy = "author")
  @JsonIgnore
  private List<AuthorToBook> authorAuthorToBooks;

  /**
   * @return authorAuthorToBooks
   */
  @OneToMany(targetEntity = AuthorToBook.class, mappedBy = "author")
  @JsonIgnore
  public List<AuthorToBook> getAuthorAuthorToBooks() {
    return this.authorAuthorToBooks;
  }

  /**
   * @param authorAuthorToBooks authorAuthorToBooks to set
   * @return Author
   */
  public <T extends Author> T setAuthorAuthorToBooks(List<AuthorToBook> authorAuthorToBooks) {
    this.authorAuthorToBooks = authorAuthorToBooks;
    return (T) this;
  }
}
