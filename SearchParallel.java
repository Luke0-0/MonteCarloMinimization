package MonteCarloMini;

// Luke Reinbach
// SearchParallel Class for MonteCarloMinimizationParallel

import java.util.concurrent.RecursiveTask;
import java.lang.Math.*;

class SearchParallel extends RecursiveTask<Integer> {
	static final int cutOff = 5000;
	int low;
	int high;
	public int globalFinder;
	public int globalMin = Integer.MAX_VALUE;
	Search[] arr;

	// SearchParallel object consisting of an array of Search objects, a low, and a high (which represents the number of searches)
	public SearchParallel(Search[] a, int l, int h) {
		low = l;
		high = h;
		arr = a;
	}

	// Get the finder number for the global minimum
	public int getFinder() {
		return globalFinder;
	}

	// Recursive compute method
	protected Integer compute() {
		if (high-low <= cutOff) {
			int min=Integer.MAX_VALUE;
			int local_min=Integer.MAX_VALUE;
			int finder = -1;
			for  (int i=low;i<high;i++) {
				local_min=arr[i].find_valleys();
				if((!arr[i].isStopped())&&(local_min<min)) { //don't look at those who stopped because hit exisiting path
					min=local_min;
					finder=i; //keep track of who found it
				}
    		}		
			return min;
		}
		// Dividing up the tasks
		else {
			SearchParallel left = new SearchParallel(arr, low, (high+low)/2);
			SearchParallel right = new SearchParallel(arr, (high+low)/2, high);
			left.fork();
			int rightAns = right.compute();
			int leftAns = left.join();
			return Math.min(leftAns, rightAns); // returning the lesser of the two minimums
		}
	}
}