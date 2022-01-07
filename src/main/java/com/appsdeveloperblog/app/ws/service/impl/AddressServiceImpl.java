package com.appsdeveloperblog.app.ws.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.AddressRepository;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressrepository;

	@Override
	public List<AddressDto> getAddresses(String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);
		List<AddressEntity> addresses = addressrepository.findAllByUser(userEntity);

		ModelMapper mapper = new ModelMapper();
		Type listType = new TypeToken<List<AddressDto>>() {
		}.getType();
		List<AddressDto> returnValue = mapper.map(addresses, listType);
		return returnValue;
	}

}
