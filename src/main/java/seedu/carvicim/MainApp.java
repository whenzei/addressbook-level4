package seedu.carvicim;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.carvicim.commons.core.Config;
import seedu.carvicim.commons.core.EventsCenter;
import seedu.carvicim.commons.core.LogsCenter;
import seedu.carvicim.commons.core.Version;
import seedu.carvicim.commons.events.ui.ExitAppRequestEvent;
import seedu.carvicim.commons.exceptions.DataConversionException;
import seedu.carvicim.commons.util.ConfigUtil;
import seedu.carvicim.commons.util.StringUtil;
import seedu.carvicim.logic.Logic;
import seedu.carvicim.logic.LogicManager;
import seedu.carvicim.model.Carvicim;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.ModelManager;
import seedu.carvicim.model.ReadOnlyCarvicim;
import seedu.carvicim.model.UserPrefs;
import seedu.carvicim.model.util.SampleDataUtil;
import seedu.carvicim.storage.ArchiveJobStorage;
import seedu.carvicim.storage.CarvicimStorage;
import seedu.carvicim.storage.JsonUserPrefsStorage;
import seedu.carvicim.storage.Storage;
import seedu.carvicim.storage.StorageManager;
import seedu.carvicim.storage.UserPrefsStorage;
import seedu.carvicim.storage.XmlArchiveJobStorage;
import seedu.carvicim.storage.XmlCarvicimStorage;
import seedu.carvicim.ui.Ui;
import seedu.carvicim.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(0, 6, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing Carvicim ]===========================");
        super.init();

        config = initConfig(getApplicationParameter("config"));

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        userPrefs = initPrefs(userPrefsStorage);
        CarvicimStorage carvicimStorage = new XmlCarvicimStorage(userPrefs.getCarvicimFilePath());
        ArchiveJobStorage archiveJobStorage = new XmlArchiveJobStorage(userPrefs.getArchiveJobFilePath());
        storage = new StorageManager(carvicimStorage, userPrefsStorage, archiveJobStorage);

        initLogging(config);

        model = initModelManager(storage, userPrefs);
        model.initJobNumber();

        logic = new LogicManager(model);

        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();
    }

    private String getApplicationParameter(String parameterName) {
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s carvicim book and {@code userPrefs}. <br>
     * The data from the sample carvicim book will be used instead if {@code storage}'s carvicim book is not found,
     * or an empty carvicim book will be used instead if errors occur when reading {@code storage}'s carvicim book.
     */
    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        Optional<ReadOnlyCarvicim> carvicimOptional;
        ReadOnlyCarvicim initialData;
        try {
            carvicimOptional = storage.readCarvicim();
            if (!carvicimOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample Carvicim");
            }
            initialData = carvicimOptional.orElseGet(SampleDataUtil::getSampleAddressBook);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty Carvicim");
            initialData = new Carvicim();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty Carvicim");
            initialData = new Carvicim();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(String configFilePath) {
        Config initializedConfig;
        String configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        String prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty Carvicim");
            initializedPrefs = new UserPrefs();
        }

        initializedPrefs.checkIntegrity();

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Carvicim " + MainApp.VERSION);
        ui.start(primaryStage);
        setTheme(primaryStage);
    }

    /**
     * Sets the theme of the main app based on user preference
     */
    private void setTheme(Stage primaryStage) {
        primaryStage.getScene().getStylesheets().clear();
        primaryStage.getScene().getStylesheets().add("/view/" + userPrefs.getThemeName() + ".css");
        primaryStage.getScene().getStylesheets().add("/view/" + userPrefs.getExtensionName() + ".css");
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
