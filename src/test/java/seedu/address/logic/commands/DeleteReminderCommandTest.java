package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showReminderAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalReminders.getTypicalReminderAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Reminder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteReminderCommand}
 */
public class DeleteReminderCommandTest {

    private Model model = new ModelManager(new AddressBook(), new UserPrefs(), getTypicalReminderAddressBook());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Reminder reminderToDelete = model.getFilteredReminderList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteReminderCommand deleteReminderCommand = new DeleteReminderCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteReminderCommand.MESSAGE_DELETE_REMINDER_SUCCESS,
                Messages.format(reminderToDelete));

        ModelManager expectedModel = new ModelManager(new AddressBook(), new UserPrefs(),
                model.getReminderAddressBook());
        expectedModel.deleteReminderInBook(reminderToDelete);

        assertCommandSuccess(deleteReminderCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredReminderList().size() + 1);
        DeleteReminderCommand deleteReminderCommand = new DeleteReminderCommand(outOfBoundIndex);

        assertCommandFailure(deleteReminderCommand, model, Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showReminderAtIndex(model, INDEX_FIRST_PERSON);

        Reminder reminderToDelete = model.getFilteredReminderList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteReminderCommand deleteReminderCommand = new DeleteReminderCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteReminderCommand.MESSAGE_DELETE_REMINDER_SUCCESS,
                Messages.format(reminderToDelete));

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs(), model.getReminderAddressBook());
        expectedModel.deleteReminderInBook(reminderToDelete);
        showNoReminder(expectedModel);

        assertCommandSuccess(deleteReminderCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showReminderAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of reminder address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getReminderAddressBook().getReminderList().size());

        DeleteReminderCommand deleteReminderCommand = new DeleteReminderCommand(outOfBoundIndex);

        assertCommandFailure(deleteReminderCommand, model, Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteReminderCommand firstCommand = new DeleteReminderCommand(INDEX_FIRST_PERSON);
        DeleteReminderCommand secondCommand = new DeleteReminderCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        DeleteReminderCommand firstCommandCopy = new DeleteReminderCommand(INDEX_FIRST_PERSON);
        assertTrue(firstCommand.equals(firstCommandCopy));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different reminder -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteReminderCommand deleteReminderCommand = new DeleteReminderCommand(targetIndex);
        String expected = DeleteReminderCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteReminderCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered reminder list to show no reminders.
     */
    private void showNoReminder(Model model) {
        model.updateFilteredReminderList(p -> false);

        assertTrue(model.getFilteredReminderList().isEmpty());
    }

}