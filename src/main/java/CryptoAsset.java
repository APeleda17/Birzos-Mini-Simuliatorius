import java.util.Random;

public class CryptoAsset extends Asset {
    private Random random = new Random();
    private static final double MIN_CHANGE = 0.6;
    private static final double MAX_CHANGE = 1.4;

    public CryptoAsset(double price, String name) {
        super(price, name);
    }

    @Override
    public double calculatePriceChange() {
        return MIN_CHANGE + (random.nextDouble() * (MAX_CHANGE - MIN_CHANGE));
    }
}
