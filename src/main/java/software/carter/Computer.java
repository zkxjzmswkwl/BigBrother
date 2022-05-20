package software.carter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Computer {
    public static List<Path> walkHeroDir() {
        // This probably won't work due to Windows being lame.
        Path workshopPath = Paths.get("heroes/");

        try {
            List<Path> files = Files.walk(Path.of("heroes/")).filter(p -> !p.toFile().isDirectory()).toList();
            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fuck you
        return null;
    }
}

