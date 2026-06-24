package chess.util;

public final class DebugLog {
    private DebugLog() {
    }

    public static void operation(String area, String action) {
        System.out.println();
        System.out.println("==== " + area + " ====");
        System.out.println(action);
    }
}
