package com.hbtheme.infigestback.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class PatientResponse extends BaseResponse {
	private List<PatientSimpleResponse> patientSimpleResponses;

	public PatientResponse(String message) {
		super();
		this.message = message;
	}
}
