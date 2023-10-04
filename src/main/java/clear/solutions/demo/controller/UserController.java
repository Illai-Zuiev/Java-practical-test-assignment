package clear.solutions.demo.controller;

import clear.solutions.demo.model.User;
import clear.solutions.demo.request.UserBirthDateRangeFilter;
import clear.solutions.demo.request.UserModificationRequest;
import clear.solutions.demo.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
    public final UserService userService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<User> findUserByRange(@Valid UserBirthDateRangeFilter filter) {
        return userService.findByBirthDateRange(filter.getFromDate(), filter.getToDate());
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid UserModificationRequest userModificationRequest) {
        return userService.createUser(userModificationRequest);
    }

    @PutMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String updateUser(@PathVariable UUID userId, @Valid UserModificationRequest userModificationRequest) {
        userService.updateUser(userId, userModificationRequest);
        return "User was updated";
    }

    @PutMapping(value = "/users/{userId}/email", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String updateUserEmail(@PathVariable UUID userId, @Email String email) {
        userService.updateUserEmail(userId, email);
        return "User was updated";
    }

    @DeleteMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
    }

}
