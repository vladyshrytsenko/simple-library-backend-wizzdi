package com.vladokk157.gmail.com.runtime.service;

import com.vladokk157.gmail.com.runtime.data.AuthorToBookRepository;
import com.vladokk157.gmail.com.runtime.model.AuthorToBook;
import com.vladokk157.gmail.com.runtime.model.AuthorToBook_;
import com.vladokk157.gmail.com.runtime.request.AuthorToBookCreate;
import com.vladokk157.gmail.com.runtime.request.AuthorToBookFilter;
import com.vladokk157.gmail.com.runtime.request.AuthorToBookUpdate;
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
public class AuthorToBookService {

  @Autowired private AuthorToBookRepository repository;

  @Autowired private BasicService basicService;

  /**
   * @param authorToBookCreate Object Used to Create AuthorToBook
   * @param securityContext
   * @return created AuthorToBook
   */
  public AuthorToBook createAuthorToBook(
      AuthorToBookCreate authorToBookCreate, UserSecurityContext securityContext) {
    AuthorToBook authorToBook = createAuthorToBookNoMerge(authorToBookCreate, securityContext);
    authorToBook = this.repository.merge(authorToBook);
    return authorToBook;
  }

  /**
   * @param authorToBookCreate Object Used to Create AuthorToBook
   * @param securityContext
   * @return created AuthorToBook unmerged
   */
  public AuthorToBook createAuthorToBookNoMerge(
      AuthorToBookCreate authorToBookCreate, UserSecurityContext securityContext) {
    AuthorToBook authorToBook = new AuthorToBook();
    authorToBook.setId(UUID.randomUUID().toString());
    updateAuthorToBookNoMerge(authorToBook, authorToBookCreate);

    return authorToBook;
  }

  /**
   * @param authorToBookCreate Object Used to Create AuthorToBook
   * @param authorToBook
   * @return if authorToBook was updated
   */
  public boolean updateAuthorToBookNoMerge(
      AuthorToBook authorToBook, AuthorToBookCreate authorToBookCreate) {
    boolean update = basicService.updateBasicNoMerge(authorToBook, authorToBookCreate);

    if (authorToBookCreate.getBook() != null
        && (authorToBook.getBook() == null
            || !authorToBookCreate.getBook().getId().equals(authorToBook.getBook().getId()))) {
      authorToBook.setBook(authorToBookCreate.getBook());
      update = true;
    }

    if (authorToBookCreate.getAuthor() != null
        && (authorToBook.getAuthor() == null
            || !authorToBookCreate.getAuthor().getId().equals(authorToBook.getAuthor().getId()))) {
      authorToBook.setAuthor(authorToBookCreate.getAuthor());
      update = true;
    }

    return update;
  }

  /**
   * @param authorToBookUpdate
   * @param securityContext
   * @return authorToBook
   */
  public AuthorToBook updateAuthorToBook(
      AuthorToBookUpdate authorToBookUpdate, UserSecurityContext securityContext) {
    AuthorToBook authorToBook = authorToBookUpdate.getAuthorToBook();
    if (updateAuthorToBookNoMerge(authorToBook, authorToBookUpdate)) {
      authorToBook = this.repository.merge(authorToBook);
    }
    return authorToBook;
  }

  /**
   * @param authorToBookFilter Object Used to List AuthorToBook
   * @param securityContext
   * @return PaginationResponse<AuthorToBook> containing paging information for AuthorToBook
   */
  public PaginationResponse<AuthorToBook> getAllAuthorToBooks(
      AuthorToBookFilter authorToBookFilter, UserSecurityContext securityContext) {
    List<AuthorToBook> list = listAllAuthorToBooks(authorToBookFilter, securityContext);
    long count = this.repository.countAllAuthorToBooks(authorToBookFilter, securityContext);
    return new PaginationResponse<>(list, authorToBookFilter.getPageSize(), count);
  }

  /**
   * @param authorToBookFilter Object Used to List AuthorToBook
   * @param securityContext
   * @return List of AuthorToBook
   */
  public List<AuthorToBook> listAllAuthorToBooks(
      AuthorToBookFilter authorToBookFilter, UserSecurityContext securityContext) {
    return this.repository.listAllAuthorToBooks(authorToBookFilter, securityContext);
  }

  public AuthorToBook deleteAuthorToBook(String id, UserSecurityContext securityContext) {
    AuthorToBook authorToBook =
        this.repository.getByIdOrNull(AuthorToBook.class, AuthorToBook_.id, id, securityContext);
    ;
    if (authorToBook == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AuthorToBook not found");
    }

    this.repository.remove(authorToBook);

    return authorToBook;
  }

  public <T extends AuthorToBook, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends AuthorToBook, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends AuthorToBook, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends AuthorToBook, I> List<T> listByIds(
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
