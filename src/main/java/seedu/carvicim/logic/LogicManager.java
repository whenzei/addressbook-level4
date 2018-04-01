package seedu.carvicim.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.carvicim.commons.core.ComponentManager;
import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.logic.commands.Command;
import seedu.carvicim.logic.commands.CommandResult;
import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.logic.parser.CarvicimParser;
import seedu.carvicim.logic.parser.exceptions.ParseException;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.model.person.Employee;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final CarvicimParser carvicimParser;
    private final UndoRedoStack undoRedoStack;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        carvicimParser = new CarvicimParser(model.getCommandWords());
        undoRedoStack = new UndoRedoStack();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = carvicimParser.parseCommand(commandText);
            command.setData(model, history, undoRedoStack);
            CommandResult result = command.execute();
            undoRedoStack.push(command);
            return result;
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public String appendCommandKeyToMessage(String message) {
        return model.appendCommandKeyToMessage(message);
    }

    @Override
    public ObservableList<Employee> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<Job> getFilteredJobList() {
        return model.getFilteredJobList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }
}
