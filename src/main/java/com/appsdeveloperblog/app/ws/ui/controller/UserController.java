package com.appsdeveloperblog.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.TypeToken;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.description.type.TypeVariableToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.exception.UserServiceException;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressRest;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")

public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		if (userDetails.getFirstName().isBlank())
			throw new UserServiceException(ErrorMessages.MISSING_MANDATORY_FIELD.getErrorMessage());

		// UserRest returnValue = new UserRest();
		ModelMapper mapper = new ModelMapper();

		/*
		 * UserDto userDto = new UserDto(); BeanUtils.copyProperties(userDetails,
		 * userDto);
		 */
		UserDto userDto = mapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		// BeanUtils.copyProperties(createdUser, returnValue);
		UserRest returnValue = mapper.map(createdUser, UserRest.class);

		return returnValue;
	}

	@GetMapping("/{userId}")
	public UserRest getUserByUserId(@PathVariable String userId) {

		// UserRest returnValue = new UserRest();
		UserDto returnedUser = userService.getUserByUserId(userId);
		// BeanUtils.copyProperties(returnedUser, returnValue);

		ModelMapper mapper = new ModelMapper();
		UserRest returnValue = mapper.map(returnedUser, UserRest.class);

		return returnValue;

	}

	@GetMapping
	public List<UserRest> getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getAllUsers(page, limit);
		for (UserDto user : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(user, userModel);
			returnValue.add(userModel);
		}
		return returnValue;
	}

	@PutMapping("/{userId}")
	public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto returnedUser = userService.updateUser(userId, userDto);
		BeanUtils.copyProperties(returnedUser, returnValue);
		return returnValue;

	}

	@DeleteMapping("/{userId}")
	public OperationStatusModel deleteUser(@PathVariable String userId) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(userId);

		returnValue.setOperationStatus(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@GetMapping("/{userId}/addresses")
	public List<AddressRest> getAddresses(@PathVariable String userId) {

		List<AddressDto> addresses = addressService.getAddresses(userId);

		ModelMapper mapper = new ModelMapper();
		Type listType = new TypeToken<List<AddressRest>>() {
		}.getType();
		List<AddressRest> returnValue = mapper.map(addresses, listType);
		return returnValue;

	}

}
