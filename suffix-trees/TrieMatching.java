import java.io.*;
import java.util.*;

class Node
{
	public static final int Letters =  4;
	public static final int NA      = -1;
	public int next [];

	Node ()
	{
		next = new int [Letters];
		Arrays.fill (next, NA);
	}
}

public class TrieMatching implements Runnable {
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
		}
		return trie;
	}

	boolean isLeaf(Map<Character, Integer> node) {
		return node.size() == 0;
	}

	Result prefixTrieMatching(List<Map<Character, Integer>> trie, String text, int startingIndex) {
		Result result = new Result();
		String matchedPattern = "";
		Character currentSymbol = text.charAt(startingIndex);
		Map<Character, Integer> currentNode = trie.get(0);
		int nextCharIndex = startingIndex;
		while(true) {
			if(nextCharIndex>text.length()) {
				result.pattern = "";
				result.startingIndex = -1;
				return result;
			}
			Set<Character> neighbours = currentNode.keySet();
			if(isLeaf(currentNode)) {
				result.startingIndex = startingIndex;
				result.pattern = matchedPattern;
				return result;
			}
			else if(neighbours.contains(currentSymbol)) {
				matchedPattern = matchedPattern + currentSymbol;
				currentNode = trie.get(currentNode.get(currentSymbol));
				if(nextCharIndex++<text.length()-1)
					currentSymbol = text.charAt(nextCharIndex);
			}
			else {
				result.pattern = "";
				result.startingIndex = -1;
				return result;
			}
		}
	}

	class Result {
		String pattern;
		Integer startingIndex;
	}

	List <Integer> solve (String text, int n, List <String> patterns) {
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
			List<Result> results = new ArrayList<>();
			for(int i=0;i<=text.length()-1;i++) {
				results.add(prefixTrieMatching(trie, text, i));
			}

			for(int i=0;i<results.size();i++) {
				if(results.get(i).pattern=="") continue;
				else indexesOfPatterns.add(i);
			}
		}
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
		new Thread (new TrieMatching ()).start ();
	}
}