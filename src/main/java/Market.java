import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Market {
    private Asset asset;

    public Market(Asset asset) {
        this.asset = asset;
    }

    public void updatePrice() {
        asset.updatePrice();
    }
}
