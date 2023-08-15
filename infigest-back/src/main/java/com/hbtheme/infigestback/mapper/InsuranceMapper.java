package com.hbtheme.infigestback.mapper;

import com.hbtheme.infigestback.dto.InsuranceRequest;
import com.hbtheme.infigestback.dto.InsuranceSimpleResponse;
import com.hbtheme.infigestback.model.Insurance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceMapper {

	public InsuranceSimpleResponse toSimpleResponse(Insurance insurance) {
		return InsuranceSimpleResponse.builder()
				.id(insurance.getId())
				.name(insurance.getName())
				.build();
	}

	public Insurance toModel(InsuranceRequest insuranceRequest) {
		return Insurance.builder()
				.id(insuranceRequest.getId())
				.name(insuranceRequest.getName())
				.build();
	}

}
