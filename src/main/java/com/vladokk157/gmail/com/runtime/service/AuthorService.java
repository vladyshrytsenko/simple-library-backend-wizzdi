package com.vladokk157.gmail.com.runtime.service;

import com.vladokk157.gmail.com.runtime.data.AuthorRepository;
import com.vladokk157.gmail.com.runtime.model.Author;
import com.vladokk157.gmail.com.runtime.model.Author_;
import com.vladokk157.gmail.com.runtime.request.AuthorCreate;
import com.vladokk157.gmail.com.runtime.request.AuthorFilter;
import com.vladokk157.gmail.com.runtime.request.AuthorUpdate;
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
public class AuthorService {

  @Autowired private AuthorRepository repository;

  @Autowired private PersonService personService;

  /**
   * @param authorCreate Object Used to Create Author
   * @param securityContext
   * @return created Author
   */
  public Author createAuthor(AuthorCreate authorCreate, UserSecurityContext securityContext) {
    Author author = createAuthorNoMerge(authorCreate, securityContext);
    author = this.repository.merge(author);
    return author;
  }

  /**
   * @param authorCreate Object Used to Create Author
   * @param securityContext
   * @return created Author unmerged
   */
  public Author createAuthorNoMerge(
      AuthorCreate authorCreate, UserSecurityContext securityContext) {
    Author author = new Author();
    author.setId(UUID.randomUUID().toString());
    updateAuthorNoMerge(author, authorCreate);

    return author;
  }

  /**
   * @param authorCreate Object Used to Create Author
   * @param author
   * @return if author was updated
   */
  public boolean updateAuthorNoMerge(Author author, AuthorCreate authorCreate) {
    boolean update = personService.updatePersonNoMerge(author, authorCreate);

    return update;
  }

  /**
   * @param authorUpdate
   * @param securityContext
   * @return author
   */
  public Author updateAuthor(AuthorUpdate authorUpdate, UserSecurityContext securityContext) {
    Author author = authorUpdate.getAuthor();
    if (updateAuthorNoMerge(author, authorUpdate)) {
      author = this.repository.merge(author);
    }
    return author;
  }

  /**
   * @param authorFilter Object Used to List Author
   * @param securityContext
   * @return PaginationResponse<Author> containing paging information for Author
   */
  public PaginationResponse<Author> getAllAuthors(
      AuthorFilter authorFilter, UserSecurityContext securityContext) {
    List<Author> list = listAllAuthors(authorFilter, securityContext);
    long count = this.repository.countAllAuthors(authorFilter, securityContext);
    return new PaginationResponse<>(list, authorFilter.getPageSize(), count);
  }

  /**
   * @param authorFilter Object Used to List Author
   * @param securityContext
   * @return List of Author
   */
  public List<Author> listAllAuthors(
      AuthorFilter authorFilter, UserSecurityContext securityContext) {
    return this.repository.listAllAuthors(authorFilter, securityContext);
  }

  public Author deleteAuthor(String id, UserSecurityContext securityContext) {
    Author author = this.repository.getByIdOrNull(Author.class, Author_.id, id, securityContext);
    ;
    if (author == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found");
    }

    this.repository.remove(author);

    return author;
  }

  public <T extends Author, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Author, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Author, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Author, I> List<T> listByIds(
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
