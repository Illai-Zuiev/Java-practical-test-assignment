package clear.solutions.demo.service.imp;

import clear.solutions.demo.exception.EntityIdNotFoundException;
import clear.solutions.demo.model.User;
import clear.solutions.demo.repository.UserRepository;
import clear.solutions.demo.request.UserModificationRequest;
import clear.solutions.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    public final UserRepository userRepository;

    @Override
    public User createUser(UserModificationRequest userModificationRequest) {
        return userRepository.save(mapUserModificationRequestToUser(userModificationRequest));
    }

    @Override
    public void updateUser(UUID userId, UserModificationRequest userModificationRequest) {
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

    @Override
    public void updateUserEmail(UUID userId, String email) {
        User userInDB = isExistById(userId);
        userInDB.setEmail(email);
        userRepository.save(userInDB);
    }

    private User isExistById(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityIdNotFoundException("User is not found by id"));
    }

    @Override
    public void deleteUserById(UUID userId) {
        isExistById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> findByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        return userRepository.findByBirthDateGreaterThanEqualAndBirthDateLessThanEqual(fromDate, toDate);
    }
}
