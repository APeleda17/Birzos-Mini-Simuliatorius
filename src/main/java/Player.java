import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private static final double TRANSACTION_FEE_RATE = 0.01;

    private double balance;
    private int stockAmount;
    private int cryptoAmount;
    private int commodityAmount;

    public Player(double balance) {
        this.balance = balance;
    }

    /** @return true, jei pirkimas ivyko; false, jei nepakanka pinigu */
    public boolean buy(Asset asset, int amount) {
        double value = tradeValue(asset, amount);
        double totalCost = value + calculateFee(value);

        if(balance >= totalCost) {
            balance -= totalCost;
            updateAssetAmount(asset, amount);
            return true;
        }
        return false;
    }

    /** @return true, jei pardavimas ivyko; false, jei nepakanka turto */
    public boolean sell(Asset asset, int amount) {
        if(getAssetAmount(asset) >= amount) {
            double value = tradeValue(asset, amount);
            updateAssetAmount(asset, -amount);
            balance += value - calculateFee(value);
            return true;
        }
        return false;
    }

    private static double tradeValue(Asset asset, int amount) {
        return asset.getPrice() * amount;
    }

    private static double calculateFee(double value) {
        return value * TRANSACTION_FEE_RATE;
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


