package com.youtube.e_commerce_backend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TestJpaObject {

    @Id
    private long id;

    @Column
    private String username;
}


