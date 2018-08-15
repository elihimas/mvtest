package com.mv.resource.controller;

import java.security.Principal;
import java.util.Optional;

import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mv.resource.model.MVUser;
import com.mv.resource.repository.UserRepository;

@EnableResourceServer
@RestController
public class ResourceController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/api/userdata")
	UserData fetchUserData(Principal principal) {
		String userName = principal.getName();
		UserData userData = new UserData();

		Optional<MVUser> optionalUser = userRepository.findByUserName(userName).findFirst();
		if (optionalUser.isPresent()) {
			MVUser user = optionalUser.get();

			userData.userName = user.getUserName();
			userData.email = user.getEmail();
			userData.name = user.getName();
		} else {
			createNewUser(userName);

			userData.userName = userName;
		}

		return userData;
	}

	private void createNewUser(String userName) {
		MVUser newUser = new MVUser();
		newUser.setUserName(userName);
		userRepository.save(newUser);
	}

	@PostMapping("/api/updateUser")
	void updateUser(Principal principal, @RequestBody UserData updatedUser) {
		MVUser mvUser = new MVUser();
		mvUser.setEmail(updatedUser.email);
		mvUser.setName(updatedUser.name);
		mvUser.setUserName(updatedUser.userName);

		long id = userRepository.findByUserName(updatedUser.userName).findFirst().get().getId();
		mvUser.setId(id);

		userRepository.saveAndFlush(mvUser);
	}

	public static class UserData {
		public String name;
		public String userName;
		public String email;
		public long id;
	}

	@Profile("!cloud")
	@Bean
	RequestDumperFilter requestDumperFilter() {
		return new RequestDumperFilter();
	}

}
