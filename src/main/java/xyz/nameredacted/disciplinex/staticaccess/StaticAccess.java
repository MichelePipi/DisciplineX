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
    public static final TextComponent PLAYERS_ONLY_MSG = PLUGIN_PREFIX.append(Component.text("Only players may execute this command.").color(NamedTextColor.WHITE));
    public static final TextComponent PLAYER_NOT_FOUND_MSG = PLUGIN_PREFIX.append(Component.text("The player you selected is either not online or does not exist."));
    public static final TextComponent PERMISSION_ERROR = PLUGIN_PREFIX.append(Component.text("Sorry, you do not have permission to access this command.").color(NamedTextColor.RED));

    // PUNISHMENT MESSAGES
    public static final TextComponent PLAYER_IS_MUTED = PLUGIN_PREFIX.append(Component.text("You are muted and cannot send messages.").color(NamedTextColor.WHITE));
    public static final TextComponent WARNED_PLAYER = PLUGIN_PREFIX.append(Component.text("You have warned ").color(NamedTextColor.WHITE));
    public static final TextComponent PLAYER_WARNED = PLUGIN_PREFIX.append(Component.text("You have been warned by ").color(NamedTextColor.WHITE));
    public static final TextComponent KICKED_PLAYER = PLUGIN_PREFIX.append(Component.text("You have been kicked by ").color(NamedTextColor.WHITE));
    public static final TextComponent PLAYER_KICKED = PLUGIN_PREFIX.append(Component.text("You have been kicked by ").color(NamedTextColor.WHITE));
    public static final TextComponent MUTED_PLAYER = PLUGIN_PREFIX.append(Component.text("You have been muted by ").color(NamedTextColor.WHITE));
    public static final TextComponent PLAYER_MUTED = PLUGIN_PREFIX.append(Component.text("You have been muted by ").color(NamedTextColor.WHITE));
    public static final TextComponent UNMUTED_PLAYER = PLUGIN_PREFIX.append(Component.text("You have been unmuted by ").color(NamedTextColor.WHITE));
    public static final TextComponent PLAYER_UNMUTED = PLUGIN_PREFIX.append(Component.text("You have been unmuted by ").color(NamedTextColor.WHITE));
    public static final TextComponent BANNED_PLAYER = PLUGIN_PREFIX.append(Component.text("You have been banned by ").color(NamedTextColor.WHITE));
    public static final TextComponent PLAYER_BANNED = PLUGIN_PREFIX.append(Component.text("You have been banned by ").color(NamedTextColor.WHITE));
    public static final TextComponent UNBANNED_PLAYER = PLUGIN_PREFIX.append(Component.text("You have been unbanned by ").color(NamedTextColor.WHITE));
    public static final TextComponent PLAYER_UNBANNED = PLUGIN_PREFIX.append(Component.text("You have been unbanned by ").color(NamedTextColor.WHITE));
}
