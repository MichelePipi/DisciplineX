package xyz.michelepip.disciplinex.api;

import java.util.Date;
import java.util.UUID;

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
}
