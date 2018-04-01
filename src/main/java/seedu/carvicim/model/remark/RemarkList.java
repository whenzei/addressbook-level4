package seedu.carvicim.model.remark;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//@@author whenzei
/**
 * Represents a list of remarks that enforces no nulls
 */
public class RemarkList implements Iterable<Remark> {

    private final ObservableList<Remark> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty RemarkList.
     */
    public RemarkList() {}

    /**
     * Creates a RemarkList using given remarks.
     * Enforces no nulls.
     */
    public RemarkList(Set<Remark> remarks) {
        requireAllNonNull(remarks);
        internalList.addAll(remarks);
    }

    /**
     * Returns all remarks in this list as a Set.
     * This set is mutable and change-insulated against the internal list.
     */
    public Set<Remark> toSet() {
        return new HashSet<>(internalList);
    }

    /**
     * Replaces the remarks in this list with those in the argument remark list
     * @param remarks
     */
    public void setRemarks(Set<Remark> remarks) {
        requireAllNonNull(remarks);
        internalList.setAll(remarks);
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
     * Returns the backing list as an unmodifiable {@code RemarkList}.
     */
    public ObservableList<Remark> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
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
