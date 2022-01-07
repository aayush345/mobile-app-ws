package com.appsdeveloperblog.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

class UserServiceImplTest {
	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	Utils utils;

	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setUserId("abcdefghijklmnopqrstuvwxyz1234");
		userEntity.setFirstName("Aayush");
		userEntity.setLastName("Joshi");
		userEntity.setEmail("aayush@joshi.com");
		userEntity.setEncryptedPassword("test-encrypted-pwd");
		userEntity.setEmailVerificationToken("test-token");
		userEntity.setEmailVerificationStatus(false);
	}

	@Test
	void testGetUserByUserId() {

		when(userRepository.findByUserId(anyString())).thenReturn(userEntity);

		UserDto userDto = userService.getUserByUserId("abcdefghijklmnopqrstuvwxyz1234");
		assertNotNull(userDto);
		assertEquals(userEntity.getUserId(), userDto.getUserId());
		assertEquals(userEntity.getFirstName(), userDto.getFirstName());
		assertEquals(userEntity.getLastName(), userDto.getLastName());
		assertEquals(userEntity.getEmail(), userDto.getEmail());

	}

	@Test
	void testGetUserByUserId_UserIdNotFound() {

		when(userRepository.findByUserId(anyString())).thenReturn(null);

		assertThrows(RuntimeException.class,

				() -> {
					userService.getUserByUserId("abcdefghijklmnopqrstuvwxyz1234");
				}

		);

	}

	@Test
	void testCreateUser() {

		UserDto userDto = new UserDto();
		userDto.setFirstName("Aayush");
		userDto.setLastName("Joshi");
		userDto.setEmail("aayush@joshi.com");
		userDto.setPassword("pwd");

		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateUserId(anyInt())).thenReturn("abcdefghijklmnopqrstuvwxyz1234");
		// Mockito.doNothing().when(utils.generateUserId( anyInt() ) );

		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

		UserDto savedUser = userService.createUser(userDto);
		assertNotNull(savedUser);
		assertEquals(userEntity.getUserId(), savedUser.getUserId());
		assertEquals(userEntity.getFirstName(), savedUser.getFirstName());
		assertEquals(userEntity.getLastName(), savedUser.getLastName());
		assertEquals(userEntity.getEmail(), savedUser.getEmail());
		verify(utils, times(1)).generateUserId(anyInt());
	}

	@Test
	void testCreateUser_RecordAlreadyExists() {
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = new UserDto();
		userDto.setFirstName("Aayush");
		userDto.setLastName("Joshi");
		userDto.setEmail("aayush@joshi.com");
		userDto.setPassword("pwd");

		assertThrows(RuntimeException.class, () -> {
			userService.createUser(userDto);
		});

	}

}
