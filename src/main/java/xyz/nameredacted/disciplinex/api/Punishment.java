package xyz.nameredacted.disciplinex.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nameredacted.disciplinex.api.db.DatabaseHandler;

import java.util.Date;
import java.util.UUID;

/**
 * This class is used to represent a punishment.
 * A punishment is a form of discipline that can be
 * issued to a player.
 * @see PunishmentTypes
 * @see PunshmentExpirationReasons
 * @see DatabaseHandler
 */
public class Punishment {

    private PunishmentTypes punishmentType;
    private UUID playerPunished;
    private UUID blame;
    private Date origin;
    private Date expiry;
    private String reason;

    // Prevent unnecessary initialisation
    private Punishment() {}

    public Punishment(@NotNull PunishmentTypes punishmentType, @NotNull UUID playerPunished,
                      @NotNull UUID blame, @NotNull Date origin,
                      @Nullable Date expiry, @Nullable String reason) {
        this.punishmentType = punishmentType;
        this.playerPunished = playerPunished;
        this.blame = blame;
        this.origin = origin;
        this.expiry = expiry;
        this.reason = reason;
    }

    /**
     * This function returns the type of punishment.
     * @return PunishmentTypes
     */
    public PunishmentTypes getPunishmentType() {
        return punishmentType;
    }

    /**
     * This function returns the UUID of the player
     *
     * @return UUID
     */
    public String getPlayerPunished() {
        return playerPunished.toString();
    }

    /**
     * This function returns the UUID of the player
     * who executed the punishment.
     *
     * @return UUID
     */
    public String getBlame() {
        return blame.toString();
    }

    /**
     * This function returns the date when the punishment
     * was issued.
     * @return Date
     */
    public Date getOrigin() {
        return origin;
    }

    /**
     * This function returns the date when the punishment
     * is set to expire.
     * @return Date
     */
    public Date getExpiry() {
        return expiry;
    }

    public String getReason() {
        return reason;
    }
}
