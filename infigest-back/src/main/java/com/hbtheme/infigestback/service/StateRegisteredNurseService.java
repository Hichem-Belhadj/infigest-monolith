package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.StateRegisteredNurseRequest;
import com.hbtheme.infigestback.model.StateRegisteredNurse;

import java.util.List;

public interface StateRegisteredNurseService {

	void saveStateRegisteredNurse(StateRegisteredNurseRequest stateRegisteredNurseRequest, boolean isIdRequired);

	List<StateRegisteredNurse> findAllStateRegisteredNurses();

	List<StateRegisteredNurse> findStateRegisteredNurseById(Long id);

	void deleteStateRegisteredNurseById(Long id);

}
