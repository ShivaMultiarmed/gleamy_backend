package mikhail.shell.gleamy.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${gleamy.root}")
    private String GLEAMY_ROOT;
    private final static String AVATAR_PATH = "/storage/imgs/avatars/";
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
        if (user.getId() != null || usersRepo.existsByLogin(user.getLogin()) || usersRepo.existsByEmail(user.getEmail()))
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
    public File getAvatarByUserId(Long userid)
    {
        if (!usersRepo.existsById(userid))
            throw new EntityNotFoundException();
        return new File(GLEAMY_ROOT+AVATAR_PATH + usersRepo.getReferenceById(userid).getAvatar());
    }
    public String editAvatarByUserId(Long userid, MultipartFile avatar) throws IOException, IllegalArgumentException
    {
        if (!usersRepo.existsById(userid))
            throw new EntityNotFoundException();
        else
        {
            Path uploadPath = Paths.get(GLEAMY_ROOT + AVATAR_PATH);
            String filetype = getFileType(avatar.getOriginalFilename());
            String filename = UUID.randomUUID().toString() + "." + filetype;
            Path targetPath = uploadPath.resolve(filename);
            Files.copy(avatar.getInputStream(), targetPath);

            User user = usersRepo.findById(userid).get();
            user.setAvatar(filename);
            usersRepo.save(user);

            return filename;
        }
    }
    private String getFileType(String filename) throws IllegalArgumentException
    {
        int dotPostion = filename.lastIndexOf(".");
        if (dotPostion != -1 && dotPostion < filename.length() - 1)
            return filename.substring(dotPostion+1);
        else
            throw new IllegalArgumentException();
    }
}
