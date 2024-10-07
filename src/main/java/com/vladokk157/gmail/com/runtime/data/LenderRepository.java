package com.vladokk157.gmail.com.runtime.data;

import com.vladokk157.gmail.com.runtime.model.Lender;
import com.vladokk157.gmail.com.runtime.model.Lender_;
import com.vladokk157.gmail.com.runtime.request.LenderFilter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LenderRepository {
  @PersistenceContext private EntityManager em;

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private PersonRepository personRepository;

  /**
   * @param lenderFilter Object Used to List Lender
   * @param securityContext
   * @return List of Lender
   */
  public List<Lender> listAllLenders(
      LenderFilter lenderFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Lender> q = cb.createQuery(Lender.class);
    Root<Lender> r = q.from(Lender.class);
    List<Predicate> preds = new ArrayList<>();
    addLenderPredicate(lenderFilter, cb, q, r, preds, securityContext);
    q.select(r).where(preds.toArray(new Predicate[0]));
    TypedQuery<Lender> query = em.createQuery(q);

    if (lenderFilter.getPageSize() != null
        && lenderFilter.getCurrentPage() != null
        && lenderFilter.getPageSize() > 0
        && lenderFilter.getCurrentPage() > -1) {
      query
          .setFirstResult(lenderFilter.getPageSize() * lenderFilter.getCurrentPage())
          .setMaxResults(lenderFilter.getPageSize());
    }

    return query.getResultList();
  }

  public <T extends Lender> void addLenderPredicate(
      LenderFilter lenderFilter,
      CriteriaBuilder cb,
      CommonAbstractCriteria q,
      From<?, T> r,
      List<Predicate> preds,
      UserSecurityContext securityContext) {

    personRepository.addPersonPredicate(lenderFilter, cb, q, r, preds, securityContext);

    if (lenderFilter.getBlocked() != null && !lenderFilter.getBlocked().isEmpty()) {
      preds.add(r.get(Lender_.blocked).in(lenderFilter.getBlocked()));
    }
  }

  /**
   * @param lenderFilter Object Used to List Lender
   * @param securityContext
   * @return count of Lender
   */
  public Long countAllLenders(LenderFilter lenderFilter, UserSecurityContext securityContext) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> q = cb.createQuery(Long.class);
    Root<Lender> r = q.from(Lender.class);
    List<Predicate> preds = new ArrayList<>();
    addLenderPredicate(lenderFilter, cb, q, r, preds, securityContext);
    q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
    TypedQuery<Long> query = em.createQuery(q);
    return query.getSingleResult();
  }

  public void remove(Object o) {
    em.remove(o);
  }

  public <T extends Lender, I> List<T> listByIds(
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

  public <T extends Lender, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return getByIdOrNull(c, idField, id, null);
  }

  public <T extends Lender, I> List<T> listByIds(
      Class<T> c, SingularAttribute<? super T, I> idField, Set<I> ids) {
    return listByIds(c, idField, ids, null);
  }

  public <T extends Lender, I> T getByIdOrNull(
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
