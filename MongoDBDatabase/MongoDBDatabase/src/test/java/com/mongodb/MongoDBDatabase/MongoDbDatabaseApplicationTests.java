package com.mongodb.MongoDBDatabase;

import com.mongodb.MongoDBDatabase.Model.UserData;
import com.mongodb.MongoDBDatabase.Repository.UserRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.mongodb.assertions.Assertions.assertFalse;

@SpringBootTest
class MongoDbDatabaseApplicationTests {

	@Autowired
	private UserRepository userRepo;


	@Test
	void contextLoads() {


		List<UserData> users = userRepo.findAll();

		for(UserData u : users){
			System.out.println(u);
		}
		// Check if any data is fetched
		assertFalse(users.isEmpty());
	}

}
