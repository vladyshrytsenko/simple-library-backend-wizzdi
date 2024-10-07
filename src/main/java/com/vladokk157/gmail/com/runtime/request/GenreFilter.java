package com.vladokk157.gmail.com.runtime.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladokk157.gmail.com.runtime.model.GenreToBook;
import com.vladokk157.gmail.com.runtime.validation.IdValid;
import java.util.List;
import java.util.Set;

/** Object Used to List Genre */
@IdValid.List({
  @IdValid(
      field = "genreGenreToBooksIds",
      fieldType = GenreToBook.class,
      targetField = "genreGenreToBookses")
})
public class GenreFilter extends BasicFilter {

  private Set<String> genreGenreToBooksIds;

  @JsonIgnore private List<GenreToBook> genreGenreToBookses;

  /**
   * @return genreGenreToBooksIds
   */
  public Set<String> getGenreGenreToBooksIds() {
    return this.genreGenreToBooksIds;
  }

  /**
   * @param genreGenreToBooksIds genreGenreToBooksIds to set
   * @return GenreFilter
   */
  public <T extends GenreFilter> T setGenreGenreToBooksIds(Set<String> genreGenreToBooksIds) {
    this.genreGenreToBooksIds = genreGenreToBooksIds;
    return (T) this;
  }

  /**
   * @return genreGenreToBookses
   */
  @JsonIgnore
  public List<GenreToBook> getGenreGenreToBookses() {
    return this.genreGenreToBookses;
  }

  /**
   * @param genreGenreToBookses genreGenreToBookses to set
   * @return GenreFilter
   */
  public <T extends GenreFilter> T setGenreGenreToBookses(List<GenreToBook> genreGenreToBookses) {
    this.genreGenreToBookses = genreGenreToBookses;
    return (T) this;
  }
}
