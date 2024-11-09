package jobizzz.ChatService.model;


import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @ManyToMany
    @JoinTable(
            name = "user_chatgroup",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chatgroup_id")
    )
    private Set<ChatRoom> groups;
}

