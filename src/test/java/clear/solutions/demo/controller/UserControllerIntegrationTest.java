package clear.solutions.demo.controller;

import clear.solutions.demo.exception.EntityIdNotFoundException;
import clear.solutions.demo.exception.UserAgeRestrictionException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void findUserByRange_ShouldReturn200_WhenRangeIsCorrect() throws Exception {
        createBasicUser();

        mockMvc.perform(get("/users")
                        .param("fromDate", "2003-07-28")
                        .param("toDate", "2003-07-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void findUserByRange_ShouldReturn404_WhenRangeIsIncorrect() throws Exception {
        createBasicUser();

        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .param("fromDate", "2043-07-28")
                        .param("toDate", "2003-07-30"))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(MethodArgumentNotValidException.class);
    }

    @Test
    void createUser_ShouldReturn201_WhenUserDataCorrect() throws Exception {
        mockMvc.perform(post("/users")
                        .param("email", "email@gmail.com")
                        .param("firstName", "illya")
                        .param("lastName", "zuiew")
                        .param("birthDate", "2003-07-28"))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_ShouldReturn404_WhenUserHasAgeRestriction() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .param("email", "email@gmail.com")
                        .param("firstName", "illya")
                        .param("lastName", "zuiew")
                        .param("birthDate", "2023-07-28"))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(UserAgeRestrictionException.class)
                .hasMessageContainingAll("User must be more than 18 age");
    }

    @Test
    void createUser_ShouldReturn404_WhenUserHasIncorrectEmail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .param("email", "emailgmail.com")
                        .param("firstName", "illya")
                        .param("lastName", "zuiew")
                        .param("birthDate", "2003-07-28"))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(MethodArgumentNotValidException.class);
    }

    @Test
    void updateUser_ShouldReturn200_WhenUserIdExistAndDataIsCorrect() throws Exception {
        String userId = createBasicUser();

        mockMvc.perform(put("/users/" + userId)
                        .param("email", "e@gmail.com")
                        .param("firstName", "ill")
                        .param("lastName", "zui")
                        .param("birthDate", "2005-07-28"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_ShouldReturn400_WhenUserIdDoesNotExist() throws Exception {
        String userId = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(put("/users/" + userId)
                        .param("email", "e@gmail.com")
                        .param("firstName", "ill")
                        .param("lastName", "zui")
                        .param("birthDate", "2005-07-28"))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(EntityIdNotFoundException.class);
    }

    @Test
    void updateUser_ShouldReturn404_WhenUserHasAgeRestriction() throws Exception {
        String userId = createBasicUser();

        MvcResult mvcResult = mockMvc.perform(put("/users/" + userId)
                        .param("email", "e@gmail.com")
                        .param("firstName", "ill")
                        .param("lastName", "zui")
                        .param("birthDate", "2010-07-28"))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(UserAgeRestrictionException.class)
                .hasMessageContainingAll("User must be more than 18 age");
    }

    @Test
    void updateUserEmail_ShouldReturn200_WhenUserIdExistAndDataIsCorrect() throws Exception {
        String userId = createBasicUser();

        mockMvc.perform(put("/users/" + userId + "/email")
                        .param("email", "eee@gmail.com"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserEmail_ShouldReturn400_WhenUserIdDoesNotExist() throws Exception {
        String userId = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(put("/users/" + userId + "/email")
                        .param("email", "eee@gmail.com"))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(EntityIdNotFoundException.class);
    }

    @Test
    void deleteUser_ShouldReturn200_WhenUserIdExist() throws Exception {
        String userId = createBasicUser();

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_ShouldReturn400_WhenUserIdDoesNotExist() throws Exception {
        String userId = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(EntityIdNotFoundException.class);
    }

    private String createBasicUser() throws Exception {
        return mockMvc.perform(post("/users")
                        .param("email", "email@gmail.com")
                        .param("firstName", "illya")
                        .param("lastName", "zuiew")
                        .param("birthDate", "2003-07-28"))
                .andReturn()
                .getResponse()
                .getContentAsString().substring(7, 43);
    }
}