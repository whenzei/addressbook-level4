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

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a job to the Carvicim. "
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

    private Job jobToClose;

    public CloseJobCommand(JobNumber targetJobNumber) {
        this.targetJobNumber = targetJobNumber;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(jobToClose);
        try {
            model.closeJob(jobToClose);
        } catch (JobNotFoundException jnfe) {
            throw new AssertionError("The target job cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_CLOSE_JOB_SUCCESS, jobToClose));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Job> lastShownJobList = model.getFilteredJobList();
        Iterator<Job> jobIterator = lastShownJobList.iterator();

        while (jobIterator.hasNext()) {
            Job currJob = jobIterator.next();
            if (currJob.getJobNumber().equals(this.targetJobNumber)) {
                jobToClose = currJob;
                break;
            }
        }

        if (jobToClose == null) {
            throw new CommandException(Messages.MESSAGE_JOB_NOT_FOUND);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CloseJobCommand // instanceof handles nulls
                && this.targetJobNumber.equals(((CloseJobCommand) other).targetJobNumber) // state check
                && Objects.equals(this.jobToClose, ((CloseJobCommand) other).jobToClose));
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

    public static final String MESSAGE_REMARK_SUCCESS = "Remark added:  %1$s";

    private final JobNumber jobNumber;
    private final Remark remark;

    private Job target;

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
        try {
            model.addRemark(target, remark);
        } catch (JobNotFoundException jnfe) {
            throw new AssertionError("The target job cannot be missing");
        }
        EventsCenter.getInstance().post(new JobDisplayPanelUpdateRequestEvent(target));
        return new CommandResult(String.format(MESSAGE_REMARK_SUCCESS, remark));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Job> lastShownJobList = model.getFilteredJobList();
        Iterator<Job> jobIterator = lastShownJobList.iterator();

        while (jobIterator.hasNext()) {
            Job currentJob = jobIterator.next();
            if (currentJob.getJobNumber().equals(jobNumber)) {
                target = currentJob;
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
}
```
###### \java\seedu\carvicim\logic\commands\ThemeCommand.java
``` java
/**
 * Changes the theme of the application
 */
public class ThemeCommand extends Command {
    public static final String COMMAND_WORD = "theme";

    public static final int NUMBER_OF_THEMES = 2;

    public static final String MESSAGE_THEME_CHANGE_SUCCESS = "Theme updated: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Applies selected theme\n"
            + "1. Teal theme\n"
            + "2. Dark theme\n"
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
     * Adds a remark to a specified job in Carvicim
     */
    public void addRemark(Job job, Remark remark) {
        Iterator<Job> iterator = jobs.iterator();
        while (iterator.hasNext()) {
            Job currJob = iterator.next();
            if (currJob.equals(job)) {
                job.addRemark(remark);
                break;
            }
        }
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
    public Set<Employee> getAssignedEmployees() {
        return Collections.unmodifiableSet(assignedEmployees.toSet());
    }

    public ObservableList getAssignedEmployeesAsObservableList() {
        return assignedEmployees.asObservableList();
    }

    /**
     * Returns an arraylist of remarks
     */
    public ArrayList<Remark> getRemarks() {
        return remarks.getRemarks();
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
                && otherJob.getAssignedEmployees().equals(this.getAssignedEmployees())
                && otherJob.getStatus().equals(this.getStatus())
                && otherJob.getRemarks().equals(this.getRemarks());
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
    public boolean remove(Job toRemove) throws JobNotFoundException {
        requireNonNull(toRemove);
        final boolean jobFoundAndDeleted = internalList.remove(toRemove);
        return jobFoundAndDeleted;
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Job> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

```
###### \java\seedu\carvicim\model\job\JobList.java
``` java
    @Override
    public Iterator<Job> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof JobList // instanceof handles nulls
                && this.internalList.equals(((JobList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\carvicim\model\job\JobNumber.java
``` java
/**
 * Represent a job number in the servicing manager
 */
public class JobNumber {
    public static final String MESSAGE_JOB_NUMBER_CONSTRAINTS = "Job number should be a positive number (non-zero)";

    private static int nextJobNumber;

    public final String value;

    public JobNumber() {
        value = Integer.toString(nextJobNumber);
        incrementNextJobNumber();
    }

    public JobNumber(String jobNumber) {
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

    public static void incrementNextJobNumber() {
        nextJobNumber++;
    }

    /**
     * Returns true if a given string is a valid job number.
     */
    public static boolean isValidJobNumber(String jobNumber) {
        int value = Integer.parseInt(jobNumber);
        return (value > 0);

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
###### \java\seedu\carvicim\model\ModelManager.java
``` java
    /**
     * Initializes the running job number based on the past job numbers.
     */
    @Override
    public void initJobNumber() {
        if (filteredJobs.isEmpty()) {
            JobNumber.initialize(ONE_AS_STRING);
            return;
        }
        int largest = filteredJobs.get(0).getJobNumber().asInteger();
        for (Job job : filteredJobs) {
            if (job.getJobNumber().asInteger() > largest) {
                largest = job.getJobNumber().asInteger();
            }
        }
        JobNumber.initialize(largest + 1);
    }

    @Override
    public void resetData(ReadOnlyCarvicim newData, CommandWords newCommandWords) {
        carvicim.resetData(newData);
        commandWords.resetData(newCommandWords);
        indicateAddressBookChanged();
    }

    @Override
    public CommandWords getCommandWords() {
        return commandWords;
    }

    @Override
    public String appendCommandKeyToMessage(String message) {
        StringBuilder builder = new StringBuilder(message);
        builder.append("\n");
        builder.append(commandWords.toString());
        return builder.toString();
    }

    @Override
    public ReadOnlyCarvicim getCarvicim() {
        return carvicim;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new CarvicimChangedEvent(carvicim));
    }

    @Override
    public synchronized void addJob(Job job) {
        carvicim.addJob(job);
        updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void closeJob(Job target) throws JobNotFoundException {
        carvicim.closeJob(target);
        updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void archiveJob(DateRange dateRange) {
        carvicim.archiveJob(dateRange);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void deletePerson(Employee target) throws EmployeeNotFoundException {
        carvicim.removeEmployee(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addRemark(Job job, Remark remark) {
        carvicim.addRemark(job, remark);
        updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(Employee employee) throws DuplicateEmployeeException {
        carvicim.addEmployee(employee);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void addJobs(List<Job> jobs) {
        for (Job job : jobs) {
            addMissingEmployees(job.getAssignedEmployees());
            addJob(job);
        }
    }

    @Override
    public void addMissingEmployees(Set<Employee> employees) {
        Iterator<Employee> employeeIterator = employees.iterator();
        while (employeeIterator.hasNext()) {
            try {
                addPerson(employeeIterator.next());
            } catch (DuplicateEmployeeException e) {
                // discard the result
            }
        }
    }

    @Override
    public void updatePerson(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException {
        requireAllNonNull(target, editedEmployee);

        carvicim.updateEmployee(target, editedEmployee);
        indicateAddressBookChanged();
    }

    @Override
    public void sortPersonList() {
        carvicim.sortList();
        indicateAddressBookChanged();
    }

    @Override
    public JobList analyseJob(JobList jobList) {
        return carvicim.analyseJob(jobList);
    }

    //=========== Filtered Employee List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Employee} backed by the internal list of
     * {@code carvicim}
     */
    @Override
    public ObservableList<Employee> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredEmployees);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Employee> predicate) {
        requireNonNull(predicate);
        filteredEmployees.setPredicate(predicate);
    }

    //=========== Filtered Job List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Job} backed by the internal list of
     * {@code carvicim}
     */
    @Override
    public ObservableList<Job> getFilteredJobList() {
        return FXCollections.unmodifiableObservableList(filteredJobs);
    }

    @Override
    public void updateFilteredJobList(Predicate<Job> predicate) {
        requireNonNull(predicate);
        filteredJobs.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return carvicim.equals(other.carvicim)
                && filteredEmployees.equals(other.filteredEmployees)
                && filteredJobs.equals(other.filteredJobs)
                && commandWords.equals(other.getCommandWords());
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
        for (Remark remark : source.getRemarks()) {
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
     * Converts a given Tag into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedRemark(Remark source) {
        remark = source.value;
    }

    /**
     * Converts this jaxb-friendly adapted tag object into the model's Remark object.
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
    private FlowPane remarks;
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
    private void handlJobDisplayPanelUpdateRequest(JobDisplayPanelUpdateRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        updateFxmlElements(event.getJob());
    }

    /**
     * Updates the necessary FXML elements
     */
    private void updateFxmlElements(Job job) {
        assignedEmployees.setVisible(true);

        //Clear previous selection's information
        assignedEmployees.refresh();
        remarks.getChildren().clear();

        jobNumber.setText(job.getJobNumber().toString());
        status.setText(job.getStatus().toString());
        date.setText(job.getDate().toString());
        vehicleNumber.setText(job.getVehicleNumber().toString());
        name.setText(job.getClient().getName().toString());
        phone.setText(job.getClient().getPhone().toString());
        email.setText(job.getClient().getEmail().toString());

        assignedEmployees.setItems(job.getAssignedEmployeesAsObservableList());

        int count = 1;
        Iterator<Remark> remarkIterator = job.getRemarks().iterator();
        while (remarkIterator.hasNext()) {
            remarks.getChildren().add(new Label(count + ") " + remarkIterator.next().value));
            count++;
        }
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

<StackPane xmlns="http://javafx.com/javafx/9.0.1" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <GridPane fx:id="jobDisplay" prefHeight="608.0" prefWidth="647.0" stylesheets="@TealTheme.css">
        <columnConstraints>
          <ColumnConstraints maxWidth="645.0" minWidth="10.0" prefWidth="194.0" />
          <ColumnConstraints hgrow="SOMETIMES" maxWidth="541.0" minWidth="0.0" prefWidth="451.0" />
        </columnConstraints>
        <rowConstraints>
          <RowConstraints maxHeight="197.0" minHeight="0.0" prefHeight="36.0" valignment="TOP" vgrow="SOMETIMES" />
          <RowConstraints maxHeight="395.0" minHeight="0.0" prefHeight="32.0" valignment="TOP" vgrow="SOMETIMES" />
            <RowConstraints maxHeight="540.0" minHeight="10.0" prefHeight="39.0" valignment="BASELINE" vgrow="SOMETIMES" />
          <RowConstraints maxHeight="540.0" minHeight="10.0" prefHeight="36.0" valignment="TOP" vgrow="SOMETIMES" />
            <RowConstraints maxHeight="540.0" minHeight="10.0" prefHeight="88.0" valignment="TOP" vgrow="SOMETIMES" />
            <RowConstraints maxHeight="540.0" minHeight="10.0" prefHeight="72.0" valignment="TOP" />
            <RowConstraints maxHeight="540.0" minHeight="10.0" prefHeight="314.0" valignment="TOP" vgrow="SOMETIMES" />
        </rowConstraints>
         <children>
            <Label alignment="TOP_LEFT" styleClass="label-bright" text="Job Number:" underline="true">
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
            <Label alignment="TOP_LEFT" styleClass="label-bright" text="Status:" underline="true" GridPane.rowIndex="1">
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
            <Label alignment="TOP_LEFT" styleClass="label-bright" text="Date Added:" underline="true" GridPane.rowIndex="2">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <Label alignment="TOP_LEFT" styleClass="label-bright" text="Vehicle Number:" underline="true" GridPane.rowIndex="3">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <Label alignment="TOP_LEFT" styleClass="label-bright" text="Client Details:" underline="true" GridPane.rowIndex="4">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <VBox prefHeight="200.0" prefWidth="100.0" GridPane.columnIndex="1" GridPane.rowIndex="4">
               <children>
                  <Label fx:id="name" lineSpacing="2.0" styleClass="label-bright" />
                  <Label fx:id="phone" lineSpacing="2.0" styleClass="label-bright" />
                  <Label fx:id="email" lineSpacing="2.0" styleClass="label-bright" />
               </children>
            </VBox>
            <Label alignment="TOP_LEFT" prefHeight="18.0" prefWidth="151.0" styleClass="label-bright" text="Assigned Employees:" underline="true" GridPane.rowIndex="5">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <Label alignment="TOP_LEFT" styleClass="label-bright" text="Remarks:" underline="true" GridPane.rowIndex="6">
               <font>
                  <Font name="MS Outlook" size="16.0" />
               </font>
               <padding>
                  <Insets left="5.0" top="2.0" />
               </padding>
            </Label>
            <FlowPane fx:id="remarks" orientation="VERTICAL" prefHeight="200.0" prefWidth="200.0" styleClass="label-bright" GridPane.columnIndex="1" GridPane.rowIndex="6" />
            <Label fx:id="vehicleNumber" styleClass="label-bright" GridPane.columnIndex="1" GridPane.rowIndex="3" />
            <Label fx:id="date" styleClass="label-bright" GridPane.columnIndex="1" GridPane.rowIndex="2" />
            <Label fx:id="status" styleClass="label-bright" GridPane.columnIndex="1" GridPane.rowIndex="1" />
            <Label fx:id="jobNumber" styleClass="label-bright" GridPane.columnIndex="1" />
            <ListView fx:id="assignedEmployees" prefHeight="200.0" prefWidth="200.0" styleClass="list-cell" visible="false" GridPane.columnIndex="1" GridPane.rowIndex="5" />
         </children>
         <effect>
            <Glow />
         </effect>
      </GridPane>
   </children>
</StackPane>
```
