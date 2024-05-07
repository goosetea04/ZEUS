package zeus.zeushop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zeus.zeushop.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
}