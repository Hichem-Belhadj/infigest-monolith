package com.hbtheme.infigestback.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class StateRegisteredNurseResponse extends BaseResponse {
	private List<StateRegisteredNurseSimpleResponse> stateRegisteredNurses;

	public StateRegisteredNurseResponse(String message) {
		super();
		this.message = message;
	}
}
