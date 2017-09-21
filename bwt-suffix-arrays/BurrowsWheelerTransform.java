import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BurrowsWheelerTransform {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    String BWT(String text) {
        StringBuilder result = new StringBuilder();
        int l = text.length();
        List<String> cyclicStrings = new ArrayList<String>();
        cyclicStrings.add(text);
        for(int i=1;i<l;i++) {
            String newString = "";
            newString = newString+ cyclicStrings.get(i-1).charAt(l-1);
            newString = newString + cyclicStrings.get(i-1).substring(0, l-1);
            cyclicStrings.add(newString);
        }
        Collections.sort(cyclicStrings);
        for(String textPerm: cyclicStrings) {
            result.append(textPerm.charAt(l-1));
        }
        return result.toString();
    }

    static public void main(String[] args) throws IOException {
        new BurrowsWheelerTransform().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        System.out.println(BWT(text));
    }
}