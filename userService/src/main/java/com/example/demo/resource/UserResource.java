package com.example.demo.resource;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.CommonResponseAPI;
import com.example.demo.dto.EventManagerDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterEventDTO;
import com.example.demo.dto.RegisterUserDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserLoginResponseJWT;
import com.example.demo.dto.UserProfileDTO;
import com.example.demo.entity.Address;
import com.example.demo.entity.EventManager;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.UserSaveFailedException;
import com.example.demo.feignclient.NotificationServiceFeign;
import com.example.demo.service.AddressService;
import com.example.demo.service.EventManagerService;
import com.example.demo.service.UserService;
import com.example.demo.utility.Constants.UserStatus;
import com.example.demo.utility.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserResource {

	@Autowired
	private UserService userService;

	@Autowired
	private EventManagerService eventManagerService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private NotificationServiceFeign feign;

	public ResponseEntity<CommonResponseAPI> registerUser(RegisterUserDTO register) {
		CommonResponseAPI response = new CommonResponseAPI();

		if (register == null) {
			response.setResponseMessage("user is null");
			response.setSuccess(false);

			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.BAD_REQUEST);
		}

		User existingUser = this.userService.getUserByEmailAndStatus(register.getEmailId(), UserStatus.ACTIVE.value());

		if (existingUser != null) {
			response.setResponseMessage("User already exists");
			response.setSuccess(false);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		EventManager existingUsers = this.eventManagerService.getUserByEmailAndStatus(register.getEmailId(),
				UserStatus.ACTIVE.value());

		if (existingUsers != null) {
			response.setResponseMessage("User already exists");
			response.setSuccess(false);

			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.BAD_REQUEST);
		}

		if (register.getRole() == null
				|| (!"user".equalsIgnoreCase(register.getRole()) && !"manager".equalsIgnoreCase(register.getRole()))) {
			response.setResponseMessage("Role is missing or invalid");
			response.setSuccess(false);

			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.BAD_REQUEST);
		}

		List<User> byPhoneNoo = userService.findByTel(register.getMob());
		if (!byPhoneNoo.isEmpty()) {
			response.setResponseMessage("A user with this phone number is already registered.");
			response.setSuccess(false);
			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.CONFLICT);
		}

		// Check for existing user by phone number
		List<EventManager> byPhoneNooo = eventManagerService.findByTel(register.getMob());
		if (!byPhoneNooo.isEmpty()) {
			response.setResponseMessage("A user with this phone number is already registered.");
			response.setSuccess(false);
			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.CONFLICT);
		}

		if ("user".equalsIgnoreCase(register.getRole())) {
			
			log.info("Received request for Register User");

			Long tel = register.getMob();
			String telString = String.valueOf(tel);

			char firstChar = telString.charAt(0);

			if (!(firstChar == '6' || firstChar == '7' || firstChar == '8' || firstChar == '9')) {
				response.setResponseMessage("Mobile number should start with one of the following: 6, 7, 8, or 9");
				response.setSuccess(false);

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (!register.getPassword().equals(register.getConfirmPassword())) {
				response.setResponseMessage("Passwords Doesn't Match");
				response.setSuccess(false);

				return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.BAD_REQUEST);
			}

			User user = RegisterUserDTO.toUserEntity(register);

			String encodedPassword = passwordEncoder.encode(user.getPassword());

			user.setStatus(UserStatus.ACTIVE.value());
			user.setPassword(encodedPassword);
			user.setUser(user);

			// Create and save the address
			Address address = new Address();
			address.setCity(register.getCity());
			address.setPincode(register.getPincode());
			address.setStreet(register.getStreet());
			address.setState(register.getState());

			Address savedAddress = this.addressService.addAddress(address);

			if (savedAddress == null) {
				throw new UserSaveFailedException("Registration Failed because of Technical issue:(");
			}

			user.setAddress(savedAddress);
			// user.setCompanyName(register.getCompanyName());
			existingUser = this.userService.addUser(user);

			if (existingUser == null) {
				throw new UserSaveFailedException("Registration Failed because of Technical issue:(");
			}

			response.setResponseMessage("Registered Successfully");
			response.setSuccess(true);
			feign.sendMail(register.getEmailId(), "Registered Successfully",
					"Registration", "", "");

			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.OK);
		} else {
			
			log.info("Received request for Register EventManager");

			Long tel = register.getMob();
			String telString = String.valueOf(tel);

			char firstChar = telString.charAt(0);

			if (!(firstChar == '6' || firstChar == '7' || firstChar == '8' || firstChar == '9')) {
				response.setResponseMessage("Mobile number should start with one of the following: 6, 7, 8, or 9");
				response.setSuccess(false);

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			
			if (!register.getPassword().equals(register.getConfirmPassword())) {
				response.setResponseMessage("Passwords Doesn't Match");
				response.setSuccess(false);

				return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.BAD_REQUEST);
			}
			EventManager user = RegisterEventDTO.toUserEntity(register);

			String encodedPassword = passwordEncoder.encode(user.getPassword());

			user.setStatus(UserStatus.ACTIVE.value());
			user.setPassword(encodedPassword);
			user.setManager(user);

			Address address = new Address();
			address.setCity(register.getCity());
			address.setPincode(register.getPincode());
			address.setStreet(register.getStreet());
			address.setState(register.getState());

			Address savedAddress = this.addressService.addAddress(address);

			if (savedAddress == null) {
				throw new UserSaveFailedException("Registration Failed because of Technical issue:(");
			}

			user.setAddress(savedAddress);
			user.setEventName(register.getEventName());
			existingUsers = this.eventManagerService.addUser(user);
			if (existingUsers == null) {
				throw new UserSaveFailedException("Registration Failed because of Technical issue:(");
			}

			response.setResponseMessage("Registered Successfully");
			response.setSuccess(true);
			feign.sendMail(register.getEmailId(), "Registered Successfully",
					"Registration", "", "");
			
			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.OK);
		}

	}

	public ResponseEntity<UserLoginResponseJWT> login(LoginDTO loginDTO) {
		log.info("Received request for User Login");

		UserLoginResponseJWT response = new UserLoginResponseJWT();

		if (loginDTO == null) {
			response.setResponseMessage("Missing Input");
			response.setSuccess(false);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		// Attempt to find User and Buyer
		User userEntity = userService.getbyemail(loginDTO.getEmailId());
		EventManager eventManagerEntity = eventManagerService.getbyemail(loginDTO.getEmailId());

		// Handle User login
		if (userEntity != null) {
			return handleUserLogin(loginDTO, response, userEntity);
		}
		// Handle Buyer login
		else if (eventManagerEntity != null) {
			return handleManagerLogin(loginDTO, response, eventManagerEntity);
		}
		// Neither User nor Buyer found
		else {
			response.setResponseMessage("Invalid email or password.");
			response.setSuccess(false);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	private ResponseEntity<UserLoginResponseJWT> handleUserLogin(LoginDTO loginDTO, UserLoginResponseJWT response,
			User userEntity) {
		try {
			authenticateUser(loginDTO, userEntity.getRole());
			String jwtToken = jwtUtils.generateToken(loginDTO.getEmailId());

			User user = userService.getUserByEmailIdAndRoleAndStatus(loginDTO.getEmailId(), userEntity.getRole(),
					UserStatus.ACTIVE.value());
			UserDTO userDto = UserDTO.toUserDtoEntity(user);

			response.setUser(userDto);
			response.setResponseMessage("Logged in successfully");
			response.setSuccess(true);
			response.setJwtToken(jwtToken);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			response.setResponseMessage("Invalid email or password.");
			response.setSuccess(false);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	private ResponseEntity<UserLoginResponseJWT> handleManagerLogin(LoginDTO loginDTO, UserLoginResponseJWT response,
			EventManager eventManagerEntity) {
		try {
			authenticateUser(loginDTO, eventManagerEntity.getRole());
			String jwtToken = jwtUtils.generateToken(loginDTO.getEmailId());

			EventManager manager = eventManagerService.getUserByEmailIdAndRoleAndStatus(loginDTO.getEmailId(),
					eventManagerEntity.getRole(), UserStatus.ACTIVE.value());
			EventManagerDTO eventManagerDto = EventManagerDTO.toUserDtoEntity(manager);

			response.setManager(eventManagerDto);
			response.setResponseMessage("Logged in successfully");
			response.setSuccess(true);
			response.setJwtToken(jwtToken);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			response.setResponseMessage("Invalid email or password.");
			response.setSuccess(false);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	private void authenticateUser(LoginDTO loginDTO, String role) {
		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role));
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDTO.getEmailId(), loginDTO.getPassword(), authorities));
	}
	
	public ResponseEntity<UserProfileDTO> getUserProfile(Long id) {
		UserProfileDTO userProfileDTO = new UserProfileDTO();
		User user = userService.findById(id);

		if (user == null) {
			// User not found
			userProfileDTO.setResponseMessage("No User registered with this id");
			userProfileDTO.setSuccess(false);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		userProfileDTO.setId(user.getId());
		userProfileDTO.setFirstName(user.getFirstName());
		userProfileDTO.setLastName(user.getLastName());
		userProfileDTO.setEmailId(user.getEmailId());
		userProfileDTO.setRole(user.getRole());
		userProfileDTO.setStatus(user.getStatus());
		userProfileDTO.setMob(user.getMob());
		userProfileDTO.setResponseMessage("Details fetched");
		userProfileDTO.setSuccess(true);

		userProfileDTO.setAddress(convertToAddressDTO(user.getAddress()));

		return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);

	}

	public ResponseEntity<UserProfileDTO> getManagerProfile(Long id) {
		UserProfileDTO userProfileDTO = new UserProfileDTO();

		EventManager manager = eventManagerService.findById(id);

		if (manager == null) {
			// User not found
			userProfileDTO.setResponseMessage("No User registered with this id");
			userProfileDTO.setSuccess(false);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		userProfileDTO.setId(manager.getId());
		userProfileDTO.setFirstName(manager.getFirstName());
		userProfileDTO.setLastName(manager.getLastName());
		userProfileDTO.setEmailId(manager.getEmailId());
		userProfileDTO.setRole(manager.getRole());
		userProfileDTO.setStatus(manager.getStatus());
		userProfileDTO.setMob(manager.getMob());
		userProfileDTO.setResponseMessage("Details fetched");
		userProfileDTO.setSuccess(true);

		userProfileDTO.setAddress(convertToAddressDTO(manager.getAddress()));

		return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
	}

	public ResponseEntity<CommonResponseAPI> deleteUserByEmail(String emailId) {
		CommonResponseAPI response = new CommonResponseAPI();
		User user = userService.findByEmailId(emailId);
		EventManager manager = eventManagerService.findByEmailId(emailId);

		if (user == null && manager == null) {
			response.setResponseMessage("No User Exists with this E-mail");
			response.setSuccess(false);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		if (user != null) {
			try {
				log.info("User found with Id: {}", emailId);
				boolean deleted = userService.deleteById(user.getId());

				if (deleted) {
					log.info("User Deleted Successfully. Id: {}", emailId);
					response.setResponseMessage("User deleted successfully");
					response.setSuccess(true);
					return ResponseEntity.ok(response);
				} else {
					log.error("Failed to delete user with Id: {}", emailId);
					response.setResponseMessage("Failed to delete user. Please try again later.");
					response.setSuccess(false);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} catch (UserNotFoundException e) {
				log.error("User not found with Id: {}", emailId, e);
				response.setResponseMessage("User not found with ID: " + emailId);
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			} catch (Exception e) {
				log.error("Error deleting user with Id: {}", emailId, e);
				response.setResponseMessage("Error deleting user. Please try again later.");
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} else {
			try {
				log.info("User found with Id: {}", emailId);
				boolean deleted = eventManagerService.deleteById(manager.getId());

				if (deleted) {
					log.info("User Deleted Successfully. Id: {}", emailId);
					response.setResponseMessage("User deleted successfully");
					response.setSuccess(true);
					return ResponseEntity.ok(response);
				} else {
					log.error("Failed to delete user with Id: {}", emailId);
					response.setResponseMessage("Failed to delete user. Please try again later.");
					response.setSuccess(false);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
			} catch (UserNotFoundException e) {
				log.error("User not found with Id: {}", emailId, e);
				response.setResponseMessage("User not found with ID: " + emailId);
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			} catch (Exception e) {
				log.error("Error deleting user with Id: {}", emailId, e);
				response.setResponseMessage("Error deleting user. Please try again later.");
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
	}

	public ResponseEntity<UserProfileDTO> getUserProfile(String emailId) {
		UserProfileDTO userProfileDTO = new UserProfileDTO();
		User user = userService.findByEmailId(emailId); // Fetch the user by email ID
		EventManager manager = eventManagerService.findByEmailId(emailId);

		if (user == null && manager == null) {
			// User not found
			userProfileDTO.setResponseMessage("No User registered with this E-mail");
			userProfileDTO.setSuccess(false);
			return new ResponseEntity<UserProfileDTO>(HttpStatus.NOT_FOUND);
		}
		if (user != null) {
			userProfileDTO.setId(user.getId());
			userProfileDTO.setFirstName(user.getFirstName());
			userProfileDTO.setLastName(user.getLastName());
			userProfileDTO.setEmailId(user.getEmailId());
			userProfileDTO.setRole(user.getRole());
			userProfileDTO.setStatus(user.getStatus());
			userProfileDTO.setMob(user.getMob());
			userProfileDTO.setResponseMessage("Details fetched");
			userProfileDTO.setSuccess(true);

			userProfileDTO.setAddress(convertToAddressDTO(user.getAddress()));

			return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
		} else {
			userProfileDTO.setId(manager.getId());
			userProfileDTO.setFirstName(manager.getFirstName());
			userProfileDTO.setLastName(manager.getLastName());
			userProfileDTO.setEmailId(manager.getEmailId());
			userProfileDTO.setRole(manager.getRole());
			userProfileDTO.setStatus(manager.getStatus());
			userProfileDTO.setMob(manager.getMob());
			userProfileDTO.setResponseMessage("Details fetched");
			userProfileDTO.setSuccess(true);

			userProfileDTO.setAddress(convertToAddressDTO(manager.getAddress()));

			return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);

		}
	}

	public AddressDTO convertToAddressDTO(Address address) {
		if (address == null) {
			return null;
		}
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setId(address.getId());
		addressDTO.setStreet(address.getStreet());
		addressDTO.setCity(address.getCity());
		addressDTO.setState(address.getState());
		addressDTO.setPincode(address.getPincode());
		return addressDTO;
	}

	public ResponseEntity<CommonResponseAPI> updateUserProfile(String emailId, UserProfileDTO userProfileDTO) {
		CommonResponseAPI response = new CommonResponseAPI();

		// Fetch the user by email ID
		User user = userService.findByEmailId(emailId);
		EventManager manager = eventManagerService.findByEmailId(emailId);

		// Check if the user exists
		if (user == null && manager == null) {
			response.setResponseMessage("Please Register, user not Available");
			response.setSuccess(false);
			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.BAD_REQUEST);
		}

		if (user != null) {
			// Update user entity with DTO values
			user.setFirstName(userProfileDTO.getFirstName());
			user.setLastName(userProfileDTO.getLastName());
			user.setEmailId(userProfileDTO.getEmailId());
			user.setRole(userProfileDTO.getRole());
			user.setStatus(userProfileDTO.getStatus());
			user.setMob(userProfileDTO.getMob());

			// Update Address if provided
			if (userProfileDTO.getAddress() != null) {
				log.info("Request Received to Update Address...");
				user.getAddress().setStreet(userProfileDTO.getAddress().getStreet());
				user.getAddress().setCity(userProfileDTO.getAddress().getCity());
				user.getAddress().setPincode(userProfileDTO.getAddress().getPincode());
				user.getAddress().setState(userProfileDTO.getAddress().getState());
				// Optionally update other fields like state, country, etc.
			}

			// Save the updated user
			userService.save(user);
			response.setResponseMessage("Profile Updated");
			response.setSuccess(true);

			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.OK);
		} else {

			// Update user entity with DTO values
			manager.setFirstName(userProfileDTO.getFirstName());
			manager.setLastName(userProfileDTO.getLastName());
			manager.setEmailId(userProfileDTO.getEmailId());
			manager.setRole(userProfileDTO.getRole());
			manager.setStatus(userProfileDTO.getStatus());
			manager.setMob(userProfileDTO.getMob());

			// Update Address
			if (userProfileDTO.getAddress() != null) {
				log.info("Request Received to Update Address...");
				manager.getAddress().setStreet(userProfileDTO.getAddress().getStreet());
				manager.getAddress().setCity(userProfileDTO.getAddress().getCity());
				manager.getAddress().setPincode(userProfileDTO.getAddress().getPincode());
				manager.getAddress().setState(userProfileDTO.getAddress().getState());
				// Optionally update other fields like state, country, etc.
			}

			eventManagerService.save(manager);
			response.setResponseMessage("Profile Updated");
			response.setSuccess(true);

			return new ResponseEntity<CommonResponseAPI>(response, HttpStatus.OK);
		}

	}

}