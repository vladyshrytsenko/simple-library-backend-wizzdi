package com.vladokk157.gmail.com.runtime;

import com.vladokk157.gmail.com.runtime.model.Author;
import com.vladokk157.gmail.com.runtime.model.Book;
import com.vladokk157.gmail.com.runtime.model.BookInstance;
import com.vladokk157.gmail.com.runtime.model.Genre;
import com.vladokk157.gmail.com.runtime.request.AppUserCreate;
import com.vladokk157.gmail.com.runtime.request.AuthorCreate;
import com.vladokk157.gmail.com.runtime.request.BookCreate;
import com.vladokk157.gmail.com.runtime.request.BookInstanceCreate;
import com.vladokk157.gmail.com.runtime.request.GenreCreate;
import com.vladokk157.gmail.com.runtime.security.UserSecurityContext;
import com.vladokk157.gmail.com.runtime.service.AppUserService;
import com.vladokk157.gmail.com.runtime.service.AuthorService;
import com.vladokk157.gmail.com.runtime.service.BookInstanceService;
import com.vladokk157.gmail.com.runtime.service.BookService;
import com.vladokk157.gmail.com.runtime.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppInitConfig {

  @Autowired private BookService bookService;

  @Autowired private BookInstanceService bookInstanceService;

  @Autowired private AuthorService authorService;

  @Autowired private GenreService genreService;

  @Autowired
  @Qualifier("adminSecurityContext")
  private UserSecurityContext securityContext;

  @Bean
  public Book book() {
    BookCreate bookCreate = new BookCreate();
    return bookService.createBook(bookCreate, securityContext);
  }

  @Bean
  public BookInstance bookInstance() {
    BookInstanceCreate bookInstanceCreate = new BookInstanceCreate();
    return bookInstanceService.createBookInstance(bookInstanceCreate, securityContext);
  }

  @Bean
  public Author author() {
    AuthorCreate authorCreate = new AuthorCreate();
    return authorService.createAuthor(authorCreate, securityContext);
  }

  @Bean
  public Genre genre() {
    GenreCreate genreCreate = new GenreCreate();
    return genreService.createGenre(genreCreate, securityContext);
  }

  @Configuration
  public static class UserConfig {
    @Bean
    @Qualifier("adminSecurityContext")
    public UserSecurityContext adminSecurityContext(AppUserService appUserService) {
      com.vladokk157.gmail.com.runtime.model.AppUser admin =
          appUserService.createAppUser(
              new AppUserCreate().setUsername("admin@flexicore.com").setPassword("admin"), null);
      return new UserSecurityContext(admin);
    }
  }
}
