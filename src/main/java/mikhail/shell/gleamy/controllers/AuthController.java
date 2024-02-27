package mikhail.shell.gleamy.controllers;

import lombok.AllArgsConstructor;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.service.AuthService;
import mikhail.shell.gleamy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
class AuthController {
    private final AuthService authService;
    private final UserService userService;
    
    @PostMapping("/login")
    ResponseEntity<User> login(@RequestParam("login")String login,
                                @RequestParam("password") String password)
    {
        String code = authService.valiateLogin(login, password);
		if (code.equals("OK"))
			return ResponseEntity.ok(userService.getUserByLogin(login));
        else
            return switch (code)
                    {
                        case "NOTFOUND" -> ResponseEntity.notFound().build();
                        case "PASSINCORRECT" -> ResponseEntity.badRequest().build();
                        default -> ResponseEntity.badRequest().build();
                    };
    }
    @PostMapping("/signup")
	ResponseEntity<User> signup (@RequestBody User user)
    {
        String code = authService.validateSignup(user.getLogin(), user.getEmail());
        if (code.equals("OK"))
            return ResponseEntity.ok(userService.addUser(user));
        else
            return switch (code)
            {
                case "NAMEEXISTS", "EMAILEXISTS" -> ResponseEntity.badRequest().build();
                default -> ResponseEntity.badRequest().build();
            };
    }
}
