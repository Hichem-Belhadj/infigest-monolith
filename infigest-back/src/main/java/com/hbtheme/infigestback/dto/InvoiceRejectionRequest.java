package com.hbtheme.infigestback.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRejectionRequest {
	private Long id;
	private Long stateRegisteredNurseId;
	private Long patientId;
	private BigDecimal amount;
	private Long insuranceId;
	private String rejectionReason;
	private String feedBackMethod;
	private String feedBackDate;
	private String paymentDate;
	private boolean softwareScoring;
	private String comment;
	private Long customerInvoiceId;
	private boolean isArchived;
}
