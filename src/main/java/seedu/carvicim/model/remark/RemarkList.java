package seedu.carvicim.model.remark;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//@@author whenzei
/**
 * Represents a list of remarks that enforces no nulls
 */
public class RemarkList implements Iterable<Remark> {

    private final ArrayList<Remark> internalList;

    /**
     * Constructs empty RemarkList.
     */
    public RemarkList() {
        internalList = new ArrayList<>();
    }

    /**
     * Creates a RemarkList using given remarks.
     * Enforces no nulls.
     */
    public RemarkList(ArrayList<Remark> remarks) {
        requireAllNonNull(remarks);
        internalList = new ArrayList<>();
        internalList.addAll(remarks);
    }

    /**
     * Adds a Remark to the list.
     */
    public void add(Remark toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    @Override
    public Iterator<Remark> iterator() {
        return internalList.iterator();
    }

    /**
     * Returns remark list
     */
    public ArrayList<Remark> getRemarks() {
        return internalList;
    }

    /**
     * Returns the list as an unmodifiable {@code RemarkList}.
     */
    public ObservableList<Remark> asObservableList() {
        return FXCollections.observableArrayList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RemarkList // instanceof handles nulls
                        && this.internalList.equals(((RemarkList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
