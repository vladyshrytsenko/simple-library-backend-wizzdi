package com.vladokk157.gmail.com.runtime.data;

import com.vladokk157.gmail.com.runtime.model.Genre;
import com.vladokk157.gmail.com.runtime.model.GenreToBook;
import com.vladokk157.gmail.com.runtime.model.GenreToBook_;
import com.vladokk157.gmail.com.runtime.model.Genre_;
import com.vladokk157.gmail.com.runtime.request.GenreFilter;
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
public class GenreRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private BasicRepository basicRepository;

  /**
   * @param genreFilter Object Used to List Genre
   * @param securityContext
   * @return List of Genre
   */
  public List<Genre> listAllGenres(GenreFilter genreFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Genre> q = cb.createQuery(Genre.class);
    Root<Genre> r = q.from(Genre.class);
    List<Predicate> preds = new ArrayList<>();
    addGenrePredicate(genreFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Genre> query = em.createQuery(q);

    if (genreFilter.getPageSize() != null
        && genreFilter.getCurrentPage() != null
        && genreFilter.getPageSize() > 0
        && genreFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(genreFilter.getPageSize() * genreFilter.getCurrentPage())
          .setMaxResults(genreFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Genre> void addGenrePredicate(
      GenreFilter genreFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    basicRepository.addBasicPredicate(genreFilter, cb, q, r, preds, securityContext);

    if (genreFilter.getGenreGenreToBookses() != null
        && !genreFilter.getGenreGenreToBookses().isEmpty()) {
      Set<String> ids =
          genreFilter.getGenreGenreToBookses().parallelStream()
              .map(f -> f.getId())
              .collect(Collectors.toSet());
      Join<T, GenreToBook> join = r.join(Genre_.genreGenreToBooks);
      preds.add(join.get(GenreToBook_.id).in(ids));
    }
  }

  /**
   * @param genreFilter Object Used to List Genre
   * @param securityContext
   * @return count of Genre
   */
  public Long countAllGenres(GenreFilter genreFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Genre> r = q.from(Genre.class);
    List<Predicate> preds = new ArrayList<>();
    addGenrePredicate(genreFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Genre, I> List<T> listByIds(
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

  public <T extends Genre, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Genre, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Genre, I> T getByIdOrNull(
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
