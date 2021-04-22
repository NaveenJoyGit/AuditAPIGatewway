package com.cognizant.zuulapigateway.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.cognizant.zuulapigateway.entity.User;
import com.cognizant.zuulapigateway.model.UserPrincipal;
import com.cognizant.zuulapigateway.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserAuthDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

    public UserAuthDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String s) throws UsernameNotFoundException {
    	log.info(s);
        User user = userRepository
                .findById(s)
                .orElseThrow(() -> new UsernameNotFoundException("User name " + s + "Not Found in DB"));
        return UserPrincipal.create(user);

    }
	
}
