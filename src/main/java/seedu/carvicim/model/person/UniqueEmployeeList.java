package seedu.carvicim.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.carvicim.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.carvicim.commons.util.CollectionUtil;
import seedu.carvicim.model.person.exceptions.DuplicateEmployeeException;
import seedu.carvicim.model.person.exceptions.EmployeeNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Employee#equals(Object)
 * @see CollectionUtil
 */
public class UniqueEmployeeList implements Iterable<Employee> {

    private final ObservableList<Employee> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent employee as the given argument.
     */
    public boolean contains(Employee toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a employee to the list.
     *
     * @throws DuplicateEmployeeException if the employee to add is a duplicate of an existing employee in the list.
     */
    public void add(Employee toAdd) throws DuplicateEmployeeException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateEmployeeException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the employee {@code target} in the list with {@code editedEmployee}.
     *
     * @throws DuplicateEmployeeException if the replacement is equivalent to another existing employee in the list.
     * @throws EmployeeNotFoundException if {@code target} could not be found in the list.
     */
    public void setEmployee(Employee target, Employee editedEmployee)
            throws DuplicateEmployeeException, EmployeeNotFoundException {
        requireNonNull(editedEmployee);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new EmployeeNotFoundException();
        }

        if (!target.equals(editedEmployee) && internalList.contains(editedEmployee)) {
            throw new DuplicateEmployeeException();
        }

        internalList.set(index, editedEmployee);
    }

    /**
     * Removes the equivalent employee from the list.
     *
     * @throws EmployeeNotFoundException if no such employee could be found in the list.
     */
    public boolean remove(Employee toRemove) throws EmployeeNotFoundException {
        requireNonNull(toRemove);
        final boolean employeeFoundAndDeleted = internalList.remove(toRemove);
        if (!employeeFoundAndDeleted) {
            throw new EmployeeNotFoundException();
        }
        return employeeFoundAndDeleted;
    }

    public void setEmployees(UniqueEmployeeList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setEmployees(List<Employee> employees) throws DuplicateEmployeeException {
        requireAllNonNull(employees);
        final UniqueEmployeeList replacement = new UniqueEmployeeList();
        for (final Employee employee : employees) {
            replacement.add(employee);
        }
        setEmployees(replacement);
    }

    /**
     * Returns all tags in this list as a Set.
     * This set is mutable and change-insulated against the internal list.
     */
    public Set<Employee> toSet() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return new HashSet<>(internalList);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Employee> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    /**
     * Sort all persons' name in list alphabetically.
     */
    public void sortName(Comparator comparator) {
        Collections.sort(internalList, comparator);
    }

    /**
     * Returns number of employees in the list
     */
    public int size() {
        return internalList.size();
    }

    @Override
    public Iterator<Employee> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueEmployeeList // instanceof handles nulls
                        && this.internalList.equals(((UniqueEmployeeList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
