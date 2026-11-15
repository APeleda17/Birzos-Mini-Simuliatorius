import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private static final double TRANSACTION_FEE_RATE = 0.01;

    @Getter
    @Setter
    private double balance;
    private final Map<Asset, Integer> holdings = new HashMap<>();

    public Player(double balance) {
        this.balance = balance;
    }

    /** @return true, jei pirkimas ivyko; false, jei kiekis neteigiamas arba nepakanka pinigu */
    public boolean buy(Asset asset, int amount) {
        if(amount <= 0) {
            return false;
        }
        double value = tradeValue(asset, amount);
        double totalCost = value + calculateFee(value);

        if(balance >= totalCost) {
            balance -= totalCost;
            updateAssetAmount(asset, amount);
            return true;
        }
        return false;
    }

    /** @return true, jei pardavimas ivyko; false, jei kiekis neteigiamas arba nepakanka turto */
    public boolean sell(Asset asset, int amount) {
        if(amount <= 0) {
            return false;
        }
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
        holdings.merge(asset, amount, Integer::sum);
    }

    public int getAssetAmount(Asset asset) {
        return holdings.getOrDefault(asset, 0);
    }
}


