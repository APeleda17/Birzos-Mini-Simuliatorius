import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private Asset stockAsset;
    private Asset cryptoAsset;
    private static final double DELTA = 0.01;

    @BeforeEach
    public void setUp() {
        player = new Player(1000.0);
        stockAsset = new StockAsset(100.0, "TestStock");
        cryptoAsset = new CryptoAsset(50.0, "TestCrypto");
    }

    /** Test 1: Tikrina ar buy operacija teisingai sumazina balansa su fee */
    @Test
    public void testBuyAsset_BalanceDecreases() {
        double initialBalance = player.getBalance();
        player.buy(stockAsset, 5);

        double expectedCost = 100.0 * 5 + (100.0 * 5 * 0.01);
        double expectedBalance = initialBalance - expectedCost;

        assertEquals(expectedBalance, player.getBalance(), DELTA);
    }

    /** Test 2: Tikrina ar buy operacija padidina asset amount */
    @Test
    public void testBuyAsset_AssetAmountIncreases() {
        player.buy(stockAsset, 5);
        assertEquals(5, player.getStockAmount());

        player.buy(cryptoAsset, 3);
        assertEquals(3, player.getCryptoAmount());
    }

    /** Test 3: Tikrina ar sell operacija teisingai padidina balansa su fee */
    @Test
    public void testSellAsset_BalanceIncreases() {
        player.buy(stockAsset, 5);
        double balanceAfterBuy = player.getBalance();

        player.sell(stockAsset, 3);

        double expectedGain = 100.0 * 3 - (100.0 * 3 * 0.01);
        double expectedBalance = balanceAfterBuy + expectedGain;

        assertEquals(expectedBalance, player.getBalance(), DELTA);
    }

    /** Test 4: Tikrina ar sell operacija sumazina asset amount */
    @Test
    public void testSellAsset_AssetAmountDecreases() {
        player.buy(stockAsset, 5);
        assertEquals(5, player.getStockAmount());

        player.sell(stockAsset, 3);
        assertEquals(2, player.getStockAmount());
    }

    /** Test 5: Tikrina ar buy nepavyksta kai nepakanka pinigu */
    @Test
    public void testBuyAsset_InsufficientFunds() {
        Player poorPlayer = new Player(10.0);
        double initialBalance = poorPlayer.getBalance();

        poorPlayer.buy(stockAsset, 5);

        assertEquals(initialBalance, poorPlayer.getBalance(), DELTA);
        assertEquals(0, poorPlayer.getStockAmount());
    }

    /** Test 6: Tikrina ar sell nepavyksta kai nepakanka assets */
    @Test
    public void testSellAsset_InsufficientAssets() {
        player.buy(stockAsset, 2);
        double balanceAfterBuy = player.getBalance();

        player.sell(stockAsset, 5);

        assertEquals(balanceAfterBuy, player.getBalance(), DELTA);
        assertEquals(2, player.getStockAmount());
    }

    /** Test 7: Tikrina transaction fee skaiciavima perkant */
    @Test
    public void testTransactionFee_OnBuy() {
        double initialBalance = 1000.0;
        player.buy(stockAsset, 10);

        double expectedFee = 100.0 * 10 * 0.01;
        double expectedCost = 100.0 * 10 + expectedFee;
        double expectedBalance = initialBalance - expectedCost;

        if (expectedCost > initialBalance) {
            assertEquals(initialBalance, player.getBalance(), DELTA);
        } else {
            assertEquals(expectedBalance, player.getBalance(), DELTA);
        }
    }

    /** Test 30 (A3): buy grazina true, kai pirkimas ivyksta */
    @Test
    public void testBuy_ReturnsTrueOnSuccess() {
        assertTrue(player.buy(stockAsset, 1));
    }

    /** Test 31 (A3): buy grazina false, kai nepakanka pinigu */
    @Test
    public void testBuy_ReturnsFalseOnInsufficientFunds() {
        Player poorPlayer = new Player(10.0);

        assertFalse(poorPlayer.buy(stockAsset, 1));
    }

    /** Test 32 (A3): sell grazina true, kai pardavimas ivyksta */
    @Test
    public void testSell_ReturnsTrueOnSuccess() {
        player.buy(stockAsset, 1);

        assertTrue(player.sell(stockAsset, 1));
    }

    /** Test 33 (A3): sell grazina false, kai nepakanka turto */
    @Test
    public void testSell_ReturnsFalseOnInsufficientAmount() {
        assertFalse(player.sell(stockAsset, 1));
    }
}
