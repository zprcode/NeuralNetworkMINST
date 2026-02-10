public class Utils {
    public static double sig(double z) {
        return 1 / (1 + Math.exp(-z));
    }

    public static double der_sig(double z) {
        return sig(z) * (1 - sig(z));
    }
}
