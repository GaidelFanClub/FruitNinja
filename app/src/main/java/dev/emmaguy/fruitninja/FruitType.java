package dev.emmaguy.fruitninja;

import java.util.Random;

public enum FruitType {
    WATERMELON(R.drawable.upgrade_displeased), STRAWBERRY(R.drawable.upgrade_evil),
    PINEAPPLE(R.drawable.upgrade_evil_2), PAPAYA(R.drawable.upgrade_kind),
    GRAPES(R.drawable.upgrade_old), APPLE(R.drawable.upgrade_sad),
    BANANA(R.drawable.upgrade_simple);

    private final int resourceId;

    private FruitType(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    private static final Random random = new Random();

    public static FruitType randomFruit() {
        return FruitType.values()[random.nextInt(FruitType.values().length)];
    }
}