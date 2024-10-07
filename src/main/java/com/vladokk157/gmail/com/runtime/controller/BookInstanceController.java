package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.model.BookInstance;
import com.vladokk157.gmail.com.runtime.request.BookInstanceCreate;
import com.vladokk157.gmail.com.runtime.request.BookInstanceFilter;
import com.vladokk157.gmail.com.runtime.request.BookInstanceUpdate;
import com.vladokk157.gmail.com.runtime.response.PaginationResponse;
import com.vladokk157.gmail.com.runtime.security.UserSecurityContext;
import com.vladokk157.gmail.com.runtime.service.BookInstanceService;
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
@RequestMapping("BookInstance")
@Tag(name = "BookInstance")
public class BookInstanceController {

  @Autowired private BookInstanceService bookInstanceService;

  @PostMapping("createBookInstance")
  @Operation(summary = "createBookInstance", description = "Creates BookInstance")
  public BookInstance createBookInstance(
      @Validated(Create.class) @RequestBody BookInstanceCreate bookInstanceCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return bookInstanceService.createBookInstance(bookInstanceCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteBookInstance", description = "Deletes BookInstance")
  public BookInstance deleteBookInstance(
      @PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return bookInstanceService.deleteBookInstance(id, securityContext);
  }

  @PostMapping("getAllBookInstances")
  @Operation(summary = "getAllBookInstances", description = "lists BookInstances")
  public PaginationResponse<BookInstance> getAllBookInstances(
      @Valid @RequestBody BookInstanceFilter bookInstanceFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return bookInstanceService.getAllBookInstances(bookInstanceFilter, securityContext);
  }

  @PutMapping("updateBookInstance")
  @Operation(summary = "updateBookInstance", description = "Updates BookInstance")
  public BookInstance updateBookInstance(
      @Validated(Update.class) @RequestBody BookInstanceUpdate bookInstanceUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return bookInstanceService.updateBookInstance(bookInstanceUpdate, securityContext);
  }
}
