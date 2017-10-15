package sedykh.tradescalc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class Calculator {

    public List<String> calculate(File file) {
        List<String> results = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        Map<Exchange, List<Trade>> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                lines.add(reader.readLine());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (Exchange e : Exchange.values()) {
            map.put(e, new ArrayList<>());
        }

        lines.remove(0);
        lines.forEach(line -> {
            String[] parsed = line.split(",");
            switch (parsed[parsed.length - 1]) {
                case "V":
                    map.get(Exchange.V).add(new Trade(parsed, Exchange.V));
                    break;
                case "D":
                    map.get(Exchange.D).add(new Trade(parsed, Exchange.D));
                    break;
                case "B":
                    map.get(Exchange.B).add(new Trade(parsed, Exchange.B));
                    break;
                case "Y":
                    map.get(Exchange.Y).add(new Trade(parsed, Exchange.Y));
                    break;
                case "J":
                    map.get(Exchange.J).add(new Trade(parsed, Exchange.J));
                    break;
                case "Q":
                    map.get(Exchange.Q).add(new Trade(parsed, Exchange.Q));
                    break;
                case "Z":
                    map.get(Exchange.Z).add(new Trade(parsed, Exchange.Z));
                    break;
                case "K":
                    map.get(Exchange.K).add(new Trade(parsed, Exchange.K));
                    break;
                case "P":
                    map.get(Exchange.P).add(new Trade(parsed, Exchange.P));
                    break;
                case "X":
                    map.get(Exchange.X).add(new Trade(parsed, Exchange.X));
                    break;
                default:
                    System.out.println("parsed[parsed.length - 1]" + parsed[parsed.length - 1]);
                    break;
            }
        });
        final Formatter formatter = new Formatter();
        for (Map.Entry<Exchange, List<Trade>> entry : map.entrySet()) {

            Map.Entry<LocalTime, Integer> maxEntry = countMaxEntry(entry.getValue());
            results.add(formatter.format("–езультаты дл€ биржы: \"%s\" : максимальное количество сделок в течение одной секунды было между %s и %s. ¬ этот интервал прошло %s сделок.",
                    entry.getKey(),
                    maxEntry.getKey(),
                    maxEntry.getKey().plus(1, ChronoUnit.SECONDS).minus(1, ChronoUnit.MILLIS),
                    maxEntry.getValue()).toString());
            System.out.printf("–езультаты дл€ биржы: \"%s\" : максимальное количество сделок в течение одной секунды было между %s и %s. ¬ этот интервал прошло %s сделок.",
                    entry.getKey(),
                    maxEntry.getKey(),
                    maxEntry.getKey().plus(1, ChronoUnit.SECONDS).minus(1, ChronoUnit.MILLIS),
                    maxEntry.getValue());
            System.out.println();
        }
        List<Trade> fullTrades = new ArrayList<>();
        map.values().forEach(fullTrades::addAll);
        final Map.Entry<LocalTime, Integer> maxEntry = countMaxEntry(fullTrades);

        results.add(formatter.format("–езультаты дл€ всех бирж: максимальное количество сделок в течение одной секунды было между %s и %s. ¬ этот интервал прошло %s сделок.",
                maxEntry.getKey(),
                maxEntry.getKey().plus(1, ChronoUnit.SECONDS).minus(1, ChronoUnit.MILLIS),
                maxEntry.getValue()).toString());
        System.out.printf("–езультаты дл€ всех бирж: максимальное количество сделок в течение одной секунды было между %s и %s. ¬ этот интервал прошло %s сделок.",
                maxEntry.getKey(),
                maxEntry.getKey().plus(1, ChronoUnit.SECONDS).minus(1, ChronoUnit.MILLIS),
                maxEntry.getValue());
        System.out.println();

        return results;
    }

    private Map.Entry<LocalTime, Integer> countMaxEntry(List<Trade> list) {
        final Map<LocalTime, Integer> countTrades = countTrades(list);
        return getLocalTimeIntegerEntry(countTrades);
    }

    private Map.Entry<LocalTime, Integer> getLocalTimeIntegerEntry(Map<LocalTime, Integer> countTrades) {
//        Map.Entry<LocalTime, Integer> maxEntry = null;
//        for (Map.Entry<LocalTime, Integer> entry1 : countTrades.entrySet()) {
//            if (maxEntry == null) {
//                maxEntry = entry1;
//            }
//            if (entry1.getValue() > maxEntry.getValue()) {
//                maxEntry = entry1;
//            }
//        }
        final Map.Entry<LocalTime, Integer> localTimeIntegerEntry = countTrades.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get();
//        return maxEntry;
        return localTimeIntegerEntry;
    }

    // можно сортировать или пройтись и раскидать по мапам
    // проверить оба варинта
    private Map<LocalTime, Integer> countTrades(List<Trade> list) {
        Map<LocalTime, Integer> mapResult = new HashMap<>();
        LocalTime startTime = null;
        Optional<Trade> time = list.parallelStream().min(Comparator.comparing(Trade::getTime));
        if (time.isPresent()) {
            startTime = time.get().getTime();
        }

        LocalTime finalStartTime = startTime.plus(1, ChronoUnit.SECONDS);
        list.sort(Comparator.comparing(Trade::getTime));
        for (Trade trade : list) {
            if (trade.getTime().compareTo(finalStartTime) < 0) {
                mapResult.put(finalStartTime, mapResult.getOrDefault(finalStartTime, 0) + 1);
            } else {
                finalStartTime = finalStartTime.plus(1, ChronoUnit.SECONDS);
            }
        }
        return mapResult;
    }


    class Trade implements Comparable<Trade> {
        private final LocalTime time;
        private final float price;
        private final int size;
        private final Exchange exchange;

        public Trade(String[] data, Exchange exchange) {
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

        public LocalTime getTime() {
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


    enum Exchange {
        V,
        D,
        B,
        Y,
        J,
        Q,
        Z,
        K,
        P,
        X
    }
}
