package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody User user ){
		try {
			User _user = userRepository.save(new User(user.getEmail(),user.getPassword()));
			return new ResponseEntity<>(_user,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<List<User>> getAllUsers(){
		try {
			List<User> users = new ArrayList<>();
			for(User u : userRepository.findAll())
				users.add(u);
			return new ResponseEntity<>(users,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/get_user_by_id/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") String id){
			Optional<User> userData = userRepository.findById(id);
			if(userData.isPresent()) {
				return new ResponseEntity<>(userData.get(),HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping("/update_user_by_id/{id}")
	public ResponseEntity<User> updateUserById(@PathVariable("id") String id,@RequestBody User user){
		Optional<User> userData = userRepository.findById(id);
		if(userData.isPresent()) {
			//performing the update operation
			User _user = userData.get();
			_user.setEmail(user.getEmail());
			_user.setPassword(user.getPassword());
			return new ResponseEntity<>(userRepository.save(_user),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@DeleteMapping("/delete_by_id/{id}")
	public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") String id){
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
