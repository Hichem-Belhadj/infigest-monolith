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
public class InvoiceRejection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private StateRegisteredNurse stateRegisteredNurse;

    @ManyToOne(optional = false)
    private Patient patient;

    private BigDecimal amount;

    @ManyToOne
    private Insurance insurance;

    @Column(length=512, columnDefinition = "TEXT")
    private String rejectionReason;

    private String feedBackMethod;

    private LocalDate feedBackDate;

    private LocalDate paymentDate;

    private boolean softwareScoring;

    @Column(length=512, columnDefinition = "TEXT")
    private String comment;

    @ManyToOne
    private CustomerInvoice customerInvoice;

    private boolean isArchived;
}
