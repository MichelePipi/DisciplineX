package xyz.michelepip.disciplinex.api;

import java.util.Date;
import java.util.UUID;

/**
 * This class is used to represent a punishment.
 * A punishment is a form of discipline that can be
 * issued to a player.
 * @see PunishmentTypes
 * @see PunshmentExpirationReasons
 * @see xyz.michelepip.disciplinex.api.db.DatabaseHandler
 */
public class Punishment {

    private PunishmentTypes punishmentType;
    private UUID playerPunished;
    private UUID blame;
    private Date origin;
    private Date expiry;

    // Prevent unnecessary initialisation
    private Punishment() {}

    public Punishment(PunishmentTypes punishmentType, UUID playerPunished,
                      UUID blame, Date origin,
                      Date expiry) {
        this.punishmentType = punishmentType;
        this.playerPunished = playerPunished;
        this.blame = blame;
        this.origin = origin;
        this.expiry = expiry;
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
     * @return UUID
     */
    public UUID getPlayerPunished() {
        return playerPunished;
    }

    /**
     * This function returns the UUID of the player
     * who executed the punishment.
     * @return UUID
     */
    public UUID getBlame() {
        return blame;
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
}
