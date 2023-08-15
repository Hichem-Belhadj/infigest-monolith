package com.hbtheme.infigestback.mapper;

import com.hbtheme.infigestback.dto.InsuranceSimpleResponse;
import com.hbtheme.infigestback.dto.PatientRequest;
import com.hbtheme.infigestback.dto.PatientSimpleResponse;
import com.hbtheme.infigestback.dto.StateRegisteredNurseSimpleResponse;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientMapper {
	
	private final InsuranceService insuranceService;
	private final StateRegisteredNurseMapper stateRegisteredNurseMapper;
	private final InsuranceMapper insuranceMapper;

	public Patient toModel(PatientRequest patientRequest, List<StateRegisteredNurse> stateRegisteredNurses) {
		return Patient.builder()
				.id(patientRequest.getId())
				.firstName(patientRequest.getFirstName())
				.lastName(patientRequest.getLastName())
				.stateRegisteredNurses(stateRegisteredNurses)
				.insurance(insuranceService.findInsuranceById(patientRequest.getInsuranceId()).get(0))
				.build();
	}
	
	public PatientSimpleResponse toSimpleResponse(Patient patient) {
		return PatientSimpleResponse.builder()
				.id(patient.getId())
				.firstName(patient.getFirstName())
				.lastName(patient.getLastName())
				.stateRegisteredNurses(patient.getStateRegisteredNurses().stream()
								.map(stateRegisteredNurseMapper::toSimpleResponse)
								.toList())
				.insurance(insuranceMapper.toSimpleResponse(patient.getInsurance()))
				.build();
	}

}
