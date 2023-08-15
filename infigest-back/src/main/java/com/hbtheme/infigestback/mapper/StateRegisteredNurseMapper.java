package com.hbtheme.infigestback.mapper;

import com.hbtheme.infigestback.dto.StateRegisteredNurseRequest;
import com.hbtheme.infigestback.dto.StateRegisteredNurseSimpleResponse;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StateRegisteredNurseMapper {

	public StateRegisteredNurse toModel(StateRegisteredNurseRequest stateRegisteredNurseRequest) {
		return StateRegisteredNurse.builder()
				.firstName(stateRegisteredNurseRequest.getFirstName())
				.lastName(stateRegisteredNurseRequest.getLastName())
				.build();
	}

	public StateRegisteredNurseSimpleResponse toSimpleResponse(StateRegisteredNurse stateRegisteredNurse) {
		return StateRegisteredNurseSimpleResponse.builder()
				.id(stateRegisteredNurse.getId())
				.firstName(stateRegisteredNurse.getFirstName())
				.lastName(stateRegisteredNurse.getLastName())
				.build();
		
	}
	
}
