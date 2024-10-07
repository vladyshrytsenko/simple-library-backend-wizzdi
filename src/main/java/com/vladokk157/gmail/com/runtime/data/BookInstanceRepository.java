package com.vladokk157.gmail.com.runtime.data;

import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.model.BookInstance;
import com.vladokk157.gmail.com.runtime.model.BookInstance_;
import com.vladokk157.gmail.com.runtime.model.Book_;
import com.vladokk157.gmail.com.runtime.request.BookInstanceFilter;
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
public class BookInstanceRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BasicRepository basicRepository;

  /**
   * @param bookInstanceFilter Object Used to List BookInstance
   * @param securityContext
   * @return List of BookInstance
   */
  public List<BookInstance> listAllBookInstances(
      BookInstanceFilter bookInstanceFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<BookInstance> q = cb.createQuery(BookInstance.class);
    Root<BookInstance> r = q.from(BookInstance.class);
    List<Predicate> preds = new ArrayList<>();
    addBookInstancePredicate(bookInstanceFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<BookInstance> query = em.createQuery(q);

    if (bookInstanceFilter.getPageSize() != null
        && bookInstanceFilter.getCurrentPage() != null
        && bookInstanceFilter.getPageSize() > 0
        && bookInstanceFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(bookInstanceFilter.getPageSize() * bookInstanceFilter.getCurrentPage())
          .setMaxResults(bookInstanceFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends BookInstance> void addBookInstancePredicate(
      BookInstanceFilter bookInstanceFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    basicRepository.addBasicPredicate(bookInstanceFilter, cb, q, r, preds, securityContext);

    if (bookInstanceFilter.getBooks() != null && !bookInstanceFilter.getBooks().isEmpty()) {
      Set<String> ids =
          bookInstanceFilter.getBooks().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Book> join = r.join(BookInstance_.book);
      preds.add(join.get(Book_.id).in(ids));
    }

    if (bookInstanceFilter.getSerialNumber() != null
        && !bookInstanceFilter.getSerialNumber().isEmpty()) {
      preds.add(r.get(BookInstance_.serialNumber).in(bookInstanceFilter.getSerialNumber()));
    }

    if (bookInstanceFilter.getBlocked() != null && !bookInstanceFilter.getBlocked().isEmpty()) {
      preds.add(r.get(BookInstance_.blocked).in(bookInstanceFilter.getBlocked()));
    }
  }

  /**
   * @param bookInstanceFilter Object Used to List BookInstance
   * @param securityContext
   * @return count of BookInstance
   */
  public Long countAllBookInstances(
      BookInstanceFilter bookInstanceFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<BookInstance> r = q.from(BookInstance.class);
    List<Predicate> preds = new ArrayList<>();
    addBookInstancePredicate(bookInstanceFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends BookInstance, I> List<T> listByIds(
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

  public <T extends BookInstance, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends BookInstance, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends BookInstance, I> T getByIdOrNull(
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
