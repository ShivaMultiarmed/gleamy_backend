package mikhail.shell.gleamy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    @Id
    @UuidGenerator
    private String uuid;
    private Type type;
    private String extension;
    private Long userid;
    @CreationTimestamp
    private LocalDateTime date_time;
    public enum Type { IMAGE, AUDIO, VIDEO }
}
