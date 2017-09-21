import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseBWT {
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

    private class BwtKey{
        boolean check;
        Character firstColumnChar;
        Character lastColumnChar;
        int index;
    }

    String inverseBWT(String bwt) {
        StringBuilder result = new StringBuilder();
        List<Character> firstColumn = new ArrayList<>();
        for(int i=0;i<bwt.length();i++) firstColumn.add(bwt.charAt(i));
        Collections.sort(firstColumn);

        List<BwtKey> bwtKeys = new ArrayList<>();
        for(int i=0;i<firstColumn.size();i++) {
            BwtKey bwtKey = new BwtKey();
            bwtKey.check=false;
            bwtKey.index=i;
            bwtKey.firstColumnChar = firstColumn.get(i);
            bwtKey.lastColumnChar = bwt.charAt(i);
            bwtKeys.add(bwtKey);
        }

        result.insert(0, bwtKeys.get(0).lastColumnChar);
        bwtKeys.get(0).check=true;
        Character charToCheck = bwtKeys.get(0).lastColumnChar;
        while(result.length()<bwt.length()-1) {
            for(int i=1;i<bwtKeys.size();i++) {
                if(bwtKeys.get(i).check==false && bwtKeys.get(i).firstColumnChar==charToCheck) {
                    result.insert(0, bwtKeys.get(i).lastColumnChar);
                    bwtKeys.get(i).check=true;
                    charToCheck = bwtKeys.get(i).lastColumnChar;
                    break;
                }
            }
        }

        return result.toString() + "$";
    }

    static public void main(String[] args) throws IOException {
        new InverseBWT().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }
}