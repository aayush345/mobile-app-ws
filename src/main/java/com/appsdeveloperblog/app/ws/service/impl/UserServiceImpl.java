package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	Utils utils;

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDto createUser(UserDto user) {
		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("Record already exists");

		UserEntity userEntity = new UserEntity();
		// BeanUtils.copyProperties(user, userEntity);

		ModelMapper mapper = new ModelMapper();
		userEntity = mapper.map(user, UserEntity.class);

		userEntity.setEmailVerificationStatus(false);
		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword("test-encrypted-pwd");

		for (int i = 0; i < userEntity.getAddresses().size(); i++) {
			userEntity.getAddresses().get(i).setUser(userEntity);
			userEntity.getAddresses().get(i).setAddressId(utils.generateAddressId(30));
		}

		UserEntity createdUser = userRepository.save(userEntity);

		/*
		 * UserDto returnValue = new UserDto(); BeanUtils.copyProperties(createdUser,
		 * returnValue);
		 */
		UserDto returnValue = mapper.map(createdUser, UserDto.class);

		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		// UserDto returnValue = new UserDto();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new RuntimeException("User Id not found");

		// BeanUtils.copyProperties(userEntity, returnValue);
		ModelMapper mapper = new ModelMapper();
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		return returnValue;
	}

	@Override
	public List<UserDto> getAllUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();

		if (page > 0)
			page = page - 1;

		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();

		for (UserEntity userEntity : users) {
			UserDto user = new UserDto();
			BeanUtils.copyProperties(userEntity, user);
			returnValue.add(user);
		}

		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserDto returnValue = new UserDto();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new RuntimeException("User Id not found");

		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());

		UserEntity updateUser = userRepository.save(userEntity);

		BeanUtils.copyProperties(updateUser, returnValue);

		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new RuntimeException("User Id not found");

		userRepository.delete(userEntity);
	}

}
