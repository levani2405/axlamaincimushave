import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SimplePythonInterpreter {
    // A map to store variable names and their corresponding integer values
    private Map<String, Integer> variables = new HashMap<>();
    // Scanner for reading user input
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Create an instance of the interpreter and run it
        SimplePythonInterpreter interpreter = new SimplePythonInterpreter();
        interpreter.run();
    }

    public void run() {
        System.out.println("Simple Python Interpreter. Type 'exit' to quit.");
        // Main loop to continuously read user input
        while (true) {
            System.out.print(">>> ");
            String input = scanner.nextLine();
            // Exit the interpreter if the user types 'exit'
            if (input.equals("exit")) {
                break;
            }
            try {
                // Execute the input command
                execute(input);
            } catch (Exception e) {
                // Print any errors that occur during execution
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void execute(String input) {
        // Trim whitespace from the input
        input = input.trim();
        // Check for print command
        if (input.startsWith("print ")) {
            print(input.substring(6).trim());
        }
        // Check for variable assignment
        else if (input.contains("=")) {
            assignVariable(input);
        }
        // Check for conditional statements
        else if (input.startsWith("if ")) {
            executeConditional(input);
        }
        // Check for loops
        else if (input.startsWith("while ")) {
            executeLoop(input);
        }
        // If the command is not recognized, throw an error
        else {
            throw new IllegalArgumentException("Invalid command: " + input);
        }
    }

    private void print(String expression) {
        // Evaluate the expression and print the result
        int value = evaluateExpression(expression);
        System.out.println(value);
    }

    private void assignVariable(String input) {
        // Split the input into variable name and value
        String[] parts = input.split("=");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid assignment: " + input);
        }
        String varName = parts[0].trim(); // Variable name
        int value = evaluateExpression(parts[1].trim()); // Evaluated value
        variables.put(varName, value); // Store the variable in the map
    }

    private void executeConditional(String input) {
        // Extract the condition and body from the input
        String condition = input.substring(3, input.indexOf(":")).trim();
        if (evaluateCondition(condition)) {
            String body = input.substring(input.indexOf(":") + 1).trim();
            execute(body); // Execute the body if the condition is true
        }
    }

    private void executeLoop(String input) {
        // Extract the loop condition and body
        String condition = input.substring(6, input.indexOf(":")).trim();
        while (evaluateCondition(condition)) {
            String body = input.substring(input.indexOf(":") + 1).trim();
            execute(body); // Execute the body while the condition is true
        }
    }

    private boolean evaluateCondition(String condition) {
        // Split the condition into parts for evaluation
        String[] parts = condition.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }
        int left = evaluateExpression(parts[0]); // Left operand
        int right = evaluateExpression(parts[2]); // Right operand
        String operator = parts[1]; // Operator

        // Evaluate the condition based on the operator
        switch (operator) {
            case "==":
                return left == right;
            case "!=":
                return left != right;
            case "<":
                return left < right;
            case "<=":
                return left <= right;
            case ">":
                return left > right;
            case ">=":
                return left >= right;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    private int evaluateExpression(String expression) {
        // Trim whitespace from the expression
        expression = expression.trim();
        // Check if the expression is a variable
        if (variables.containsKey(expression)) {
            return variables.get(expression); // Return the variable's value
        }
        try {
            return Integer.parseInt(expression); // Try to parse the expression as an integer
        } catch (NumberFormatException e) {
            // Handle arithmetic expressions
            String[] tokens = expression.split(" ");
            // Evaluate the first term in the expression
            int result = evaluateTerm(tokens[0]);
            // Iterate through the tokens to evaluate the entire expression
            for (int i = 1; i < tokens.length; i += 2) {
                String operator = tokens[i]; // Get the operator
                int nextTerm = evaluateTerm(tokens[i + 1]); // Get the next term
                result = applyOperator(result, operator, nextTerm); // Apply the operator
            }
            return result; // Return the final result of the expression
        }
    }

    private int evaluateTerm(String term) {
        // Check if the term is a variable
        if (variables.containsKey(term)) {
            return variables.get(term); // Return the variable's value
        }
        return Integer.parseInt(term); // Otherwise, parse it as an integer
    }

    private int applyOperator(int left, String operator, int right) {
        // Apply the specified operator to the left and right operands
        switch (operator) {
            case "+":
                return left + right; // Addition
            case "-":
                return left - right; // Subtraction
            case "*":
                return left * right; // Multiplication
            case "/":
                if (right == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
                return left / right; // Division
            case "%":
                return left % right; // Modulus
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}