package com.vladokk157.gmail.com.runtime.service;

import com.vladokk157.gmail.com.runtime.data.PersonRepository;
import com.vladokk157.gmail.com.runtime.model.Person;
import com.vladokk157.gmail.com.runtime.model.Person_;
import com.vladokk157.gmail.com.runtime.request.PersonCreate;
import com.vladokk157.gmail.com.runtime.request.PersonFilter;
import com.vladokk157.gmail.com.runtime.request.PersonUpdate;
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
public class PersonService {

  @Autowired private PersonRepository repository;

  @Autowired private BasicService basicService;

  /**
   * @param personCreate Object Used to Create Person
   * @param securityContext
   * @return created Person
   */
  public Person createPerson(PersonCreate personCreate, UserSecurityContext securityContext) {
    Person person = createPersonNoMerge(personCreate, securityContext);
    person = this.repository.merge(person);
    return person;
  }

  /**
   * @param personCreate Object Used to Create Person
   * @param securityContext
   * @return created Person unmerged
   */
  public Person createPersonNoMerge(
      PersonCreate personCreate, UserSecurityContext securityContext) {
    Person person = new Person();
    person.setId(UUID.randomUUID().toString());
    updatePersonNoMerge(person, personCreate);

    return person;
  }

  /**
   * @param personCreate Object Used to Create Person
   * @param person
   * @return if person was updated
   */
  public boolean updatePersonNoMerge(Person person, PersonCreate personCreate) {
    boolean update = basicService.updateBasicNoMerge(person, personCreate);

    if (personCreate.getSocialSecurityNumber() != null
        && (!personCreate.getSocialSecurityNumber().equals(person.getSocialSecurityNumber()))) {
      person.setSocialSecurityNumber(personCreate.getSocialSecurityNumber());
      update = true;
    }

    if (personCreate.getPhoneNumber() != null
        && (!personCreate.getPhoneNumber().equals(person.getPhoneNumber()))) {
      person.setPhoneNumber(personCreate.getPhoneNumber());
      update = true;
    }

    if (personCreate.getEmail() != null && (!personCreate.getEmail().equals(person.getEmail()))) {
      person.setEmail(personCreate.getEmail());
      update = true;
    }

    if (personCreate.getAddress() != null
        && (!personCreate.getAddress().equals(person.getAddress()))) {
      person.setAddress(personCreate.getAddress());
      update = true;
    }

    if (personCreate.getBirthDate() != null
        && (!personCreate.getBirthDate().equals(person.getBirthDate()))) {
      person.setBirthDate(personCreate.getBirthDate());
      update = true;
    }

    return update;
  }

  /**
   * @param personUpdate
   * @param securityContext
   * @return person
   */
  public Person updatePerson(PersonUpdate personUpdate, UserSecurityContext securityContext) {
    Person person = personUpdate.getPerson();
    if (updatePersonNoMerge(person, personUpdate)) {
      person = this.repository.merge(person);
    }
    return person;
  }

  /**
   * @param personFilter Object Used to List Person
   * @param securityContext
   * @return PaginationResponse<Person> containing paging information for Person
   */
  public PaginationResponse<Person> getAllPersons(
      PersonFilter personFilter, UserSecurityContext securityContext) {
    List<Person> list = listAllPersons(personFilter, securityContext);
    long count = this.repository.countAllPersons(personFilter, securityContext);
    return new PaginationResponse<>(list, personFilter.getPageSize(), count);
  }

  /**
   * @param personFilter Object Used to List Person
   * @param securityContext
   * @return List of Person
   */
  public List<Person> listAllPersons(
      PersonFilter personFilter, UserSecurityContext securityContext) {
    return this.repository.listAllPersons(personFilter, securityContext);
  }

  public Person deletePerson(String id, UserSecurityContext securityContext) {
    Person person = this.repository.getByIdOrNull(Person.class, Person_.id, id, securityContext);
    ;
    if (person == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person not found");
    }

    this.repository.remove(person);

    return person;
  }

  public <T extends Person, I> List<T> listByIds(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      Set<I> ids,
      UserSecurityContext securityContext) {
    return repository.listByIds(c, idField, ids, securityContext);
  }

  public <T extends Person, I> T getByIdOrNull(
      Class<T> c,
      SingularAttribute<? super T, I> idField,
      I id,
      UserSecurityContext securityContext) {
    return repository.getByIdOrNull(c, idField, id, securityContext);
  }

  public <T extends Person, I> T getByIdOrNull(
      Class<T> c, SingularAttribute<? super T, I> idField, I id) {
    return repository.getByIdOrNull(c, idField, id);
  }

  public <T extends Person, I> List<T> listByIds(
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
