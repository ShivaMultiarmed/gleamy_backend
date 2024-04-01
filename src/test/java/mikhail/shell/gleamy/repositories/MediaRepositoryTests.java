package mikhail.shell.gleamy.repositories;

import lombok.Setter;
import mikhail.shell.gleamy.models.Media;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@SpringJUnitConfig
class MediaRepositoryTests {
    @Autowired
    private MediaRepository mediaRepository;
    //@Test
    void testMediaRepository()
    {
        LocalDateTime expectedDateTime = LocalDateTime.of(2024,3,26,11, 18, 41);
        Media expected = new Media("977e638e-0cb3-4f32-abec-f30351b8edaf","image","gif",2L, expectedDateTime);
        Media real = mediaRepository.findById("977e638e-0cb3-4f32-abec-f30351b8edaf").orElseThrow();
        Assertions.assertEquals(expected, real);
    }
    //@Test
    void testAddMedia()
    {
        Media media = new Media(null,"video", "gif", 3L, null);
        media = mediaRepository.save(media);
        Assertions.assertNotNull(media);
    }
    //@Test
    void testListOfMedia()
    {
        List<String> expected = List.of(
                "977e638e-0cb3-4f32-abec-f30351b8edaf",
                "c651f2bd-641e-40b5-8786-46845d563a2d"
                );
        List<Media> real = mediaRepository.getMediaPortionByUserId(2L, "image", 0L, 12);
        Assertions.assertIterableEquals(expected, real);
    }
}
