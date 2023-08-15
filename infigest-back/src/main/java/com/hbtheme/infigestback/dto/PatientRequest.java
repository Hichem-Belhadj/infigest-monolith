package com.hbtheme.infigestback.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequest {
	private Long id;
	private String firstName;
	private String lastName;
	private List<Long> stateRegisteredNursesIds;
	private Long insuranceId;
}
