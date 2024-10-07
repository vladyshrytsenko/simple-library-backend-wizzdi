package com.vladokk157.gmail.com.runtime.data;

import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.model.Book_;
import com.vladokk157.gmail.com.runtime.model.Genre;
import com.vladokk157.gmail.com.runtime.model.GenreToBook;
import com.vladokk157.gmail.com.runtime.model.GenreToBook_;
import com.vladokk157.gmail.com.runtime.model.Genre_;
import com.vladokk157.gmail.com.runtime.request.GenreToBookFilter;
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
public class GenreToBookRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BasicRepository basicRepository;

  /**
   * @param genreToBookFilter Object Used to List GenreToBook
   * @param securityContext
   * @return List of GenreToBook
   */
  public List<GenreToBook> listAllGenreToBooks(
      GenreToBookFilter genreToBookFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<GenreToBook> q = cb.createQuery(GenreToBook.class);
    Root<GenreToBook> r = q.from(GenreToBook.class);
    List<Predicate> preds = new ArrayList<>();
    addGenreToBookPredicate(genreToBookFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<GenreToBook> query = em.createQuery(q);

    if (genreToBookFilter.getPageSize() != null
        && genreToBookFilter.getCurrentPage() != null
        && genreToBookFilter.getPageSize() > 0
        && genreToBookFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(genreToBookFilter.getPageSize() * genreToBookFilter.getCurrentPage())
          .setMaxResults(genreToBookFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends GenreToBook> void addGenreToBookPredicate(
      GenreToBookFilter genreToBookFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    basicRepository.addBasicPredicate(genreToBookFilter, cb, q, r, preds, securityContext);

    if (genreToBookFilter.getBooks() != null && !genreToBookFilter.getBooks().isEmpty()) {
      Set<String> ids =
          genreToBookFilter.getBooks().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Book> join = r.join(GenreToBook_.book);
      preds.add(join.get(Book_.id).in(ids));
    }

    if (genreToBookFilter.getGenres() != null && !genreToBookFilter.getGenres().isEmpty()) {
      Set<String> ids =
          genreToBookFilter.getGenres().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, Genre> join = r.join(GenreToBook_.genre);
      preds.add(join.get(Genre_.id).in(ids));
    }
  }

  /**
   * @param genreToBookFilter Object Used to List GenreToBook
   * @param securityContext
   * @return count of GenreToBook
   */
  public Long countAllGenreToBooks(
      GenreToBookFilter genreToBookFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<GenreToBook> r = q.from(GenreToBook.class);
    List<Predicate> preds = new ArrayList<>();
    addGenreToBookPredicate(genreToBookFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends GenreToBook, I> List<T> listByIds(
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

  public <T extends GenreToBook, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends GenreToBook, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends GenreToBook, I> T getByIdOrNull(
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
