package clear.solutions.demo.controller;

import clear.solutions.demo.exception.EntityIdNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void findUserByRange_ShouldReturn200_WhenRangeIsCorrect() throws Exception {
        createBasicUser();

        mockMvc.perform(get("/users")
                        .param("fromDate", "2003-07-28")
                        .param("toDate", "2003-07-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is("email@gmail.com")))
                .andExpect(jsonPath("$[0].firstName", is("illya")))
                .andExpect(jsonPath("$[0].lastName", is("zuiew")))
                .andExpect(jsonPath("$[0].birthDate", is("2003-07-28")));
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("email@gmail.com")))
                .andExpect(jsonPath("$.firstName", is("illya")))
                .andExpect(jsonPath("$.lastName", is("zuiew")))
                .andExpect(jsonPath("$.birthDate", is("2003-07-28")));
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
                .isInstanceOf(MethodArgumentNotValidException.class);
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("User was updated")));
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
                .isInstanceOf(EntityIdNotFoundException.class)
                .hasMessage("User is not found by id");
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
                .isInstanceOf(MethodArgumentNotValidException.class);
    }

    @Test
    void updateUserEmail_ShouldReturn200_WhenUserIdExistAndDataIsCorrect() throws Exception {
        String userId = createBasicUser();

        mockMvc.perform(put("/users/" + userId + "/email")
                        .param("email", "eee@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("User was updated")));
    }

    @Test
    void updateUserEmail_ShouldReturn400_WhenUserIdDoesNotExist() throws Exception {
        String userId = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(put("/users/" + userId + "/email")
                        .param("email", "eee@gmail.com"))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(EntityIdNotFoundException.class)
                .hasMessage("User is not found by id");
    }

    @Test
    void deleteUser_ShouldReturn204_WhenUserIdExist() throws Exception {
        String userId = createBasicUser();

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_ShouldReturn400_WhenUserIdDoesNotExist() throws Exception {
        String userId = UUID.randomUUID().toString();

        MvcResult mvcResult = mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(EntityIdNotFoundException.class)
                .hasMessage("User is not found by id");
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