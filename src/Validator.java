import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Validator {

    private Validator() {
        // No constructor
    }

    public static void validateInput(String url, String regex) {
        if (!isValidUrl(url)) {
            throw new IllegalArgumentException("Invalid URL");
        }

        if (!isValidRegex(regex)) {
            throw new IllegalArgumentException("Invalid regex!");
        }
    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private static boolean isValidRegex(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException ex) {
            return false;
        }
    }

}
