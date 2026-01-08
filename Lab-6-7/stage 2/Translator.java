import java.util.*;

class Translator {
    private static Translator instance;
    private ResourceBundle bundle;

    private Translator(String language, String country) {
        Locale locale = new Locale(language, country);
        try {
            this.bundle = ResourceBundle.getBundle("MessagesBundle", locale);
        } catch (MissingResourceException e) {
            // Если файл не найден, используем русский по умолчанию
            this.bundle = ResourceBundle.getBundle("MessagesBundle", new Locale("ru", "RU"));
        }
    }

    public static void initialize(String language, String country) {
        instance = new Translator(language, country);
    }

    public static Translator getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ResourceManager not initialized");
        }
        return instance;
    }

    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key ;
        }
    }
}