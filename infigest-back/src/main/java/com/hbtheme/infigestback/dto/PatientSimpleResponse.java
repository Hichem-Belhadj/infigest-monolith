package com.hbtheme.infigestback.dto;

import com.hbtheme.infigestback.model.Insurance;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientSimpleResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private List<StateRegisteredNurseSimpleResponse> stateRegisteredNurses;
	private InsuranceSimpleResponse insurance;
}
