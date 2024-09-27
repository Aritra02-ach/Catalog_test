package test;

import org.json.JSONObject;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Findconstant {

    // Method to convert a value from any base to base 10
    private static int convertToBase10(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Lagrange interpolation method to find the constant term (or any polynomial term)
    private static double lagrangeInterpolation(List<Integer> xValues, List<Integer> yValues, int point) {
        int n = xValues.size();
        double result = 0;

        // Loop to calculate the sum of Lagrange terms
        for (int i = 0; i < n; i++) {
            // Start with y_i
            double term = yValues.get(i);

            // Loop to calculate product terms for each L_i(x)
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term = term * (point - xValues.get(j)) / (xValues.get(i) - xValues.get(j));
                }
            }

            // Add the term to the final result
            result += term;
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            // Load the JSON file
            File file = new File("D:/Programs/Catalog_test/src/test/input.json");
            FileReader reader = new FileReader(file);
            char[] buffer = new char[(int) file.length()];
            reader.read(buffer);
            reader.close();

            // Parse JSON file into a JSONObject
            String jString = new String(buffer);
            JSONObject jObject = new JSONObject(jString);

            // Extract the number of roots (n) and the required number of roots (k)
            JSONObject keys = jObject.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            // Lists to hold x-values and y-values
            List<Integer> xValues = new ArrayList<>();
            List<Integer> yValues = new ArrayList<>();

            // Extract root values from JSON and decode them
            for (int i = 1; i <= n; i++) {
                if (jObject.has(String.valueOf(i))) {
                    JSONObject rootData = jObject.getJSONObject(String.valueOf(i));
                    int base = Integer.parseInt(rootData.getString("base"));
                    String value = rootData.getString("value");

                    // Convert value to base 10 and store in yValues
                    int yValue = convertToBase10(value, base);
                    yValues.add(yValue);

                    // Add the index as the x-value
                    xValues.add(i);
                }
            }

            // Calculate the constant term using Lagrange interpolation at x = 0
            double constantTerm = lagrangeInterpolation(xValues.subList(0, k), yValues.subList(0, k), 0);

            // Print the constant term
            System.out.println("The constant term (c) is: " + constantTerm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
