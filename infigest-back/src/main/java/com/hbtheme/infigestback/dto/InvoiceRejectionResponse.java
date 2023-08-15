package com.hbtheme.infigestback.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class InvoiceRejectionResponse extends BaseResponse {
	private List<InvoiceRejectionSimpleResponse> invoiceRejectionSimpleResponses;

	public InvoiceRejectionResponse(String message) {
		super();
		this.message = message;
	}
}
