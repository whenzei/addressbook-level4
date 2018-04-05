# richardson0694
###### \java\seedu\carvicim\commons\util\FileUtil.java
``` java
    /**
     * Creates a file if it does not exist along with its missing parent directories.
     * @throws IOException if the file or directory cannot be created.
     */
    public static void createEvenIfExist(File file) throws IOException {
        if (isFileExists(file)) {
            file.delete();
            createFile(file);
        } else {
            createFile(file);
        }
    }

    /**
     * Creates a file if it does not exist along with its missing parent directories
     *
     * @return true if file is created, false if file already exists
     */
    public static boolean createFile(File file) throws IOException {
        if (file.exists()) {
            return false;
        }

        createParentDirsOfFile(file);

        return file.createNewFile();
    }

    /**
     * Creates the given directory along with its parent directories
     *
     * @param dir the directory to be created; assumed not null
     * @throws IOException if the directory or a parent directory cannot be created
     */
    public static void createDirs(File dir) throws IOException {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to make directories of " + dir.getName());
        }
    }

    /**
     * Creates parent directories of file if it has a parent directory
     */
    public static void createParentDirsOfFile(File file) throws IOException {
        File parentDir = file.getParentFile();

        if (parentDir != null) {
            createDirs(parentDir);
        }
    }

    /**
     * Assumes file exists
     */
    public static String readFromFile(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), CHARSET);
    }

    /**
     * Writes given string to a file.
     * Will create the file if it does not exist yet.
     */
    public static void writeToFile(File file, String content) throws IOException {
        Files.write(file.toPath(), content.getBytes(CHARSET));
    }

    /**
     * Converts a string to a platform-specific file path
     * @param pathWithForwardSlash A String representing a file path but using '/' as the separator
     * @return {@code pathWithForwardSlash} but '/' replaced with {@code File.separator}
     */
    public static String getPath(String pathWithForwardSlash) {
        checkArgument(pathWithForwardSlash.contains("/"));
        return pathWithForwardSlash.replace("/", File.separator);
    }

}
```
###### \java\seedu\carvicim\logic\commands\AnalyseCommand.java
``` java
/**
 * Analyse job entries
 */
public class AnalyseCommand extends Command {

    public static final String COMMAND_WORD = "analyse";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Analyse job entries within the month.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Result: %1$s";

    private JobList toAnalyse;

    /**
     * Creates an ArchiveCommand to archive the job entries within the specified {@code DateRange}
     */
    public AnalyseCommand() {
        toAnalyse = new JobList();
    }

    @Override
    public CommandResult execute() {
        requireNonNull(model);
        toAnalyse = model.analyseJob(toAnalyse);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAnalyse.getAnalyseResult()));
    }

}
```
###### \java\seedu\carvicim\logic\commands\ArchiveCommand.java
``` java
/**
 * Archives job entries within selected date range.
 */
public class ArchiveCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "archive";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Archives job entries within selected date range. "
            + "Parameters: "
            + PREFIX_START_DATE + "START_DATE "
            + PREFIX_END_DATE + "END_DATE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_START_DATE + "Mar 03 2018 "
            + PREFIX_END_DATE + "Mar 25 2018";

    public static final String MESSAGE_SUCCESS = "Archived successfully";

    private final DateRange toArchive;

    /**
     * Creates an ArchiveCommand to archive the job entries within the specified {@code DateRange}
     */
    public ArchiveCommand(DateRange dateRange) {
        requireNonNull(dateRange);
        toArchive = dateRange;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (toArchive.compareTo(toArchive.getStartDate(), toArchive.getEndDate()) > 0) {
            throw new CommandException(MESSAGE_INVALID_DATERANGE);
        }
        requireNonNull(model);
        model.archiveJob(toArchive);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ArchiveCommand // instanceof handles nulls
                && toArchive.equals(((ArchiveCommand) other).toArchive));
    }
}
```
###### \java\seedu\carvicim\logic\commands\SortCommand.java
``` java
/**
 * Sorts all persons alphabetically by names in the carvicim book to the user.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays all persons in the carvicim book as a list in alphabetical order.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Sorted all persons";

    protected Carvicim carvicim;

    @Override
    public CommandResult execute() {
        model.sortPersonList();
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
```
###### \java\seedu\carvicim\logic\parser\ArchiveCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ArchiveCommand object
 */
public class ArchiveCommandParser implements Parser<ArchiveCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the ArchiveCommand
     * and returns an ArchiveCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ArchiveCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_START_DATE, PREFIX_END_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_START_DATE, PREFIX_END_DATE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ArchiveCommand.MESSAGE_USAGE));
        }

        try {
            Date startDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_START_DATE)).get();
            Date endDate = ParserUtil.parseDate(argMultimap.getValue(PREFIX_END_DATE)).get();

            DateRange dateRange = new DateRange(startDate, endDate);
            return new ArchiveCommand(dateRange);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
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
     * Parses a {@code String date} into a {@code Date}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code date} is invalid.
     */
    public static Date parseDate(String date) throws IllegalValueException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        if (!Date.isValidDate(trimmedDate)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        return new Date(date);
    }

    /**
     * Parses a {@code Optional<String> date} into an {@code Optional<Date>}
     * if {@code date} is present.
     *
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Date> parseDate(Optional<String> date) throws IllegalValueException {
        requireNonNull(date);
        return date.isPresent() ? Optional.of(parseDate(date.get())) : Optional.empty();
    }

```
###### \java\seedu\carvicim\model\Carvicim.java
``` java
    /**
     * Archives job entries in Carvicim.
     */
    public void archiveJob(DateRange dateRange) {
        archiveJobs = new JobList();
        Iterator<Job> iterator = jobs.iterator();
        while (iterator.hasNext()) {
            Job job = iterator.next();
            Date date = job.getDate();
            Date startDate = dateRange.getStartDate();
            Date endDate = dateRange.getEndDate();
            if (dateRange.compareTo(date, startDate) >= 0 && dateRange.compareTo(date, endDate) <= 0) {
                archiveJobs.add(job);
            }
        }
    }

    /**
     * Analyses job entries in Carvicim for this month.
     */
    public JobList analyseJob(JobList jobList) {
        return jobList.analyseList(jobs);
    }

    //// employee-level operations

    /**
     * Adds a employee to the carvicim book.
     * Also checks the new employee's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the employee to point to those in {@link #tags}.
     *
     * @throws DuplicateEmployeeException if an equivalent employee already exists.
     */
    public void addEmployee(Employee p) throws DuplicateEmployeeException {
        Employee employee = syncWithMasterTagList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any employee
        // in the employee list.
        employees.add(employee);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code Carvicim}'s tag list will be updated with the tags of {@code editedPerson}.
     *
     * @throws DuplicateEmployeeException if updating the employee's details causes the employee to be equivalent to
     *      another existing person in the list.
     * @throws EmployeeNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Employee)
     */
    public void updateEmployee(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException {
        requireNonNull(editedEmployee);

        Employee syncedEditedEmployee = syncWithMasterTagList(editedEmployee);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any employee
        // in the employee list.
        employees.setEmployee(target, syncedEditedEmployee);
    }

    /**
     *  Updates the master tag list to include tags in {@code employee} that are not in the list.
     *  @return a copy of this {@code employee} such that every tag in this employee points
     *  to a Tag object in the master list.
     */
    private Employee syncWithMasterTagList(Employee employee) {
        final UniqueTagList employeeTags = new UniqueTagList(employee.getTags());
        tags.mergeFrom(employeeTags);

        // Create map with values = tag object references in the master list
        // used for checking employee tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of employee tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        employeeTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Employee(employee.getName(), employee.getPhone(), employee.getEmail(),
                correctTagReferences);
    }

    /**
     * Removes {@code key} from this {@code Carvicim}.
     * @throws EmployeeNotFoundException if the {@code key} is not in this {@code Carvicim}.
     */
    public boolean removeEmployee(Employee key) throws EmployeeNotFoundException {
        if (employees.remove(key)) {
            return true;
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

```
###### \java\seedu\carvicim\model\Carvicim.java
``` java
    /**
     * Sort all employees' name in list alphabetically.
     */
    public UniqueEmployeeList sortList() {
        employees.sortName(new Comparator<Employee>() {
            @Override
            public int compare(Employee employee1, Employee employee2) {
                return employee1.getName().toString().compareToIgnoreCase(employee2.getName().toString());
            }
        });
        return employees;
    }

    //// util methods
```
###### \java\seedu\carvicim\model\job\DateRange.java
``` java
/**
 * Represents a date range in the car servicing manager
 */
public class DateRange {

    private final Date startDate;
    private final Date endDate;

    public DateRange(Date startDate, Date endDate) {
        requireAllNonNull(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    /**
     * Compare the startDate with endDate
     */
    public int compareTo(Date startDate, Date endDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(startDate.getYear(), startDate.getMonth(), startDate.getDay());
        cal2.set(endDate.getYear(), endDate.getMonth(), endDate.getDay());
        return cal1.compareTo(cal2);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DateRange)) {
            return false;
        }

        DateRange otherDateRange = (DateRange) other;
        return otherDateRange.getStartDate().equals(this.getStartDate())
                && otherDateRange.getEndDate().equals(this.getEndDate());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Starting Date: ")
                .append(getStartDate())
                .append(" Ending Date: ")
                .append(getEndDate());
        return builder.toString();
    }

}
```
###### \java\seedu\carvicim\model\job\JobList.java
``` java
    /**
     * Get the job list for the current month.
     */
    public JobList getCurrentMonthJobList() {
        JobList currentMonthList = new JobList();
        LocalDate localDate = LocalDate.now();
        int month = localDate.getMonth().getValue();
        Iterator<Job> iterator = internalList.iterator();
        while (iterator.hasNext()) {
            Job job = iterator.next();
            if (job.getDate().getMonth() == month) {
                currentMonthList.add(job);
            }
        }
        return currentMonthList;
    }

    /**
     * Get the respective job counts for the current month.
     */
    public JobList analyseList(JobList analyseList) {
        analyseList = analyseList.getCurrentMonthJobList();
        Iterator<Job> iterator = analyseList.iterator();
        while (iterator.hasNext()) {
            Job job = iterator.next();
            Status ongoing = new Status("ongoing");
            if (job.getStatus().equals(ongoing)) {
                analyseList.ongoingCount++;
            } else {
                analyseList.closedCount++;
            }
            analyseList.jobCount++;
        }
        return analyseList;
    }

    /**
     * Get the respective job counts.
     */
    public String getAnalyseResult() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Number of Jobs: ")
                .append(jobCount)
                .append(" Number of Ongoing: ")
                .append(ongoingCount)
                .append(" Number of Closed: ")
                .append(closedCount)
                .append("\n");
        return builder.toString();
    }

```
###### \java\seedu\carvicim\storage\ArchiveJobStorage.java
``` java
/**
 * Represents a storage for {@link Carvicim}.
 */
public interface ArchiveJobStorage {

    /**
     * Returns the file path of the data file.
     */
    String getArchiveJobFilePath();

    /**
     * Returns Carvicim data as a {@link ReadOnlyCarvicim}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyCarvicim> readArchiveJob() throws DataConversionException, IOException;

    /**
     * @see #getArchiveJobFilePath()
     */
    Optional<ReadOnlyCarvicim> readArchiveJob(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyCarvicim} to the storage.
     * @param carvicim cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveArchiveJob(ReadOnlyCarvicim carvicim) throws IOException;

    /**
     * @see #saveArchiveJob(ReadOnlyCarvicim)
     */
    void saveArchiveJob(ReadOnlyCarvicim carvicim, String filePath) throws IOException;

}
```
###### \java\seedu\carvicim\storage\StorageManager.java
``` java
    // ================ ArchiveJob methods ==============================

    @Override
    public String getArchiveJobFilePath() {
        return archiveJobStorage.getArchiveJobFilePath();
    }

    @Override
    public Optional<ReadOnlyCarvicim> readArchiveJob() throws DataConversionException, IOException {
        return readArchiveJob(archiveJobStorage.getArchiveJobFilePath());
    }

    @Override
    public Optional<ReadOnlyCarvicim> readArchiveJob(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return archiveJobStorage.readArchiveJob(filePath);
    }

    @Override
    public void saveArchiveJob(ReadOnlyCarvicim carvicim) throws IOException {
        saveArchiveJob(carvicim, archiveJobStorage.getArchiveJobFilePath());
    }

    @Override
    public void saveArchiveJob(ReadOnlyCarvicim carvicim, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        archiveJobStorage.saveArchiveJob(carvicim, filePath);
    }

    @Override
    @Subscribe
    public void handleArchiveEvent(CarvicimChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Archiving data, saving to file"));
        try {
            saveArchiveJob(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
```
###### \java\seedu\carvicim\storage\XmlArchiveJobStorage.java
``` java
/**
 * A class to access Archive data stored as an xml file on the hard disk.
 */
public class XmlArchiveJobStorage implements ArchiveJobStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlArchiveJobStorage.class);

    private String filePath;

    public XmlArchiveJobStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getArchiveJobFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyCarvicim> readArchiveJob() throws DataConversionException, IOException {
        return readArchiveJob(filePath);
    }

    /**
     * Similar to {@link #readArchiveJob()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyCarvicim> readArchiveJob(String filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        File archiveJobFile = new File(filePath);

        if (!archiveJobFile.exists()) {
            logger.info("ArchiveJob file "  + archiveJobFile + " not found");
            return Optional.empty();
        }

        XmlSerializableArchiveJob xmlArchiveJob = XmlFileStorage.loadDataFromArchiveFile(new File(filePath));
        try {
            return Optional.of(xmlArchiveJob.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + archiveJobFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveArchiveJob(ReadOnlyCarvicim carvicim) throws IOException {
        saveArchiveJob(carvicim, filePath);
    }

    /**
     * Similar to {@link #saveArchiveJob(ReadOnlyCarvicim)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveArchiveJob(ReadOnlyCarvicim carvicim, String filePath) throws IOException {
        requireNonNull(carvicim);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createEvenIfExist(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableArchiveJob(carvicim));
    }

}
```
###### \java\seedu\carvicim\storage\XmlFileStorage.java
``` java
    /**
     * Saves the given archivejob data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableArchiveJob archiveJob)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, archiveJob);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

```
###### \java\seedu\carvicim\storage\XmlFileStorage.java
``` java
    /**
     * Returns archive job in the file or an empty carvicim book
     */
    public static XmlSerializableArchiveJob loadDataFromArchiveFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableArchiveJob.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
```
###### \java\seedu\carvicim\storage\XmlSerializableArchiveJob.java
``` java
/**
 * An Immutable Archive that is serializable to XML format
 */
@XmlRootElement(name = "archive")
public class XmlSerializableArchiveJob {

    @XmlElement
    private List<XmlAdaptedJob> jobs;

    /**
     * Creates an empty XmlSerializableArchiveJob.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableArchiveJob() {
        jobs = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableArchiveJob(ReadOnlyCarvicim src) {
        this();
        jobs.addAll(src.getArchiveJobList().stream().map(XmlAdaptedJob::new).collect(Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code Carvicim} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedJob}.
     */
    public Carvicim toModelType() throws IllegalValueException {
        Carvicim carvicim = new Carvicim();
        for (XmlAdaptedJob j : jobs) {
            carvicim.addJob(j.toModelType());
        }
        return carvicim;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableArchiveJob)) {
            return false;
        }

        XmlSerializableArchiveJob otherAb = (XmlSerializableArchiveJob) other;
        return jobs.equals(otherAb.jobs);
    }
}
```
