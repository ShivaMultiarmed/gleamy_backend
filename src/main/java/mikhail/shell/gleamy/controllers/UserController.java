package mikhail.shell.gleamy.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.repositories.UsersRepo;
import mikhail.shell.gleamy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Slf4j
public class UserController
{
	@Autowired
	private final UsersRepo usersRepo;
	@Autowired
	private final UserService userService;
	@GetMapping("/search")
	ResponseEntity<List<User>> searchForUsers(@RequestParam("login") String login)
	{
		try{
			List<User> users = userService.getUsersByLogin(login);
			return ResponseEntity.ok(users);
		}
		catch (Exception e)
		{
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/{id}")
	ResponseEntity<User> getUser(@PathVariable("id") Long userid)
	{
		if (userid == null)
			return ResponseEntity.badRequest().build();
		try
		{
			return ResponseEntity.ok(userService.getUserById(userid));
		}
		catch (IllegalArgumentException e)
		{
			return ResponseEntity.notFound().build();
		}
	}
	@PatchMapping("/{id}")
	ResponseEntity<User> patchUser(@RequestBody User user)
	{
		try{
			return ResponseEntity.ok(userService.patchUser(user));
		}
		catch (IllegalArgumentException e)
		{
			return ResponseEntity.badRequest().build();
		}
	}
	@PostMapping
	ResponseEntity<User> addUser(@RequestBody User user)
	{
		try
		{
			return ResponseEntity.ok(userService.addUser(user));
		}
		catch (IllegalArgumentException e)
		{
			return ResponseEntity.badRequest().build();
		}
	}
	@DeleteMapping("/{id}")
	ResponseEntity deleteUser(@PathVariable Long id)
	{
		if (id == null)
			return ResponseEntity.badRequest().build();
		try {
			return userService.removeUser(id) ?
					ResponseEntity.ok("Removed successfully") : ResponseEntity.internalServerError().body("Error while deleting");
		} catch (IllegalArgumentException e)
		{
			return ResponseEntity.badRequest().body("No such user");
		}
	}
	@GetMapping("/bylogin/{login}")
	ResponseEntity<User> getUser(@PathVariable String login)
	{
		try
		{
			return ResponseEntity.ok(userService.getUserByLogin(login));
		}
		catch (IllegalArgumentException e)
		{
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(value = "/{id}/avatar", produces = "image/*")
	HttpEntity<byte[]> getAvatarById(@PathVariable("id") final Long userid) {
			File avatarFile = userService.getAvatarByUserId(userid);
			log.info(avatarFile.getAbsolutePath());
			try {
				FileInputStream stream = new FileInputStream(avatarFile);
				byte[] bytes = stream.readAllBytes();

				String type = avatarFile.getName().split("\\.")[1];
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", "image/" + type);

				HttpEntity<byte[]> httpEntity = new HttpEntity<>(bytes, headers);
				stream.close();
				return httpEntity;
			}catch (EntityNotFoundException e){
				return ResponseEntity.badRequest().build();
			}catch (FileNotFoundException e) {
				return new ResponseEntity<>(HttpStatusCode.valueOf(404));
			} catch (IOException e) {
				return new ResponseEntity<>(HttpStatusCode.valueOf(503));
			}
	}

}