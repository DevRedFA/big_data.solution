package sedykh.tradescalc;

import java.time.LocalTime;

public class Trade implements Comparable<Trade> {
    private final LocalTime time;
    private final float price;
    private final int size;
    private final Exchange exchange;

    Trade(String[] data, Exchange exchange) {
        this.exchange = exchange;
        size = Integer.parseInt(data[2]);
        price = Float.parseFloat(data[1]);
        time = LocalTime.parse(data[0]);
    }

    public Trade(LocalTime time, float price, int size, Exchange exchange) {
        this.time = time;
        this.price = price;
        this.size = size;
        this.exchange = exchange;
    }

    LocalTime getTime() {
        return time;
    }

    public float getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public Exchange getExchange() {
        return exchange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        return Float.compare(trade.price, price) == 0
                && size == trade.size
                && (time != null ? time.equals(trade.time)
                : trade.time == null)
                && exchange == trade.exchange;
    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + size;
        result = 31 * result + (exchange != null ? exchange.hashCode() : 0);
        return result;
    }


    @Override
    public int compareTo(Trade o) {
        return this.time.compareTo(o.getTime());
    }
}