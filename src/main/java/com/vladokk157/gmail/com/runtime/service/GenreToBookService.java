package com.vladokk157.gmail.com.runtime.service;

import com.vladokk157.gmail.com.runtime.data.GenreToBookRepository;
import com.vladokk157.gmail.com.runtime.model.GenreToBook;
import com.vladokk157.gmail.com.runtime.model.GenreToBook_;
import com.vladokk157.gmail.com.runtime.request.GenreToBookCreate;
import com.vladokk157.gmail.com.runtime.request.GenreToBookFilter;
import com.vladokk157.gmail.com.runtime.request.GenreToBookUpdate;
import com.vladokk157.gmail.com.runtime.response.PaginationResponse;
import com.vladokk157.gmail.com.runtime.security.UserSecurityContext;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GenreToBookService {

  @Autowired private GenreToBookRepository repository;

  @Autowired private BasicService basicService;

  /**
   * @param genreToBookCreate Object Used to Create GenreToBook
   * @param securityContext
   * @return created GenreToBook
   */
  public GenreToBook createGenreToBook(
      GenreToBookCreate genreToBookCreate, UserSecurityContext securityContext) {
    GenreToBook genreToBook = createGenreToBookNoMerge(genreToBookCreate, securityContext);
    genreToBook = this.repository.merge(genreToBook);
    return genreToBook;
  }

  /**
   * @param genreToBookCreate Object Used to Create GenreToBook
   * @param securityContext
   * @return created GenreToBook unmerged
   */
  public GenreToBook createGenreToBookNoMerge(
      GenreToBookCreate genreToBookCreate, UserSecurityContext securityContext) {
    GenreToBook genreToBook = new GenreToBook();
    genreToBook.setId(UUID.randomUUID().toString());
    updateGenreToBookNoMerge(genreToBook, genreToBookCreate);

    return genreToBook;
  }

  /**
   * @param genreToBookCreate Object Used to Create GenreToBook
   * @param genreToBook
   * @return if genreToBook was updated
   */
  public boolean updateGenreToBookNoMerge(
      GenreToBook genreToBook, GenreToBookCreate genreToBookCreate) {
    boolean update = basicService.updateBasicNoMerge(genreToBook, genreToBookCreate);

    if (genreToBookCreate.getBook() != null
        && (genreToBook.getBook() == null
            || !genreToBookCreate.getBook().getId().equals(genreToBook.getBook().getId()))) {
      genreToBook.setBook(genreToBookCreate.getBook());
      update = true;
    }

    if (genreToBookCreate.getGenre() != null
        && (genreToBook.getGenre() == null
            || !genreToBookCreate.getGenre().getId().equals(genreToBook.getGenre().getId()))) {
      genreToBook.setGenre(genreToBookCreate.getGenre());
      update = true;
    }

    return update;
  }

  /**
   * @param genreToBookUpdate
   * @param securityContext
   * @return genreToBook
   */
  public GenreToBook updateGenreToBook(
      GenreToBookUpdate genreToBookUpdate, UserSecurityContext securityContext) {
    GenreToBook genreToBook = genreToBookUpdate.getGenreToBook();
    if (updateGenreToBookNoMerge(genreToBook, genreToBookUpdate)) {
      genreToBook = this.repository.merge(genreToBook);
    }
    return genreToBook;
  }

  /**
   * @param genreToBookFilter Object Used to List GenreToBook
   * @param securityContext
   * @return PaginationResponse<GenreToBook> containing paging information for GenreToBook
   */
  public PaginationResponse<GenreToBook> getAllGenreToBooks(
      GenreToBookFilter genreToBookFilter, UserSecurityContext securityContext) {
    List<GenreToBook> list = listAllGenreToBooks(genreToBookFilter, securityContext);
    long count = this.repository.countAllGenreToBooks(genreToBookFilter, securityContext);
    return new PaginationResponse<>(list, genreToBookFilter.getPageSize(), count);
  }

  /**
   * @param genreToBookFilter Object Used to List GenreToBook
   * @param securityContext
   * @return List of GenreToBook
   */
  public List<GenreToBook> listAllGenreToBooks(
      GenreToBookFilter genreToBookFilter, UserSecurityContext securityContext) {
    return this.repository.listAllGenreToBooks(genreToBookFilter, securityContext);
  }

  public GenreToBook deleteGenreToBook(String id, UserSecurityContext securityContext) {
    GenreToBook genreToBook =
        this.repository.getByIdOrNull(GenreToBook.class, GenreToBook_.id, id, securityContext);
    ;
    if (genreToBook == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GenreToBook not found");
    }

    this.repository.remove(genreToBook);

    return genreToBook;
  }

  public <T extends GenreToBook, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends GenreToBook, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends GenreToBook, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends GenreToBook, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return repository.listByIds(c, idField, ids);
  }

  public <T> T merge(T base) {
    return this.repository.merge(base);
  }

  public void massMerge(List<?> toMerge) {
    this.repository.massMerge(toMerge);
  }
}
