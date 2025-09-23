package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.persistence.repositories.UserRepository;
import is.hi.hbv501g.hbv1.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImplementation implements AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
