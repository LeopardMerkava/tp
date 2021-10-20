package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TASK_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TASK_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

public class EditTaskCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_filteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Task> tasks = new ArrayList<>(personToEdit.getTasks());
        Task newTask = new Task("walk");
        tasks.set(0, newTask);
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), tasks, personToEdit.getDescription());
        EditTaskCommand editTaskCommand = new EditTaskCommand(INDEX_FIRST_PERSON, INDEX_FIRST_TASK, newTask);

        String expectedMessage = String.format(EditTaskCommand.MESSAGE_EDIT_TASK_SUCCESS, newTask.taskName);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_outOfBoundsPersonIndexFilteredList_failure() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromZeroBased(person.getTasks().size());
        EditTaskCommand editTaskCommand = new EditTaskCommand(
                INDEX_FIRST_PERSON, outOfBoundIndex, new Task("walk")
        );
        assertCommandFailure(
                editTaskCommand, model, String.format(EditTaskCommand.MESSAGE_INVALID_TASK, person.getName())
        );
    }

    @Test
    public void execute_duplicateTask_failure() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Task task = person.getTasks().get(0);
        EditTaskCommand editTaskCommand = new EditTaskCommand(INDEX_FIRST_PERSON, INDEX_FIRST_TASK, task);

        assertCommandFailure(editTaskCommand, model, EditTaskCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditTaskCommand editTaskCommand = new EditTaskCommand(outOfBoundIndex, INDEX_FIRST_TASK,
                new Task(VALID_TASK_TASK1));

        assertCommandFailure(editTaskCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditTaskCommand editTaskCommand = new EditTaskCommand(outOfBoundIndex, INDEX_FIRST_TASK,
                new Task(VALID_TASK_TASK1));

        assertCommandFailure(editTaskCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditTaskCommand standardCommand = new EditTaskCommand(INDEX_FIRST_PERSON, INDEX_FIRST_TASK,
                new Task(VALID_TASK_TASK1));

        // same values -> returns true
        EditTaskCommand commandWithSameValues = new EditTaskCommand(INDEX_FIRST_PERSON, INDEX_FIRST_TASK,
                new Task(VALID_TASK_TASK1));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different person index -> returns false
        assertFalse(standardCommand.equals(new EditTaskCommand(INDEX_SECOND_PERSON, INDEX_FIRST_TASK,
                new Task(VALID_TASK_TASK1))));

        // different task index -> returns false
        assertFalse(standardCommand.equals(new EditTaskCommand(INDEX_FIRST_PERSON, INDEX_SECOND_TASK,
                new Task(VALID_TASK_TASK1))));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditTaskCommand(INDEX_FIRST_PERSON, INDEX_FIRST_TASK,
                new Task(VALID_TASK_TASK2))));
    }
}