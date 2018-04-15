# charmaineleehc
###### \java\seedu\carvicim\commons\GmailAuthenticator.java
``` java
/**
 * Allow for Gmail authentication process to take place
 */
public class GmailAuthenticator {

    private static final String APPLICATION_NAME = "CarviciM";

    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            "./src/main/resources/.credentials/carvicim-gmail");

    private static FileDataStoreFactory dataStoreFactory;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static HttpTransport httpTransport;

    private static String scope = "https://mail.google.com/";

    private Gmail gmailService;

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates a GmailAuthenticator to authenticate user.
     * @throws IOException
     */
    public GmailAuthenticator() throws IOException {
        this.gmailService = buildGmailService();
    }

    /**
     * @return a Gmail service
     */
    public Gmail getGmailService() {
        return gmailService;
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        InputStream in = GmailAuthenticator.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, Arrays.asList(scope))
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized Gmail client service.
     * @return an authorized Gmail client service
     * @throws IOException
     */
    public static Gmail buildGmailService() throws IOException {
        Credential credential = authorize();
        return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}
```
###### \java\seedu\carvicim\logic\commands\EmailCommand.java
``` java
/**
 * Sends email to employee.
 */
public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": emails employee about job details.\n"
            + "Parameters: "
            + PREFIX_JOB_NUMBER + "JOB_NUMBER "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_JOB_NUMBER + "12 ";

    public static final String MESSAGE_SUCCESS = "The email has been successfully sent to your employee!";

    private static final String CARVICIM_EMAIL = "me";
    private static final String EMAIL_SUBJECT = "Job details";

    private final JobNumber jobNumber;

    /**
     * Creates an EmailCommand
     * @param jobNumber of the job details to be sent via email to the employee(s) in charge
     */
    public EmailCommand(JobNumber jobNumber) {
        this.jobNumber = jobNumber;
    }

    public UniqueEmployeeList getListOfEmployeesOfJob() throws CommandException {
        ObservableList<Job> filteredJobList = model.getFilteredJobList();

        if (jobNumber.asInteger() >= filteredJobList.size() || jobNumber.asInteger() < 0) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_NUMBER);
        }

        UniqueEmployeeList listOfEmployeesOfJob = filteredJobList.get(jobNumber.asInteger() - 1).getAssignedEmployees();

        return listOfEmployeesOfJob;
    }

    /**
     * Sends an email to each employee in {@code listOfEmployeesOfJob}
     * @param listOfEmployeesOfJob
     * @throws MessagingException
     * @throws IOException
     */
    public void sendEmails(UniqueEmployeeList listOfEmployeesOfJob) throws MessagingException, IOException {
        ObservableList<Job> filteredJobList = model.getFilteredJobList();

        for (Employee employee : listOfEmployeesOfJob) {
            Job job = filteredJobList.get(jobNumber.asInteger() - 1);

            String emailContent = "Hi " + employee.getName().toString() + ",\n\n"
                    + "Thank you for all your hard work thus far.\n\n"
                    + "Here are the job details for the job assigned to you on " + job.getDate().toString() + ":\n"
                    + "Client: " + job.getClient().getName().toString() + "\n"
                    + "Client's phone number: " + job.getClient().getPhone().toString() + "\n"
                    + "Vehicle number: " + job.getVehicleNumber().toString() + "\n"
                    + "Remarks: " + job.getRemarkList().getRemarks().toString() + "\n\n"
                    + "Thank you, and happy servicing!\n\n";

            GmailAuthenticator gmailAuthenticator = new GmailAuthenticator();
            MimeMessage mimeMessage = ModelManager.createEmail(
                    employee.getEmail().toString(), CARVICIM_EMAIL, EMAIL_SUBJECT, emailContent);
            ModelManager.sendMessage(gmailAuthenticator.getGmailService(), CARVICIM_EMAIL, mimeMessage);
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);

        UniqueEmployeeList listOfEmployeesOfJob = getListOfEmployeesOfJob();

        try {
            sendEmails(listOfEmployeesOfJob);
        } catch (MessagingException | IOException e) {
            System.exit(1);
        }

        return new CommandResult(MESSAGE_SUCCESS);

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand // instanceof handles nulls
                && jobNumber.equals(((EmailCommand) other).jobNumber));
    }

}
```
###### \java\seedu\carvicim\logic\commands\FindByTagCommand.java
``` java
/**
 * Finds and lists all employees in carvicim who has been tagged by any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindByTagCommand extends Command {

    public static final String COMMAND_WORD = "findt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all employees who has been tagged by any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " mechanic technician";

    private final TagContainsKeywordsPredicate predicate;

    public FindByTagCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindByTagCommand // instanceof handles nulls
                && this.predicate.equals(((FindByTagCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\carvicim\logic\commands\LoginCommand.java
``` java
/**
 * Directs user to the login page of Gmail for user to log in.
 */
public class LoginCommand extends Command {

    public static final String COMMAND_WORD = "login";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Logs in user into Gmail account.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "You have successfully logged into your Gmail account!";

    @Override
    public CommandResult execute() {
        try {
            new GmailAuthenticator();
        } catch (IOException ioe) {
            System.exit(1);
        }

        return new CommandResult(MESSAGE_SUCCESS);
    }

}
```
###### \java\seedu\carvicim\logic\parser\EmailCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EmailCommand object
 */
public class EmailCommandParser implements Parser<EmailCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EmailCommand
     * and returns an EmailCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EmailCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_JOB_NUMBER);

        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_NUMBER)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
        }

        try {
            JobNumber jobNumber = ParserUtil.parseJobNumber(argMultimap.getValue(PREFIX_JOB_NUMBER)).get();
            return new EmailCommand(jobNumber);
        } catch (IllegalValueException | NumberFormatException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
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
###### \java\seedu\carvicim\logic\parser\FindByTagCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindByTagCommand object
 */
public class FindByTagCommandParser implements Parser<FindByTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindByTagCommand
     * and returns an FindByTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindByTagCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindByTagCommand.MESSAGE_USAGE));
        }

        String[] tagKeywords = trimmedArgs.split("\\s+");

        return new FindByTagCommand(new TagContainsKeywordsPredicate(Arrays.asList(tagKeywords)));
    }

}
```
###### \java\seedu\carvicim\model\ModelManager.java
``` java
    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws javax.mail.MessagingException
     */
    public static MimeMessage createEmail(
            String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws java.io.IOException
     * @throws MessagingException
     */
    public static Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"can be used to indicate the authenticated user.
     * @param emailContent Email to be sent.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    public static Message sendMessage(
            Gmail service, String userId, MimeMessage emailContent) throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();

        return message;
    }
```
###### \java\seedu\carvicim\model\person\TagContainsKeywordsPredicate.java
``` java
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
```
