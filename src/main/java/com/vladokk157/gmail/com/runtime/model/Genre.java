package com.vladokk157.gmail.com.runtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Genre extends Basic {

  @OneToMany(targetEntity = GenreToBook.class, mappedBy = "genre")
  @JsonIgnore
  private List<GenreToBook> genreGenreToBooks;

  /**
   * @return genreGenreToBooks
   */
  @OneToMany(targetEntity = GenreToBook.class, mappedBy = "genre")
  @JsonIgnore
  public List<GenreToBook> getGenreGenreToBooks() {
    return this.genreGenreToBooks;
  }

  /**
   * @param genreGenreToBooks genreGenreToBooks to set
   * @return Genre
   */
  public <T extends Genre> T setGenreGenreToBooks(List<GenreToBook> genreGenreToBooks) {
    this.genreGenreToBooks = genreGenreToBooks;
    return (T) this;
  }
}
