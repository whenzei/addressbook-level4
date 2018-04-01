package seedu.carvicim.model;

import java.util.Objects;

import seedu.carvicim.commons.core.GuiSettings;
import seedu.carvicim.logic.commands.CommandWords;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private String carvicimFilePath = "data/carvicim.xml";
    private String archiveJobFilePath = "data/archivejob.xml";
    private String carvicimName = "MyCarviciM";
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

    public String getCarvicimFilePath() {
        return carvicimFilePath;
    }

    public void setCarvicimFilePath(String carvicimFilePath) {
        this.carvicimFilePath = carvicimFilePath;
    }

    public String getArchiveJobFilePath() {
        return archiveJobFilePath;
    }

    public void setArchiveJobFilePath(String archiveJobFilePath) {
        this.archiveJobFilePath = archiveJobFilePath;
    }

    public String getCarvicimName() {
        return carvicimName;
    }

    public void setCarvicimName(String carvicimName) {
        this.carvicimName = carvicimName;
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
                && Objects.equals(carvicimFilePath, o.carvicimFilePath)
                && Objects.equals(carvicimName, o.carvicimName)
                && Objects.equals(themeName, o.themeName)
                && Objects.equals(extensionName, o.extensionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, carvicimFilePath, carvicimName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal data file location : " + carvicimFilePath);
        sb.append("\nCarvicim name : " + carvicimName);
        sb.append("\nTheme : " + themeName);

        return sb.toString();
    }

}
