package seedu.address.ui;

import java.awt.*;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JobPanelSelectionChangedEvent;
import seedu.address.model.job.Job;

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
    private FlowPane assignedEmployees;

    public JobDisplayPanel() {
        super(FXML);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleJobPanelSelectionChangedEvent(JobPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        final Job job = event.getJob();
        jobNumber.setText(job.getJobNumber().value);
        status.setText(job.getStatus().value);
        date.setText(job.getDate().value);
        vehicleNumber.setText(job.getVehicleNumber().value);
        name.setText(job.getClient().getName().fullName);
        phone.setText(job.getClient().getPhone().value);
        email.setText(job.getClient().getEmail().value);
    }
}
