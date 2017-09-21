import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class KnuthMorrisPratt {
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

    // Find all the occurrences of the pattern in the text and return
    // a list of all positions in the text (starting from 0) where
    // the pattern starts in the text.
    private int[] computePrefixFunctions(String s) {
        int[] prefixes = new int[s.length()];
        prefixes[0] = 0;
        int border = 0;
        for(int i=1;i<=s.length()-1;i++) {
            while(border>0 && s.charAt(i)!=s.charAt(border))
                border = prefixes[border-1];
            if(s.charAt(i)==s.charAt(border)) border = border+1;
            else border = 0;
            prefixes[i] = border;
        }
        return prefixes;
    }

    private List<Integer> findPattern(String pattern, String text) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        String s = pattern + "$" + text;
        int[] prefixes = computePrefixFunctions(s);
        for(int i=pattern.length()+1;i<=s.length()-1;i++) {
            if(prefixes[i]==pattern.length()) result.add(i-(2*pattern.length()));
        }
        return result;
    }

    static public void main(String[] args) throws IOException {
        new KnuthMorrisPratt().run();
    }

    public void print(List<Integer> x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String pattern = scanner.next();
        String text = scanner.next();
        List<Integer> positions = findPattern(pattern, text);
        print(positions);
    }
}