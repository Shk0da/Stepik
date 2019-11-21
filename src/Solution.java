import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Solution {

    private static List<Object> storage = new ArrayList<>();

    public static void main(String[] args) {

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
                }}.stream().filter(it -> it != first && it != second).findFirst().get();
                String newRow = s.substring(0, i) + newCharacter + s.substring(i + 2);
                System.out.println(newRow);
                return replace(newRow);
            }
        }
        return s;
    }

    // Complete the stringReduction function below.
    static int stringReduction(String s) {
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
    public static void checkBrackets(String input, char left, char right) {
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
}
