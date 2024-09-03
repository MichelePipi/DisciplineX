package xyz.michelepip.disciplinex.exception;


import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * To prevent repetition in the several
 * command classes in this plugin, this exception
 * class was created to manage the exceptions for the command. A common implementation
 * of the exception:
 *
 * <pre>
 * {@code
 * if (args.length == 0) {
 *     throw new IncorrectArgumentException("Sorry, those are the incorrect arguments.");
 * }
 * } </pre>
 * @see xyz.michelepip.disciplinex.cmd.moderation.MuteCommand
 */
public class IncorrectArgumentException extends Exception {

    public IncorrectArgumentException(Component errorMessage, Player p) {
        super(errorMessage.toString());
        p.sendMessage(errorMessage);
    }
}
