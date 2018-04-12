package seedu.carvicim.model.person;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.carvicim.commons.util.StringUtil;
import seedu.carvicim.model.tag.Tag;

//@@author charmaineleehc
/**
 * Tests that a {@code Employee}'s {@code Name} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<Employee> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Employee employee) {
        Set<Tag> employeeTags = employee.getTags();
        String tagsConcatenated = "";
        for (Tag tag: employeeTags) {
            tagsConcatenated = tagsConcatenated + tag.getTagName() + " ";
        }
        final String allTagNames = tagsConcatenated;

        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(allTagNames, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }
}
