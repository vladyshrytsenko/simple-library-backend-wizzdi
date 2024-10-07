package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.AppInit;
import com.vladokk157.gmail.com.runtime.model.Genre;
import com.vladokk157.gmail.com.runtime.request.GenreCreate;
import com.vladokk157.gmail.com.runtime.request.GenreFilter;
import com.vladokk157.gmail.com.runtime.request.GenreUpdate;
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
public class GenreControllerTest {

  private Genre testGenre;
  @Autowired private TestRestTemplate restTemplate;

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
  public void testGenreCreate() {
    GenreCreate request = new GenreCreate();

    ResponseEntity<Genre> response =
        this.restTemplate.postForEntity("/Genre/createGenre", request, Genre.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testGenre = response.getBody();
    assertGenre(request, testGenre);
  }

  @Test
  @Order(2)
  public void testListAllGenres() {
    GenreFilter request = new GenreFilter();
    ParameterizedTypeReference<PaginationResponse<Genre>> t = new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Genre>> response =
        this.restTemplate.exchange(
            "/Genre/getAllGenres", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Genre> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Genre> Genres = body.getList();
    Assertions.assertNotEquals(0, Genres.size());
    Assertions.assertTrue(Genres.stream().anyMatch(f -> f.getId().equals(testGenre.getId())));
  }

  public void assertGenre(GenreCreate request, Genre testGenre) {
    Assertions.assertNotNull(testGenre);
  }

  @Test
  @Order(3)
  public void testGenreUpdate() {
    GenreUpdate request = new GenreUpdate().setId(testGenre.getId());
    ResponseEntity<Genre> response =
        this.restTemplate.exchange(
            "/Genre/updateGenre", HttpMethod.PUT, new HttpEntity<>(request), Genre.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testGenre = response.getBody();
    assertGenre(request, testGenre);
  }
}
