package com.hbtheme.infigestback.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceSimpleResponse {
	private Long id;
	private StateRegisteredNurseSimpleResponse stateRegisteredNurse;
	private PatientSimpleResponse patient;
	private Long batchNumber;
	private String patientInvoiceNumber;
	private LocalDate invoiceDate;
	private LocalDate careStartDate;
	private LocalDate careEndDate;
	private BigDecimal totalBilled;
	private String rejectionFeedback;
	private BigDecimal cpamPayment;
	private BigDecimal insurancePayment;
	private BigDecimal patientShare;
	private String comment;
	private CustomerInvoiceSimpleResponse customerInvoice;
	private boolean isArchived;
}
