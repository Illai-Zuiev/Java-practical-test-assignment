package clear.solutions.demo.service;

import clear.solutions.demo.exception.EntityIdNotFoundException;
import clear.solutions.demo.exception.UserAgeRestrictionException;
import clear.solutions.demo.model.User;
import clear.solutions.demo.repository.UserRepository;
import clear.solutions.demo.request.UserModificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;

    @Value("${user.age.restriction}")
    private int userAgeRestriction;

    public User createUser(UserModificationRequest userModificationRequest) {
        isNotAgeRestriction(userModificationRequest.getBirthDate());

        return userRepository.save(mapUserModificationRequestToUser(userModificationRequest));
    }

    public void updateUser(UUID userId, UserModificationRequest userModificationRequest) {
        isNotAgeRestriction(userModificationRequest.getBirthDate());
        isExistById(userId);

        User user = mapUserModificationRequestToUser(userModificationRequest);
        user.setId(userId);
        userRepository.save(user);
    }


    public User mapUserModificationRequestToUser(UserModificationRequest userModificationRequest) {
        User user = new User();
        user.setEmail(userModificationRequest.getEmail());
        user.setFirstName(userModificationRequest.getFirstName());
        user.setLastName(userModificationRequest.getLastName());
        user.setBirthDate(userModificationRequest.getBirthDate());
        user.setAddress(userModificationRequest.getAddress());
        user.setPhone(userModificationRequest.getPhone());

        return user;
    }

    public void updateUserEmail(UUID userId, String email) {
        User userInDB = isExistById(userId);
        userInDB.setEmail(email);
        userRepository.save(userInDB);
    }

    private void isNotAgeRestriction(LocalDate birthDate) {
        if (birthDate.plusYears(userAgeRestriction).isAfter(LocalDate.now())) {
            throw new UserAgeRestrictionException("User must be more than " + userAgeRestriction + " age");
        }
    }

    private User isExistById(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityIdNotFoundException("user with id " + userId + " is not found"));
    }

    public void deleteUserById(UUID userId) {
        isExistById(userId);
        userRepository.deleteById(userId);
    }

    public List<User> findByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        return userRepository.findByBirthDateGreaterThanEqualAndBirthDateLessThanEqual(fromDate, toDate);
    }
}
