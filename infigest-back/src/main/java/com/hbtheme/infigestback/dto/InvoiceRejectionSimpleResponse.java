package com.hbtheme.infigestback.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRejectionSimpleResponse {
	private Long id;
	private PatientSimpleResponse patient;
	private BigDecimal amount;
	private String rejectionReason;
	private String feedBackMethod;
	private LocalDate feedBackDate;
	private LocalDate paymentDate;
	private boolean softwareScoring;
	private String comment;
	private CustomerInvoiceSimpleResponse customerInvoice;
	private boolean isArchived;
}
