package is.hi.hbv501g.hbv1.services;

import is.hi.hbv501g.hbv1.persistence.entities.User;

import java.util.List;

public interface AuthService {
    User findByEmail(String email);
    User findById(Long id);
    User save(User user);
}
