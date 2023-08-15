package com.hbtheme.infigestback.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private StateRegisteredNurse stateRegisteredNurse;

    @ManyToOne(optional = false)
    private Patient patient;

    private Long batchNumber;

    @Column(length = 150)
    private String patientInvoiceNumber;

    private LocalDate invoiceDate;

    private LocalDate careStartDate;

    private LocalDate careEndDate;

    private BigDecimal totalBilled;

    private String rejectionFeedback;

    private BigDecimal cpamPayment;

    private BigDecimal insurancePayment;

    private BigDecimal patientShare;

    @Lob
    @Column(length = 512)
    private String comment;

    @ManyToOne
    private CustomerInvoice customerInvoice;

    private boolean isArchived;
}
