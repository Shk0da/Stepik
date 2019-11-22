import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Optional.ofNullable;

public class Solution {

    private static List<Object> storage = new ArrayList<>();

    public static void main(String[] args) throws IOException {

    }

    // greedyIceCream(getStringArrayFromFile("ice2.in"));
    private static void greedyIceCream(String[] data) {
        int cnt = 1;
        Set<String> used = new HashSet<>();
        for (int i = 0; i < data.length; i++) {
            if (used.contains(data[i])) {
                cnt++;
                used = new HashSet<>();
            }
            used.add(data[i]);
        }
        System.out.println(cnt);
    }

    // greedyContest(new int[]{30, 40, 20}, 60);
    // int[] data = getArrayFromFile("contest.in");
    // greedyContest(data, 50000);
    private static void greedyContest(int[] times, int allTime) {
        Arrays.sort(times);
        int i;
        int penaltyTime = 0;
        int totalSpendTime = 0;
        for (i = 0; i < times.length; i++) {
            if (totalSpendTime + times[i] >= allTime) break;
            totalSpendTime += times[i];
            penaltyTime += totalSpendTime;
        }
        System.out.println(i);
        System.out.println(penaltyTime);
    }

    // int[][] data = getFromFile("request2.in");
    // greedyUnlimitedSchedule(data[0], data[1]);
    private static void greedyUnlimitedSchedule(int[] start, int[] end) {
        sortAscTwoArraysBySecond(end, start);
        Queue<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < start.length; i++) {
            queue.add(new int[]{start[i], end[i]});
        }

        int cnt = 0;
        while (!queue.isEmpty()) {
            List<int[]> used = new ArrayList<>();
            AtomicInteger previousEnd = new AtomicInteger(0);
            queue.iterator().forEachRemaining(time -> {
                if (time[0] >= previousEnd.get()) {
                    previousEnd.set(time[1]);
                    used.add(time);
                }
            });
            cnt++;
            previousEnd.set(0);
            queue.removeAll(used);
        }
        System.out.println(cnt);
    }

    //  greedyMinimalGasStations(new int[]{0, 13, 16, 21, 35, 38, 61, 67, 70, 77, 81, 100}, 25);
    /*
        int[] azs = new int[50002];
        azs[0] = 0;
        BufferedReader file = new BufferedReader(new FileReader("petrol2.in"));
        AtomicLong idx = new AtomicLong(1);
        file.lines().forEach(line -> {
            String[] data = line.trim().split(" ");
            for (String datum : data) {
                azs[idx.intValue()] = Integer.parseInt(datum);
                idx.incrementAndGet();
            }
        });
        azs[50001] = 1000000000;
        greedyMinimalGasStations(azs, 1000000);
    */
    private static void greedyMinimalGasStations(int[] distance, final int fullFuelDistance) {
        int cnt = 0;
        int previousDist = 0;
        int fuel = fullFuelDistance;
        for (int i = 0; i < distance.length - 1; i++) {
            int dist = distance[i];
            int way = dist - previousDist;
            fuel = fuel - way;
            int nextWay = distance[i + 1] - dist;
            System.out.printf("fuel: %d, way: %d, gasStation: %d, nextWay: %d\n", fuel, dist, distance[i], nextWay);
            if (fuel <= nextWay) {
                System.out.printf("GAS STATION!: fuel: %d -> %d\n", fuel, fullFuelDistance);
                cnt++;
                fuel = fullFuelDistance;
            }
            previousDist = dist;
        }
        System.out.println(cnt);
    }

    // greedyContinuousBag(new int[]{2, 4, 10}, new int[]{10, 20, 30}, 14);
    private static void greedyContinuousBag(int[] weight, int[] cost, int maxWeight) {
        sortDescTwoArraysByRelationshipSecondToFirst(weight, cost);
        double currentCost = 0;
        double currentWeight = 0;
        for (int i = 0; i < weight.length; i++) {
            if (currentWeight + weight[i] <= maxWeight) {
                currentCost += cost[i];
                currentWeight += weight[i];
                System.out.println("weight: " + weight[i] + ", cost: " + cost[i]);
            } else {
                double partWeight = maxWeight - currentWeight;
                double partCost = cost[i] / (weight[i] / partWeight);
                currentCost += partCost;
                currentWeight += partWeight;
                System.out.println("weight: " + partWeight + " of " + weight[i] + ", cost: " + partCost + " of " + cost[i]);
            }
            if (currentWeight == maxWeight) break;
        }
        System.out.println("bag: " + currentWeight + ", cost: " + currentCost);
    }

    // greedyBag(new int[]{2, 5, 10}, new int[]{10, 20, 30}, 12);
    private static void greedyBag(int[] weight, int[] cost, int maxWeight) {
        sortDescTwoArraysByRelationshipSecondToFirst(weight, cost);
        int currentCost = 0;
        int currentWeight = 0;
        for (int i = 0; i < weight.length; i++) {
            if (currentWeight + weight[i] <= maxWeight) {
                currentCost += cost[i];
                currentWeight += weight[i];
                System.out.println("weight: " + weight[i] + ", cost: " + cost[i]);
            } else break;
        }
        System.out.println("bag: " + currentWeight + ", cost: " + currentCost);
    }

    // greedySchedule(new int[]{5, 3, 1, 7}, new int[]{7, 6, 5, 9});
    private static void greedySchedule(int[] start, int[] end) {
        sortAscTwoArraysBySecond(start, end);
        int cnt = 1;
        int last = 0;
        System.out.print(start[last] + " -> ");
        for (int i = 1; i < start.length; i++) {
            if (start[i] >= end[last]) {
                cnt++;
                last = i;
                System.out.print(start[i] + " -> ");
            }
        }
        System.out.println(end[last]);
        System.out.println(cnt);
    }

    // greedyScheduleWithSumTime(new int[]{1, 3, 5}, new int[]{5, 6, 7});
    private static void greedyScheduleWithSumTime(int[] start, int[] end) {
        Map<List<int[]>, Integer> ways = new HashMap<>();
        for (int idx : bestOrderByCost(end)) {
            List<int[]> lines = new ArrayList<>();
            int a = start[idx];
            int b = end[idx];
            lines.add(new int[]{a, b});
            for (int j = idx + 1; j < start.length; j++) {
                int a2 = start[j];
                int b2 = end[j];
                if (a2 >= b) {
                    lines.add(new int[]{a2, b2});
                    break;
                }
            }
            if (!lines.isEmpty()) {
                ways.put(lines, lines.stream().map(line -> line[1] - line[0]).reduce(0, Integer::sum));
            }
        }
        Map.Entry<List<int[]>, Integer> bestResult = ways.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow(() -> new IllegalArgumentException("Not found best way"));
        System.out.println("Total: " + ways.size());
        System.out.print("Best: ");
        bestResult.getKey().forEach(it -> System.out.print(Arrays.toString(it) + " "));
        System.out.println("-> " + bestResult.getValue());
    }

    // greedyPlan(new int[]{1, 2, 2, 3, 5}, new int[]{2, 5, 4, 1, 3});
    private static void greedyPlan(int[] deadline, int[] cost) {
        long sum = 0;
        boolean[] usedDays = new boolean[deadline.length];
        int[] bestOrderByCost = bestOrderByCost(cost);
        for (int k = 0; k < bestOrderByCost.length; k++) {
            int idx = bestOrderByCost[k];
            int day = deadline[idx];
            while (day >= 1 && usedDays[day]) {
                day--;
            }
            if (day == 0) continue;
            usedDays[day] = true;
            sum += cost[idx];
        }
        System.out.println(sum);
    }

    // moneyChange(27, new int[]{1, 2, 8, 10}, 3);
    private static void moneyChange(int sum, int[] coins, int coinIndex) {
        List<Integer> result = new ArrayList<>();
        while (sum != 0 && coinIndex >= 0) {
            int coin = coins[coinIndex];
            if (sum >= coin) {
                result.add(coin);
                sum = sum - coin;
            } else {
                coinIndex--;
            }
        }
        System.out.println(result);
    }

    //  recChips(0, new char[5], 2, false);
    private static void recChips(int idx, char[] chips, int size, boolean used) {
        if (size == 0) {
            System.out.println(Arrays.toString(chips));
            return;
        }
        if (idx == chips.length) return;

        if (!used) {
            chips[idx] = '*';
            recChips(idx + 1, chips, size - 1, true);
        }
        chips[idx] = '.';
        recChips(idx + 1, chips, size, false);
    }

    private static String replace(String s) {
        if (s.length() <= 1) return s;
        for (int i = 0; i < s.length() - 1; i++) {
            char first = s.charAt(i);
            char second = s.charAt(i + 1);
            if (first != second) {
                char newCharacter = new HashSet<Character>() {{
                    add('a');
                    add('b');
                    add('c');
                }}.stream().filter(it -> it != first && it != second).findFirst().orElseThrow();
                String newRow = s.substring(0, i) + newCharacter + s.substring(i + 2);
                System.out.println(newRow);
                return replace(newRow);
            }
        }
        return s;
    }

    // Complete the stringReduction function below.
    private static int stringReduction(String s) {
        boolean isSame = true;
        for (int i = s.length() - 1; i > 0; i--) {
            if (s.charAt(i) != s.charAt(i - 1)) {
                isSame = false;
                break;
            }
        }
        if (isSame) return s.length();

        int[] count = new int[3];
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            count[c - 'a']++;
        }
        if (s.length() % 2 == 0) {
            for (int i = 0; i < 3; i++) {
                if (count[i] % 2 != 0) return 1;
            }
            return 2;
        } else {
            for (int i = 0; i < 3; i++) {
                if (count[i] % 2 == 0) return 1;
            }
            return 2;
        }
    }

    // salesman()
    public static void salesman() {
        Integer[][] roads = new Integer[10][10];
        roads[0] = Arrays.stream("0 41 67 0 78 5 91 4 18 67".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        roads[1] = Arrays.stream("41 0 34 69 58 45 95 2 95 99".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        roads[2] = Arrays.stream("67 34 0 24 62 81 42 53 47 35".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        roads[3] = Arrays.stream("0 69 24 0 64 27 27 92 26 94".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        roads[4] = Arrays.stream("78 58 62 64 0 61 36 82 71 3".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        roads[5] = Arrays.stream("5 45 81 27 61 0 91 21 38 11".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        roads[6] = Arrays.stream("91 95 42 27 36 91 0 16 69 22".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        roads[7] = Arrays.stream("4 2 53 92 82 21 16 0 12 33".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        roads[8] = Arrays.stream("18 95 47 26 71 38 69 12 0 73".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        roads[9] = Arrays.stream("67 99 35 94 3 11 22 33 73 0".split(" ")).map(Integer::parseInt).toArray(Integer[]::new);

        int[] current = new int[roads.length];
        boolean[] used = new boolean[roads.length];
        search(1, roads, current, used, 0);
    }

    private static int result = Integer.MAX_VALUE;

    private static void search(int idx, Integer[][] roads, int[] current, boolean[] used, int len) {
        if (len >= result) return;
        if (idx == roads.length) {
            result = Math.min(result, len + roads[current[idx - 1]][0]);
            System.out.println(result);
            System.out.println(Arrays.toString(current));
            return;
        }
        for (int i = 1; i < roads.length; i++) {
            if (used[i]) continue;
            current[idx] = i;
            used[i] = true;
            search(idx + 1, roads, current, used, len + roads[current[idx - 1]][i]);
            used[i] = false;
        }
    }

    // recSum(0, 0, 1, new int[35]);
    private static void recSum(int idx, int sum, int last, int[] nums) {
        if (sum == nums.length) {
            System.out.println(Arrays.toString(Arrays.copyOf(nums, idx)));
            return;
        }
        for (int i = last; i <= nums.length - sum; i++) {
            nums[idx] = i;
            recSum(idx + 1, sum + i, i, nums);
        }
    }

    // recDiffBrackets(0, new char[14], 0, 0);
    private static void recDiffBrackets(int i, char[] mass, int bal1, int bal2) {
        if (i == mass.length) {
            if (bal1 == 0 && bal2 == 0 && checkDiffBrackets(new String(mass))) {
                System.out.println(Arrays.toString(mass));
            }
            return;
        }

        mass[i] = '(';
        recDiffBrackets(i + 1, mass, bal1 + 1, bal2);
        if (bal1 > 0 && (bal2 == 0 || bal2 > 0)) {
            mass[i] = ')';
            recDiffBrackets(i + 1, mass, bal1 - 1, bal2);
        }

        mass[i] = '[';
        recDiffBrackets(i + 1, mass, bal1, bal2 + 1);
        if (bal2 > 0 && (bal1 == 0 || bal1 > 0)) {
            mass[i] = ']';
            recDiffBrackets(i + 1, mass, bal1, bal2 - 1);
        }
    }

    // recBrackets(0, new char[6], 0);
    private static void recBrackets(int i, char[] mass, int bal) {
        if (i == mass.length) {
            if (bal == 0) {
                System.out.println(Arrays.toString(mass));
            }
            return;
        }

        mass[i] = '(';
        recBrackets(i + 1, mass, bal + 1);
        if (bal == 0) return;
        mass[i] = ')';
        recBrackets(i + 1, mass, bal - 1);
    }

    // checkDiffBrackets("(()[([][]())[()][()][][]])([])()");
    private static boolean checkDiffBrackets(String input) {
        if (input.isEmpty()) {
            return true;
        }
        if (input.contains("()")) {
            return checkDiffBrackets(input.replace("()", ""));
        }
        if (input.contains("[]")) {
            return checkDiffBrackets(input.replace("[]", ""));
        }
        return false;
    }

    // checkBrackets("()()", '(', ')');
    private static void checkBrackets(String input, char left, char right) {
        boolean result = true;
        int leftCounter = 0;
        int rightCounter = 0;
        char[] inputToChar = input.toCharArray();
        for (int i = 0; i < inputToChar.length; i++) {
            if (inputToChar[i] == left) leftCounter++;
            if (inputToChar[i] == right) rightCounter++;
            if (rightCounter > leftCounter) {
                result = false;
                break;
            }
        }
        if (leftCounter != rightCounter) result = false;
        System.out.println(result);
    }

    private static void recAlphabet(int i, char[] arr) {
        if (i == arr.length) {
            System.out.println(Arrays.toString(arr));
            return;
        }
        for (char it = 'a'; it < 'a' + arr.length; it++) {
            arr[i] = it;
            recAlphabet(i + 1, arr);
        }
    }

    // recNumbersWithDuplicates(0, new int[6], 4);
    private static void recNumbersWithDuplicates(int i, int[] arr, int len) {
        if (i == arr.length) {
            System.out.println(Arrays.toString(arr));
            return;
        }
        for (char it = 1; it <= len; it++) {
            arr[i] = it;
            recNumbersWithDuplicates(i + 1, arr, len);
        }
    }

    //  recNumbers(0, new int[7], 7, new boolean[8]);
    private static void recNumbers(int i, int[] arr, int len, boolean[] used) {
        if (i == arr.length) {
            System.out.println(Arrays.toString(arr));
            return;
        }
        for (char it = 1; it <= len; it++) {
            if (used[it]) continue;
            arr[i] = it;
            used[it] = true;
            recNumbers(i + 1, arr, len, used);
            used[it] = false;
        }
    }

    private static void sortDescTwoArraysByRelationshipSecondToFirst(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr2.length; i++) {
            for (int j = i; j < arr2.length; j++) {
                if (arr2[j] / arr1[j] > arr2[i] / arr1[i]) {
                    swap(arr1, i, j);
                    swap(arr2, i, j);
                }
            }
        }
    }

    private static void sortDesc(int[] arr1) {
        for (int i = 0; i < arr1.length; i++) {
            for (int j = i; j < arr1.length; j++) {
                if (arr1[j] > arr1[i]) {
                    swap(arr1, i, j);
                }
            }
        }
    }

    private static void sortDescTwoArraysBySecond(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr2.length; i++) {
            for (int j = i; j < arr2.length; j++) {
                if (arr2[j] > arr2[i]) {
                    swap(arr1, i, j);
                    swap(arr2, i, j);
                }
            }
        }
    }

    private static void sortAscTwoArraysBySecond(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr2.length; i++) {
            for (int j = i; j < arr2.length; j++) {
                if (arr2[j] < arr2[i]) {
                    swap(arr1, i, j);
                    swap(arr2, i, j);
                }
            }
        }
    }

    private static void swap(int[] x, int a, int b) {
        int t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    private static int[] bestOrderByCost(final int[] cost) {
        int[] mass = Arrays.copyOf(cost, cost.length);
        Arrays.sort(mass);
        Queue<Integer> maxes = new ArrayDeque<>();
        for (int i = mass.length - 1; i >= 0; i--) {
            maxes.add(mass[i]);
        }
        int[] order = new int[cost.length];
        for (int i = 0; i < order.length; i++) {
            int maxCost = ofNullable(maxes.poll()).orElse(0);
            for (int j = 0; j < cost.length; j++) {
                if (cost[j] == maxCost) {
                    order[i] = j;
                    break;
                }
            }
        }
        return order;
    }

    private static int[][] getFromFile(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        int size = Integer.parseInt(file.readLine().trim());
        int[] ax = new int[size];
        int[] bx = new int[size];
        AtomicLong idx = new AtomicLong(0);
        file.lines().forEach(line -> {
            String[] data = line.trim().split(" ");
            ax[idx.intValue()] = Integer.parseInt(data[0]);
            bx[idx.intValue()] = Integer.parseInt(data[1]);
            idx.incrementAndGet();
        });
        return new int[][]{ax, bx};
    }

    private static int[] getArrayFromFile(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        int size = Integer.parseInt(file.readLine().trim());
        List<Integer> collection = new ArrayList<>(size);
        file.lines().forEach(line -> {
            String[] data = line.trim().split(" ");
            for (String datum : data) {
                collection.add(Integer.parseInt(datum));
            }
        });
        return collection.stream().mapToInt(i -> i).toArray();
    }

    private static String[] getStringArrayFromFile(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        int size = Integer.parseInt(file.readLine().trim());
        String[] collection = new String[size];
        AtomicLong idx = new AtomicLong(0);
        file.lines().forEach(line -> {
            collection[idx.intValue()] = line.trim();
            idx.incrementAndGet();
        });
        return collection;
    }
}
