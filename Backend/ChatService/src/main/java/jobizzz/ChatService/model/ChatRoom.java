package jobizzz.ChatService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import java.util.Set;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private String name;

    @ManyToMany
    private Set<User> members;
}
