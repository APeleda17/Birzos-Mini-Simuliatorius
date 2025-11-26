import java.util.Random;

public class StockAsset extends Asset {
    private Random random = new Random();
    private static final double NORMAL_MIN = 0.98;
    private static final double NORMAL_MAX = 1.02;
    private static final double NEWS_EVENT_CHANCE = 0.05;
    private static final double NEWS_IMPACT_MIN = 0.7;
    private static final double NEWS_IMPACT_MAX = 1.4;

    public StockAsset(double price, String name) {
        super(price, name);
    }

    @Override
    public double calculatePriceChange() {
        if (random.nextDouble() < NEWS_EVENT_CHANCE) {
            return NEWS_IMPACT_MIN + (random.nextDouble() * (NEWS_IMPACT_MAX - NEWS_IMPACT_MIN));
        } else {
            return NORMAL_MIN + (random.nextDouble() * (NORMAL_MAX - NORMAL_MIN));
        }
    }
}
