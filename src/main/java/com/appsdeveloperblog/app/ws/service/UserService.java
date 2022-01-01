package com.appsdeveloperblog.app.ws.service;

import java.util.List;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;

public interface UserService {
	UserDto createUser(UserDto user);

	UserDto getUserByUserId(String userId);

	List<UserDto> getAllUsers(int page, int limit);

	UserDto updateUser(String userId, UserDto userDto);

	void deleteUser(String userId);
}
