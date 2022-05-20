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

import java.util.HashMap;

public class Player {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private HashMap<Hero, Long> heroPlayTime;

    public Player() {
        heroPlayTime = new HashMap<>();
    }
}
