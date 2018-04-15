# whenzei
###### \java\seedu\carvicim\commons\events\ui\JobDisplayPanelUpdateRequestEvent.java
``` java
/**
 * Indicates a request to update the job display panel
 */
public class JobDisplayPanelUpdateRequestEvent extends BaseEvent {

    private final Job job;

    public JobDisplayPanelUpdateRequestEvent(Job job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public Job getJob() {
        return this.job;
    }
}
```
###### \java\seedu\carvicim\commons\events\ui\JobPanelSelectionChangedEvent.java
``` java
/**
 * Represents a selection change in the Job List Panel
 */
public class JobPanelSelectionChangedEvent extends BaseEvent {
    private final JobCard newSelection;

    public JobPanelSelectionChangedEvent(JobCard newSelection) {
        this.newSelection = newSelection;
    }

    public Job getJob() {
        return newSelection.job;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public JobCard getNewSelection() {
        return newSelection;
    }
}
```
###### \java\seedu\carvicim\commons\events\ui\SetThemeRequestEvent.java
``` java
/**
 * An event request to set a new theme
 */
public class SetThemeRequestEvent extends BaseEvent {

    private final Index selectedIndex;

    public SetThemeRequestEvent(Index selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public Index getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\carvicim\logic\commands\AddJobCommand.java
``` java
/**
 * Adds a job to Carvicim
 */
public class AddJobCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a job to the Carvicim.\n"
            + "Parameters: "
            + PREFIX_NAME + "CLIENT_NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_VEHICLE_NUMBER + "VEHICLE_NUMBER "
            + PREFIX_ASSIGNED_EMPLOYEE + "ASSIGNED_EMPLOYEE_INDEX+ (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_VEHICLE_NUMBER + "SHG123A "
            + PREFIX_ASSIGNED_EMPLOYEE + "3 "
            + PREFIX_ASSIGNED_EMPLOYEE + "6 ";

    public static final String MESSAGE_SUCCESS = "New job added: %1$s";

    private final Person client;
    private final VehicleNumber vehicleNumber;
    private final ArrayList<Index> targetIndices;
    private final UniqueEmployeeList assignedEmployees;

    private Job toAdd;

    /**
     * Creates an AddJobCommand to add the specified {@code Job}
     */
    public AddJobCommand(Person client, VehicleNumber vehicleNumber, ArrayList<Index> targetIndices) {
        requireAllNonNull(client, vehicleNumber, targetIndices);
        this.client = client;
        this.vehicleNumber = vehicleNumber;
        this.targetIndices = targetIndices;
        assignedEmployees = new UniqueEmployeeList();
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Employee> lastShownList = model.getFilteredPersonList();

        //Check for valid employee indices
        for (Index targetIndex : targetIndices) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_EMPLOYEE_DISPLAYED_INDEX);
            }
        }

        try {
            for (Index targetIndex : targetIndices) {
                assignedEmployees.add(lastShownList.get(targetIndex.getZeroBased()));
            }
            toAdd = new Job(client, vehicleNumber, new JobNumber(), new Date(), assignedEmployees,
                    new Status(Status.STATUS_ONGOING), new RemarkList());
            prevJobNumber = toAdd.getJobNumber().value;
        } catch (DuplicateEmployeeException e) {
            throw new CommandException("Duplicate employee index");
        }

    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        model.addJob(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddJobCommand // instanceof handles nulls
                && client.equals(((AddJobCommand) other).client)
                && vehicleNumber.equals(((AddJobCommand) other).vehicleNumber)
                && targetIndices.equals(((AddJobCommand) other).targetIndices)
                && assignedEmployees.equals(((AddJobCommand) other).assignedEmployees)
                && Objects.equals(this.toAdd, ((AddJobCommand) other).toAdd));
    }
}
```
###### \java\seedu\carvicim\logic\commands\CloseJobCommand.java
``` java
/**
 * Closes an ongoing job in Carvicim
 */
public class CloseJobCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "closej";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Closes the job specified by the job number.\n"
            + "Parameters: " + PREFIX_JOB_NUMBER + "JOB_NUMBER\n"
            + "Example: " + COMMAND_WORD + " j/22";

    public static final String MESSAGE_CLOSE_JOB_SUCCESS = "Closed Job: %1$s";

    private final JobNumber targetJobNumber;

    private Job target;
    private Job updatedJob;

    public CloseJobCommand(JobNumber targetJobNumber) {
        this.targetJobNumber = targetJobNumber;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(target);
        requireNonNull(updatedJob);
        try {
            model.closeJob(target, updatedJob);
        } catch (JobNotFoundException jnfe) {
            throw new AssertionError("The target job cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_CLOSE_JOB_SUCCESS, updatedJob));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Job> lastShownJobList = model.getFilteredJobList();
        Iterator<Job> jobIterator = lastShownJobList.iterator();

        while (jobIterator.hasNext()) {
            Job currJob = jobIterator.next();
            if (currJob.getJobNumber().equals(this.targetJobNumber)
                    && (currJob.getStatus().value).equals(Status.STATUS_ONGOING)) {
                target = currJob;
                updatedJob = createUpdatedJob(currJob);
                break;
            }
        }

        if (target == null) {
            throw new CommandException(Messages.MESSAGE_JOB_NOT_FOUND);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CloseJobCommand // instanceof handles nulls
                && this.targetJobNumber.equals(((CloseJobCommand) other).targetJobNumber) // state check
                && Objects.equals(this.target, ((CloseJobCommand) other).target));
    }

    /**
     * Creates and returns a new {@code Job} with a closed status
     */
    public static Job createUpdatedJob(Job jobToEdit) {
        assert jobToEdit != null;

        return new Job(jobToEdit.getClient(), jobToEdit.getVehicleNumber(), jobToEdit.getJobNumber(),
                jobToEdit.getDate(), jobToEdit.getAssignedEmployees(),
                new Status(Status.STATUS_CLOSED), jobToEdit.getRemarkList());
    }
}
```
###### \java\seedu\carvicim\logic\commands\ListOngoingJobCommand.java
``` java
/**
 * Lists all ongoing job in CarviciM
 */
public class ListOngoingJobCommand extends Command {

    public static final String COMMAND_WORD = "listoj";

    public static final String MESSAGE_SUCCESS = "Listed all ongoing jobs";

    @Override
    public CommandResult execute() {
        model.showOngoingJobs();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\carvicim\logic\commands\RemarkCommand.java
``` java
/**
 * Adds a remark to a job in Carvicim
 */
public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a remark to the a job.\n"
            + "Parameters: " + PREFIX_JOB_NUMBER + "JOB_NUMBER " + PREFIX_REMARK + "REMARK\n"
            + "Example: " + COMMAND_WORD + " j/1" + " r/hellooooo";

    public static final String MESSAGE_REMARK_SUCCESS = "Remark added to job number %2$s:  %1$s";

    private final JobNumber jobNumber;
    private final Remark remark;

    private Job target;
    private Job updatedJob;

    /**
     * Creates a RemarkCommand to add the specified {@code Remark}
     */
    public RemarkCommand(Remark remark, JobNumber jobNumber) {
        requireAllNonNull(remark, jobNumber);
        this.remark = remark;
        this.jobNumber = jobNumber;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(target);
        requireNonNull(updatedJob);
        try {
            model.addRemark(target, updatedJob);
        } catch (JobNotFoundException jnfe) {
            throw new AssertionError("The target job cannot be missing");
        }
        return new CommandResult(String.format(MESSAGE_REMARK_SUCCESS, remark, jobNumber));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Job> lastShownJobList = model.getFilteredJobList();
        Iterator<Job> jobIterator = lastShownJobList.iterator();

        while (jobIterator.hasNext()) {
            Job currentJob = jobIterator.next();
            if (currentJob.getJobNumber().equals(jobNumber)
                    && (currentJob.getStatus().value).equals(Status.STATUS_ONGOING)) {
                target = currentJob;
                updatedJob = createUpdatedJob(target, remark);
                break;
            }
        }

        if (target == null) {
            throw new CommandException(Messages.MESSAGE_JOB_NOT_FOUND);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof RemarkCommand
                && this.jobNumber.equals(((RemarkCommand) other).jobNumber)
                && this.remark.equals(((RemarkCommand) other).remark)
                && Objects.equals(this.target, ((RemarkCommand) other).target));
    }

    /**
     * Creates and returns a new {@code Job} with a new remark added
     */
    public static Job createUpdatedJob(Job jobToEdit, Remark remark) {
        assert jobToEdit != null;
        RemarkList remarks = new RemarkList(jobToEdit.getRemarkList().getRemarks());
        remarks.add(remark);

        return new Job(jobToEdit.getClient(), jobToEdit.getVehicleNumber(), jobToEdit.getJobNumber(),
                jobToEdit.getDate(), jobToEdit.getAssignedEmployees(), jobToEdit.getStatus(), remarks);
    }
}
```
###### \java\seedu\carvicim\logic\commands\ThemeCommand.java
``` java
/**
 * Changes the theme of the application
 */
public class ThemeCommand extends Command {
    public static final String COMMAND_WORD = "theme";

    public static final int NUMBER_OF_THEMES = 3;

    public static final String MESSAGE_THEME_CHANGE_SUCCESS = "Theme updated: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Applies selected theme\n"
            + "1. Mauve theme\n"
            + "2. Dark theme\n"
            + "3. Light theme\n"
            + "Parameters: INDEX (positive integer)\n"
            + "Example: " + COMMAND_WORD + " 2";

    private final Index selectedIndex;

    public ThemeCommand(Index selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (selectedIndex.getZeroBased() >= NUMBER_OF_THEMES) {
            throw new CommandException(Messages.MESSAGE_INVALID_THEME_INDEX);
        }
        EventsCenter.getInstance().post(new SetThemeRequestEvent(selectedIndex));
        return new CommandResult(String.format(MESSAGE_THEME_CHANGE_SUCCESS, selectedIndex.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ThemeCommand // instanceof handles nulls
                && this.selectedIndex.equals(((ThemeCommand) other).selectedIndex)); // state check

    }
}
```
###### \java\seedu\carvicim\logic\parser\AddJobCommandParser.java
``` java
/**
 * Parses the input arguments and creates a new AddJobCommand object
 */
public class AddJobCommandParser implements Parser<AddJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddJobCommand
     * and returns an AddJobCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddJobCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_VEHICLE_NUMBER, PREFIX_ASSIGNED_EMPLOYEE);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE,
                PREFIX_EMAIL, PREFIX_VEHICLE_NUMBER, PREFIX_ASSIGNED_EMPLOYEE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get();
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get();
            VehicleNumber vehicleNumber =
                    ParserUtil.parseVehicleNumber(argMultimap.getValue(PREFIX_VEHICLE_NUMBER)).get();
            ArrayList<Index> assignedEmployeeIndices =
                    ParserUtil.parseIndices(argMultimap.getAllValues(PREFIX_ASSIGNED_EMPLOYEE));

            Person client = new Person(name, phone, email);
            return new AddJobCommand(client, vehicleNumber, assignedEmployeeIndices);

        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \java\seedu\carvicim\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String remark} into a {@code Remark}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code remark} is invalid.
     */
    public static Remark parseRemark(String remark) throws IllegalValueException {
        requireNonNull(remark);
        String trimmedRemark = remark.trim();
        if (!Remark.isValidRemark(trimmedRemark)) {
            throw new IllegalValueException(Remark.MESSAGE_REMARKS_CONSTRAINTS);
        }
        return new Remark(trimmedRemark);
    }

    /**
     * Parses a {@code Optional<String> remark} into an {@code Optional<Remark>} if {@code remark} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Remark> parseRemark(Optional<String> remark) throws IllegalValueException {
        requireNonNull(remark);
        return remark.isPresent() ? Optional.of(parseRemark(remark.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String jobNumber} into a {@code JobNumber}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code jobNumber} is invalid.
     */
    public static JobNumber parseJobNumber(String jobNumber) throws IllegalValueException {
        requireNonNull(jobNumber);
        String trimmedJobNumber = jobNumber.trim();
        if (!JobNumber.isValidJobNumber(trimmedJobNumber)) {
            throw new IllegalValueException(JobNumber.MESSAGE_JOB_NUMBER_CONSTRAINTS);
        }
        return new JobNumber(trimmedJobNumber);
    }

    /**
     * Parses a {@code Optional<String> jobNumber} into an {@code Optional<JobNumber>} if {@code jobNumber} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<JobNumber> parseJobNumber(Optional<String> jobNumber) throws IllegalValueException {
        requireNonNull(jobNumber);
        return jobNumber.isPresent() ? Optional.of(parseJobNumber(jobNumber.get())) : Optional.empty();
    }
}
```
###### \java\seedu\carvicim\model\Carvicim.java
``` java
    /**
     * Adds a job to Carvicim.
     */
    public void addJob(Job job) {
        jobs.add(job);
    }

    /**
     * Removes {@code job} from this {@code Carvicim}.
     * @throws JobNotFoundException if the {@code job} is not in this {@code Carvicim}.
     */
    public boolean closeJob(Job job) throws JobNotFoundException {
        if (jobs.remove(job)) {
            return true;
        } else {
            throw new JobNotFoundException();
        }
    }


    /**
     * Replaces a target job with an updated job in CariviciM
     */
    public void updateJob(Job target, Job updatedJob) {
        jobs.replace(target, updatedJob);
    }

```
###### \java\seedu\carvicim\model\job\Date.java
``` java
/**
 * Represent the date of job creation in the servicing manager
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS = "Date should be of the format MMM DD YYYY";
    public static final String DATE_VALIDATION_REGEX = "\\w\\w\\w\\s(0[1-9]|[12][0-9]|3[01])\\s(19|20)\\d\\d";

    private static final String DATE_FORMATTER_PATTERN = "MMM dd yyyy";

    private static final String DATE_SPLIT_REGEX = " ";
    private static final int DATE_DATA_INDEX_DAY = 1;
    private static final int DATE_DATA_INDEX_MONTH = 0;
    private static final int DATE_DATA_INDEX_YEAR = 2;
    private static final String[] monthAbbreviation = {"Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public final String value;
    private int day;
    private int month;
    private int year;

    public Date() {
        value = generateDate();
    }

    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        String trimmedDate = date.trim();
        String[] splitAddress = trimmedDate.split(DATE_SPLIT_REGEX);
        this.day = Integer.parseInt(splitAddress[DATE_DATA_INDEX_DAY]);
        this.month = convertMonth(splitAddress[DATE_DATA_INDEX_MONTH]);
        this.year = Integer.parseInt(splitAddress[DATE_DATA_INDEX_YEAR]);
        this.value = date;
    }

    /**
     * Generates the string representation of the current date on the system
     */
    private String generateDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN);
        return localDate.format(formatter);
    }

    /**
     * Returns true if a given string is a valid date
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }

    /**
     * Convert month abbreviation to number
     */
    private int convertMonth(String month) {
        int i;
        for (i = 0; i < 12; i++) {
            if (month.equals(monthAbbreviation[i])) {
                break;
            }
        }
        return i + 1;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && this.value.equals(((Date) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\carvicim\model\job\Job.java
``` java
/**
 * Represents a Job in the car servicing manager
 */
public class Job {
    protected final RemarkList remarks;

    private final Person client;
    private final VehicleNumber vehicleNumber;
    private final JobNumber jobNumber;
    private final Date date;
    private final Status status;

    private final UniqueEmployeeList assignedEmployees;

    public Job(Person client, VehicleNumber vehicleNumber, JobNumber jobNumber,
               Date date, UniqueEmployeeList assignedEmployees, Status status, RemarkList remarks) {

        requireAllNonNull(client, vehicleNumber, jobNumber, date, assignedEmployees, status);
        this.client = client;
        this.vehicleNumber = vehicleNumber;
        this.jobNumber = jobNumber;
        this.date = date;
        this.assignedEmployees = assignedEmployees;
        this.status = status;
        this.remarks = remarks;
    }

    public JobNumber getJobNumber() {
        return jobNumber;
    }

    public VehicleNumber getVehicleNumber() {
        return vehicleNumber;
    }

    public Person getClient() {
        return client;
    }

    public Date getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Employee> getAssignedEmployeesAsSet() {
        return Collections.unmodifiableSet(assignedEmployees.toSet());
    }

    /**
     * Returns assignedEmployees as UniqueEmployeeList
     * @return
     */
    public UniqueEmployeeList getAssignedEmployees() {
        return assignedEmployees;
    }

    public ObservableList getAssignedEmployeesAsObservableList() {
        return assignedEmployees.asObservableList();
    }

    /**
     * Returns an arraylist of remarks
     */
    public RemarkList getRemarkList() {
        return remarks;
    }

    public void addRemark(Remark remark) {
        remarks.add(remark);
    }

    public boolean hasEmployee(Employee employee) {
        return assignedEmployees.contains(employee);
    }

    public int getEmployeeCount() {
        return assignedEmployees.size();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Job)) {
            return false;
        }

        Job otherJob = (Job) other;
        return otherJob.getClient().equals(this.getClient())
                && otherJob.getVehicleNumber().equals(this.getVehicleNumber())
                && otherJob.getJobNumber().equals(this.getJobNumber())
                && otherJob.getDate().equals(this.getDate())
                && otherJob.getAssignedEmployeesAsSet().equals(this.getAssignedEmployeesAsSet())
                && otherJob.getStatus().equals(this.getStatus())
                && otherJob.getRemarkList().equals(this.getRemarkList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, vehicleNumber, jobNumber, date,
                assignedEmployees, status, remarks);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\nJob Number: ")
                .append(getJobNumber())
                .append("[" + getStatus() + "]")
                .append(" Start Date: ")
                .append(getDate())
                .append(" \nVehicle ID: ")
                .append(getVehicleNumber())
                .append(" Client: ")
                .append(getClient())
                .append(" \nRemarks: ");

        for (Remark remark : remarks) {
            builder.append("\n" + remark);
        }

        builder.append(" \nAssigned Employees:");
        for (Employee assignedEmployee : assignedEmployees) {
            builder.append("\n" + assignedEmployee);
        }

        return builder.toString();
    }
}
```
###### \java\seedu\carvicim\model\job\JobDetailsContainKeyWordsPredicate.java
``` java
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
```
###### \java\seedu\carvicim\model\job\JobList.java
``` java
/**
 * A list of jobs that does not allow nulls
 */
public class JobList implements Iterable<Job> {

    private final ObservableList<Job> internalList = FXCollections.observableArrayList();
    private int jobCount = 0;
    private int ongoingCount = 0;
    private int closedCount = 0;
    private HashMap<Name, Integer> analyse = new HashMap<Name, Integer>();

    /**
     * Returns true if the list contains an equivalent employee as the given argument
     */
    public boolean contains(Job toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    public void setJobs(JobList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setJobs(List<Job> jobs) {
        requireAllNonNull(jobs);
        final JobList replacement = new JobList();
        for (final Job job : jobs) {
            replacement.add(job);
        }
        setJobs(replacement);
    }

    /**
     * Adds a job to the list
     *
     */
    public void add(Job toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    /**
     * Removes the equivalent job from the list
     *
     * @throws JobNotFoundException if no such job could be found in the list
     */
    public boolean remove(Job toRemove) {
        requireNonNull(toRemove);
        final boolean jobFoundAndDeleted = internalList.remove(toRemove);
        return jobFoundAndDeleted;
    }

    /**
     * Replaces the {@targetJob} with the {@updateJob}
     *
     * @throws JobNotFoundException if no {@targetJob} exists
     */
    public void replace(Job targetJob, Job updatedJob) {
        requireAllNonNull(targetJob, updatedJob);
        int targetIndex = internalList.indexOf(targetJob);
        internalList.set(targetIndex, updatedJob);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Job> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

```
###### \java\seedu\carvicim\model\job\JobNumber.java
``` java

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.AppUtil.checkArgument;

/**
 * Represent a job number in the servicing manager
 */
public class JobNumber {
    public static final String MESSAGE_JOB_NUMBER_CONSTRAINTS = "Job number should be a positive number (non-zero)";

    public static final String JOB_NUMBER_VALIDATION_REGEX = "[0-9]+";

    private static int nextJobNumber;

    public final String value;

    public JobNumber() {
        value = Integer.toString(nextJobNumber);
        incrementNextJobNumber();
    }

    public JobNumber(String jobNumber) {
        requireNonNull(jobNumber);
        checkArgument(isValidJobNumber(jobNumber), MESSAGE_JOB_NUMBER_CONSTRAINTS);
        value = jobNumber;
    }

    /**
     * Initialize the next job number of the car servicing manager
     */
    public static void initialize(String arg) {
        nextJobNumber = Integer.parseInt(arg);
    }

    public static void initialize(int arg) {
        nextJobNumber = arg;
    }

    public void incrementNextJobNumber() {
        nextJobNumber++;
    }

    public static String getNextJobNumber() {
        return nextJobNumber + "";
    }

    public static void setNextJobNumber(String arg) {
        nextJobNumber = Integer.parseInt(arg);
    }

    /**
     * Returns true if a given string is a valid job number.
     */
    public static boolean isValidJobNumber(String jobNumber) {
        return jobNumber.matches(JOB_NUMBER_VALIDATION_REGEX);
    }

    public int asInteger() {
        return Integer.parseInt(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof JobNumber // instanceof handles nulls
                && this.value.equals(((JobNumber) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\carvicim\model\job\OngoingJobPredicate.java
``` java
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
```
###### \java\seedu\carvicim\model\job\Status.java
``` java
/**
 * Represents the status of a car servicing job
 */
public class Status {
    public static final String STATUS_ONGOING = "ongoing";
    public static final String STATUS_CLOSED = "closed";

    public final String value;

    public Status(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Status // instanceof handles nulls
                && this.value.equals(((Status) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\carvicim\model\job\VehicleNumber.java
``` java
/**
 * Represents a Vehicle ID in the Job
 */
public class VehicleNumber {
    public static final String DEFAULT_VEHICLE_NUMBER = "SAS123J";
    public static final String MESSAGE_VEHICLE_ID_CONSTRAINTS =
            "Vehicle ID should only contain alphanumeric characters and should not be blank";

    public static final String VEHICLE_ID_VALIDATION_REGEX = "[a-zA-Z]+[0-9]+[a-zA-Z0-9]*|[0-9]+[a-zA-Z][a-zA-Z0-9]*";

    public final String value;

    public VehicleNumber(String value) {
        requireNonNull(value);
        checkArgument(isValidVehicleNumber(value), MESSAGE_VEHICLE_ID_CONSTRAINTS);
        this.value = value;
    }

    /**
     * Returns true if a given string is a valid vehicle ID
     */
    public static boolean isValidVehicleNumber(String test) {
        return test.matches(VEHICLE_ID_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof VehicleNumber // instanceof handles nulls
                && this.value.equals(((VehicleNumber) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }


}
```
###### \java\seedu\carvicim\model\remark\Remark.java
``` java
/**
 * Represents a remark for a job in the car servicing manager
 */
public class Remark {
    public static final String MESSAGE_REMARKS_CONSTRAINTS =
            "Remark can take any values, and it should not be blank";

    /*
     * Remark argument should be anything, except just whitespace or newline
     */
    public static final String REMARK_VALIDATION_REGEX = "(?!^ +$)^.+$";

    public final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A valid remark.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        checkArgument(isValidRemark(remark), MESSAGE_REMARKS_CONSTRAINTS);
        this.value = remark;
    }

    /**
     * Returns true if a given string is a valid remark.
     */
    public static boolean isValidRemark(String test) {
        return test.matches(REMARK_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && this.value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\carvicim\model\remark\RemarkList.java
``` java
/**
 * Represents a list of remarks that enforces no nulls
 */
public class RemarkList implements Iterable<Remark> {

    private final ArrayList<Remark> internalList;

    /**
     * Constructs empty RemarkList.
     */
    public RemarkList() {
        internalList = new ArrayList<>();
    }

    /**
     * Creates a RemarkList using given remarks.
     * Enforces no nulls.
     */
    public RemarkList(ArrayList<Remark> remarks) {
        requireAllNonNull(remarks);
        internalList = new ArrayList<>();
        internalList.addAll(remarks);
    }

    /**
     * Adds a Remark to the list.
     */
    public void add(Remark toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    @Override
    public Iterator<Remark> iterator() {
        return internalList.iterator();
    }

    /**
     * Returns remark list
     */
    public ArrayList<Remark> getRemarks() {
        return internalList;
    }

    /**
     * Returns the list as an unmodifiable {@code RemarkList}.
     */
    public ObservableList<Remark> asObservableList() {
        return FXCollections.observableArrayList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RemarkList // instanceof handles nulls
                        && this.internalList.equals(((RemarkList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\carvicim\storage\XmlAdaptedJob.java
``` java
/**
 * JAXB-friendly version of the Job
 */
public class XmlAdaptedJob {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Job's %s field is missing!";

    @XmlElement(required = true)
    private String jobNumber;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String vehicleNumber;
    @XmlElement(required = true)
    private String status;
    @XmlElement(required = true)
    private String date;

    @XmlElement(required = true)
    private List<XmlAdaptedEmployee> assignedEmployees = new ArrayList<>();
    @XmlElement(required = true)
    private List<XmlAdaptedRemark> remarks = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedJob.
     * This is the no-arg constructor that is required by JAXB
     */
    public XmlAdaptedJob() {}

    /**
     * Constructs an {@code XmlAdaptedJob} with the given job details.
     */
    public XmlAdaptedJob(String jobNumber, String name, String phone, String email, String vehicleNumber,
                         String status, String date, List<XmlAdaptedEmployee> assignedEmployees,
                         List<XmlAdaptedRemark> remarks) {

        this.jobNumber = jobNumber;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.vehicleNumber = vehicleNumber;
        this.status = status;
        this.date = date;
        if (assignedEmployees != null) {
            this.assignedEmployees = new ArrayList<>(assignedEmployees);
        }
        if (remarks != null) {
            this.remarks = new ArrayList<>(remarks);
        }
    }

    /**
     * Converts a given Job into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEmployee
     */
    public XmlAdaptedJob(Job source) {
        jobNumber = source.getJobNumber().value;
        name = source.getClient().getName().fullName;
        phone = source.getClient().getPhone().value;
        email = source.getClient().getEmail().value;
        status = source.getStatus().value;
        vehicleNumber = source.getVehicleNumber().value;
        date = source.getDate().value;
        for (Employee employee : source.getAssignedEmployees()) {
            assignedEmployees.add(new XmlAdaptedEmployee(employee));
        }
        for (Remark remark : source.getRemarkList()) {
            remarks.add(new XmlAdaptedRemark(remark));
        }
    }

    /**
     * Converts this jaxb-friendly adapted job object into the model's Job object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted job
     */
    public Job toModelType() throws IllegalValueException {
        final List<Remark> jobRemarks = new ArrayList<>();
        final List<Employee> jobAssignedEmployees = new ArrayList<>();
        for (XmlAdaptedRemark remark : remarks) {
            jobRemarks.add(remark.toModelType());
        }
        for (XmlAdaptedEmployee assignedEmployee : assignedEmployees) {
            jobAssignedEmployees.add(assignedEmployee.toModelType());
        }

        if (this.jobNumber == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    JobNumber.class.getSimpleName()));
        }
        if (!JobNumber.isValidJobNumber(this.jobNumber)) {
            throw new IllegalValueException(JobNumber.MESSAGE_JOB_NUMBER_CONSTRAINTS);
        }
        final JobNumber jobNumber = new JobNumber(this.jobNumber);

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone phone = new Phone(this.phone);

        if (this.email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(this.email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email email = new Email(this.email);
        final Person client = new Person(name, phone, email);

        if (this.vehicleNumber == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    VehicleNumber.class.getSimpleName()));
        }
        if (!VehicleNumber.isValidVehicleNumber(this.vehicleNumber)) {
            throw new IllegalValueException(VehicleNumber.MESSAGE_VEHICLE_ID_CONSTRAINTS);
        }
        final VehicleNumber vehicleNumber = new VehicleNumber(this.vehicleNumber);

        if (this.status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName()));
        }
        final Status status = new Status(this.status);

        if (this.date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(this.date)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        final Date date = new Date(this.date);


        final RemarkList remarks = new RemarkList(new ArrayList<>(jobRemarks));
        final UniqueEmployeeList assignedEmployees = new UniqueEmployeeList();
        assignedEmployees.setEmployees(jobAssignedEmployees);

        return new Job(client, vehicleNumber, jobNumber, date, assignedEmployees, status, remarks);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedJob)) {
            return false;
        }

        XmlAdaptedJob otherJob = (XmlAdaptedJob) other;
        return Objects.equals(jobNumber, otherJob.jobNumber)
                && Objects.equals(name, otherJob.name)
                && Objects.equals(phone, otherJob.phone)
                && Objects.equals(email, otherJob.email)
                && Objects.equals(date, otherJob.date)
                && Objects.equals(vehicleNumber, otherJob.vehicleNumber)
                && Objects.equals(status, otherJob.status)
                && assignedEmployees.equals(otherJob.assignedEmployees)
                && remarks.equals(otherJob.remarks);
    }
}
```
###### \java\seedu\carvicim\storage\XmlAdaptedRemark.java
``` java

import javax.xml.bind.annotation.XmlValue;

import seedu.carvicim.commons.exceptions.IllegalValueException;
import seedu.carvicim.model.remark.Remark;

/**
 * JAXB-friendly adapted version of the Remark.
 */
public class XmlAdaptedRemark {

    @XmlValue
    private String remark;

    /**
     * Constructs an XmlAdaptedRemark.
     * This is the no-arg constructor that is required by JAXB
     */
    public XmlAdaptedRemark() {}

    /**
     * Construct a {@code XmlAdaptedRemark} with the given {@code remark}.
     */
    public XmlAdaptedRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Converts a given Remark into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedRemark(Remark source) {
        remark = source.value;
    }

    /**
     * Converts this jaxb-friendly adapted remark object into the model's Remark object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted job
     */
    public Remark toModelType() throws IllegalValueException {
        if (!Remark.isValidRemark(remark)) {
            throw new IllegalValueException(Remark.MESSAGE_REMARKS_CONSTRAINTS);
        }
        return new Remark(remark);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof  XmlAdaptedRemark)) {
            return false;
        }

        return remark.equals(((XmlAdaptedRemark) other).remark);
    }
}
```
###### \java\seedu\carvicim\ui\JobDisplayPanel.java
``` java
/**
 * The Job Display Panel of the App.
 */
public class JobDisplayPanel extends UiPart<Region> {

    private static final String FXML = "JobDisplayPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private GridPane jobDisplay;
    @FXML
    private Label jobNumber;
    @FXML
    private Label status;
    @FXML
    private Label date;
    @FXML
    private Label vehicleNumber;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private ListView remarks;
    @FXML
    private ListView assignedEmployees;

    public JobDisplayPanel() {
        super(FXML);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleJobPanelSelectionChangedEvent(JobPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        updateFxmlElements(event.getJob());
    }

    @Subscribe
    private void handlJobDisplayPanelUpdateRequestEvent(JobDisplayPanelUpdateRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        updateFxmlElements(event.getJob());
    }

    @Subscribe
    private void handleJobDisplayPanelResetRequestEvent(JobDisplayPanelResetRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        clearFxmlElements();
    }

    /**
     * Updates the necessary FXML elements
     */
    private void updateFxmlElements(Job job) {
        assignedEmployees.setVisible(true);
        remarks.setVisible(true);

        //Clear previous selection's information
        assignedEmployees.refresh();
        remarks.refresh();

        jobNumber.setText(job.getJobNumber().toString());

        status.setText(job.getStatus().toString());
        setStatusLabelColour(job.getStatus().value);

        date.setText(job.getDate().toString());
        vehicleNumber.setText(job.getVehicleNumber().toString());
        name.setText(job.getClient().getName().toString());
        phone.setText(job.getClient().getPhone().toString());
        email.setText(job.getClient().getEmail().toString());

        assignedEmployees.setItems(job.getAssignedEmployeesAsObservableList());
        remarks.setItems(job.getRemarkList().asObservableList());
    }

    private void setStatusLabelColour(String status) {
        if (status.equals(Status.STATUS_ONGOING)) {
            this.status.setStyle("-fx-text-fill: green");
        } else {
            this.status.setStyle("-fx-text-fill: red");
        }
    }

    /**
     * Clear the FXML elements
     */
    private void clearFxmlElements() {
        assignedEmployees.setVisible(false);
        remarks.setVisible(false);

        jobNumber.setText("");
        status.setText("");
        date.setText("");
        vehicleNumber.setText("");
        name.setText("");
        phone.setText("");
        email.setText("");
    }
}
```
###### \java\seedu\carvicim\ui\MainWindow.java
``` java
    /**
     * Sets the the theme based on user's preference
     */
    private void setTheme(Index selectedIndex) throws CommandException {
        String themeName = themes[selectedIndex.getZeroBased()];
        if (MainApp.class.getResource(FXML_FILE_FOLDER + themeName + "Theme.css") == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_FILE_PATH);
        }

        getRoot().getScene().getStylesheets().clear();
        getRoot().getScene().getStylesheets().add(FXML_FILE_FOLDER + themeName + "Theme.css");
        getRoot().getScene().getStylesheets().add(FXML_FILE_FOLDER + "Extensions" + themeName + ".css");
        prefs.setExtensionName("Extensions" + themeName);
        prefs.setThemeName(themeName + "Theme");

    }

    @Subscribe
    private void handleSetThemeRequestEvent(SetThemeRequestEvent event) throws CommandException {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        setTheme(event.getSelectedIndex());
    }
}
```
###### \resources\view\JobDisplayPanel.fxml
``` fxml

<StackPane minHeight="-Infinity" minWidth="-Infinity" prefHeight="328.0" prefWidth="426.0" xmlns="http://javafx.com/javafx/9.0.1" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <GridPane fx:id="jobDisplay" gridLinesVisible="true" prefHeight="395.0" prefWidth="426.0">
        <columnConstraints>
          <ColumnConstraints maxWidth="645.0" minWidth="10.0" prefWidth="159.0" />
          <ColumnConstraints hgrow="SOMETIMES" maxWidth="709.0" minWidth="0.0" prefWidth="267.0" />
        </columnConstraints>
        <rowConstraints>
          <RowConstraints maxHeight="197.0" minHeight="0.0" prefHeight="25.0" valignment="TOP" vgrow="SOMETIMES" />
          <RowConstraints maxHeight="395.0" minHeight="0.0" prefHeight="27.0" valignment="TOP" vgrow="SOMETIMES" />
            <RowConstraints maxHeight="540.0" minHeight="2.0" prefHeight="25.0" valignment="BASELINE" vgrow="SOMETIMES" />
          <RowConstraints maxHeight="540.0" minHeight="10.0" prefHeight="25.0" valignment="TOP" vgrow="SOMETIMES" />
            <RowConstraints maxHeight="540.0" minHeight="10.0" prefHeight="63.0" valignment="TOP" vgrow="SOMETIMES" />
            <RowConstraints maxHeight="540.0" minHeight="10.0" prefHeight="76.0" valignment="TOP" />
            <RowConstraints maxHeight="540.0" minHeight="10.0" prefHeight="89.0" valignment="TOP" vgrow="SOMETIMES" />
        </rowConstraints>
         <children>
            <Label alignment="TOP_LEFT" styleClass="label-header" text="Job Number:" underline="true">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
               <opaqueInsets>
                  <Insets />
               </opaqueInsets>
            </Label>
            <Label alignment="TOP_LEFT" styleClass="label-header" text="Status:" underline="true" GridPane.rowIndex="1">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
               <opaqueInsets>
                  <Insets />
               </opaqueInsets>
            </Label>
            <Label alignment="TOP_LEFT" prefHeight="20.0" prefWidth="155.0" styleClass="label-header" text="Date Added:" underline="true" GridPane.rowIndex="2">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <Label alignment="TOP_LEFT" prefHeight="20.0" prefWidth="224.0" styleClass="label-header" text="Vehicle Number:" underline="true" GridPane.rowIndex="3">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <Label alignment="TOP_LEFT" prefHeight="20.0" prefWidth="230.0" styleClass="label-header" text="Client Details:" underline="true" GridPane.rowIndex="4">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <VBox prefHeight="88.0" prefWidth="593.0" GridPane.columnIndex="1" GridPane.rowIndex="4">
               <children>
                  <Label fx:id="name" lineSpacing="2.0">
                     <padding>
                        <Insets left="5.0" />
                     </padding></Label>
                  <Label fx:id="phone" lineSpacing="2.0">
                     <padding>
                        <Insets left="5.0" />
                     </padding></Label>
                  <Label fx:id="email" lineSpacing="2.0">
                     <padding>
                        <Insets left="5.0" />
                     </padding></Label>
               </children>
            </VBox>
            <Label alignment="TOP_LEFT" prefHeight="20.0" prefWidth="239.0" styleClass="label-header" text="Assigned Employees:" underline="true" GridPane.rowIndex="5">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <Label alignment="TOP_LEFT" prefHeight="20.0" prefWidth="116.0" styleClass="label-header" text="Remarks:" underline="true" GridPane.rowIndex="6">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <Label fx:id="vehicleNumber" GridPane.columnIndex="1" GridPane.rowIndex="3">
               <padding>
                  <Insets left="5.0" />
               </padding></Label>
            <Label fx:id="date" GridPane.columnIndex="1" GridPane.rowIndex="2">
               <padding>
                  <Insets left="5.0" />
               </padding></Label>
            <Label fx:id="status" GridPane.columnIndex="1" GridPane.rowIndex="1">
               <padding>
                  <Insets left="5.0" />
               </padding></Label>
            <Label fx:id="jobNumber" GridPane.columnIndex="1">
               <padding>
                  <Insets left="5.0" />
               </padding></Label>
            <ListView fx:id="assignedEmployees" prefHeight="200.0" prefWidth="200.0" styleClass="label" visible="false" GridPane.columnIndex="1" GridPane.rowIndex="5">
               <GridPane.margin>
                  <Insets bottom="10.0" left="3.0" right="3.0" top="10.0" />
               </GridPane.margin></ListView>
            <ListView fx:id="remarks" prefHeight="200.0" prefWidth="200.0" styleClass="label" visible="false" GridPane.columnIndex="1" GridPane.rowIndex="6">
               <GridPane.margin>
                  <Insets bottom="10.0" left="3.0" right="3.0" top="10.0" />
               </GridPane.margin>
            </ListView>
         </children>
         <effect>
            <Glow />
         </effect>
      </GridPane>
   </children>
</StackPane>
```
###### \resources\view\LightTheme.css
``` css
.background {
    -fx-background-color: derive(#e2d4cf, 20%);
    background-color: #ffffff; /* Used in the default.html file */
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: black;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #ffffff;
    -fx-control-inner-background: #ffffff;
    -fx-background-color: #ffffff;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: derive(#a6a6a6, 20%);
    -fx-border-color: transparent transparent transparent #4d4d4d;
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 1;
    -fx-background-color: derive(#ffffff, 20%);
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: derive(#ffffff, 20%);
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
}

.list-cell:filled:even {
    -fx-background-color: #f3f7ed;
}

.list-cell:filled:odd {
    -fx-background-color: #f2f2f2;
}

.list-cell:filled:selected {
    -fx-background-color: #ffffcc;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: #999900;
    -fx-border-width: 1;
}

.list-cell .label {
    -fx-text-fill: #402c26;
}

.cell_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 16px;
    -fx-text-fill: #010504;
}

.cell_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 13px;
    -fx-text-fill: #010504;
}

.anchor-pane {
     -fx-background-color: derive(#ffffff, 20%);
}

.pane-with-border {
     -fx-background-color: derive(#ffffff, 20%);
     -fx-border-color: derive(#ffffff, 10%);
     -fx-border-top-width: 1px;
}

.status-bar {
    -fx-background-color: derive(#b48a7e, 20%);
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: transparent;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
}

.status-bar-with-border {
    -fx-background-color: derive(#eadedb, 30%);
    -fx-border-color: derive(#eadedb, 25%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: white;
}

.grid-pane {
    -fx-background-color: derive(#ffffff, 30%);
    -fx-border-color: derive(#ffffff, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#a6a6a6, 30%);
}

.context-menu {
    -fx-background-color: derive(#a6a6a6, 50%);
}

.context-menu .label {
    -fx-text-fill: brown;
}

.menu-bar {
    -fx-background-color: derive(#a6a6a6, 20%);
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: white;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: #e2e2e2;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #1d1d1d;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #d8d8d8;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #3a3a3a;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: white;
  -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: white, white;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #1d1d1d;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #eadedb;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #eadedb;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: brown;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#eadedb, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: white;
    -fx-text-fill: white;
}

.scroll-bar {
    -fx-background-color: derive(#c5d7a7, 20%);
}

.scroll-bar .thumb {
    -fx-background-color: derive(#ffffff, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #F70D1A;
}

#commandTextField {
    -fx-background-color: transparent #ece2df transparent #ece2df;
    -fx-background-insets: 0;
    -fx-border-color: #f3f7ed #f3f7ed #30211d #f3f7ed;
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: #402c26;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, #503730, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: transparent, #b9d095, transparent, #f2f2f2;
    -fx-background-radius: 0;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-text-fill: white;
    -fx-background-color: #75a3a3;
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}
```
###### \resources\view\MauveTheme.css
``` css
.background {
    -fx-background-color: derive(#e2d4cf, 20%);
    background-color: #ece2df; /* Used in the default.html file */
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #725d40;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: #725d40;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #ece2df;
    -fx-control-inner-background: #ece2df;
    -fx-background-color: #ece2df;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: derive(#eadedb, 20%);
    -fx-border-color: transparent transparent transparent #916155;
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 1;
    -fx-background-color: derive(#eadedb, 20%);
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: derive(#eadedb, 20%);
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
}

.list-cell:filled:even {
    -fx-background-color: #f5f1ef;
}

.list-cell:filled:odd {
    -fx-background-color: #d8c8c0;
}

.list-cell:filled:selected {
    -fx-background-color: #ff9966;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: #cc4400;
    -fx-border-width: 1;
}

.list-cell .label {
    -fx-text-fill: #402c26;
}

.cell_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 16px;
    -fx-text-fill: #010504;
}

.cell_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 13px;
    -fx-text-fill: #010504;
}

.anchor-pane {
     -fx-background-color: derive(#eadedb, 20%);
}

.pane-with-border {
     -fx-background-color: derive(#eadedb, 20%);
     -fx-border-color: derive(#eadedb, 10%);
     -fx-border-top-width: 1px;
}

.status-bar {
    -fx-background-color: derive(#b48a7e, 20%);
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: transparent;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: brown;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
}

.status-bar-with-border {
    -fx-background-color: derive(#eadedb, 30%);
    -fx-border-color: derive(#eadedb, 25%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: white;
}

.grid-pane {
    -fx-background-color: derive(#eadedb, 30%);
    -fx-border-color: derive(#eadedb, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#b48a7e, 30%);
}

.context-menu {
    -fx-background-color: derive(#b48a7e, 50%);
}

.context-menu .label {
    -fx-text-fill: brown;
}

.menu-bar {
    -fx-background-color: derive(#b48a7e, 20%);
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: white;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: #e2e2e2;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #1d1d1d;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #d8d8d8;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #3a3a3a;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: white;
  -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: white, white;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #1d1d1d;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #eadedb;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #eadedb;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: brown;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#eadedb, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: white;
    -fx-text-fill: white;
}

.scroll-bar {
    -fx-background-color: derive(#eadedb, 20%);
}

.scroll-bar .thumb {
    -fx-background-color: derive(#eadedb, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #F70D1A;
}

#commandTextField {
    -fx-background-color: transparent #ece2df transparent #ece2df;
    -fx-background-insets: 0;
    -fx-border-color: #ece2df #ece2df #30211d #ece2df;
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: #402c26;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, #503730, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: transparent, #f5f0ef, transparent, #f5f0ef;
    -fx-background-radius: 0;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-text-fill: #402c26;
    -fx-background-color: #dda1dd;
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}
```
