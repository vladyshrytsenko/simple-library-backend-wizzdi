package com.vladokk157.gmail.com.runtime.service;

import com.vladokk157.gmail.com.runtime.data.LenderRepository;
import com.vladokk157.gmail.com.runtime.model.Lender;
import com.vladokk157.gmail.com.runtime.model.Lender_;
import com.vladokk157.gmail.com.runtime.request.LenderCreate;
import com.vladokk157.gmail.com.runtime.request.LenderFilter;
import com.vladokk157.gmail.com.runtime.request.LenderUpdate;
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
public class LenderService {

  @Autowired private LenderRepository repository;

  @Autowired private PersonService personService;

  /**
   * @param lenderCreate Object Used to Create Lender
   * @param securityContext
   * @return created Lender
   */
  public Lender createLender(LenderCreate lenderCreate, UserSecurityContext securityContext) {
    Lender lender = createLenderNoMerge(lenderCreate, securityContext);
    lender = this.repository.merge(lender);
    return lender;
  }

  /**
   * @param lenderCreate Object Used to Create Lender
   * @param securityContext
   * @return created Lender unmerged
   */
  public Lender createLenderNoMerge(
      LenderCreate lenderCreate, UserSecurityContext securityContext) {
    Lender lender = new Lender();
    lender.setId(UUID.randomUUID().toString());
    updateLenderNoMerge(lender, lenderCreate);

    return lender;
  }

  /**
   * @param lenderCreate Object Used to Create Lender
   * @param lender
   * @return if lender was updated
   */
  public boolean updateLenderNoMerge(Lender lender, LenderCreate lenderCreate) {
    boolean update = personService.updatePersonNoMerge(lender, lenderCreate);

    if (lenderCreate.getBlocked() != null
        && (!lenderCreate.getBlocked().equals(lender.isBlocked()))) {
      lender.setBlocked(lenderCreate.getBlocked());
      update = true;
    }

    return update;
  }

  /**
   * @param lenderUpdate
   * @param securityContext
   * @return lender
   */
  public Lender updateLender(LenderUpdate lenderUpdate, UserSecurityContext securityContext) {
    Lender lender = lenderUpdate.getLender();
    if (updateLenderNoMerge(lender, lenderUpdate)) {
      lender = this.repository.merge(lender);
    }
    return lender;
  }

  /**
   * @param lenderFilter Object Used to List Lender
   * @param securityContext
   * @return PaginationResponse<Lender> containing paging information for Lender
   */
  public PaginationResponse<Lender> getAllLenders(
      LenderFilter lenderFilter, UserSecurityContext securityContext) {
    List<Lender> list = listAllLenders(lenderFilter, securityContext);
    long count = this.repository.countAllLenders(lenderFilter, securityContext);
    return new PaginationResponse<>(list, lenderFilter.getPageSize(), count);
  }

  /**
   * @param lenderFilter Object Used to List Lender
   * @param securityContext
   * @return List of Lender
   */
  public List<Lender> listAllLenders(
      LenderFilter lenderFilter, UserSecurityContext securityContext) {
    return this.repository.listAllLenders(lenderFilter, securityContext);
  }

  public Lender deleteLender(String id, UserSecurityContext securityContext) {
    Lender lender = this.repository.getByIdOrNull(Lender.class, Lender_.id, id, securityContext);
    ;
    if (lender == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lender not found");
    }

    this.repository.remove(lender);

    return lender;
  }

  public <T extends Lender, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Lender, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Lender, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Lender, I> List<T> listByIds(
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
