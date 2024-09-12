package xyz.nameredacted.disciplinex.staticaccess;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import xyz.nameredacted.disciplinex.cmd.moderation.KickCommand;

/**
 * This class is used to statically access various constants
 * used across various classes. It is to only be imported
 * statically. See command classes for correct implementations.
 * @see KickCommand
 */
public class StaticAccess {

    public static final boolean COMMAND_FAILED = false;
    public static final boolean COMMAND_SUCCESS = true;

//    PLUGIN PREFIX COLOUR CONSTANTS
    private static final int PLUGIN_PREFIX_COLOR_RED = 162;
    private static final int PLUGIN_PREFIX_COLOR_GREEN = 128;
    private static final int PLUGIN_PREFIX_COLOR_BLUE = 209;

//    PLUGIN PREFIX
    public static final TextComponent PLUGIN_PREFIX = Component.text("DisciplineX>> ")
        .color(TextColor.color(PLUGIN_PREFIX_COLOR_RED, PLUGIN_PREFIX_COLOR_GREEN, PLUGIN_PREFIX_COLOR_BLUE));

//    ERROR MESSAGES
    public static final TextComponent PLAYERS_ONLY_MSG = PLUGIN_PREFIX.append(Component.text("Only players may execute this command."));
    public static final TextComponent PLAYER_NOT_FOUND_MSG = PLUGIN_PREFIX.append(Component.text("The player you selected is either not online or does not exist."));
    public static final TextComponent PERMISSION_ERROR = PLUGIN_PREFIX.append(Component.text("Sorry, you do not have permission to access this command.").color(NamedTextColor.RED));

    // PUNISHMENT MESSAGES
    public static final TextComponent PLAYER_IS_MUTED = PLUGIN_PREFIX.append(Component.text("You are muted and cannot send messages.").color(NamedTextColor.WHITE));
}
