import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private static final double TRANSACTION_FEE_RATE = 0.01;
    public static final String RED_BOLD = "\033[1;31m";

    private double balance;
    private int stockAmount;
    private int cryptoAmount;
    private int commodityAmount;

    public Player(double balance, int stockAmount, int cryptoAmount, int commodityAmount) {
        this.balance = balance;
        this.stockAmount = 0;
        this.cryptoAmount = 0;
        this.commodityAmount = 0;
    }

    public void buy(Asset asset, int amount) {
        double fee = asset.getPrice() * amount * TRANSACTION_FEE_RATE;
        double totalCost = asset.getPrice() * amount + fee;

        if(balance >= totalCost) {
            balance -= totalCost;
            updateAssetAmount(asset, amount);
        } else {
            System.out.println(RED_BOLD + "Neturi tiek pinigų");
        }
    }
    public void sell(Asset asset, int amount) {
        double fee = asset.getPrice() * amount * TRANSACTION_FEE_RATE;
        int currentAmount = getAssetAmount(asset);

        if(currentAmount >= amount) {
            updateAssetAmount(asset, -amount);
            balance += asset.getPrice() * amount - fee;
        } else {
            System.out.println(RED_BOLD + "Neturi tiek coins");
        }
    }

    private void updateAssetAmount(Asset asset, int amount) {
        if(asset instanceof StockAsset) {
            stockAmount += amount;
        } else if(asset instanceof CryptoAsset) {
            cryptoAmount += amount;
        } else if(asset instanceof CommodityAsset) {
            commodityAmount += amount;
        }
    }

    private int getAssetAmount(Asset asset) {
        if(asset instanceof StockAsset) {
            return stockAmount;
        } else if(asset instanceof CryptoAsset) {
            return cryptoAmount;
        } else if(asset instanceof CommodityAsset) {
            return commodityAmount;
        }
        return 0;
    }
}


