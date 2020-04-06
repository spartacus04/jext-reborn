package me.tajam.jext.exception;

public class InvalidDiscFormatException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidDiscFormatException() {
    super("ItemStack is not a valid disc material or contains invalid lores format.");
  }
  
}