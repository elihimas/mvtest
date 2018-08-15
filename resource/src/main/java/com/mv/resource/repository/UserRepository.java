package com.mv.resource.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mv.resource.model.MVUser;

@Repository
public interface UserRepository extends JpaRepository<MVUser, Long> {
	
	Stream<MVUser> findByUserName(String userName);
}
