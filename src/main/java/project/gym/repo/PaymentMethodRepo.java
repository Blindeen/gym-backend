package project.gym.repo;

import org.springframework.data.repository.CrudRepository;
import project.gym.model.PaymentMethod;

public interface PaymentMethodRepo extends CrudRepository<PaymentMethod, Long> {
}
