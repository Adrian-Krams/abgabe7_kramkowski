package zoo.command;

import zoo.Enclosure;
import zoo.animal.Animal;

import java.util.Objects;

public class AddAnimalCommand<A extends Animal>
        implements Command<Enclosure<? super A>, ZooError, A> {

    private final A animal;
    private boolean executed;

    public AddAnimalCommand(A animal) {
        this.animal = Objects.requireNonNull(animal);
        this.executed = false;
    }

    @Override
    public Result<ZooError, A> execute(Enclosure<? super A> target) {
        Objects.requireNonNull(target);

        if (executed) {
            return Result.failure(ZooError.COMMAND_ALREADY_EXECUTED);
        }

        boolean added = target.add(animal);

        if (!added) {
            return Result.failure(ZooError.ANIMAL_NOT_ADDED);
        }

        executed = true;
        return Result.success(animal);
    }

    @Override
    public Result<ZooError, A> undo(Enclosure<? super A> target) {
        Objects.requireNonNull(target);

        if (!executed) {
            return Result.failure(ZooError.UNDO_NOT_POSSIBLE);
        }

        boolean removed = target.remove(animal);

        if (!removed) {
            return Result.failure(ZooError.ANIMAL_NOT_REMOVED);
        }

        executed = false;
        return Result.success(animal);
    }

    @Override
    public String description() {
        return "Tier hinzufuegen: " + animal.name();
    }
}