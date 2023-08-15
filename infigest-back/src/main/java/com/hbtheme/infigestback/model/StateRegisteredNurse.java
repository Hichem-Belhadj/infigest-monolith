package com.hbtheme.infigestback.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateRegisteredNurse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String firstName;

    @Column(nullable = false, length = 150)
    private String lastName;

    @ManyToMany(mappedBy = "stateRegisteredNurses")
    private List<Patient> patients;

    @OneToMany(mappedBy = "stateRegisteredNurse")
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "stateRegisteredNurse")
    private List<InvoiceRejection> invoiceRejections;

    @OneToMany(mappedBy = "stateRegisteredNurse")
    private List<CustomerInvoice> customerInvoices;
}
