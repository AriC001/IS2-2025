package com.example.taller.error;


public class ErrorServicio extends RuntimeException {
  public ErrorServicio(String message) {
    super(message);
  }
}
