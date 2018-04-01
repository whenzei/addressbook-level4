package seedu.carvicim.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.commons.events.ui.JobPanelSelectionChangedEvent;
import seedu.carvicim.model.job.Job;

//@@author whenzei
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
        assignedEmployees.setVisible(true);
        assignedEmployees.refresh();

        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        final Job job = event.getJob();
        jobNumber.setText(job.getJobNumber().toString());
        status.setText(job.getStatus().toString());
        date.setText(job.getDate().toString());
        vehicleNumber.setText(job.getVehicleNumber().toString());
        name.setText(job.getClient().getName().toString());
        phone.setText(job.getClient().getPhone().toString());
        email.setText(job.getClient().getEmail().toString());

        assignedEmployees.setItems(job.getAssignedEmployeesAsObservableList());
    }
}
