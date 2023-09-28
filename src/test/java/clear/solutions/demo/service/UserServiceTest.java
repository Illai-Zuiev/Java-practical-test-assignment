package clear.solutions.demo.service;

import clear.solutions.demo.exception.EntityIdNotFoundException;
import clear.solutions.demo.exception.UserAgeRestrictionException;
import clear.solutions.demo.model.User;
import clear.solutions.demo.repository.UserRepository;
import clear.solutions.demo.request.UserModificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private User userFromDb;
    private UserModificationRequest userModificationRequest;

    @BeforeEach
    void setUp() {
        userFromDb = new User(UUID.randomUUID(), "e@gmail.com",
                "i", "z", LocalDate.of(2003, 7, 28), null, null);
        userModificationRequest = new UserModificationRequest("e@gmail.com", "i",
                "z", LocalDate.of(2003, 7, 28), null, null);
    }

    @Test
    void createUser_shouldCreateUser_WhenUserDataIsCorrect() {
        when(userRepository.save(any(User.class))).thenReturn(userFromDb);

        userService.createUser(userModificationRequest);

        assertAll(
                () -> verify(userRepository).save(any(User.class))
        );
    }

    @Test
    void createUser_shouldThrowException_WhenUserHasAgeRestriction() {
        userModificationRequest.setBirthDate(LocalDate.of(2009, 10, 10));

        assertAll(
                () -> assertThrows(UserAgeRestrictionException.class, () -> userService.createUser(userModificationRequest)),
                () -> verifyNoInteractions(userRepository)
        );
    }

    @Test
    void updateUser_shouldUpdateUser_WhenUserDataIsCorrect() {
        when(userRepository.save(any(User.class))).thenReturn(userFromDb);
        when(userRepository.findById(userFromDb.getId())).thenReturn(Optional.of(userFromDb));

        userService.updateUser(userFromDb.getId(), userModificationRequest);

        assertAll(
                () -> verify(userRepository).save(any(User.class)),
                () -> verify(userRepository).findById(any(UUID.class))
        );
    }

    @Test
    void updateUser_shouldThrowException_WhenUserHasAgeRestriction() {
        userModificationRequest.setBirthDate(LocalDate.of(2009, 10, 10));

        assertAll(
                () -> assertThrows(UserAgeRestrictionException.class, () -> userService.updateUser(userFromDb.getId(), userModificationRequest)),
                () -> verifyNoInteractions(userRepository)
        );
    }

    @Test
    void updateUser_shouldThrowException_WhenUserIdDoesNotExist() {
        when(userRepository.findById(userFromDb.getId())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(EntityIdNotFoundException.class, () -> userService.updateUser(userFromDb.getId(), userModificationRequest)),
                () -> verify(userRepository).findById(userFromDb.getId())
        );
    }

    @Test
    void updateUserEmail_shouldUpdateUserEmail_WhenUserDataIsCorrect() {
        when(userRepository.save(any(User.class))).thenReturn(userFromDb);
        when(userRepository.findById(userFromDb.getId())).thenReturn(Optional.of(userFromDb));

        userService.updateUserEmail(userFromDb.getId(), "e@e.e");

        assertAll(
                () -> verify(userRepository).save(any(User.class)),
                () -> verify(userRepository).findById(any(UUID.class))
        );
    }

    @Test
    void updateUserEmail_shouldThrowException_WhenUserIdDoesNotExist() {
        when(userRepository.findById(userFromDb.getId())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(EntityIdNotFoundException.class, () -> userService.updateUserEmail(userFromDb.getId(), "e@e.e")),
                () -> verify(userRepository).findById(userFromDb.getId())
        );
    }

    @Test
    void deleteUser_shouldDeleteUser_WhenUserIdExist() {
        when(userRepository.findById(userFromDb.getId())).thenReturn(Optional.of(userFromDb));

        userService.deleteUserById(userFromDb.getId());

        assertAll(
                () -> verify(userRepository).deleteById(any(UUID.class)),
                () -> verify(userRepository).findById(any(UUID.class))
        );
    }

    @Test
    void deleteUser_shouldThrowException_WhenUserIdDoesNotExist() {
        when(userRepository.findById(userFromDb.getId())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(EntityIdNotFoundException.class, () -> userService.deleteUserById(userFromDb.getId())),
                () -> verify(userRepository).findById(userFromDb.getId())
        );
    }
}