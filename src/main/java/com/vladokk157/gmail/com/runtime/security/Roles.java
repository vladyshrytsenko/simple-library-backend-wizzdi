package com.vladokk157.gmail.com.runtime.security;

public enum Roles {
  Admin(true),

  User(false);

  private final boolean admin;

  Roles(boolean admin) {
    this.admin = admin;
  }

  public boolean isAdmin() {
    return admin;
  }
}
