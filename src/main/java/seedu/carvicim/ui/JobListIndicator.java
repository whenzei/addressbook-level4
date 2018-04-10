package seedu.carvicim.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.commons.events.ui.JobListSwitchEvent;

//@@author yuhongherald
/**
 * A text box to indicate which tab the job list panel is on
 */
public class JobListIndicator extends UiPart<Region> {

    public static final String IMPORTED = "unreviewed jobs";
    public static final String SAVED = "saved jobs";

    private static final Logger logger = LogsCenter.getLogger(JobListIndicator.class);
    private static final String FXML = "JobListIndicator.fxml";

    private final StringProperty displayed = new SimpleStringProperty(SAVED);

    @FXML
    private TextArea jobListIndicator;

    public JobListIndicator() {
        super(FXML);
        jobListIndicator.textProperty().bind(displayed);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleNewResultAvailableEvent(JobListSwitchEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> displayed.setValue(event.message));
    }

}
