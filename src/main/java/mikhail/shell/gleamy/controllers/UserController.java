package mikhail.shell.gleamy.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikhail.shell.gleamy.api.ActionModel;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.repositories.UsersRepo;
import mikhail.shell.gleamy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

	@GetMapping(value = "/{id}/avatar")//, produces = "image/*")
	HttpEntity<ActionModel> getAvatarByUserId(@PathVariable("id") final Long userid) {
			File avatarFile = userService.getAvatarByUserId(userid);
			log.info(avatarFile.getAbsolutePath());
			try {
				FileInputStream stream = new FileInputStream(avatarFile);
				byte[] bytes = stream.readAllBytes();

				String type = avatarFile.getName().split("\\.")[1];
				HttpHeaders headers = new HttpHeaders();
				//headers.add("Content-Type", "image/" + type);

				Map<String, String> details = new HashMap<>();
				details.put("filename", avatarFile.getName());
				HttpEntity<ActionModel> httpEntity = new HttpEntity<>(new ActionModel("FETCHEDAVA", bytes, details), headers);
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
	@PatchMapping("/{userid}/avatar")
	ResponseEntity<Map<String, Object>> editAvatarByUserId(@PathVariable Long userid, @RequestParam("avatar") MultipartFile avatar)
	{
		if (userid == null)
			return ResponseEntity.badRequest().build();
		else
			try {
				String filename = userService.editAvatarByUserId(userid, avatar);
				Map<String, Object> fileDetails = new HashMap<>();
				fileDetails.put("filename", filename);
				return new ResponseEntity<>(fileDetails, HttpStatus.OK);
			}
			catch (IllegalArgumentException e)
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			catch (EntityNotFoundException e)
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			catch(IOException e)
			{
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	@DeleteMapping("/{userid}/avatar")
	ResponseEntity<Map<String, Object>> deleteAvatarByUserId(@PathVariable Long userid){
		if (userid == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		else{
			try
			{
				Map<String, Object> result = new HashMap<>();
				result.put("result", userService.deleteAvatarByUserId(userid));
				return new ResponseEntity<>(result, HttpStatus.OK);
			}catch(IllegalArgumentException e)
			{
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}
		}
	}
	@GetMapping("/{userid}/media")
	ResponseEntity<List<Media>> getMediaPortionByNumber(
			@PathVariable Long userid,
			@RequestParam("portion_num") Long portion_num,
			@RequestParam("type") Media.Type type)
	{
		if (userid == null || portion_num == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		try{
			List<Media> mediaList = userService.getImagesPortionByUserId(userid, portion_num);
			return new ResponseEntity<>(mediaList, HttpStatus.OK);
		}catch (EntityNotFoundException e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping(path = "/media", produces = "image/*")
	ResponseEntity<byte[]> getUserMediaById(@RequestParam String uuid)
	{
		if (uuid == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		else
			try{
				File file = userService.getMediaById(uuid);
				FileInputStream stream = new FileInputStream(file);
				byte[] bytes = stream.readAllBytes();
				stream.close();

				HttpHeaders headers = new HttpHeaders();
				String mime = mimeFromFile(file.getName());
				headers.setContentType(MediaType.valueOf(mime));

				return ResponseEntity
						.ok()
						.headers(headers)
						.body(bytes);
			}catch(EntityNotFoundException | FileNotFoundException e)
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			catch (IOException e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

	}
	private static String mimeFromFile(String filename)
	{
		final String extension = getFileExtension(filename);

		final Map<String, String> mimeTypes = new HashMap<>();
		mimeTypes.put("jpg", "image/jpg");
		mimeTypes.put("png", "image/png");
		mimeTypes.put("jfif", "image/jfif");

		return mimeTypes.getOrDefault(extension, null);
	}
	private static String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1 || dotIndex == fileName.length() - 1)
			return null;
		else
			return fileName.substring(dotIndex + 1);
	}
}