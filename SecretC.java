import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.util.Arrays;


public class Main {
    public static JSONObject parseJSON(String filename) throws Exception {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(filename);
        return (JSONObject) parser.parse(reader);
    }
    public static BigInteger decodeValue(int base, String value) {
        return new BigInteger(value, base);
    }
    public static BigInteger lagrangeInterpolation(List<Point> points, BigInteger x) {
        BigInteger result = BigInteger.ZERO;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            BigInteger term = points.get(i).y;
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    term = term.multiply(x.subtract(points.get(j).x)).divide(points.get(i).x.subtract(points.get(j).x));
                }
            }
            result = result.add(term);
        }

        return result;
    }
    public static void findConstantTerm(String inputFile) throws Exception {
        JSONObject input = parseJSON(inputFile);
        JSONObject keys = (JSONObject) input.get("keys");
        int n = ((Long) keys.get("n")).intValue();
        int k = ((Long) keys.get("k")).intValue();

        List<Point> points = new ArrayList<>();

        for (Object key : input.keySet()) {
            if (!key.equals("keys")) {
                JSONObject point = (JSONObject) input.get(key);
                int x = Integer.parseInt((String) key);
                int base = Integer.parseInt((String) point.get("base"));
                String value = (String) point.get("value");
                BigInteger y = decodeValue(base, value);
                points.add(new Point(BigInteger.valueOf(x), y));
            }
        }

        BigInteger constantTerm = lagrangeInterpolation(points, BigInteger.ZERO);
        System.out.println("Constant term (c) for " + inputFile + ": " + constantTerm);
    }

    public static void main(String[] args) throws Exception {
        findConstantTerm("src/main/java/testcase1.json");
        findConstantTerm("src/main/java/testcase2.json");
    }

    static class Point {
        BigInteger x;
        BigInteger y;

        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }
}
