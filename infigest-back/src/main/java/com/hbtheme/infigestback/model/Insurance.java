package com.hbtheme.infigestback.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToMany
    private List<Patient> patients;

    @OneToMany(mappedBy = "insurance")
    private List<InvoiceRejection> invoiceRejections;
}
