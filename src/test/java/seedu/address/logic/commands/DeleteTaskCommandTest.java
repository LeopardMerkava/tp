package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteTaskCommand}.
 */
class DeleteTaskCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        Index targetTasksIndex = Index.fromZeroBased(model.getFilteredPersonList()
                .get(1)
                .getTasks()
                .size());
        List<Index> targetTaskIndexList = new ArrayList<>();
        targetTaskIndexList.add(targetTasksIndex);

        assertThrows(NullPointerException.class, () -> new DeleteTaskCommand(null, null));
        assertThrows(NullPointerException.class, () -> new DeleteTaskCommand(INDEX_FIRST_PERSON, null));
        assertThrows(NullPointerException.class, () -> new DeleteTaskCommand(null, targetTaskIndexList));
    }

    @Test
    public void constructor_correctArgs_success() {
        Index targetTasksIndex = Index.fromOneBased(model.getFilteredPersonList()
                .get(0)
                .getTasks()
                .size());
        List<Index> targetTaskIndexList = new ArrayList<>();
        targetTaskIndexList.add(targetTasksIndex);

        assertAll(() -> new DeleteTaskCommand(INDEX_FIRST_PERSON, targetTaskIndexList));
    }

    @Test
    void execute_invalidTargetPersonIndex_throwsCommandException() {
        Index targetPersonIndex = Index.fromZeroBased(model.getFilteredPersonList().size());
        Index targetTask = Index.fromZeroBased(0);
        List<Index> targetTaskIndexList = new ArrayList<>();
        targetTaskIndexList.add(targetTask);
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(targetPersonIndex, targetTaskIndexList);

        CommandException commandException = Assertions.assertThrows(
            CommandException.class, () -> deleteTaskCommand.execute(model));

        assert commandException != null;

        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, commandException.getMessage());
    }

    @Test
    void execute_invalidTargetTaskIndex_throwsCommandException() {
        Person person = model.getFilteredPersonList().get(0);
        Name personName = person.getName();
        Index targetTaskIndex = Index.fromZeroBased(person.getTasks().size());
        List<Index> targetTaskIndexList = new ArrayList<>();
        targetTaskIndexList.add(targetTaskIndex);

        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(INDEX_FIRST_PERSON, targetTaskIndexList);

        CommandException commandException = Assertions.assertThrows(
            CommandException.class, () -> deleteTaskCommand.execute(model));

        assert commandException != null;

        assertEquals(String.format(Messages.MESSAGE_INVALID_TASK, personName),
                commandException.getMessage());
    }

    @Test
    void execute_success() {
        try {
            Index personIndex = Index.fromOneBased(model.getFilteredPersonList().size());
            Person person = model.getFilteredPersonList().get(personIndex.getZeroBased());

            List<Task> taskListCopy = new ArrayList<>(person.getTasks());
            Index targetTaskIndex = Index.fromOneBased(taskListCopy.size());
            List<Index> targetTaskIndexList = new ArrayList<>();
            targetTaskIndexList.add(targetTaskIndex);

            taskListCopy.remove(targetTaskIndex.getZeroBased());

            DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(personIndex, targetTaskIndexList);

            String feedback = String.format(DeleteTaskCommand.MESSAGE_SUCCESS, 1, "task", person.getName());
            CommandResult resultCopy = new CommandResult(feedback);
            CommandResult result = deleteTaskCommand.execute(model);

            person = model.getFilteredPersonList().get(personIndex.getZeroBased());

            assertEquals(person.getTasks(), taskListCopy);
            assertEquals(result, resultCopy);
        } catch (CommandException e) {
            fail();
        }
    }

    @Test
    void execute_success_multiple() {
        try {
            Index personIndex = Index.fromOneBased(model.getFilteredPersonList().size());
            Person person = model.getFilteredPersonList().get(personIndex.getZeroBased());

            List<Task> taskListCopy = new ArrayList<>(person.getTasks());
            Index targetTaskIndex = Index.fromOneBased(taskListCopy.size());
            Index targetTaskIndex2 = Index.fromOneBased(taskListCopy.size() - 1);
            List<Index> targetTaskIndexList = new ArrayList<>();
            targetTaskIndexList.add(targetTaskIndex);
            targetTaskIndexList.add(targetTaskIndex2);

            taskListCopy.remove(targetTaskIndex.getZeroBased());
            taskListCopy.remove(targetTaskIndex.getZeroBased() - 1);

            DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(personIndex, targetTaskIndexList);

            String feedback = String.format(DeleteTaskCommand.MESSAGE_SUCCESS, 2, "tasks", person.getName());
            CommandResult resultCopy = new CommandResult(feedback);
            CommandResult result = deleteTaskCommand.execute(model);

            person = model.getFilteredPersonList().get(personIndex.getZeroBased());

            assertEquals(person.getTasks(), taskListCopy);
            assertEquals(result, resultCopy);
        } catch (CommandException e) {
            fail();
        }
    }

    @Test
    void getCommand_success() {
        String commandWord = DeleteTaskCommand.COMMAND_WORD;
        Index targetTaskIndex = Index.fromOneBased(1);
        Index targetTaskIndex2 = Index.fromOneBased(2);
        Index targetTaskIndex3 = Index.fromOneBased(3);

        List<Index> targetTaskIndexList = new ArrayList<>();
        targetTaskIndexList.add(targetTaskIndex);

        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(INDEX_FIRST_PERSON,
                targetTaskIndexList);
        assertEquals(deleteTaskCommand.getCommand(), commandWord);

        targetTaskIndexList.add(targetTaskIndex2);
        targetTaskIndexList.add(targetTaskIndex3);

        DeleteTaskCommand deleteTaskCommand2 = new DeleteTaskCommand(INDEX_FIRST_PERSON,
                targetTaskIndexList);
        assertEquals(deleteTaskCommand2.getCommand(), commandWord);
    }

    @Test
    void getDescription_success() {
        String description = DeleteTaskCommand.DESCRIPTION;
        Index targetTaskIndex = Index.fromOneBased(1);
        List<Index> targetTaskIndexList = new ArrayList<>();
        targetTaskIndexList.add(targetTaskIndex);
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(INDEX_FIRST_PERSON,
                targetTaskIndexList);
        assertEquals(deleteTaskCommand.getDescription(), description);
    }

    @Test
    public void equals() {
        Index targetTaskIndex1 = Index.fromOneBased(1);
        Index targetTaskIndex2 = Index.fromOneBased(2);

        List<Index> targetTaskIndexList1 = new ArrayList<>();
        List<Index> targetTaskIndexList2 = new ArrayList<>();
        List<Index> targetTaskIndexList3 = new ArrayList<>();

        targetTaskIndexList1.add(targetTaskIndex1);
        targetTaskIndexList2.add(targetTaskIndex2);
        targetTaskIndexList3.add(targetTaskIndex1);

        DeleteTaskCommand deleteFirstCommand = new DeleteTaskCommand(INDEX_FIRST_PERSON, targetTaskIndexList1);
        DeleteTaskCommand deleteSecondCommand = new DeleteTaskCommand(INDEX_FIRST_PERSON, targetTaskIndexList2);
        DeleteTaskCommand deleteThirdCommand = new DeleteTaskCommand(INDEX_FIRST_PERSON, targetTaskIndexList3);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteTaskCommand deleteFirstCommandCopy = new DeleteTaskCommand(INDEX_FIRST_PERSON, targetTaskIndexList1);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));
        assertFalse(deleteFirstCommand.equals(new DeleteCommand(INDEX_FIRST_PERSON)));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different task -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(new DeleteTaskCommand(INDEX_SECOND_PERSON, targetTaskIndexList1)));

        // different list but same tasks -> returns true
        assertTrue(deleteFirstCommand.equals(deleteThirdCommand));

        // different list & different tasks -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }
}
