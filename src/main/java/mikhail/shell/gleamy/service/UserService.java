package mikhail.shell.gleamy.service;

import lombok.AllArgsConstructor;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UsersRepo usersRepo;
    public User getUserById(Long userid) throws IllegalArgumentException {
        return usersRepo.findById(userid).orElseThrow(IllegalArgumentException::new);
    }
    public User patchUser(User user) throws IllegalArgumentException
    {
        if (!usersRepo.existsById(user.getId()))
            throw new IllegalArgumentException();
        else
            return usersRepo.save(user);
    }
    public User addUser(User user) throws IllegalArgumentException
    {
        if (usersRepo.existsById(user.getId()) || usersRepo.existsByLogin(user.getLogin()))
            throw new IllegalArgumentException();
        else
            return usersRepo.save(user);
    }
    public Boolean removeUser(Long id) throws IllegalArgumentException
    {
        if (!usersRepo.existsById(id))
            throw new IllegalArgumentException();
        usersRepo.deleteById(id);
        return !usersRepo.existsById(id);
    }
    public List<User> getUsersByLogin(String login) throws Exception
    {
        List<User> users = usersRepo.findByLoginContaining(login);
        if (users.isEmpty())
            throw new Exception();
        else
            return users;
    }
    public User getUserByLogin(String login) throws IllegalArgumentException
    {
        if (!usersRepo.existsByLogin(login))
            throw new IllegalArgumentException();
        else
            return usersRepo.getByLogin(login);
    }
}
