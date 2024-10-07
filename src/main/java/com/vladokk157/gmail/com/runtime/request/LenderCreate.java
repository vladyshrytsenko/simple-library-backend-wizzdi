package com.vladokk157.gmail.com.runtime.request;

/** Object Used to Create Lender */
public class LenderCreate extends PersonCreate {

  private Boolean blocked;

  /**
   * @return blocked
   */
  public Boolean getBlocked() {
    return this.blocked;
  }

  /**
   * @param blocked blocked to set
   * @return LenderCreate
   */
  public <T extends LenderCreate> T setBlocked(Boolean blocked) {
    this.blocked = blocked;
    return (T) this;
  }
}
