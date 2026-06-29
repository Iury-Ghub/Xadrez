package xadrez.util;

public final class LogDepuracao {
    private LogDepuracao() {
    }

    public static void operacao(String area, String acao) {
        System.out.println();
        System.out.println("==== " + area + " ====");
        System.out.println(acao);
    }
}
