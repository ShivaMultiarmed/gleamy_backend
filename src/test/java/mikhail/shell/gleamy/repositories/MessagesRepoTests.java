package mikhail.shell.gleamy.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
@SpringJUnitConfig
public class MessagesRepoTests {

    @Autowired
    private MessagesRepository repo;

    /*@Test
    public void checkGetAllMembersIdsByChatId()

    {
        final List<Long> expectedIds = List.of(2L, 1L);
        final List<Long> realIds = repo.findAllMembersIdsByChatid(1L);
        Assertions.assertEquals(expectedIds, realIds);
    }*/
}
