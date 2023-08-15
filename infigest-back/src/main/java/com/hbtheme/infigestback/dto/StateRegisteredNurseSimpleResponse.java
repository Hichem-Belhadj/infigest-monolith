package com.hbtheme.infigestback.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateRegisteredNurseSimpleResponse {
	private Long id;
	private String firstName;
	private String lastName;
}
