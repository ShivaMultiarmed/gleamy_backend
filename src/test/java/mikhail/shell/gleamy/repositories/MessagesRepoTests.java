package mikhail.shell.gleamy.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@SpringJUnitConfig
public class MessagesRepoTests {

    @Autowired
    private MessagesRepo repo;

    /*@Test
    public void checkGetAllMembersIdsByChatId()

    {
        final List<Long> expectedIds = List.of(2L, 1L);
        final List<Long> realIds = repo.findAllMembersIdsByChatid(1L);
        Assertions.assertEquals(expectedIds, realIds);
    }*/
}
