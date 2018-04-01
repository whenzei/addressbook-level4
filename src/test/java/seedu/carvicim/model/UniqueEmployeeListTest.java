package seedu.carvicim.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.carvicim.model.person.UniqueEmployeeList;

public class UniqueEmployeeListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueEmployeeList uniqueEmployeeList = new UniqueEmployeeList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueEmployeeList.asObservableList().remove(0);
    }
}
