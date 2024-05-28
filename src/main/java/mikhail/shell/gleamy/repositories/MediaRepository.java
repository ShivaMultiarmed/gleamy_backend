package mikhail.shell.gleamy.repositories;

import mikhail.shell.gleamy.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MediaRepository extends JpaRepository<Media, String> {
    @Query(value = "SELECT * FROM `media` WHERE `userid` = :userid AND `type` = :#{#type.name()} ORDER BY `date_time` DESC LIMIT :begin, :portion", nativeQuery = true)
    List<Media> getMediaPortionByUserId(@Param("userid") Long userid,@Param("type") Media.Type type,@Param("begin") Long begin,@Param("portion") Integer portion);
}
