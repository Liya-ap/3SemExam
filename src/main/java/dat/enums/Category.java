package dat.enums;

public enum Category {
    BEACH,
    CITY,
    FOREST,
    LAKE,
    SEA,
    SNOW;


    public static Category fromString(String value) {
        return Category.valueOf(value.toUpperCase());
    }
}
