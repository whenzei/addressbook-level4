package seedu.carvicim.ui;

import java.util.Iterator;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.commons.events.ui.JobDisplayPanelUpdateRequestEvent;
import seedu.carvicim.commons.events.ui.JobPanelSelectionChangedEvent;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.remark.Remark;

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
