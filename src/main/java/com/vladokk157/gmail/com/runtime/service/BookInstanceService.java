package com.vladokk157.gmail.com.runtime.service;

import com.vladokk157.gmail.com.runtime.data.BookInstanceRepository;
import com.vladokk157.gmail.com.runtime.model.BookInstance;
import com.vladokk157.gmail.com.runtime.model.BookInstance_;
import com.vladokk157.gmail.com.runtime.request.BookInstanceCreate;
import com.vladokk157.gmail.com.runtime.request.BookInstanceFilter;
import com.vladokk157.gmail.com.runtime.request.BookInstanceUpdate;
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
public class BookInstanceService {

  @Autowired private BookInstanceRepository repository;

  @Autowired private BasicService basicService;

  /**
   * @param bookInstanceCreate Object Used to Create BookInstance
   * @param securityContext
   * @return created BookInstance
   */
  public BookInstance createBookInstance(
      BookInstanceCreate bookInstanceCreate, UserSecurityContext securityContext) {
    BookInstance bookInstance = createBookInstanceNoMerge(bookInstanceCreate, securityContext);
    bookInstance = this.repository.merge(bookInstance);
    return bookInstance;
  }

  /**
   * @param bookInstanceCreate Object Used to Create BookInstance
   * @param securityContext
   * @return created BookInstance unmerged
   */
  public BookInstance createBookInstanceNoMerge(
      BookInstanceCreate bookInstanceCreate, UserSecurityContext securityContext) {
    BookInstance bookInstance = new BookInstance();
    bookInstance.setId(UUID.randomUUID().toString());
    updateBookInstanceNoMerge(bookInstance, bookInstanceCreate);

    return bookInstance;
  }

  /**
   * @param bookInstanceCreate Object Used to Create BookInstance
   * @param bookInstance
   * @return if bookInstance was updated
   */
  public boolean updateBookInstanceNoMerge(
      BookInstance bookInstance, BookInstanceCreate bookInstanceCreate) {
    boolean update = basicService.updateBasicNoMerge(bookInstance, bookInstanceCreate);

    if (bookInstanceCreate.getBook() != null
        && (bookInstance.getBook() == null
            || !bookInstanceCreate.getBook().getId().equals(bookInstance.getBook().getId()))) {
      bookInstance.setBook(bookInstanceCreate.getBook());
      update = true;
    }

    if (bookInstanceCreate.getSerialNumber() != null
        && (!bookInstanceCreate.getSerialNumber().equals(bookInstance.getSerialNumber()))) {
      bookInstance.setSerialNumber(bookInstanceCreate.getSerialNumber());
      update = true;
    }

    if (bookInstanceCreate.getBlocked() != null
        && (!bookInstanceCreate.getBlocked().equals(bookInstance.isBlocked()))) {
      bookInstance.setBlocked(bookInstanceCreate.getBlocked());
      update = true;
    }

    return update;
  }

  /**
   * @param bookInstanceUpdate
   * @param securityContext
   * @return bookInstance
   */
  public BookInstance updateBookInstance(
      BookInstanceUpdate bookInstanceUpdate, UserSecurityContext securityContext) {
    BookInstance bookInstance = bookInstanceUpdate.getBookInstance();
    if (updateBookInstanceNoMerge(bookInstance, bookInstanceUpdate)) {
      bookInstance = this.repository.merge(bookInstance);
    }
    return bookInstance;
  }

  /**
   * @param bookInstanceFilter Object Used to List BookInstance
   * @param securityContext
   * @return PaginationResponse<BookInstance> containing paging information for BookInstance
   */
  public PaginationResponse<BookInstance> getAllBookInstances(
      BookInstanceFilter bookInstanceFilter, UserSecurityContext securityContext) {
    List<BookInstance> list = listAllBookInstances(bookInstanceFilter, securityContext);
    long count = this.repository.countAllBookInstances(bookInstanceFilter, securityContext);
    return new PaginationResponse<>(list, bookInstanceFilter.getPageSize(), count);
  }

  /**
   * @param bookInstanceFilter Object Used to List BookInstance
   * @param securityContext
   * @return List of BookInstance
   */
  public List<BookInstance> listAllBookInstances(
      BookInstanceFilter bookInstanceFilter, UserSecurityContext securityContext) {
    return this.repository.listAllBookInstances(bookInstanceFilter, securityContext);
  }

  public BookInstance deleteBookInstance(String id, UserSecurityContext securityContext) {
    BookInstance bookInstance =
        this.repository.getByIdOrNull(BookInstance.class, BookInstance_.id, id, securityContext);
    ;
    if (bookInstance == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookInstance not found");
    }

    this.repository.remove(bookInstance);

    return bookInstance;
  }

  public <T extends BookInstance, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends BookInstance, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends BookInstance, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends BookInstance, I> List<T> listByIds(
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
