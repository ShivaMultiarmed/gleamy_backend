package mikhail.shell.gleamy.controllers;

import lombok.AllArgsConstructor;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.service.AuthService;
import mikhail.shell.gleamy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
class AuthController {
    private final AuthService authService;
    private final UserService userService;
    
    @PostMapping("/login")
    ResponseEntity login(@RequestParam("login")String login,
                                @RequestParam("password") String password)
    {
        String code = authService.valiateLogin(login, password);
		if (code.equals("OK"))
			return ResponseEntity.ok(userService.getUserByLogin(login));
        else
            return switch (code)
                    {
                        case "NOTFOUND" -> ResponseEntity.notFound().build();
                        case "PASSINCORRECT" -> ResponseEntity.badRequest().body(code);
                        default -> ResponseEntity.badRequest().body(code);
                    };
    }
    @PostMapping("/signup")
	ResponseEntity signup (@RequestParam("login")String login,
            @RequestParam("password") String password,
            @RequestParam("email") String email)
    {
        String code = authService.validateSignup(login, email);
        if (code.equals("OK"))
            return ResponseEntity.ok(userService.addUser(new User(null,login, password,email,null)));
        else
            return switch (code)
            {
                case "NAMEEXISTS", "EMAILEXISTS" -> ResponseEntity.badRequest().body(code);
                default -> ResponseEntity.badRequest().body(code);
            };
    }
}
