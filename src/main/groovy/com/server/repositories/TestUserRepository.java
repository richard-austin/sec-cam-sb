package com.server.repositories;

import com.server.commands.TestUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestUserRepository extends CrudRepository<TestUser, Long> {}
