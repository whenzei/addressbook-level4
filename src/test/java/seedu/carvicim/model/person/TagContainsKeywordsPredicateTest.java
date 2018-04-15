package seedu.carvicim.model.person;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.carvicim.testutil.EmployeeBuilder;

//@@author charmaineleehc
public class TagContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TagContainsKeywordsPredicate firstPredicate = new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        TagContainsKeywordsPredicate secondPredicate = new TagContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TagContainsKeywordsPredicate firstPredicateCopy = new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personHasTagsMatchingKeywords_returnsTrue() {
        // One keyword
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("mechanic"));
        assertTrue(predicate.test(new EmployeeBuilder().withTags("mechanic", "technician").build()));

        // Multiple keywords
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("mechanic", "technician"));
        assertTrue(predicate.test(new EmployeeBuilder().withTags("mechanic", "technician").build()));

        // Only one matching keyword
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("mechanic", "technician"));
        assertTrue(predicate.test(new EmployeeBuilder().withTags("cleaner", "technician").build()));

        // Mixed-case keywords
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("mEcHaNiC", "TeChNiCiAn"));
        assertTrue(predicate.test(new EmployeeBuilder().withTags("mechanic", "technician").build()));

    }

    @Test
    public void test_personDoesNotHaveTagsContainingKeywords_returnsFalse() {
        // Zero keywords
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new EmployeeBuilder().withTags("mechanic").build()));

        // Non-matching keyword
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("mechanic"));
        assertFalse(predicate.test(new EmployeeBuilder().withTags("technician", "cleaner").build()));

        // Keywords match phone, email and address, but does not match any tag name
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("12345", "alice@email.com", "Main", "Street"));
        assertFalse(predicate.test(new EmployeeBuilder().withTags("mechanic").withPhone("12345")
                .withEmail("alice@email.com").build()));
    }
}
