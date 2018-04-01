package seedu.carvicim.testutil;

import java.io.File;
import java.io.IOException;

import seedu.carvicim.commons.core.index.Index;
import seedu.carvicim.commons.util.FileUtil;
import seedu.carvicim.model.Model;
import seedu.carvicim.model.person.Employee;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting string.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    /**
     * Returns the middle index of the employee in the {@code model}'s employee list.
     */
    public static Index getMidIndex(Model model) {
        return Index.fromOneBased(model.getCarvicim().getEmployeeList().size() / 2);
    }

    /**
     * Returns the last index of the employee in the {@code model}'s employee list.
     */
    public static Index getLastIndex(Model model) {
        return Index.fromOneBased(model.getCarvicim().getEmployeeList().size());
    }

    /**
     * Returns the employee in the {@code model}'s employee list at {@code index}.
     */
    public static Employee getPerson(Model model, Index index) {
        return model.getCarvicim().getEmployeeList().get(index.getZeroBased());
    }
}
