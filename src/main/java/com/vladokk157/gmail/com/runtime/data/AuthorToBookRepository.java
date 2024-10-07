package com.vladokk157.gmail.com.runtime.data;

import com.vladokk157.gmail.com.runtime.model.Author;
import com.vladokk157.gmail.com.runtime.model.AuthorToBook;
import com.vladokk157.gmail.com.runtime.model.AuthorToBook_;
import com.vladokk157.gmail.com.runtime.model.Author_;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.model.Book_;
import com.vladokk157.gmail.com.runtime.request.AuthorToBookFilter;
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
public class AuthorToBookRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BasicRepository basicRepository;

  /**
   * @param authorToBookFilter Object Used to List AuthorToBook
   * @param securityContext
   * @return List of AuthorToBook
   */
  public List<AuthorToBook> listAllAuthorToBooks(
      AuthorToBookFilter authorToBookFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<AuthorToBook> q = cb.createQuery(AuthorToBook.class);
    Root<AuthorToBook> r = q.from(AuthorToBook.class);
    List<Predicate> preds = new ArrayList<>();
    addAuthorToBookPredicate(authorToBookFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<AuthorToBook> query = em.createQuery(q);

    if (authorToBookFilter.getPageSize() != null
        && authorToBookFilter.getCurrentPage() != null
        && authorToBookFilter.getPageSize() > 0
        && authorToBookFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(authorToBookFilter.getPageSize() * authorToBookFilter.getCurrentPage())
          .setMaxResults(authorToBookFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends AuthorToBook> void addAuthorToBookPredicate(
      AuthorToBookFilter authorToBookFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    basicRepository.addBasicPredicate(authorToBookFilter, cb, q, r, preds, securityContext);

    if (authorToBookFilter.getBooks() != null && !authorToBookFilter.getBooks().isEmpty()) {
      Set<String> ids =
          authorToBookFilter.getBooks().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Book> join = r.join(AuthorToBook_.book);
      preds.add(join.get(Book_.id).in(ids));
    }

    if (authorToBookFilter.getAuthors() != null && !authorToBookFilter.getAuthors().isEmpty()) {
      Set<String> ids =
          authorToBookFilter.getAuthors().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Author> join = r.join(AuthorToBook_.author);
      preds.add(join.get(Author_.id).in(ids));
    }
  }

  /**
   * @param authorToBookFilter Object Used to List AuthorToBook
   * @param securityContext
   * @return count of AuthorToBook
   */
  public Long countAllAuthorToBooks(
      AuthorToBookFilter authorToBookFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<AuthorToBook> r = q.from(AuthorToBook.class);
    List<Predicate> preds = new ArrayList<>();
    addAuthorToBookPredicate(authorToBookFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends AuthorToBook, I> List<T> listByIds(
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

  public <T extends AuthorToBook, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends AuthorToBook, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends AuthorToBook, I> T getByIdOrNull(
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
