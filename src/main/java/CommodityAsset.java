import java.util.Random;

public class CommodityAsset extends Asset {
    private Random random = new Random();
    private boolean isBullTrend = true;
    private int trendCounter = 0;
    private static final int TREND_DURATION_MIN = 10;
    private static final int TREND_DURATION_MAX = 15;
    private int currentTrendDuration;

    private static final double BULL_MIN = 0.98;
    private static final double BULL_MAX = 1.08;
    private static final double BEAR_MIN = 0.92;
    private static final double BEAR_MAX = 1.02;

    public CommodityAsset(double price, String name) {
        super(price, name);
        currentTrendDuration = TREND_DURATION_MIN + random.nextInt(TREND_DURATION_MAX - TREND_DURATION_MIN + 1);
    }

    @Override
    public double calculatePriceChange() {
        trendCounter++;

        if (trendCounter >= currentTrendDuration) {
            isBullTrend = !isBullTrend;
            trendCounter = 0;
            currentTrendDuration = TREND_DURATION_MIN + random.nextInt(TREND_DURATION_MAX - TREND_DURATION_MIN + 1);
        }

        if (isBullTrend) {
            return BULL_MIN + (random.nextDouble() * (BULL_MAX - BULL_MIN));
        } else {
            return BEAR_MIN + (random.nextDouble() * (BEAR_MAX - BEAR_MIN));
        }
    }
}
