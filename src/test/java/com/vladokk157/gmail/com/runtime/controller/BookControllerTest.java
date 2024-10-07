package com.vladokk157.gmail.com.runtime.controller;

import com.vladokk157.gmail.com.runtime.AppInit;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.model.BookInstance;
import com.vladokk157.gmail.com.runtime.request.BookCreate;
import com.vladokk157.gmail.com.runtime.request.BookFilter;
import com.vladokk157.gmail.com.runtime.request.BookUpdate;
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
public class BookControllerTest {

  private Book testBook;
  @Autowired private TestRestTemplate restTemplate;

  @Autowired private BookInstance bookInstance;

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
  public void testBookCreate() {
    BookCreate request = new BookCreate();

    request.setBookInstanceId(this.bookInstance.getId());

    ResponseEntity<Book> response =
        this.restTemplate.postForEntity("/Book/createBook", request, Book.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testBook = response.getBody();
    assertBook(request, testBook);
  }

  @Test
  @Order(2)
  public void testListAllBooks() {
    BookFilter request = new BookFilter();
    ParameterizedTypeReference<PaginationResponse<Book>> t = new ParameterizedTypeReference<>() {};

    ResponseEntity<PaginationResponse<Book>> response =
        this.restTemplate.exchange(
            "/Book/getAllBooks", HttpMethod.POST, new HttpEntity<>(request), t);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    PaginationResponse<Book> body = response.getBody();
    Assertions.assertNotNull(body);
    List<Book> Books = body.getList();
    Assertions.assertNotEquals(0, Books.size());
    Assertions.assertTrue(Books.stream().anyMatch(f -> f.getId().equals(testBook.getId())));
  }

  public void assertBook(BookCreate request, Book testBook) {
    Assertions.assertNotNull(testBook);

    if (request.getBookInstanceId() != null) {

      Assertions.assertNotNull(testBook.getBookInstance());
      Assertions.assertEquals(request.getBookInstanceId(), testBook.getBookInstance().getId());
    }
  }

  @Test
  @Order(3)
  public void testBookUpdate() {
    BookUpdate request = new BookUpdate().setId(testBook.getId());
    ResponseEntity<Book> response =
        this.restTemplate.exchange(
            "/Book/updateBook", HttpMethod.PUT, new HttpEntity<>(request), Book.class);
    Assertions.assertEquals(200, response.getStatusCodeValue());
    testBook = response.getBody();
    assertBook(request, testBook);
  }
}
