package zeus.zeushop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zeus.zeushop.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
