package mikhail.shell.gleamy.service;

import lombok.AllArgsConstructor;
import mikhail.shell.gleamy.repositories.UsersRepo;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UsersRepo usersRepo;
    public String valiateLogin(String username, String password)
    {
        if (!usersRepo.existsByLogin(username))
            return "NOTFOUND";
        else if (!usersRepo.getByLogin(username).getPassword().equals(password))
            return "PASSINCORRECT";
        else
            return "OK";
    }
    public String validateSignup(String username, String email)
    {
        if (usersRepo.existsByLogin(username))
            return "NAMEEXISTS";
        else if (usersRepo.existsByEmail(email))
            return "EMAILEXISTS";
        else
            return "OK";
    }
}
