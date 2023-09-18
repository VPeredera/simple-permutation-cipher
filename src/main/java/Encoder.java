import java.util.*;

public class Encoder {

    public static int key = 5;
    public static int[] sequence = {3, 2, 5, 1, 4};
    private static List<Integer> whitespacePositions;
    private static int garbageLength;

    public static String encode(String message) {
        getWhitespacePositions(message);
        StringBuilder input = prepareForEncode(message);
        List<StringBuilder> list = splitByKeyValue(input);

        StringBuilder result = new StringBuilder();
        for (StringBuilder sb : list) {
            for (int i = 0; i <= key - 1 ; i++) {
                result.append(sb.charAt(sequence[i] - 1));
            }
        }
        return result.toString();
    }

    public static String decode(String message) {
        StringBuilder input = new StringBuilder(message);
        List<StringBuilder> list = splitByKeyValue(input);

        StringBuilder result = new StringBuilder();
        List<Integer> array = Arrays.stream(sequence).boxed().toList();
        for (StringBuilder sb : list) {
            for (int i = 0; i <= key - 1 ; i++) {
                result.append(sb.charAt(array.indexOf(i + 1)));
            }
        }

        result = insertWhitespaces(new StringBuilder(result.substring(0, result.length() - garbageLength)));
        return result.toString();
    }

    public static List<Integer> getWhitespaces() {
        return whitespacePositions;
    }

    public static int getGarbageLength() {
        return garbageLength;
    }

    public static void clearGarbage() {
        garbageLength = 0;
    }

    public static void setGarbageLength(int length) {
        garbageLength = length;
    }

    public static String sendPositions(List<Integer> whitespacePositions) {
        StringBuilder result = new StringBuilder();
        for (Integer pos : whitespacePositions) {
            result.append(pos).append(",");
        }
        return result.substring(0, result.length() - 1);
    }

    public static void setPositions(String positions) {
        List<Integer> result = new ArrayList<>();
        String[] separatedPositions = positions.split(",");
        for (String pos : separatedPositions) {
            result.add(Integer.valueOf(pos));
        }
        whitespacePositions = result;
    }

    private static char getRandomCharacter() {
        Random r = new Random();
        return (char) (r.nextInt('a', 'z'));
    }

    private static void getWhitespacePositions(String message) {
        whitespacePositions = new ArrayList<>();
        int pos = message.indexOf(" ");
        while (pos != -1) {
            whitespacePositions.add(pos);
            pos = message.indexOf(" ", pos + 1);
        }
    }

    private static StringBuilder insertWhitespaces(StringBuilder input) {
        if (whitespacePositions != null && !whitespacePositions.isEmpty()) {
            for (Integer index : whitespacePositions)  {
                input.insert(index, " ");
            }
            whitespacePositions.clear();
        }
        return input;
    }

    private static StringBuilder prepareForEncode(String message) {
        StringBuilder result = new StringBuilder(message.replace(" ", ""));
        while (result.length() % key != 0) {
            result.append(getRandomCharacter());
            garbageLength++;
        }
        return result;
    }

    private static List<StringBuilder> splitByKeyValue(StringBuilder message) {
        List<StringBuilder> result = new ArrayList<>();
        int length = message.length();
        for (int i = 0; i < length; i += key) {
            result.add(new StringBuilder(message.substring(i, Math.min(length, i + key))));
        }
        return result;
    }

    public static void changeKey(String newSequence) {
        int[] result = Arrays.stream(newSequence.split(", ")).mapToInt(Integer::parseInt).toArray();
        key = result.length;
        sequence = result;
    }
}
