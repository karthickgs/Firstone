package String;
import java.util.ArrayList;
import java.util.List;

public class ParenthesesGenerator {

    // Function to generate all well-formed parentheses combinations
    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        generateParenthesisHelper(result, "", 0, 0, n);
        return result;
    }

    // Helper function for backtracking
    private static void generateParenthesisHelper(List<String> result, String current, int open, int close, int n) {
        // If the current string has reached the length 2 * n, it is a valid combination
        if (current.length() == 2 * n) {
            result.add(current);
            return;
        }

        // If we can add an opening parenthesis, do so
        if (open < n) {
            generateParenthesisHelper(result, current + "(", open + 1, close, n);
        }

        // If we can add a closing parenthesis, do so
        if (close < open) {
            generateParenthesisHelper(result, current + ")", open, close + 1, n);
        }
    }

    public static void main(String[] args) {
        int n = 4;  // Example: Generate all valid combinations for 3 pairs of parentheses
        List<String> result = generateParenthesis(n);
        // Print the results
        for (String combination : result) {
            System.out.println(combination);
        }
    }
}
