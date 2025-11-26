import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AssetTest {
    private Asset stockAsset;
    private static final double DELTA = 0.01;

    @BeforeEach
    public void setUp() {
        stockAsset = new StockAsset(100.0, "TestStock");
    }

    /** Test 12: Tikrina ar updatePrice() pakeicia kaina */
    @Test
    public void testUpdatePrice_ChangesPrice() {
        double initialPrice = stockAsset.getPrice();
        stockAsset.updatePrice();
        double newPrice = stockAsset.getPrice();

        assertNotEquals(initialPrice, newPrice);
        assertTrue(newPrice > 0);
    }

    /** Test 13: Tikrina ar queueAverage() teisingai skaiciuoja vidurki */
    @Test
    public void testQueueAverage_CalculatesCorrectly() {
        stockAsset.addQueue(100.0);
        stockAsset.addQueue(110.0);
        stockAsset.addQueue(120.0);
        stockAsset.addQueue(130.0);
        stockAsset.addQueue(140.0);

        double average = stockAsset.queueAverage();
        double expectedAverage = (100.0 + 110.0 + 120.0 + 130.0 + 140.0) / 5.0;

        assertEquals(expectedAverage, average, DELTA);
    }

    /** Test 14: Tikrina ar queueIsReady() grazina true kai queue size = 5 */
    @Test
    public void testQueueIsReady_ReturnsTrueWhenFull() {
        assertFalse(stockAsset.queueIsReady());

        stockAsset.addQueue(100.0);
        stockAsset.addQueue(110.0);
        stockAsset.addQueue(120.0);
        stockAsset.addQueue(130.0);
        assertFalse(stockAsset.queueIsReady());

        stockAsset.addQueue(140.0);
        assertTrue(stockAsset.queueIsReady());
    }
}
