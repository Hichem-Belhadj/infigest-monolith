package com.hbtheme.infigestback.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInvoiceRequest {

	private Long id;
	private String customerInvoiceNumber;
	private Long stateRegisteredNurseId;
	private BigDecimal amount;
	
}
