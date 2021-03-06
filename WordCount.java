/**
 * CS 146 Section 6 
 * Data Structures and Algorithms
 * Project 4
 * 
 * Used and edited by: Michelle Lai, Michelle Song
 *
 * An executable that counts the words in a files and prints out the counts in
 * descending order or the number of unique elements in the file
 * Uses the data structure and sorting method specified by the user
 * */

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
@SuppressWarnings("unchecked")

public class WordCount {
	private static DataCounter<String> counter;
	private static String sort;

	/**
	 * Constructs a WordCount with the data structure specified by the user
	 * 
	 * @param a the string key specified by the user that represents a data structure
	 */
	public WordCount(String dataStructure, String sortingMethod)
	{
		// Passing first parameter input to create the corresponding data structure

		// Selected binary search tree
		if(dataStructure.compareTo("-b")==0)
		{
			BinarySearchTree bst = new BinarySearchTree();
			counter = (DataCounter<String>)(bst);
			System.out.println("Using Binary Search Tree");
		}
		// Selected AVL tree
		else if(dataStructure.compareTo("-a")==0)
		{
			AvlTree avl = new AvlTree();
			counter = (DataCounter<String>)(avl);
			System.out.println("Using AvlTree");
		}
		// Selected hashtable
		else if(dataStructure.compareTo("-h")==0)
		{
			counter = new HashTable(100);
			System.out.println("Using HashTable");
		}
		else {
			System.err.println("\tSaw "+ dataStructure +" instead of -b -a -h as first argument");
			System.exit(1);
		}

		// Passing second parameter input to use the corresponding sorting method

		if (sortingMethod.compareTo("-is") == 0 || sortingMethod.compareTo("-qs") == 0 || sortingMethod.compareTo("-ms") == 0)
		{
			sort = sortingMethod;
		}
		else 
		{
			System.err.println("\tSaw "+ sortingMethod +" instead of -is -qs -ms as first argument");
			System.exit(1);	
		}
	}

	/**
	 * Calculates the frequencies for all the words in a given file
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
	 * Sorts the given array in descending order by its count element with the specified sorting method
	 * 
	 * @param counts the array to be sorted
	 */
	private static <E extends Comparable<? super E>> void sortByDescendingCount(DataCount<E>[] counts) {

		if (sort.compareTo("-is") == 0)
		{
			insertionSort(counts);
		}
		else if (sort.compareTo("-qs") == 0)
		{
			System.out.println("Quicksort");
			quickSort(counts,0,counts.length-1);
		}
		else if (sort.compareTo("-ms") == 0)
		{
			mergeSort(counts);
		}
	}

	/**
	 * Sorts the given array in descending order by its count element with insertion sort
	 * 
	 * @param counts the array to be sorted
	 */
	private static <E extends Comparable<? super E>> void insertionSort(DataCount<E>[] counts)
	{
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
	 * Sorts the given array in descending order by its count element with quick sort
	 * 
	 * @param counts the array to be sorted
	 * @param low the lowest bound index to sort
	 * @param high the highest bound index to sort
	 */
	private static <E extends Comparable<? super E>> void quickSort(DataCount<E>[] counts, int low, int high) 
	{
		int left = low;
		int right = high;

		if (high-low == 1)
		{
			if (counts[low].count < counts[high].count)
			{
				swap(counts, low, high);
			}
		}

		// If there are 3 or more elements
		// Find median of low, mid, high
		// Move pointers

		else if ( left != right )
		{
			int mid = low + (high-low)/2;
			DataCount[] getMedian = new DataCount[3];
			getMedian[0] = counts[low];
			getMedian[1] = counts[mid];
			getMedian[2] = counts[high];
			for (int i = 1; i < getMedian.length; i++) 
			{
				DataCount x = getMedian[i];
				int j;
				for (j = i - 1; j >= 0; j--) {
					if (getMedian[j] != null && x != null)
					{
						if (getMedian[j].count >= x.count) {
							break;
						}
						getMedian[j + 1] = getMedian[j];
					}
				}
				getMedian[j + 1] = x;
			}
			// Pivot is the median

			DataCount pivot = getMedian[1];
			int pivotIndex = 0;

			// if all are the same, the first value is the median
			if (getMedian[0].count == getMedian[1].count && getMedian[0].count == getMedian[2].count && getMedian[2].count == getMedian[1].count)
			{
				pivot = getMedian[0];
				pivotIndex = low;
			}
			else
			{
				if (pivot == counts[mid]) pivotIndex = mid;
				else if (pivot == counts[low]) pivotIndex = low;
				else if (pivot == counts[high]) pivotIndex = high;
			}

			if (counts[pivotIndex].count != counts[low].count)
			{
				swap(counts, pivotIndex, low);
			}

			left++;
			while (left < right)
			{
				while ( counts[left].count >= pivot.count && (left < high))
				{
					left++;	
				}
				while (counts[right].count < pivot.count && (right > low))
				{
					right--;
				}
				if (left < right && (counts[left].count != counts[right].count)) 
				{
					swap(counts, right, left);
				}
			}

			if (counts[low].count != counts[left-1].count)
			{
				swap(counts, low, left-1);
			}

			pivotIndex = left-1;
			if (low < pivotIndex-1)
			{
				quickSort(counts, low, pivotIndex-1);
			}
			if (high > pivotIndex+1)
			{
				quickSort(counts, pivotIndex+1, high);
			}
		}


	}

	/**
	 * Swaps the given to elements around
	 * 
	 * @param counts the array in which the two elements will be switched
	 * @param a the first element which needs to be swapped
	 * @param b the second element which needs to be swapped
	 */
	public static <E> void swap (DataCount<E>[] counts, int a, int b)
	{
		DataCount temp = counts[a];
		counts[a] = counts[b];
		counts[b] = temp;

	}

	/**
	 * Sorts the given array in descending order by its count element with quick sort
	 * Splits the array into bounds
	 * 
	 * @param counts the array to be sorted
	 */
	private static <E extends Comparable<? super E>> void mergeSort(DataCount<E>[] counts)
	{
		int length = 1;
		while (length <= counts.length)
		{
			int start = 0;
			if ((length*2 > counts.length))
			{
				merge(start,start+length-1, counts.length-1, counts);
			}
			else
			{
				while (start+length < counts.length) 
				{
					merge(start, start+length-1, start+length+length-1, counts);
					start = start + 2*length;			
				}
			}
			length = length*2;
		}
	}

	/**
	 * Sorts between the given bounds and merges them back together after comparisons
	 * 
	 * @param counts the array to be sorted
	 * @param low the lower bound of the segment that needs to be sorted
	 * @param mid the middle index of the segment that needs to be sorted
	 * @param high the upper bound of the segment that needs to be sorted
	 */
	private static <E extends Comparable<? super E>> void merge(int low, int mid, int high, DataCount<E>[] counts)
	{  
		if (high >= counts.length)
		{
			high = counts.length-1;
		}
		
		DataCount<E>[] firstHalf = new DataCount[mid-low+1];
		DataCount<E>[] secondHalf = new DataCount[high-mid];
		int i = 0;
		int j = 0;
		
		for (int p = low; p < mid+1; p++)
		{
			firstHalf[i] = counts[p];
			i++;
		}
		for (int q = mid+1; q < high+1; q++)
		{
			secondHalf[j] = counts[q];
			j++;
		}

		int first = 0;
		int second = 0;
		int newValueIndex = low;
		while (first < firstHalf.length && second < secondHalf.length)
		{
			if (firstHalf[first].count > secondHalf[second].count)
			{
				counts[newValueIndex] = firstHalf[first];
				first++;
				newValueIndex++;
			}
			else
			{
				counts[newValueIndex] = secondHalf[second];
				second++;
				newValueIndex++;
			}
		}

		while (first < firstHalf.length) 
		{ 
			counts[newValueIndex] = firstHalf[first]; 
			first++; 
			newValueIndex++;
		}
		while (second < secondHalf.length) 
		{ 
			counts[newValueIndex] = secondHalf[second]; 
			second++; 
			newValueIndex++;
		}
	}


	/**
	 * The main class that takes in 4 user arguments, a specific data structure, the desired sorting method,
	 * the desired computation, and the file
	 * The possible data structures are Binary Search Tree, -b, AVL tree, -a, and Hash Table, -h
	 * The possible sorting methods are Insertion Sort, -is, Quick Sort, -qs, and Merge Sort, -ms
	 * The possible computations are the frequencies of all the elements, -frequency, and
	 * the number of total unique words in the file, -num_unique
	 * Prints return statement for the desired computation
	 */
	public static void main(String[] args) {
		System.out.println("dataStructure: " + args[0] + ", sortingMethod: " + args[1]);
		WordCount wc = new WordCount(args[0], args[1]);

		if (args[2].equals("-frequency"))
		{
			wc.countWords(args[3]);
		}
		else if (args[2].equals("-num_unique")) 
		{
			wc.numUnique(args[3]);
		}
	}
}
