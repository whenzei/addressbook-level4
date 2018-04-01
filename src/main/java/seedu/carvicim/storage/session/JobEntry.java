package seedu.carvicim.storage.session;

import static seedu.carvicim.model.remark.Remark.isValidRemark;

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
 * Represents a job entry in an (@link ImportSession)
 */
public class JobEntry extends Job implements ExcelRowReference {
    public static final String NEWLINE = "\n";

    private final int sheetNumber;
    private final int rowNumber;

    private boolean reviewed;
    private boolean approved;
    private final ArrayList<String> comments;

    public JobEntry (Person client, VehicleNumber vehicleNumber, JobNumber jobNumber, Date date,
                     UniqueEmployeeList assignedEmployees, Status status, RemarkList remarks,
                     int sheetNumber, int rowNumber, String importComment) {
        super(client, vehicleNumber, jobNumber, date, assignedEmployees, status, remarks);
        this.sheetNumber = sheetNumber;
        this.rowNumber = rowNumber;
        comments = new ArrayList<>();
        addComment(importComment);
        reviewed = false;
    }

    /**
     * Adds a non-empty comment to both remarks and comments.
     */
    private void addComment(String comment) {
        if (comment != null && isValidRemark(comment)) {
            remarks.add(new Remark(comment));
            comments.add(comment);
        }
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public boolean isApproved() {
        return approved;
    }

    /**
     * Marks (@code JobEntry) as reviewed.
     * @param approved whether (@code JobEntry) is going to be added to Carvicim
     * @param comment feedback for (@code JobEntry) in String representation
     */
    public void review(boolean approved, String comment) {
        this.approved = approved;
        addComment(comment);
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

    @Override public int getSheetNumber() {
        return sheetNumber;
    }

    @Override public int getRowNumber() {
        return rowNumber;
    }
}
