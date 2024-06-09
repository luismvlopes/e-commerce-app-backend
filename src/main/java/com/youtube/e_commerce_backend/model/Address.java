package com.youtube.e_commerce_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "address")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "address_line_1", nullable = false, length = 512)
    private String addressLine1;

    @Column(name = "address_line_2", length = 512)
    private String addressLine2;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private LocalUser user;

}
