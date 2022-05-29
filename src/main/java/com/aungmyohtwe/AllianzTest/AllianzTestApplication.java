package com.aungmyohtwe.AllianzTest;

import com.aungmyohtwe.AllianzTest.model.User;
import com.aungmyohtwe.AllianzTest.model.UserRoleName;
import com.aungmyohtwe.AllianzTest.payload.UserResponse;
import com.aungmyohtwe.AllianzTest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.aungmyohtwe.AllianzTest.*"})
@EnableJpaRepositories
@ServletComponentScan
public class AllianzTestApplication implements ApplicationRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(AllianzTestApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<UserResponse> userList = userService.getAllUsers();
		if (userList.size() == 0) {
			User user = new User();
			user.setUsername("aungaung");
			user.setEmail("aungaung@gmail.com");
			user.setPassword("PaS$w0&4");
			user.setRole(UserRoleName.ADMIN);
			user.setEnabled(true);
			userService.createUser(user);
		}
	}
}
