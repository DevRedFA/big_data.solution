package sedykh.tradescalc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


class Calculator {
    private static int PARTITIONS = 8;

    public List<String> calculateByMaxPartition(File file) {
        Map<Exchange, Map.Entry<LocalTime, Integer>> MaxOfThree = new HashMap<>();
        List<String> resultsMaxOfThree = new ArrayList<>();

        Map<Exchange, List<Trade>> map = getExchangeMap(file);

        map.forEach((key, value) -> {
            if (!value.isEmpty()) {
                final Map.Entry<LocalTime, Integer> localTimeIntegerEntry = getMaxByPartition(value);
                MaxOfThree.put(key, localTimeIntegerEntry);
            }
        });


        MaxOfThree.forEach((key, value) -> {
            if (value.getValue() != 0) {
                final List<Trade> trades = map.get(key);
                LocalTime startTime = null;
                int iStartTime = 0;
                LocalTime endTime = null;
                int iStopTime = 0;
                final LocalTime startOfThree = value
                        .getKey()
                        .minus(1000, ChronoUnit.MILLIS);
                final LocalTime endOfThree = value
                        .getKey()
                        .plus(1000, ChronoUnit.MILLIS);
                for (int i = 0; i < trades.size(); i++) {
                    if (trades.get(i).getTime().compareTo(startOfThree) >= 0 && startTime == null) {
                        startTime = trades.get(i).getTime();
                        iStartTime = i;
                    }
                    if (trades.get(i).getTime().compareTo(endOfThree) >= 0 && endTime == null) {
                        endTime = trades.get(i).getTime();
                        iStopTime = i;
                    }
                }
                final List<Trade> trades1 = trades.subList(iStartTime, iStopTime);
                Map.Entry<LocalTime, Integer> maxEntry = countMaxEntry(trades1);
                final String formattedResult = getFormattedResult(maxEntry, key);
                resultsMaxOfThree.add(formattedResult);
            }
        });


        List<Trade> fullTrades = new ArrayList<>();
        map.values().forEach(fullTrades::addAll);
        fullTrades.sort(Comparator.comparing(Trade::getTime));
        final Map.Entry<LocalTime, Integer> entry = getMaxByPartition(fullTrades);
        LocalTime startTime = null;
        int iStartTime = 0;
        LocalTime endTime = null;
        int iStopTime = 0;
        final LocalTime startOfThree = entry
                .getKey()
                .minus(1000, ChronoUnit.MILLIS);
        final LocalTime endOfThree = entry
                .getKey()
                .plus(1000, ChronoUnit.MILLIS);
        for (int i = 0; i < fullTrades.size(); i++) {
            if (fullTrades.get(i).getTime().compareTo(startOfThree) >= 0 && startTime == null) {
                startTime = fullTrades.get(i).getTime();
                iStartTime = i;
            }
            if (fullTrades.get(i).getTime().compareTo(endOfThree) >= 0 && endTime == null) {
                endTime = fullTrades.get(i).getTime();
                iStopTime = i;
            }
        }
        final List<Trade> trades1 = fullTrades.subList(iStartTime, iStopTime + 1);
        Map.Entry<LocalTime, Integer> maxEntry2 = countMaxEntry(trades1);
        resultsMaxOfThree.add(getFormattedResult(maxEntry2));

        return resultsMaxOfThree;
    }

    public List<String> calculateByNative(File file) {
        List<String> results = new ArrayList<>();

        Map<Exchange, List<Trade>> map = getExchangeMap(file);

        map.forEach((key, value) -> {
            if (!value.isEmpty()) {
                Map.Entry<LocalTime, Integer> maxEntry = countMaxEntry(value);
                final String formattedResult = getFormattedResult(maxEntry, key);
                results.add(formattedResult);
            }
        });

        List<Trade> fullTrades = new ArrayList<>();
        map.values().forEach(fullTrades::addAll);
        final Map.Entry<LocalTime, Integer> maxEntry = countMaxEntry(fullTrades);
        final String formattedResult = getFormattedResult(maxEntry);
        results.add(formattedResult);

        return results;
    }

    /**
     * Parse file to map.
     *
     * @param file data file.
     * @return card containing exchanges and related trades
     */
    private Map<Exchange, List<Trade>> getExchangeMap(File file) {
        Map<Exchange, List<Trade>> map = new HashMap<>();
        List<String> lines = new ArrayList<>();
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
                    throw new RuntimeException("Wrong Exchange: " + parsed[parsed.length - 1]);
            }
        });
        return map;
    }

    /**
     * Get max sum of n intervals.
     *
     * @param list list of trade.
     * @return entry with time of max sum and max value.
     */
    private Map.Entry<LocalTime, Integer> getMaxByPartition(List<Trade> list) {
        final Map<LocalTime, Integer> countTrades = countTrades(list);
        final ArrayList<Map.Entry<LocalTime, Integer>> entries = new ArrayList<>(countTrades.entrySet());
        final Map<LocalTime, Integer> countByThreeSec = new LinkedHashMap<>();
        final int parts = PARTITIONS;
        for (int i = 0; i < entries.size() - parts + 1; i++) {
            int sum = 0;
            for (int j = i; j < i + parts; j++) {
                final Map.Entry<LocalTime, Integer> thisEntry = entries.get(j);
                sum += thisEntry.getValue();
            }
            countByThreeSec.put(entries.get(i).getKey(), sum);
        }
        return getLocalTimeIntegerEntry(countByThreeSec);
    }

    private String getFormattedResult(Map.Entry<LocalTime, Integer> maxEntry) {
        return new Formatter().format("Результаты для всех бирж: максимальное количество сделок в течение одной секунды было между %s и %s. В этот интервал прошло %s сделок.",
                maxEntry.getKey(),
                maxEntry.getKey().plus(1, ChronoUnit.SECONDS).minus(1, ChronoUnit.MILLIS),
                maxEntry.getValue()).toString();
    }

    private String getFormattedResult(Map.Entry<LocalTime, Integer> maxEntry, Exchange e) {
        return new Formatter().format("Результаты для биржы: \"%s\" : максимальное количество сделок в течение одной секунды было между %s и %s. В этот интервал прошло %s сделок.",
                e,
                maxEntry.getKey(),
                maxEntry.getKey().plus(1, ChronoUnit.SECONDS).minus(1, ChronoUnit.MILLIS),
                maxEntry.getValue()).toString();
    }

    /**
     * Calculation of trades falling in seconds, and choosing max result.
     *
     * @param list list of trades.
     * @return entry with time of max sum and max value.
     */
    private Map.Entry<LocalTime, Integer> countMaxEntry(List<Trade> list) {
        final Map<LocalTime, Integer> countTrades = nativeFind(list);
        return getLocalTimeIntegerEntry(countTrades);
    }

    /**
     * Get entry of map with max value.
     *
     * @param countTrades map containing entry with start of interval and count of trades in it.
     * @return entry with time of max sum and max value.
     */
    private Map.Entry<LocalTime, Integer> getLocalTimeIntegerEntry(Map<LocalTime, Integer> countTrades) {
        return countTrades.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();
    }

    /**
     * Calculation of trades falling in seconds.
     *
     * @param list list of trades.
     * @return map containing entry with start of interval and count of trades in it.
     */
    private Map<LocalTime, Integer> nativeFind(List<Trade> list) {
        final Map<LocalTime, Integer> mapResult = new HashMap<>();
        list.sort(Comparator.comparing(Trade::getTime));
        for1:
        for (int i = 0; i < list.size(); i++) {
            final Trade trade = list.get(i);
            if (mapResult.containsKey(trade.getTime())) {
                continue;
            }
            LocalTime startTime = trade.getTime();
            LocalTime endTime = startTime.plus(1, ChronoUnit.SECONDS);
            for (int j = i + 1; j < list.size(); j++) {
                final Trade tradeMayBe = list.get(j);
                if (tradeMayBe.getTime().compareTo(endTime) < 0) {
                    mapResult.put(startTime, mapResult.getOrDefault(startTime, 1) + 1);
                } else {
                    continue for1;
                }
            }
        }
        return mapResult;
    }

    /**
     * Split time scale and count spaced trades.
     *
     * @param list list of trades.
     * @return map containing entry with start of interval and count of trades in it.
     */
    private Map<LocalTime, Integer> countTrades(List<Trade> list) {
        final Map<LocalTime, Integer> mapResult = new LinkedHashMap<>();
        final Optional<Trade> timeEnd = list.parallelStream().max(Comparator.comparing(Trade::getTime));
        LocalTime startTime = LocalTime.of(10, 0, 0, 0);
        final int step = (1000) / PARTITIONS;
        if (timeEnd.isPresent()) {
            final LocalTime endTime = timeEnd.get().getTime();
            LocalTime currentTime = startTime;
            while (currentTime.compareTo(endTime) < 0) {
                mapResult.put(currentTime, 0);
                currentTime = currentTime.plus(step, ChronoUnit.MILLIS);
            }
            final Iterator<LocalTime> iterator = mapResult.keySet().iterator();
            final LocalTime[] key = {iterator.next()};
            final LocalTime[] threshold = {iterator.next()};
            list.parallelStream().
                    sorted(Comparator.comparing(Trade::getTime))
                    .sequential()
                    .forEach(trade -> {
                        while (iterator.hasNext()) {
                            if (trade.getTime().compareTo(threshold[0]) < 0) {
                                mapResult.put(key[0], mapResult.getOrDefault(key[0], 0) + 1);
                                break;
                            } else {
                                key[0] = threshold[0];
                                threshold[0] = iterator.next();
                            }
                        }
                    });
        }
        return mapResult;
    }
}
