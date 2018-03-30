package seedu.address.model;

import java.util.Objects;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandWords;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private String addressBookFilePath = "data/addressbook.xml";
    private String archiveJobFilePath = "data/archivejob.xml";
    private String addressBookName = "MyAddressBook";
    private CommandWords commandWords;
    private String themeName;
    private String extensionName;

    public UserPrefs() {
        setGuiSettingsDefault();
        setDefaultExtensionName();
        setDefaultThemeName();
        commandWords = new CommandWords();
    }

    /**
     * Checks the integrity of the user preferences file and reinitializes any corrupted fields.
     */
    public void checkIntegrity() {
        if (commandWords == null) {
            commandWords = new CommandWords();
        }
        if (guiSettings == null) {
            setGuiSettingsDefault();
        }
        commandWords.checkIntegrity();
    }

    public CommandWords getCommandWords() {
        return commandWords == null ? new CommandWords() : commandWords;
    }

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public String getThemeName() {
        return themeName;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName =  extensionName;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    public void setDefaultThemeName() {
        this.themeName = "DarkTheme";
    }

    public void setDefaultExtensionName() {
        this.extensionName = "ExtensionsDark";
    }


    public void setGuiSettingsDefault() {
        this.setGuiSettings(500, 500, 0, 0);
    }

    public String getAddressBookFilePath() {
        return addressBookFilePath;
    }

    public void setAddressBookFilePath(String addressBookFilePath) {
        this.addressBookFilePath = addressBookFilePath;
    }

    public String getArchiveJobFilePath() {
        return archiveJobFilePath;
    }

    public void setArchiveJobFilePath(String archiveJobFilePath) {
        this.archiveJobFilePath = archiveJobFilePath;
    }

    public String getAddressBookName() {
        return addressBookName;
    }

    public void setAddressBookName(String addressBookName) {
        this.addressBookName = addressBookName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings)
                && Objects.equals(addressBookFilePath, o.addressBookFilePath)
                && Objects.equals(addressBookName, o.addressBookName)
                && Objects.equals(themeName, o.themeName)
                && Objects.equals(extensionName, o.extensionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, addressBookFilePath, addressBookName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal data file location : " + addressBookFilePath);
        sb.append("\nAddressBook name : " + addressBookName);
        sb.append("\nTheme : " + themeName);

        return sb.toString();
    }

}
