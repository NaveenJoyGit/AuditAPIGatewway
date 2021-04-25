package com.cognizant.zuulapigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.cognizant.zuulapigateway.entity.User;
import com.cognizant.zuulapigateway.repository.UserRepository;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ZuulApiGatewayApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(ZuulApiGatewayApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		User naveen = new User("Naveen", "$2y$12$B6I4IDz/WUqRn/9EWJPFFulwmNe6qC.ijNTR6VWSvV.C5yvTzbGXC", "ROLE_ADMIN");
		User archana = new User("Archana", "$2y$12$B6I4IDz/WUqRn/9EWJPFFulwmNe6qC.ijNTR6VWSvV.C5yvTzbGXC", "ROLE_ADMIN");
		User meera = new User("Meera", "$2y$12$B6I4IDz/WUqRn/9EWJPFFulwmNe6qC.ijNTR6VWSvV.C5yvTzbGXC", "ROLE_ADMIN");
		User test = new User("test", "$2y$12$B6I4IDz/WUqRn/9EWJPFFulwmNe6qC.ijNTR6VWSvV.C5yvTzbGXC", "ROLE_USER");
		userRepository.save(archana);
		userRepository.save(naveen);
		userRepository.save(meera);
		userRepository.save(test);			
	}

}
