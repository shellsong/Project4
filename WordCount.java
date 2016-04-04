/**
 * CS 146 Section 6 
 * Data Structures and Algorithms
 * Project 3
 * 
 * Used and edited by: Michelle Lai, Michelle Song
 *
 * An executable that counts the words in a files and prints out the counts in
 * descending order or the number of unique elements in the file
 * Uses the data structure specified by the user
 * */

import java.io.IOException;
import java.util.Arrays;
@SuppressWarnings("unchecked")

public class WordCount {
	private static DataCounter<String> counter;

	/**
	 * Constructs a WordCount with the data structure specified by the user
	 * 
	 * @param a the string key specified by the user that represents a data structure
	 */
	public WordCount(String a)
	{
		if(a.compareTo("-b")==0)
		{
			BinarySearchTree bst = new BinarySearchTree();
			counter = (DataCounter<String>)(bst);
			System.out.println("Using Binary Search Tree");
		}
		else if(a.compareTo("-a")==0)
		{
			AvlTree avl = new AvlTree();
			counter = (DataCounter<String>)(avl);
			System.out.println("Using AvlTree");
		}
		else if(a.compareTo("-h")==0)
		{
			counter = new HashTable(100);
			System.out.println("Using HashTable");
		}
		else {
			System.err.println("\tSaw "+ a +" instead of -b -a -h as first argument");
			System.exit(1);
		}
	}

	/**
	 * Calculates the frequences for all the words in a given file
	 * Uses file reader to cycle through all the words in the file and updates them into
	 * the specified data structure by simply adding them or incrementing the count if already present
	 * Prints the count and word for all the words in the file in descending order
	 * 
	 * @param file the file which frequencies need to be calculated
	 */
	private static void countWords(String file) {

		try {
			FileWordReader reader = new FileWordReader(file);
			String word = reader.nextWord();
			while (word != null) {
				counter.incCount(word);
				word = reader.nextWord();
			}
		} catch (IOException e) {
			System.err.println("Error processing " + file + e);
			System.exit(1);
		}
		DataCount<String>[] counts = counter.getCounts();
		sortByDescendingCount(counts);
		for (DataCount<String> c : counts)
		{
			if (c != null)
			{
				System.out.println(c.count + " \t" + c.data);
			}
		}
	}

	/**
	 * Counts the number of unique words in the given file and prints the result
	 * 
	 * @param file the file to be observed
	 */
	private static void numUnique(String file) {
		int num = 0;
		try {
			FileWordReader reader = new FileWordReader(file);
			String word = reader.nextWord();
			while (word != null) {
				counter.incCount(word);
				word = reader.nextWord();
			}
		} catch (IOException e) {
			System.err.println("Error processing " + file + e);
			System.exit(1);
		}

		DataCount<String>[] counts = counter.getCounts();
		sortByDescendingCount(counts);
		for (DataCount<String> c : counts)
		{
			num++;
		}
		System.out.println("The number of unique words in " + file + " is " + num);
	}

	/**
	 * Sorts the given array in descending order by its count element
	 * 
	 * @param counts the array to be sorted
	 */
	private static <E extends Comparable<? super E>> void sortByDescendingCount(
			DataCount<E>[] counts) {
		for (int i = 1; i < counts.length; i++) {
			DataCount<E> x = counts[i];
			int j;
			for (j = i - 1; j >= 0; j--) {
				if (counts[j] != null && x != null)
				{
					if (counts[j].count >= x.count) {
						break;
					}
					counts[j + 1] = counts[j];
				}
			}
			counts[j + 1] = x;
		}
	}
	
	/**
	 * The main class that takes in 3 user arguments, a specific data structure,
	 * the desired computation, and the file
	 * The possible data structures are Binary Search Tree, -b, AVL tree, -a, and
	 * Hash Table, -h
	 * The possible computations are the frequencies of all the elements, -frequency, and
	 * the number of total unique words in the file, -num_unique
	 * Prints return statement for the desired computation
	 */
	public static void main(String[] args) {
		WordCount wc = new WordCount(args[0]);
		if (args[1].equals("-frequency"))
		{
			System.out.println("Using frequency");
			System.out.println("File is " + args[2]);
			wc.countWords(args[2]);
		}
		else if (args[1].equals("-num_unique")) 
		{
			System.out.println("Using num_unique");
			System.out.println("File is " + args[2]);
			wc.numUnique(args[2]);
		}
	}
}