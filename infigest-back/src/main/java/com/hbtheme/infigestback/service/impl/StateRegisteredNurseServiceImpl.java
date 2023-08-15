package com.hbtheme.infigestback.service.impl;

import com.hbtheme.infigestback.dto.StateRegisteredNurseRequest;
import com.hbtheme.infigestback.mapper.StateRegisteredNurseMapper;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.StateRegisteredNurseDao;
import com.hbtheme.infigestback.service.StateRegisteredNurseService;
import com.hbtheme.infigestback.service.validator.StateRegisteredNurseValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StateRegisteredNurseServiceImpl implements StateRegisteredNurseService {

	private final StateRegisteredNurseMapper stateRegisteredNurseRequestMapper;

	private final StateRegisteredNurseDao stateRegisteredNurseDao;
	
	private final StateRegisteredNurseValidator stateRegisteredNurseValidator;

	@Override
	public void saveStateRegisteredNurse(StateRegisteredNurseRequest stateRegisteredNurseRequest, boolean isIdRequired) {
		if (stateRegisteredNurseRequest == null) {
			throw new IllegalArgumentException();
		}
		
		List<String> errors = stateRegisteredNurseValidator.validate(stateRegisteredNurseRequest, isIdRequired);
		if (!errors.isEmpty()) {
			throw new IllegalStateException(String.join("; ", errors));
		}

		if (isIdRequired) {
			findStateRegisteredNurseById(stateRegisteredNurseRequest.getId());
		}
		
		StateRegisteredNurse stateRegisteredNurse = stateRegisteredNurseRequestMapper
				.toModel(stateRegisteredNurseRequest);

		stateRegisteredNurseDao.save(stateRegisteredNurse);
	}

	@Override
	public List<StateRegisteredNurse> findAllStateRegisteredNurses() {
		return stateRegisteredNurseDao.findAll();
	}

	@Override
	public List<StateRegisteredNurse> findStateRegisteredNurseById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("You must specify a nurse");
		}
		List<StateRegisteredNurse> stateRegisteredNurses = new ArrayList<>();
		StateRegisteredNurse stateRegisteredNurse = stateRegisteredNurseDao.findById(id).orElseThrow(() ->
				new EntityNotFoundException("the specified nurse was not found"));
		stateRegisteredNurses.add(stateRegisteredNurse);
		return stateRegisteredNurses;
	}

	@Override
	public void deleteStateRegisteredNurseById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		findStateRegisteredNurseById(id);
		stateRegisteredNurseDao.deleteById(id);
	}

}
