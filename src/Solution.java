import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.Math.*;
import static java.util.Optional.ofNullable;

public class Solution {

    private static List<Object> storage = new ArrayList<>();

    public static void main(String[] arg) throws IOException {

    }

    // dynamicArithmeticExpression(getArrayFromFile("arithm.in"), 25);
    private static void dynamicArithmeticExpression(int[] args, int result) {
        int n = args.length;
        int[][] storage = new int[n][];
        char[][] actions = new char[n][];

        storage[0] = new int[]{args[0]};
        actions[0] = new char[1];
        for (int i = 1; i < args.length; i++) {
            storage[i] = new int[storage[i - 1].length * 2];
            actions[i] = new char[actions[i - 1].length * 2];
            for (int k = 0, idx = 0; k < storage[i - 1].length; k++, idx = idx + 2) {
                int prev = storage[i - 1][k];
                storage[i][idx] = prev + args[i];
                actions[i][idx] = '+';
                storage[i][idx + 1] = prev - args[i];
                actions[i][idx + 1] = '-';
            }
        }

        for (int j = 0; j < storage[n - 1].length; j++) {
            int val = storage[n - 1][j];
            if (val == result) {
                StringBuilder sb = new StringBuilder();
                for (int i = n - 1, moult = 1; i >= 0; i--, moult = moult * 2) {
                    sb.insert(0, args[i]);
                    if (i != 0) {
                        sb.insert(0, actions[i][j / moult]);
                    }
                }
                System.out.println(sb + "=" + result);
            }
        }
    }

    // treeArithmeticExpression(getArrayFromFile("arithm.in"), 25);
    private static void treeArithmeticExpression(int[] args, int result) {
        StringBuilder sb = new StringBuilder();
        recFillTree(new Node(null, args[0], args[0]), args, 1, result, sb);
        System.out.println(sb.toString() + "=" + result);
    }

    private static void recFillTree(Node root, int[] args, int i, int result, StringBuilder sb) {
        if (i == args.length) {
            if (root.value == result) {
                recNodeExpression(root, sb);
            }
            return;
        }

        root.plus = new Node(root, args[i], root.value + args[i]);
        root.minus = new Node(root, args[i], root.value - args[i]);

        recFillTree(root.plus, args, i + 1, result, sb);
        recFillTree(root.minus, args, i + 1, result, sb);
    }

    private static void recNodeExpression(Node currentNode, StringBuilder sb) {
        Node parent = currentNode.previous;
        sb.insert(0, currentNode.arg);
        if (null == parent) return;
        if (parent.plus == currentNode) {
            sb.insert(0, "+");
        }
        if (parent.minus == currentNode) {
            sb.insert(0, "-");
        }
        recNodeExpression(parent, sb);
    }

    private static final class Node {
        private int arg;
        private int value;
        private Node previous;
        private Node plus;
        private Node minus;

        Node(Node previous, int arg, int value) {
            this.previous = previous;
            this.arg = arg;
            this.value = value;
            plus = minus = null;
        }
    }

    // dynamicLimitedBag(new int[]{2, 5, 10}, new int[]{10, 20, 30}, 12);
    // int[][] data =  getFromFile("rucksack.in");
    // dynamicLimitedBag(data[0], data[1], 10000);
    private static void dynamicLimitedBag(int[] weights, int[] costs, int needed) {
        int n = weights.length;
        int[][] dp = new int[needed + 1][n + 1];
        for (int j = 1; j <= n; j++) {
            for (int w = 1; w <= needed; w++) {
                dp[w][j] = dp[w][j - 1];
                if (weights[j - 1] <= w && dp[w - weights[j - 1]][j - 1] + costs[j - 1] > dp[w][j - 1]) {
                    dp[w][j] = dp[w - weights[j - 1]][j - 1] + costs[j - 1];
                }
            }
        }
        int sum = dp[needed][n];
        System.out.println(sum);

        Set<Integer> items = new TreeSet<>();
        for (int i = n; i > 0 && sum > 0; i--) {
            if (sum == dp[needed][i - 1]) continue;
            items.add(weights[i - 1]);
            sum = sum - costs[i - 1];
            needed = needed - weights[i - 1];
        }
        items.forEach(it -> System.out.print(it + " "));
        System.out.println();
    }

    // pascalTriangle(50, 20)
    private static long pascalTriangle(int n, int k) {
        long[][] c = new long[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            c[i][0] = c[i][i] = 1;
            for (int j = 1; j < i; j++) {
                c[i][j] = c[i - 1][j - 1] + c[i - 1][j];
            }
        }
        System.out.println(c[n][k]);
        return c[n][k];
    }

    // dynamicCountSubSequenceGrowUp(new int[]{3, 2, 1, 4, 6, 5, 7});
    private static void dynamicCountSubSequenceGrowUp(int[] array) {
        int n = array.length;
        int k = dynamicSubSequenceGrowUp(array);

        long sum = 0;
        int[][] dp = new int[k][n];
        for (int i = 0; i < n; i++) {
            dp[0][i] = 1;
        }

        for (int l = 1; l < k; l++) {
            for (int i = l; i < n; i++) {
                dp[l][i] = 0;
                for (int j = l - 1; j < i; j++) {
                    if (array[j] < array[i]) {
                        dp[l][i] += dp[l - 1][j];
                    }
                }
            }
        }

        for (int i = k - 1; i < n; i++) {
            sum += dp[k - 1][i];
        }
        System.out.println(sum);
    }

    // dynamicSubSequenceGrowUp(new int[]{7, 1, 3, 2, 4});
    private static int dynamicSubSequenceGrowUp(int[] array) {
        int[] subsequence = new int[array.length + 1];
        int[] lengthOfSubsequence = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            lengthOfSubsequence[i] = 1;
            subsequence[i] = -1;
            for (int j = 0; j < i; j++) {
                if (array[j] < array[i] && lengthOfSubsequence[j] + 1 > lengthOfSubsequence[i]) {
                    lengthOfSubsequence[i] = lengthOfSubsequence[j] + 1;
                    subsequence[i] = j;
                }
            }
        }

        int pos = 0;
        int ans = lengthOfSubsequence[0];
        for (int i = 0; i < array.length; ++i) {
            if (lengthOfSubsequence[i] > ans) {
                ans = lengthOfSubsequence[i];
                pos = i;
            }
        }

        Deque<Integer> path = new ArrayDeque<>();
        while (pos != -1) {
            path.push(pos);
            pos = subsequence[pos];
        }

        path.forEach(it -> System.out.print(array[it] + " "));
        System.out.println();
        System.out.println(ans);
        System.out.println();
        return ans;
    }

    // dynamicDominoByMod(42, 100);
    // dynamicDominoByMod(100000, 1000000000);
    private static void dynamicDominoByMod(int n, int m) {
        if (n < 50) {
            double goldenSection = (sqrt(5) + 1) / 2;
            int fastResult = (int) round(pow(goldenSection, n + 1) / sqrt(5));
            System.out.println(fastResult % m);
            return;
        }

        int x1 = 1;
        int x2 = 1;
        int res = 0;
        for (int i = 2; i < n + 1; i++) {
            res = (x1 + x2) % m;
            x1 = x2;
            x2 = res;
        }
        System.out.println(res);
    }

    //  BufferedReader file = new BufferedReader(new FileReader("seq2.in"));
    //  int firstSize = parseInt(file.readLine().trim());
    //  int[] firstArg = Arrays.stream(file.readLine().trim().split(" ")).mapToInt(Integer::parseInt).toArray();
    //  int secondSize = parseInt(file.readLine().trim());
    //  int[] secondArg = Arrays.stream(file.readLine().trim().split(" ")).mapToInt(Integer::parseInt).toArray();
    // dynamicSubSequence(new int[]{1, 2, 3, 4}, new int[]{3, 1, 4, 5, 3, 1, 2, 4});
    private static void dynamicSubSequence(int[] a, int[] b) {
        if (a.length > b.length) {
            int[] tmp = a;
            a = b;
            b = tmp;
        }
        int[][] maxCounts = new int[a.length + 1][b.length + 1];
        for (int i = 1; i <= a.length; i++) {
            for (int j = 1; j <= b.length; j++) {
                maxCounts[i][j] = max(maxCounts[i - 1][j], maxCounts[i][j - 1]);
                if (a[i - 1] == b[j - 1] && maxCounts[i - 1][j - 1] + 1 > maxCounts[i][j]) {
                    maxCounts[i][j] = maxCounts[i - 1][j - 1] + 1;
                }
            }
        }
        System.out.println(maxCounts[a.length][b.length]);

        int maxCount = 0;
        List<Integer> subSequence = new ArrayList<>();
        for (int i = 0; i < maxCounts.length; i++) {
            for (int j = 0; j < maxCounts[i].length; j++) {
                if (maxCounts[i][j] > maxCount) {
                    maxCount = maxCounts[i][j];
                    subSequence.add(a[i - 1]);
                }
            }
        }
        System.out.println(subSequence.stream().map(String::valueOf).collect(Collectors.joining(" ")));
    }

    // dynamicBag(new int[]{2, 4, 10}, new int[]{10, 20, 30}, 14);
    private static void dynamicBag(int[] weight, int[] cost, int maxWeight) {
        int[] weights = new int[maxWeight + 1];
        int[] costs = new int[maxWeight + 1];
        for (int weightSum = 1; weightSum <= maxWeight; weightSum++) {
            costs[weightSum] = costs[weightSum - 1];
            weights[weightSum] = weights[weightSum - 1];
            for (int costIdx = 0; costIdx < cost.length; costIdx++) {
                int currentCost = cost[costIdx];
                int currentWeight = weight[costIdx];
                if ((weightSum - weights[weightSum - 1]) - currentWeight >= 0 && costs[weightSum - 1] + currentCost > costs[weightSum]) {
                    costs[weightSum] = costs[weightSum - 1] + currentCost;
                    weights[weightSum] = weights[weightSum - 1] + currentWeight;
                }
            }
        }
        System.out.println(costs[maxWeight]);
    }

    // dynamicMoneyChange(27, new int[]{1, 2, 8, 10});
    private static void dynamicMoneyChange(int sum, int[] coins) {
        int[] change = new int[sum + 1];
        int[] counter = new int[sum + 1];
        for (int money = 1; money <= sum; money++) {
            counter[money] = Integer.MAX_VALUE;
            for (int j = 0; j < coins.length; j++) {
                int coin = coins[j];
                if (money - coin >= 0 && counter[money - coin] + 1 < counter[money]) {
                    counter[money] = counter[money - coin] + 1;
                    change[money] = coin;
                }
            }
        }
        System.out.println(counter[sum]);
        recount(change, sum);
        System.out.println();
    }

    private static void recount(int[] change, int i) {
        if (i == 0) return;
        recount(change, i - change[i]);
        if (i - change[i] > 0) {
            System.out.print("+");
        }
        System.out.print(change[i]);
    }

    //  dynamicSquare(new int[][]{
    //                // j0 j1 jn
    //                {1, 3, 7, -1, 7, 11}, // i0
    //                {2, 6, 5, 1, 1, 3,}, // i1
    //                {-3, 0, 2, 0, 3, 8}, // in
    //                {5, 1, 3, 1, 4, 7},
    //                {6, 1, -2, 2, 1, 0},
    //        }, 5, 6, 5, 6);
    private static int[][] sum = null;

    private static int dynamicSquare(int[][] field, int x1, int y1, int x2, int y2) {
        int n = field.length;
        int m = field[0].length;
        if (sum == null) {
            sum = new int[n][m];
            sum[0][0] = field[0][0];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (i > 0 && j > 0) {
                        sum[i][j] = field[i][j] + (sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1]);
                    } else if (i > 0) {
                        sum[i][j] = field[i][j] + sum[i - 1][j];
                    } else if (j > 0) {
                        sum[i][j] = field[i][j] + sum[i][j - 1];
                    } else {
                        sum[i][j] = field[i][j];
                    }
                }
            }
        }

        x1 = x1 - 1;
        x2 = x2 - 1;
        y1 = y1 - 1;
        y2 = y2 - 1;
        int s = sum[x2][y2];
        int a = x1 > 0 ? sum[x1 - 1][y2] : 0;
        int b = y1 > 0 ? sum[x2][y1 - 1] : 0;
        int ab = x1 > 0 && y1 > 0 ? sum[x1 - 1][y1 - 1] : 0;
        int res = s - a - b + ab;
        return res;
    }

    // dynamicBug(new int[][]{
    //                {5, 3, 2, 2},
    //                {2, 1, 7, 3},
    //                {4, 3, 1, 2},
    //        });
    private static void dynamicBug(int[][] field) {
        int n = field.length;
        int m = field[0].length;
        int[][] d = new int[n][m];
        int[][] p = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                d[i][j] = field[i][j];
                int iMax = 0;
                if (i > 0) {
                    iMax = max(d[i][j], d[i - 1][j] + field[i][j]);
                    d[i][j] = max(d[i][j], iMax);
                    p[i][j] = 0;
                }
                if (j > 0) {
                    int jMax = max(d[i][j], d[i][j - 1] + field[i][j]);
                    d[i][j] = max(d[i][j], jMax);
                    if (jMax > iMax) {
                        p[i][j] = 1;
                    }
                }
            }
        }
        System.out.println(d[n - 1][m - 1]);
        recount(p, n - 1, m - 1);
        System.out.println();
    }

    private static void recount(int[][] p, int i, int j) {
        if (i == 0 && j == 0) return;
        if (p[i][j] == 0) {
            recount(p, i - 1, j);
            System.out.print("D");
        }
        if (p[i][j] == 1) {
            recount(p, i, j - 1);
            System.out.print("R");
        }
    }

    // nonDynamicBug(new int[][]{
    //                {5, 3, 2, 2},
    //                {2, 1, 7, 3},
    //                {4, 3, 1, 2},
    //        });
    private static void nonDynamicBug(int[][] field) {
        int res = 0;
        for (int i = 0, j = 0; i < field.length && j < field[i].length; ) {
            res += field[i][j];
            if (i + 1 >= field.length) {
                j++;
                System.out.print("D");
            } else if (j + 1 >= field[i].length) {
                i++;
                System.out.print("R");
            } else if (field[i + 1][j] > field[i][j + 1]) {
                i++;
                System.out.print("D");
            } else {
                j++;
                System.out.print("R");
            }
        }
        System.out.println();
        System.out.println(res);
    }

    // dynamicTripleDomino(10);
    private static void dynamicTripleDomino(int n) {
        int x1 = 1;
        int x2 = 1;
        int x3 = x1 + x2;
        int res = 0;
        for (int i = 3; i < n; i++) {
            res = x3 + x1;
            x1 = x2;
            x2 = x3;
            x3 = res;
        }
        System.out.println(res);
    }

    // dynamicDomino(25);
    private static void dynamicDomino(int n) {
        int x1 = 1;
        int x2 = 1;
        int res = 0;
        for (int i = 2; i < n; i++) {
            res = x1 + x2;
            x1 = x2;
            x2 = res;
        }
        System.out.println(res);

        double goldenSection = (sqrt(5) + 1) / 2;
        int fastResult = (int) round(pow(goldenSection, n) / sqrt(5));
        System.out.println(fastResult);
    }

    // dynamicRecDomino(4, 0);
    // System.out.println(ans);
    private static int ans = 0;

    private static void dynamicRecDomino(int n, int sum) {
        if (sum > n) return;
        if (sum == n) {
            ans++;
            return;
        }
        dynamicRecDomino(n, sum + 1);
        dynamicRecDomino(n, sum + 2);
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

    private static int[][] getFromFileDoubleSize(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        String[] nm = file.readLine().trim().split(" ");
        int[][] arr = new int[parseInt(nm[0])][parseInt(nm[1])];
        AtomicLong idx = new AtomicLong(0);
        file.lines().forEach(line -> {
            arr[idx.intValue()] = Arrays.stream(line.trim().split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(i -> i)
                    .toArray();
            idx.incrementAndGet();
        });
        return arr;
    }

    private static int[][] getFromFile(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        int size = parseInt(file.readLine().trim());
        int[] ax = new int[size];
        int[] bx = new int[size];
        AtomicLong idx = new AtomicLong(0);
        file.lines().forEach(line -> {
            String[] data = line.trim().split(" ");
            ax[idx.intValue()] = parseInt(data[0]);
            bx[idx.intValue()] = parseInt(data[1]);
            idx.incrementAndGet();
        });
        return new int[][]{ax, bx};
    }

    private static int[] getArrayFromFile(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        int size = parseInt(file.readLine().trim());
        List<Integer> collection = new ArrayList<>(size);
        file.lines().forEach(line -> {
            String[] data = line.trim().split(" ");
            for (String datum : data) {
                collection.add(parseInt(datum));
            }
        });
        return collection.stream().mapToInt(i -> i).toArray();
    }

    private static String[] getStringArrayFromFile(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        int size = parseInt(file.readLine().trim());
        String[] collection = new String[size];
        AtomicLong idx = new AtomicLong(0);
        file.lines().forEach(line -> {
            collection[idx.intValue()] = line.trim();
            idx.incrementAndGet();
        });
        return collection;
    }
}
