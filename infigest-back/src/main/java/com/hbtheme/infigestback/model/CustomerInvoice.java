package com.hbtheme.infigestback.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String customerInvoiceNumber;

    @ManyToOne(optional = false)
    private StateRegisteredNurse stateRegisteredNurse;

    private BigDecimal amount;

    @OneToMany(mappedBy = "customerInvoice")
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "customerInvoice")
    private List<InvoiceRejection> invoiceRejections;
}
