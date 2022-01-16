package a01;

import edu.princeton.cs.algs4.*;

/**
 * Prints the statistics of the percolation program. T is the amount of
 * independent experiments performed on an N by N grid. Shows the Mean, Standard
 * Deviation, and High/Low confidence end-points to determine the percolation
 * threshold and timings.
 * 
 * @author Jeff Daffern + Marjorie Cottrell
 *
 */

public class PercolationStats {
	private int experimentCount, openSites;
	private double[] percolationThreshold;
	private Percolation percolation;

	//int T; // independent experiments
	//double[] p; // Percolation thresholds for T experiments 
	/**
	 * perform T independent experiments on an N-by-N grid
	 * throw an IllegalArgumentException if either N <= 0 or T <= 0
	 * 
	 * @param N
	 * @param T
	 */
	public PercolationStats (int N, int T) {
		// Perform the experiment T times
		// 	create an N x N percolation system
		// 	until system percolates, choose a cell (i,j) at random and open it if not already open
		// 	calculate percolation threshold as the fraction of sites opened and store value in p[]
		if (T <= 0 || N <= 0)
			throw new IllegalArgumentException("T and N must be greater than 0.");

		experimentCount = T;
		percolationThreshold = new double[experimentCount];

		for (int currentExperiment = 0; currentExperiment < percolationThreshold.length; currentExperiment++)
		{
			openSites = 0;
			percolation = new Percolation(N);

			while (!percolation.percolates())
			{
				int i = StdRandom.uniform(N); 
				int j = StdRandom.uniform(N); 
				
				if(!percolation.isOpen(i,j))
				{
					percolation.open(i, j);
					openSites++;					
				}
			}
			percolationThreshold[currentExperiment] = (double) openSites / Math.pow(N, 2);
		}
		// perform T independent experiments on an N x N grid
	}
	
	/**
	 * sample mean of percolation threshold
	 * 
	 * Hint: use StdStats method for mean
	 * @return
	 */
	public double mean () {
		
		return StdStats.mean(percolationThreshold);
	}                   
	
	/**
	 * sample standard deviation of percolation threshold
	 * 
	 * Hint: use StdStats method for standard deviation
	 * @return
	 */
	public double stddev () {
		
		return StdStats.stddev(percolationThreshold);
	}
	
	/**
	 * low  end of 95% confidence interval
	 * @return
	 */
	public double confidenceLow () {
		
		return mean() - ((1.95 * stddev()) / Math.sqrt(experimentCount));
	}          
	
	/**
	 * high end of 95% confidence interval
	 * @return
	 */
	public double confidenceHigh ()  {
		
		return mean() + ((1.95 * stddev()) / Math.sqrt(experimentCount));
	}        

	public static void main(String[] args){

	    int n = 200;
	    int m = 100;
		
		PercolationStats test = new PercolationStats(n, m);
		
		StdOut.println("Test Results: ");
		StdOut.println();
	    StdOut.printf("Percolation threshold for a %d x %d system:\n", n, n);
	    StdOut.printf("%-20s %.10f\n", "Mean:", test.mean());
		StdOut.printf("%-20s %.10f\n", "Standard Deviation:", test.stddev());
		StdOut.printf("%-20s %.10f\n", "Lower Bounds:", test.confidenceLow());
		StdOut.printf("%-20s %.10f\n", "Upper Bounds:", test.confidenceHigh());
	}
	
	// Unit tests the data type. [DO NOT EDIT]
    //public static void main(String[] args) {
    //    int n = Integer.parseInt(args[0]);
    //    int m = Integer.parseInt(args[1]);
    //    PercolationStats stats = new PercolationStats(n, m);
    //    StdOut.printf("Percolation threshold for a %d x %d system:\n", n, n);
    //    StdOut.printf("  Mean                = %.3f\n", stats.mean());
    //    StdOut.printf("  Standard deviation  = %.3f\n", stats.stddev());
    //    StdOut.printf("  Confidence interval = [%.3f, %.3f]\n", stats.confidenceLow(),
    //            stats.confidenceHigh());
    //}
    
}