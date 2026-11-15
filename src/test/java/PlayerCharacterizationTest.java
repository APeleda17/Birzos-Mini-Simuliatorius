import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Charakterizavimo testai Player klasei (2 ND).
 * Fiksuoja DABARTINE elgsena pries refaktoringa: turi praeiti ir pries, ir po jo.
 */
public class PlayerCharacterizationTest {
    private static final double DELTA = 0.0001;

    private Player player;
    private Asset stock;
    private Asset crypto;
    private Asset commodity;

    @BeforeEach
    public void setUp() {
        player = new Player(1000.0);
        stock = new StockAsset(100.0, "TestStock");
        crypto = new CryptoAsset(50.0, "TestCrypto");
        commodity = new CommodityAsset(200.0, "TestGold");
    }

    /** Test 15: Crypto pirkimas: balansas = 1000 - (4 * 50 + 1 % mokestis) */
    @Test
    public void testBuyCrypto_UpdatesBalanceAndAmount() {
        player.buy(crypto, 4);

        assertEquals(1000.0 - 202.0, player.getBalance(), DELTA);
        assertEquals(4, player.getAssetAmount(crypto));
    }

    /** Test 16: Commodity pirkimas: balansas = 1000 - (2 * 200 + 1 % mokestis) */
    @Test
    public void testBuyCommodity_UpdatesBalanceAndAmount() {
        player.buy(commodity, 2);

        assertEquals(1000.0 - 404.0, player.getBalance(), DELTA);
        assertEquals(2, player.getAssetAmount(commodity));
    }

    /** Test 17: Crypto pardavimas: gaunama 4 * 50 - 1 % mokestis */
    @Test
    public void testSellCrypto_UpdatesBalanceAndAmount() {
        player.buy(crypto, 4);
        player.sell(crypto, 4);

        assertEquals(798.0 + 198.0, player.getBalance(), DELTA);
        assertEquals(0, player.getAssetAmount(crypto));
    }

    /** Test 18: Commodity pardavimas: gaunama 1 * 200 - 1 % mokestis */
    @Test
    public void testSellCommodity_UpdatesBalanceAndAmount() {
        player.buy(commodity, 2);
        player.sell(commodity, 1);

        assertEquals(596.0 + 198.0, player.getBalance(), DELTA);
        assertEquals(1, player.getAssetAmount(commodity));
    }

    /** Test 19: Pardavimo mokestis: nupirkus ir pardavus 5 akcijas prarandama 2 * 5 EUR mokesciu */
    @Test
    public void testSellFee_RoundTripLosesBothFees() {
        player.buy(stock, 5);
        assertEquals(495.0, player.getBalance(), DELTA);

        player.sell(stock, 5);

        assertEquals(990.0, player.getBalance(), DELTA);
        assertEquals(0, player.getAssetAmount(stock));
    }

    /** Test 20: Crypto pirkimas nepavyksta, kai nepakanka pinigu */
    @Test
    public void testBuyCrypto_InsufficientFunds() {
        Player poorPlayer = new Player(10.0);

        poorPlayer.buy(crypto, 1);

        assertEquals(10.0, poorPlayer.getBalance(), DELTA);
        assertEquals(0, poorPlayer.getAssetAmount(crypto));
    }

    /** Test 21: Commodity pirkimas nepavyksta, kai nepakanka pinigu */
    @Test
    public void testBuyCommodity_InsufficientFunds() {
        Player poorPlayer = new Player(10.0);

        poorPlayer.buy(commodity, 1);

        assertEquals(10.0, poorPlayer.getBalance(), DELTA);
        assertEquals(0, poorPlayer.getAssetAmount(commodity));
    }

    /** Test 22: Crypto pardavimas nepavyksta, kai nepakanka kiekio */
    @Test
    public void testSellCrypto_InsufficientAmount() {
        player.buy(crypto, 1);
        double balanceAfterBuy = player.getBalance();

        player.sell(crypto, 2);

        assertEquals(balanceAfterBuy, player.getBalance(), DELTA);
        assertEquals(1, player.getAssetAmount(crypto));
    }

    /** Test 23: Commodity pardavimas nepavyksta, kai nepakanka kiekio */
    @Test
    public void testSellCommodity_InsufficientAmount() {
        player.sell(commodity, 1);

        assertEquals(1000.0, player.getBalance(), DELTA);
        assertEquals(0, player.getAssetAmount(commodity));
    }

    /** Test 24: Riba: kai balansas lygiai lygus kainai su mokesciu, pirkimas pavyksta (>=) */
    @Test
    public void testBuy_ExactBalanceIsEnough() {
        Player exactPlayer = new Player(505.0);

        exactPlayer.buy(stock, 5);

        assertEquals(0.0, exactPlayer.getBalance(), DELTA);
        assertEquals(5, exactPlayer.getAssetAmount(stock));
    }

    /** Test 25: Skirtingu tipu kiekiai nesimaiso */
    @Test
    public void testAmountsAreTrackedSeparatelyPerAsset() {
        player.buy(stock, 3);
        player.buy(crypto, 2);

        assertEquals(3, player.getAssetAmount(stock));
        assertEquals(2, player.getAssetAmount(crypto));
        assertEquals(0, player.getAssetAmount(commodity));
    }

    /** Test 26: Pardavus 0 vienetu (taip daro Game.gameOver), niekas nepasikeicia */
    @Test
    public void testSellZero_NothingChanges() {
        player.sell(stock, 0);

        assertEquals(1000.0, player.getBalance(), DELTA);
        assertEquals(0, player.getAssetAmount(stock));
    }

    /** Test 27: Nezinomo tipo turto Player neturi, todel pardavimas nepavyksta */
    @Test
    public void testSellUnknownAssetType_Fails() {
        Asset unknown = new Asset(100.0, "Unknown") {
            @Override
            protected double calculatePriceChange() {
                return 1.0;
            }
        };

        player.sell(unknown, 1);

        assertEquals(1000.0, player.getBalance(), DELTA);
    }

    /** Test 28 (A1 bug fix): buy(-5) atmetamas; anksciau balansas tapdavo 1505, kiekis -5 */
    @Test
    public void testBuyNegativeAmount_IsRejected() {
        assertFalse(player.buy(stock, -5));

        assertEquals(1000.0, player.getBalance(), DELTA);
        assertEquals(0, player.getAssetAmount(stock));
    }

    /** Test 29 (A1 bug fix): sell(-5) atmetamas; anksciau "nupirkdavo" 5 akcijas uz 505 EUR */
    @Test
    public void testSellNegativeAmount_IsRejected() {
        assertFalse(player.sell(stock, -5));

        assertEquals(1000.0, player.getBalance(), DELTA);
        assertEquals(0, player.getAssetAmount(stock));
    }

    /** Test 34 (A1 bug fix): buy(0) atmetamas, niekas nepasikeicia */
    @Test
    public void testBuyZero_IsRejected() {
        assertFalse(player.buy(stock, 0));

        assertEquals(1000.0, player.getBalance(), DELTA);
        assertEquals(0, player.getAssetAmount(stock));
    }

    /** Test 35 (A1 bug fix): sell(0) grazina false; Game.gameOver to nenaudoja, todel zaidimo pabaiga veikia */
    @Test
    public void testSellZero_ReturnsFalse() {
        assertFalse(player.sell(stock, 0));
    }
}
