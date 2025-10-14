package com.example.etemplate.entities;

public interface SoftDeletable {
    void setDeleted(boolean deleted);
    boolean isDeleted();
}