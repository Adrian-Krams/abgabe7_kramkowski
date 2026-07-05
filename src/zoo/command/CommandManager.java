package zoo.command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandManager<G> {

    private static final Logger LOG = Logger.getLogger(CommandManager.class.getName());

    private final Deque<Command<? super G, ZooError, ?>> undoStack = new ArrayDeque<>();
    private final Deque<Command<? super G, ZooError, ?>> redoStack = new ArrayDeque<>();

    public void executeCommand(Command<? super G, ZooError, ?> command, G target) {
        LOG.info(() -> "executeCommand gestartet: " + command.description());

        Result<ZooError, ?> result = command.execute(target);

        if (result instanceof Result.Success<?, ?> success) {
            undoStack.push(command);
            redoStack.clear();

            LOG.info(() -> "Command erfolgreich ausgefuehrt: "
                    + command.description()
                    + ", Ergebnis: "
                    + success.value());
        }

        if (result instanceof Result.Failure<?, ?> failure) {
            ZooError error = (ZooError) failure.error();

            LOG.log(Level.WARNING, "Command fehlgeschlagen: "
                    + command.description()
                    + ", Fehler: "
                    + error);
        }

        LOG.fine(() -> "Zustand des Ziels nach executeCommand: " + target);
    }

    public void undo(G target) {
        LOG.info("undo gestartet");

        if (undoStack.isEmpty()) {
            LOG.warning("Undo nicht moeglich, da der undoStack leer ist.");
            LOG.fine(() -> "Zustand des Ziels nach undo: " + target);
            return;
        }

        Command<? super G, ZooError, ?> command = undoStack.pop();

        Result<ZooError, ?> result = command.undo(target);

        if (result instanceof Result.Success<?, ?> success) {
            redoStack.push(command);

            LOG.info(() -> "Undo erfolgreich: "
                    + command.description()
                    + ", Ergebnis: "
                    + success.value());
        }

        if (result instanceof Result.Failure<?, ?> failure) {
            undoStack.push(command);

            ZooError error = (ZooError) failure.error();

            LOG.log(Level.WARNING, "Undo fehlgeschlagen: "
                    + command.description()
                    + ", Fehler: "
                    + error);
        }

        LOG.fine(() -> "Zustand des Ziels nach undo: " + target);
    }

    public void redo(G target) {
        LOG.info("redo gestartet");

        if (redoStack.isEmpty()) {
            LOG.warning("Redo nicht moeglich, da der redoStack leer ist.");
            LOG.fine(() -> "Zustand des Ziels nach redo: " + target);
            return;
        }

        Command<? super G, ZooError, ?> command = redoStack.pop();

        Result<ZooError, ?> result = command.execute(target);

        if (result instanceof Result.Success<?, ?> success) {
            undoStack.push(command);

            LOG.info(() -> "Redo erfolgreich: "
                    + command.description()
                    + ", Ergebnis: "
                    + success.value());
        }

        if (result instanceof Result.Failure<?, ?> failure) {
            redoStack.push(command);

            ZooError error = (ZooError) failure.error();

            LOG.log(Level.WARNING, "Redo fehlgeschlagen: "
                    + command.description()
                    + ", Fehler: "
                    + error);
        }

        LOG.fine(() -> "Zustand des Ziels nach redo: " + target);
    }
}