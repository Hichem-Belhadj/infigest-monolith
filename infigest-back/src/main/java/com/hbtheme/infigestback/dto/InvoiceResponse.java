package com.hbtheme.infigestback.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class InvoiceResponse extends BaseResponse {
	private List<InvoiceSimpleResponse> invoiceSimpleResponses;

	public InvoiceResponse(String message) {
		super();
		this.message = message;
	}
}
