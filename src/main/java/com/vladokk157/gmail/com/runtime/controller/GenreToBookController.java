package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.model.GenreToBook;
import com.vladokk157.gmail.com.runtime.request.GenreToBookCreate;
import com.vladokk157.gmail.com.runtime.request.GenreToBookFilter;
import com.vladokk157.gmail.com.runtime.request.GenreToBookUpdate;
import com.vladokk157.gmail.com.runtime.response.PaginationResponse;
import com.vladokk157.gmail.com.runtime.security.UserSecurityContext;
import com.vladokk157.gmail.com.runtime.service.GenreToBookService;
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
@RequestMapping("GenreToBook")
@Tag(name = "GenreToBook")
public class GenreToBookController {

  @Autowired private GenreToBookService genreToBookService;

  @PostMapping("createGenreToBook")
  @Operation(summary = "createGenreToBook", description = "Creates GenreToBook")
  public GenreToBook createGenreToBook(
      @Validated(Create.class) @RequestBody GenreToBookCreate genreToBookCreate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return genreToBookService.createGenreToBook(genreToBookCreate, securityContext);
  }

  @DeleteMapping("{id}")
  @Operation(summary = "deleteGenreToBook", description = "Deletes GenreToBook")
  public GenreToBook deleteGenreToBook(
      @PathVariable("id") String id, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return genreToBookService.deleteGenreToBook(id, securityContext);
  }

  @PostMapping("getAllGenreToBooks")
  @Operation(summary = "getAllGenreToBooks", description = "lists GenreToBooks")
  public PaginationResponse<GenreToBook> getAllGenreToBooks(
      @Valid @RequestBody GenreToBookFilter genreToBookFilter, Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return genreToBookService.getAllGenreToBooks(genreToBookFilter, securityContext);
  }

  @PutMapping("updateGenreToBook")
  @Operation(summary = "updateGenreToBook", description = "Updates GenreToBook")
  public GenreToBook updateGenreToBook(
      @Validated(Update.class) @RequestBody GenreToBookUpdate genreToBookUpdate,
      Authentication authentication) {

    UserSecurityContext securityContext = (UserSecurityContext) authentication.getPrincipal();

    return genreToBookService.updateGenreToBook(genreToBookUpdate, securityContext);
  }
}
