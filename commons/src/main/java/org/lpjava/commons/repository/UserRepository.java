package org.lpjava.commons.repository;

import org.lpjava.commons.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	

	User findByOpenId(String openId);

}
