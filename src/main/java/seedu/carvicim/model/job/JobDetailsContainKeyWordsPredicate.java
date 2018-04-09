package seedu.carvicim.model.job;

import java.util.List;
import java.util.function.Predicate;

import seedu.carvicim.commons.util.StringUtil;

//@@author whenzei
/**
 * Tests that a {@code Job}'s details matches any of the keywords given.
 */
public class JobDetailsContainKeyWordsPredicate implements Predicate<Job> {
    private final List<String> keywords;

    public JobDetailsContainKeyWordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Job job) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(job.getClient().getName().fullName, keyword))
                 || keywords.stream()
                        .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(job.getDate().value, keyword))
                 || keywords.stream()
                        .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(job.getJobNumber().value, keyword))
                 || keywords.stream()
                        .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(job.getVehicleNumber().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof JobDetailsContainKeyWordsPredicate // instanceof handles nulls
                && this.keywords.equals(((JobDetailsContainKeyWordsPredicate) other).keywords)); // state check
    }

}
