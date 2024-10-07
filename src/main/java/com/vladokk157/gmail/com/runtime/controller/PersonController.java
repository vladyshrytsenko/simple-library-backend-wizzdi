package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.model.Person;
import com.vladokk157.gmail.com.runtime.request.PersonCreate;
import com.vladokk157.gmail.com.runtime.request.PersonFilter;
import com.vladokk157.gmail.com.runtime.request.PersonUpdate;
import com.vladokk157.gmail.com.runtime.response.PaginationResponse;
import com.vladokk157.gmail.com.runtime.security.UserSecurityContext;
import com.vladokk157.gmail.com.runtime.service.PersonService;
import com.vladokk157.gmail.com.runtime.validation.Create;
import com.vladokk157.gmail.com.runtime.validation.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("Person")
@Tag(name = "Person")
public class PersonController {

  @Autowired private PersonService personService;

  @PostMapping("createPerson")
  @Operation(summary = "createPerson", description = "Creates Person")
  public Person createPerson(
      @Validated(Create.class) @RequestBody PersonCreate personCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return personService.createPerson(personCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deletePerson", description = "Deletes Person")
  public Person deletePerson(@PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return personService.deletePerson(id, securityContext);
  }

  @PostMapping("getAllPersons")
  @Operation(summary = "getAllPersons", description = "lists Persons")
  public PaginationResponse<Person> getAllPersons(
      @Valid @RequestBody PersonFilter personFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return personService.getAllPersons(personFilter, securityContext);
  }

  @PutMapping("updatePerson")
  @Operation(summary = "updatePerson", description = "Updates Person")
  public Person updatePerson(
      @Validated(Update.class) @RequestBody PersonUpdate personUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return personService.updatePerson(personUpdate, securityContext);
  }
}
