package sedykh.tradescalc;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Experiment {
//        final ExecutorService executorService = Executors.newCachedThreadPool();

//        for (Map.Entry<Exchange, List<Trade>> entry : map.entrySet()) {
//            Map.Entry<LocalTime, Integer> maxEntry = countMaxEntry(entry.getValue());
//            final String formattedResult = getFormattedResult(maxEntry, entry);
//            results.add(formattedResult);
//        }

//        List<Future<String>> futureList = new CopyOnWriteArrayList<>();
//        for (Map.Entry<Exchange, List<Trade>> entry : map.entrySet()) {
//            final Future<String> submit = executorService.submit(() -> {
//                Map.Entry<LocalTime, Integer> maxEntry = countMaxEntry(entry.getValue());
//                return getFormattedResult(maxEntry, entry);
//            });
//            futureList.add(submit);
//        }

//        map.entrySet().parallelStream().forEach(entry -> {
//            final Future<String> submit = executorService.submit(() -> {
//                Map.Entry<LocalTime, Integer> maxEntry = countMaxEntry(entry.getValue());
//                return getFormattedResult(maxEntry, entry);
//            });
//            futureList.add(submit);
//        });

//        futureList.forEach(s -> {
//            try {
//                results.add(s.get());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        });


    private Map<LocalTime, Integer> nativeFindOnLamda(List<Trade> list) {
        final Map<LocalTime, Integer> mapResult = new HashMap<>();
        list.sort(Comparator.comparing(Trade::getTime));
        for (int i = 0; i < list.size(); i++) {
            final Trade trade = list.get(i);
            if (mapResult.containsKey(trade.getTime())) {
                continue;
            }
            LocalTime startTime = trade.getTime();
            LocalTime endTime = startTime.plus(1, ChronoUnit.SECONDS);
            final long count = list.parallelStream()
                    //                    .sorted()
//                    .sorted(Comparator.comparing(Trade::getTime))
                    .skip(i)
                    .filter(s -> s.getTime().compareTo(startTime) >= 0 && s.getTime().compareTo(endTime) < 0)
//                    .filter(s ->  s.getTime().compareTo(endTime) < 0)
                    .count();
            mapResult.put(trade.getTime(), (int) count);
        }
        return mapResult;
    }
}
