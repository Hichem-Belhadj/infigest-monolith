package com.hbtheme.infigestback.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class InsuranceResponse extends BaseResponse {
	private List<InsuranceSimpleResponse> insuranceSimpleResponses;

	public InsuranceResponse(String message) {
		super();
		this.message = message;
	}
}
