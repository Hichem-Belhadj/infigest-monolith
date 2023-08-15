package com.hbtheme.infigestback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String firstName;

    @Column(nullable = false, length = 150)
    private String lastName;

    @ManyToOne(optional = false)
    private Insurance insurance;

    @ManyToMany()
    private List<StateRegisteredNurse> stateRegisteredNurses;

    @OneToMany(mappedBy = "patient")
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "patient")
    private List<InvoiceRejection> invoiceRejections;
}
