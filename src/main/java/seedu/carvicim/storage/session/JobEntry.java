package seedu.carvicim.storage.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.carvicim.model.job.Date;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.job.JobNumber;
import seedu.carvicim.model.job.Status;
import seedu.carvicim.model.job.VehicleNumber;
import seedu.carvicim.model.person.Person;
import seedu.carvicim.model.person.UniqueEmployeeList;
import seedu.carvicim.model.remark.Remark;
import seedu.carvicim.model.remark.RemarkList;

//@@author yuhongherald
/**
 * Represents a job entry in an {@link ImportSession}
 */
public class JobEntry extends Job {
    public static final String NEWLINE = "\n";

    private final int sheetNumber;
    private final int rowNumber;

    private final ArrayList<String> comments;

    public JobEntry (Person client, VehicleNumber vehicleNumber, JobNumber jobNumber, Date date,
                     UniqueEmployeeList assignedEmployees, Status status, RemarkList remarks,
                     int sheetNumber, int rowNumber, String importComment) {
        super(client, vehicleNumber, jobNumber, date, assignedEmployees, status, remarks);
        this.sheetNumber = sheetNumber;
        this.rowNumber = rowNumber;
        comments = new ArrayList<>();
        if (importComment != null &&  !importComment.isEmpty()) {
            comments.add(importComment);
            remarks.add(new Remark(importComment));
        }
    }

    /**
     * Marks {@code JobEntry} as reviewed.
     * @param approved whether {@code JobEntry} is going to be added to Carvicim
     * @param comment feedback for {@code JobEntry} in String representation
     */
    public void review(boolean approved, String comment) {
        comments.add(comment);
    }

    /**
     * Removes last comment from comments and remarks in the event that it cannot be saved to disk
     */
    public void unreviewLastComment() {
        if (comments.isEmpty()) {
            return;
        }
        comments.remove(comments.size() - 1);
    }

    /**
     * Adds all comments into remarks
     */
    public void confirmLastReview() {
        if (comments.isEmpty()) {
            return;
        }
        if (!comments.get(comments.size() - 1).isEmpty()) {
            remarks.add(new Remark(comments.get(comments.size() - 1)));
        }
    }

    public List<String> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public String getCommentsAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String comment : comments) {
            stringBuilder.append(comment);
            stringBuilder.append(NEWLINE);
        }
        return stringBuilder.toString();
    }

    public int getSheetNumber() {
        return sheetNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }
}
