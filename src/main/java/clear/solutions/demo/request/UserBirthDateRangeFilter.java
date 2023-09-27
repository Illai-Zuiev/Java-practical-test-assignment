package clear.solutions.demo.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserBirthDateRangeFilter {
    @Past
    private LocalDate fromDate;
    @Past
    private LocalDate toDate;

    @AssertTrue(message = "Date range is not valid")
    private boolean isValidDateRange() {
        return fromDate.isBefore(toDate);
    }
}
