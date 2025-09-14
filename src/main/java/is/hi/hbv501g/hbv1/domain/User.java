package is.hi.hbv501g.hbv1.domain;
import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String username;
    private String passwordHash;
    
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

}
