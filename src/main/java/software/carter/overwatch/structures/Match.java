package software.carter.overwatch.structures;
/*
 * --------------------
 * Authored by: Carter
 * Timestamp: 5/19/2022
 * --------------------
 * Edit by: No one, yet.
 * Timestamp: nil
 */

import lombok.Getter;
import lombok.Setter;

public class Match {
    @Getter
    private Team blueTeam;
    @Getter
    private Team redTeam;
    @Getter @Setter
    private Team winningTeam;
    @Getter @Setter
    private int totalTime;
    @Getter @Setter
    private int currentTime;
}
