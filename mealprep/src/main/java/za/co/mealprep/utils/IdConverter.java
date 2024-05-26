package za.co.mealprep.utils;

public class IdConverter {

    public static String convertId(Long id) {
        return id.toString();
    }
    public static Long convertId(String id) {
        if (id == null) {
            return null;
        }
        return Long.valueOf(Long.parseLong(id));
    }
}
