package com.cognizant.zuulapigateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.zuulapigateway.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
