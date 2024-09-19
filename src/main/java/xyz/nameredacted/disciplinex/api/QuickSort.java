package xyz.nameredacted.disciplinex.api;

/**
 * This function contains a QuickSort algorthm.
 * Given arr, some array of values, it will sort the array.
 * Quicksort is a "divide-and-conquer" algorithm, which divides the array in half,
 * sorting each side. This is known as "partitioning" and each divided array is known as a "partition".
 * This continues until the array is sorted. This sorting algorithm was selected rather than
 * Bubble sort or Merge sort, as Quicksort is one of the fastest sorting algorithms, with an average time complexity of O(nlogn).
 * O(nlogn) means that, on average, the function will complete nlogn operations. By comparison, this is much faster than bubble sort, which is
 * O(n^2). This means that for an array with 100 elements, bubble sort will perform 10000 operations, compared to 200 for quick sort.
 *
 * @see java.util.Arrays
 * @see java.util.DualPivotQuicksort.Sorter
 */
public class QuickSort {

    /**
     * // Pseudo-code for QuickSort
     * function quickSort(arr, low, high):
     *     if (low < high):
     *         // Partition the array and get the pivot index
     *         pivotIndex = partition(arr, low, high)
     *
     *         // Recursively sort elements before and after partition
     *         quickSort(arr, low, pivotIndex - 1)
     *         quickSort(arr, pivotIndex + 1, high)
     * <p>
     * function partition(arr, low, high):
     *     // Select the last element as pivot
     *     pivot = arr[high]
     *
     *     // Pointer for greater element
     *     i = low - 1
     *
     *     // Traverse through all elements
     *     for j = low to high - 1:
     *         if arr[j] < pivot:
     *             i = i + 1
     *             // Swap elements
     *             swap(arr, i, j)
     *
     *     // Swap pivot with the element at i + 1
     *     swap(arr, i + 1, high)
     *
     *     return i + 1
     *
     * function swap(arr, i, j):
     *     temp = arr[i]
     *     arr[i] = arr[j]
     *     arr[j] = temp
     */
}
