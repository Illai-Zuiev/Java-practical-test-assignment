package clear.solutions.demo.repository;

import clear.solutions.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByBirthDateGreaterThanEqualAndBirthDateLessThanEqual(LocalDate fromDate, LocalDate toDate);
}
