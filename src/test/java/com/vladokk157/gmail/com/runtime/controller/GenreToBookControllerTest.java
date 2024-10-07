package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.AppInit;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.model.Genre;
import com.vladokk157.gmail.com.runtime.model.GenreToBook;
import com.vladokk157.gmail.com.runtime.request.GenreToBookCreate;
import com.vladokk157.gmail.com.runtime.request.GenreToBookFilter;
import com.vladokk157.gmail.com.runtime.request.GenreToBookUpdate;
import com.vladokk157.gmail.com.runtime.request.LoginRequest;
import com.vladokk157.gmail.com.runtime.response.PaginationResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AppInit.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class GenreToBookControllerTest {

  private GenreToBook testGenreToBook;
  @Autowired private TestRestTemplate restTemplate;

  @Autowired private Book book;

  @Autowired private Genre genre;

  @BeforeAll
  private void init() {
    ResponseEntity<Object> authenticationResponse =
        this.restTemplate.postForEntity(
            "/login",
            new LoginRequest().setUsername("admin@flexicore.com").setPassword("admin"),
            Object.class);
    String authenticationKey =
        authenticationResponse.getHeaders().get(HttpHeaders.AUTHORIZATION).stream()
            .findFirst()
            .orElse(null);
    restTemplate
        .getRestTemplate()
        .setInterceptors(
            Collections.singletonList(
                (request, body, execution) -> {
                  request.getHeaders().add("Authorization", "Bearer " + authenticationKey);
                  return execution.execute(request, body);
                }));
  }

  @Test
  @Order(1)
  public void testGenreToBookCreate() {
    GenreToBookCreate request = new GenreToBookCreate();

    request.setBookId(this.book.getId());

    request.setGenreId(this.genre.getId());

    ResponseEntity<GenreToBook> response =
        this.restTemplate.postForEntity(
            "/GenreToBook/createGenreToBook", request, GenreToBook.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testGenreToBook = response.getBody();
    assertGenreToBook(request, testGenreToBook);
  }

  @Test
  @Order(2)
  public void testListAllGenreToBooks() {
    GenreToBookFilter request = new GenreToBookFilter();
    ParameterizedTypeReference<PaginationResponse<GenreToBook>> t =
        new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<GenreToBook>> response =
        this.restTemplate.exchange(
            "/GenreToBook/getAllGenreToBooks", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<GenreToBook> body = response.getBody();
    Assertions.assertNotNull(body);
    List<GenreToBook> GenreToBooks = body.getList();
    Assertions.assertNotEquals(0, GenreToBooks.size());
    Assertions.assertTrue(
        GenreToBooks.stream().anyMatch(f -> f.getId().equals(testGenreToBook.getId())));
  }

  public void assertGenreToBook(GenreToBookCreate request, GenreToBook testGenreToBook) {
    Assertions.assertNotNull(testGenreToBook);

    if (request.getBookId() != null) {

      Assertions.assertNotNull(testGenreToBook.getBook());
      Assertions.assertEquals(request.getBookId(), testGenreToBook.getBook().getId());
    }

    if (request.getGenreId() != null) {

      Assertions.assertNotNull(testGenreToBook.getGenre());
      Assertions.assertEquals(request.getGenreId(), testGenreToBook.getGenre().getId());
    }
  }

  @Test
  @Order(3)
  public void testGenreToBookUpdate() {
    GenreToBookUpdate request = new GenreToBookUpdate().setId(testGenreToBook.getId());
    ResponseEntity<GenreToBook> response =
        this.restTemplate.exchange(
            "/GenreToBook/updateGenreToBook",
            HttpMethod.PUT,
            new HttpEntity<>(request),
            GenreToBook.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testGenreToBook = response.getBody();
    assertGenreToBook(request, testGenreToBook);
  }
}
