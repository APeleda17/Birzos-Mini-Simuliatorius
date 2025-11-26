import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public abstract class Asset {
    private static final int MAX_QUEUE_SIZE = 6;
    private static final int REQUIRED_QUEUE_SIZE = 5;

    private double price;
    private String name;
    private Queue<Double> priceQueue = new LinkedList<>();
    private double sum;

    public Asset(double price, String name) {
        this.price = price;
        this.name = name;
    }

    public final void updatePrice() {
        double change = calculatePriceChange();
        this.price = this.price * change;
        addQueue(this.price);
    }

    protected abstract double calculatePriceChange();

    public void addQueue(double q) {
        priceQueue.add(q);

        if(priceQueue.size() == MAX_QUEUE_SIZE) {
            priceQueue.poll();
        }
    }

    public double queueAverage() {
        sum = sum * 0;
        for(var s : priceQueue) {
            sum += s;
        }
        return sum / priceQueue.size();
    }

    public boolean queueIsReady() {
        return priceQueue.size() == REQUIRED_QUEUE_SIZE;
    }
}
