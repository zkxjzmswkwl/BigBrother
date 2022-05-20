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

import java.util.ArrayList;

public class Team {
    @Getter @Setter
    private ArrayList<Player> players;
    @Getter @Setter
    private int score;
}
