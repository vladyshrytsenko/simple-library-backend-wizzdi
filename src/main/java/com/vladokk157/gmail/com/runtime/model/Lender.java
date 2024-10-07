package com.vladokk157.gmail.com.runtime.model;

import jakarta.persistence.Entity;

@Entity
public class Lender extends Person {

  private boolean blocked;

  /**
   * @return blocked
   */
  public boolean isBlocked() {
    return this.blocked;
  }

  /**
   * @param blocked blocked to set
   * @return Lender
   */
  public <T extends Lender> T setBlocked(boolean blocked) {
    this.blocked = blocked;
    return (T) this;
  }
}
