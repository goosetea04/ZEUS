package zeus.zeushop.repository;

import zeus.zeushop.model.TopUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopUpRepository extends JpaRepository<TopUp, String> {
    List<TopUp> findByUserId(String userId);
}

