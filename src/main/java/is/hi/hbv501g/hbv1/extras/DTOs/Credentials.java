package is.hi.hbv501g.hbv1.extras.DTOs;

/**
 * A DTO containing the email and password for an account
 * used when the user is logging in or registering an account
 */
public class Credentials {
    private String email;
    private String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
