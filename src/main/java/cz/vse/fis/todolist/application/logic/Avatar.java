package cz.vse.fis.todolist.application.logic;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Avatar {
    FEMALE("female"),
    MALE("male");

    private final String avatarIdentifier;
    private static final Map<String, Image> AVATAR_IMAGES = new HashMap<>();

    Avatar(final String avatarIdentifier) {
        this.avatarIdentifier = avatarIdentifier;
    }

    static {
        for (Avatar a : values()) {
            InputStream imageStream = Avatar.class.getClassLoader().getResourceAsStream(a.avatarIdentifier + "_avatar.png");
            Image avatarImage = new Image(Objects.requireNonNull(imageStream));
            AVATAR_IMAGES.put(a.avatarIdentifier, avatarImage);
        }
    }

    public String getAvatarIdentifier() {
        return avatarIdentifier;
    }

    public static Image getImageForAvatar(String avatarIdentifier) {
        return AVATAR_IMAGES.get(avatarIdentifier);
    }
}
