package com.hbtheme.infigestback.dto;

import com.hbtheme.infigestback.model.CustomerInvoice;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequest {
	private Long id;
	private Long stateRegisteredNurseId;
	private Long patientId;
	private Long batchNumber;
	private String patientInvoiceNumber;
	private String invoiceDate;
	private String careStartDate;
	private String careEndDate;
	private BigDecimal totalBilled;
	private String rejectionFeedback;
	private BigDecimal cpamPayment;
	private BigDecimal insurancePayment;
	private BigDecimal patientShare;
	private String comment;
	private Long customerInvoiceId;
	private boolean isArchived;
}
