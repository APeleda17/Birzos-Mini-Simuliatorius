import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AssetFactoryTest {
    private AssetFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new AssetFactory();
    }

    /** Test 8: Tikrina ar factory sukuria StockAsset tipo objekta */
    @Test
    public void testCreateStockAsset() {
        Asset asset = factory.createAsset("STOCK", 100.0, "TestStock");

        assertNotNull(asset);
        assertTrue(asset instanceof StockAsset);
        assertEquals(100.0, asset.getPrice());
        assertEquals("TestStock", asset.getName());
    }

    /** Test 9: Tikrina ar factory sukuria CryptoAsset tipo objekta */
    @Test
    public void testCreateCryptoAsset() {
        Asset asset = factory.createAsset("CRYPTO", 50.0, "TestCrypto");

        assertNotNull(asset);
        assertTrue(asset instanceof CryptoAsset);
        assertEquals(50.0, asset.getPrice());
        assertEquals("TestCrypto", asset.getName());
    }

    /** Test 10: Tikrina ar factory sukuria CommodityAsset tipo objekta */
    @Test
    public void testCreateCommodityAsset() {
        Asset asset = factory.createAsset("COMMODITY", 200.0, "TestGold");

        assertNotNull(asset);
        assertTrue(asset instanceof CommodityAsset);
        assertEquals(200.0, asset.getPrice());
        assertEquals("TestGold", asset.getName());
    }

    /** Test 11: Tikrina ar factory meta exception su nezinomais tipais */
    @Test
    public void testCreateAsset_InvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createAsset("UNKNOWN", 100.0, "Test");
        });
    }
}
