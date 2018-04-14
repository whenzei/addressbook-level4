# yuhongherald-reused
###### \java\seedu\carvicim\ui\JobListPanel.java
``` java
/**
 * Panel containing the list of jobs.
 */
public class JobListPanel extends UiPart<Region> {
    private static final String FXML = "JobListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(JobListPanel.class);

    private ObservableList<Job> jobList;

    @FXML
    private ListView<JobCard> jobListView;

    public JobListPanel(ObservableList<Job> jobList) {
        super(FXML);
        this.jobList = jobList;
        setConnections(jobList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Job> jobList) {
        ObservableList<JobCard> mappedList = EasyBind.map(
                jobList, (job) -> new JobCard(job, jobList.indexOf(job) + 1));
        jobListView.setItems(mappedList);
        jobListView.setCellFactory(listView -> new JobListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        jobListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in job list panel changed to : '" + newValue + "'");
                        raise(new JobPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code JobCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            jobListView.scrollTo(index);
            jobListView.getSelectionModel().clearAndSelect(index);
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code JobCard}.
     */
    class JobListViewCell extends ListCell<JobCard> {

        @Override
        protected void updateItem(JobCard person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(person.getRoot());
            }
        }
    }

    @Subscribe
    private void handleDisplayAllJobsEvent(DisplayAllJobsEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        jobList = event.getJobList();
        setConnections(jobList);
    }

    @Subscribe
    private void handleJumpToJobListRequestEvent(JumpToJobListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }


}
```
