package com.example.medicine.errors;

public class ErrorServicio extends RuntimeException {
  public ErrorServicio(String message) {
    super(message);
  }
}
