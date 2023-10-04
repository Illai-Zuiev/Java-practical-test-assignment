package clear.solutions.demo.service;

import clear.solutions.demo.model.User;
import clear.solutions.demo.request.UserModificationRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UserModificationRequest userModificationRequest);

    void updateUser(UUID userId, UserModificationRequest userModificationRequest);

    void updateUserEmail(UUID userId, String email);

    void deleteUserById(UUID userId);

    List<User> findByBirthDateRange(LocalDate fromDate, LocalDate toDate);
}
