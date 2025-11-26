public class AssetFactory {
    private static final String STOCK_TYPE = "STOCK";
    private static final String CRYPTO_TYPE = "CRYPTO";
    private static final String COMMODITY_TYPE = "COMMODITY";

    public Asset createAsset(String type, double price, String name) {
        switch(type.toUpperCase()) {
            case STOCK_TYPE:
                return new StockAsset(price, name);
            case CRYPTO_TYPE:
                return new CryptoAsset(price, name);
            case COMMODITY_TYPE:
                return new CommodityAsset(price, name);
            default:
                throw new IllegalArgumentException("Nezinomas asset tipas: " + type);
        }
    }
}
