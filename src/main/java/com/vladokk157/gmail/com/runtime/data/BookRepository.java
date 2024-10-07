package com.vladokk157.gmail.com.runtime.data;

import com.vladokk157.gmail.com.runtime.model.AuthorToBook;
import com.vladokk157.gmail.com.runtime.model.AuthorToBook_;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.model.BookInstance;
import com.vladokk157.gmail.com.runtime.model.BookInstance_;
import com.vladokk157.gmail.com.runtime.model.Book_;
import com.vladokk157.gmail.com.runtime.model.GenreToBook;
import com.vladokk157.gmail.com.runtime.model.GenreToBook_;
import com.vladokk157.gmail.com.runtime.request.BookFilter;
import com.vladokk157.gmail.com.runtime.security.UserSecurityContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BookRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BasicRepository basicRepository;

  /**
   * @param bookFilter Object Used to List Book
   * @param securityContext
   * @return List of Book
   */
  public List<Book> listAllBooks(BookFilter bookFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Book> q = cb.createQuery(Book.class);
    Root<Book> r = q.from(Book.class);
    List<Predicate> preds = new ArrayList<>();
    addBookPredicate(bookFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Book> query = em.createQuery(q);

    if (bookFilter.getPageSize() != null
        && bookFilter.getCurrentPage() != null
        && bookFilter.getPageSize() > 0
        && bookFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(bookFilter.getPageSize() * bookFilter.getCurrentPage())
          .setMaxResults(bookFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Book> void addBookPredicate(
      BookFilter bookFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    basicRepository.addBasicPredicate(bookFilter, cb, q, r, preds, securityContext);

    if (bookFilter.getBookInstances() != null && !bookFilter.getBookInstances().isEmpty()) {
      Set<String> ids =
          bookFilter.getBookInstances().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, BookInstance> join = r.join(Book_.bookInstance);
      preds.add(join.get(BookInstance_.id).in(ids));
    }

    if (bookFilter.getBookGenreToBookses() != null
        && !bookFilter.getBookGenreToBookses().isEmpty()) {
      Set<String> ids =
          bookFilter.getBookGenreToBookses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, GenreToBook> join = r.join(Book_.bookGenreToBooks);
      preds.add(join.get(GenreToBook_.id).in(ids));
    }

    if (bookFilter.getBookBookInstanceses() != null
        && !bookFilter.getBookBookInstanceses().isEmpty()) {
      Set<String> ids =
          bookFilter.getBookBookInstanceses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, BookInstance> join = r.join(Book_.bookBookInstances);
      preds.add(join.get(BookInstance_.id).in(ids));
    }

    if (bookFilter.getBookAuthorToBookses() != null
        && !bookFilter.getBookAuthorToBookses().isEmpty()) {
      Set<String> ids =
          bookFilter.getBookAuthorToBookses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, AuthorToBook> join = r.join(Book_.bookAuthorToBooks);
      preds.add(join.get(AuthorToBook_.id).in(ids));
    }
  }

  /**
   * @param bookFilter Object Used to List Book
   * @param securityContext
   * @return count of Book
   */
  public Long countAllBooks(BookFilter bookFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Book> r = q.from(Book.class);
    List<Predicate> preds = new ArrayList<>();
    addBookPredicate(bookFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Book, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(c);
    Root<T> r = q.from(c);
    List<Predicate> preds = new ArrayList<>();
    preds.add(r.get(idField).in(ids));

    q.select(r).where(preds.toArray(new Predicate[0]));
    return em.createQuery(q).getResultList();
  }

  public <T extends Book, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Book, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Book, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return listByIds(c, idField, Collections.singleton(id), securityContext).stream()
        .findFirst()
        .orElse(null);
  }

  @Transactional
  public <T> T merge(T base) {
    T updated = em.merge(base);
    applicationEventPublisher.publishEvent(updated);
    return updated;
  }

  @Transactional
  public void massMerge(List<?> toMerge) {
    for (Object o : toMerge) {
      java.lang.Object updated = em.merge(o);
      applicationEventPublisher.publishEvent(updated);
    }
  }
}
