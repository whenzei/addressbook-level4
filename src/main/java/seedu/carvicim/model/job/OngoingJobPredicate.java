package seedu.carvicim.model.job;

import java.util.function.Predicate;

//@@author whenzei
/**
 * Tests that a {@code Job}'s {@code Status} matches an ongoing status
 */
public class OngoingJobPredicate implements Predicate<Job> {

    @Override
    public boolean test(Job job) {
        String ongoingStatus = Status.STATUS_ONGOING;
        return ongoingStatus.equals(job.getStatus().value);
    }
}
