import java.io.*;
import java.util.*;

class Node
{
	public static final int Letters =  4;
	public static final int NA      = -1;
	public int next [];
	public boolean patternEnd;

	Node ()
	{
		next = new int [Letters];
		Arrays.fill (next, NA);
		patternEnd = false;
	}
	public boolean isPatternEnd() {
		return patternEnd;
	}
}

public class TrieMatchingExtended implements Runnable {
    int letterToIndex (char letter)
    {
        switch (letter)
        {
            case 'A': return 0;
            case 'C': return 1;
            case 'G': return 2;
            case 'T': return 3;
            default: assert (false); return Node.NA;
        }
    }

	List<Map<Character, Integer>> patternsToTrie(List<String> patterns) {
		List<Map<Character, Integer>> trie = new ArrayList<Map<Character, Integer>>();
		if(patterns.size()<1) return trie;
		Map<Character, Integer> root = new HashMap<>();
		trie.add(root);
		for(String pattern:patterns) {
			Map<Character, Integer> currentNode = root;
			for(int i=0;i<pattern.length();i++) {
				Set<Character> neighbours = currentNode.keySet();
				Character currentSymbol = pattern.charAt(i);
				if(neighbours!=null && neighbours.contains(currentSymbol)) {
					currentNode = trie.get(currentNode.get(currentSymbol));
				}
				else {
					Map<Character, Integer> newNode = new HashMap<>();
					trie.add(newNode);
					currentNode.put(currentSymbol, trie.size()-1);
					currentNode = newNode;
				}
			}
			Map<Character, Integer> newNode = new HashMap<>();
			trie.add(newNode);
			currentNode.put('$', trie.size()-1);
		}
		return trie;
	}

	boolean isLeaf(Map<Character, Integer> node, List<Map<Character, Integer>> trie) {
		return node.containsKey('$') && node.size()==1;
	}

	boolean isIntermediaryLeaf(Map<Character, Integer> node, List<Map<Character, Integer>> trie) {
		return node.containsKey('$') && node.size()>1;
	}

	List<Result> prefixTrieMatching(List<Map<Character, Integer>> trie, String text, int startingIndex) {
		List<Result> results = new ArrayList<Result>();
		String matchedPattern = "";
		Character currentSymbol = text.charAt(startingIndex);
		Map<Character, Integer> currentNode = trie.get(0);
		int nextCharIndex = startingIndex;
		while(true) {
			if(nextCharIndex>text.length()) {
				Result result = new Result();
				result.pattern = "";
				result.startingIndex = -1;
				results.add(result);
				return results;
			}
			Set<Character> neighbours = currentNode.keySet();
			if(isLeaf(currentNode, trie)) {
				Result result = new Result();
				result.startingIndex = startingIndex;
				result.pattern = matchedPattern;
				results.add(result);
				return results;
			}
			else if(neighbours.contains(currentSymbol)) {
				matchedPattern = matchedPattern + currentSymbol;
				currentNode = trie.get(currentNode.get(currentSymbol));

				// taking care of in between patterns
				if(isIntermediaryLeaf(currentNode, trie)) {
					Result result = new Result();
					result.startingIndex = startingIndex;
					result.pattern = matchedPattern;
					results.add(result);
				}

				if(nextCharIndex++<text.length()-1)
					currentSymbol = text.charAt(nextCharIndex);
			}
			else {
				Result result = new Result();
				result.pattern = "";
				result.startingIndex = -1;
				results.add(result);
				return results;
			}
		}
	}

	class Result {
		String pattern;
		Integer startingIndex;
	}

	List <Integer> solve (String text, int n, List <String> patterns) {
		Set<Integer> indexes = new HashSet<>();
		List <Integer> indexesOfPatterns = new ArrayList<>();
		List<String> modifiedPatterns = new ArrayList<>();
		int size = text.length();

		// build suffix tree of all the patterns.
		// check the text one by one and if it reaches a leaf, then pattern found

		for(String pattern : patterns) {
			if(pattern.length()<=text.length()) modifiedPatterns.add(pattern);
		}
		if(modifiedPatterns.size()<1) {
			return indexesOfPatterns;
		}
		else {
			List<Map<Character, Integer>> trie = patternsToTrie(modifiedPatterns);
			List<Result> results = new ArrayList<Result>();
			for(int i=0;i<=text.length()-1;i++) {
				results.addAll(prefixTrieMatching(trie, text, i));
			}

			for(int i=0;i<results.size();i++) {
				if(results.get(i).pattern=="") continue;
				else indexes.add(results.get(i).startingIndex);
			}
		}
		for(Integer i:indexes) indexesOfPatterns.add(i);
		Collections.sort(indexesOfPatterns);
		return indexesOfPatterns;
	}

	public void run () {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			String text = in.readLine ();
			int n = Integer.parseInt (in.readLine ());
			List <String> patterns = new ArrayList <String> ();
			for (int i = 0; i < n; i++) {
				patterns.add (in.readLine ());
			}

			List <Integer> ans = solve (text, n, patterns);

			for (int j = 0; j < ans.size (); j++) {
				System.out.print ("" + ans.get (j));
				System.out.print (j + 1 < ans.size () ? " " : "\n");
			}
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new TrieMatchingExtended ()).start ();
	}
}