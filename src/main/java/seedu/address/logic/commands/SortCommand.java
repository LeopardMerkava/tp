package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Changes the remark of an existing person in the address book.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the list of persons "
            + "by the alphabetical order of their name.\n"
            + "Parameters: [-r]\n"
            + "Example: " + COMMAND_WORD
            + " -r";

    private String message_success = "Sorted all persons";

    private final boolean isReverseOrder;

    public SortCommand(boolean isReverseOrder) {
        this.isReverseOrder = isReverseOrder;
    }

    @Override
    public CommandResult execute(Model model) {
        if (isReverseOrder) {
            message_success = message_success + " in reverse order";
        }
        requireNonNull(model);
        model.updateSortedPersonList(isReverseOrder);
        return new CommandResult(message_success);
    }

}