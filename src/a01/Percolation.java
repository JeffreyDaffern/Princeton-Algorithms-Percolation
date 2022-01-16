package a01;

import edu.princeton.cs.algs4.*;

/**
 * Creates an N by N boolean grid and determines whether there is a path from
 * the top to the bottom(percolation). opens sites by marking grid indexes as
 * true. If there is a path, the program fills the spots. uses the weighted
 * quick union algorithm to link open spots.
 * 
 * @author Jeff Daffern + Marjorie Cottrell
 *
 */
public class Percolation
{
	private int gridDimension, size, sourceTop, sinkBottom;
	private boolean[][] grid;
	private WeightedQuickUnionUF percolationUF, fullUF;

	// location id = i * n + j

	/**
	 * Creates an N by N grid with all sites blocked Throw IllegalArgumentException
	 * if N <= 0
	 * 
	 * 
	 * Hints: public Percolation(int n) Initialize instance variables Connect the uf
	 * sites corresponding to first and last rows of the percolation system with the
	 * source and sink sites respectively The 3 by 3 system with its top and bottom
	 * rows connected to the source and sink
	 * 
	 * @param N length and width of grid
	 */
	public Percolation(int N)
	{
		if (N <= 0)
			throw new IllegalArgumentException("N must be greater than zero");
		gridDimension = N;
		size = N * N;

		// initialize variables for WeightedQuickUnionUF class
		percolationUF = new WeightedQuickUnionUF(size + 2);
		fullUF = new WeightedQuickUnionUF(size + 1);

		// initialize virtual top and bottom for weighted quick unions
		sourceTop = 0;
		sinkBottom = size + 1;

		// initialize grid with all sites blocked
		// default value of boolean array is false
		grid = new boolean[N][N];

		// connect/union first row to sourceTop
		// connect/union bottom row to sinkBottom
		int i = 0;
		int k = N - 1;
		for (int j = 0; j < N; j++)
		{
			fullUF.union(encode(i, j), sourceTop);
			percolationUF.union(encode(i, j), sourceTop);
			percolationUF.union(encode(k, j), sinkBottom);
		}
	}

	/**
	 * Opens site (row i, column j) if it is not open already Throw
	 * IndexOutOfBoundsException if any open, isopen, isfull is outside of range.
	 * 
	 * 
	 * Hints: void open(int i, int j) Open the site (i, j) if it is not already open
	 * ?? Increment openSites by one Check if any of the neighbors to the north,
	 * east, west, and south of (i, j) is open, and if so, connect the uf site
	 * corresponding to (i, j) with the uf site corresponding to that neighbor
	 * 
	 * @param i row
	 * @param j column
	 */
	public void open(int i, int j)
	{
		if (i < 0 || i >= gridDimension)
			throw new IndexOutOfBoundsException("row index " + i + " must be between 0 and " + (gridDimension - 1));
		if (j < 0 || j >= gridDimension)
			throw new IndexOutOfBoundsException("column index " + j + " must be between 0 and " + (gridDimension - 1));

		// if !isOpen then open
		if (!isOpen(i, j))
			grid[i][j] = true;

		// check position with neighbors
		// union with neighbor?
		// fill site?

		// check if on top row and if site north is true/open
		if (i - 1 >= 0 && isOpen(i - 1, j))
		{
			// grid location union(neighbor, self)
			percolationUF.union(encode(i - 1, j), encode(i, j));
			fullUF.union(encode(i - 1, j), encode(i, j));
		}

		// check if on bottom row and if site south is open
		if (i + 1 < gridDimension && isOpen(i + 1, j))
		{
			// grid location union(neighbor, self)
			percolationUF.union(encode(i + 1, j), encode(i, j));
			fullUF.union(encode(i + 1, j), encode(i, j));
		}

		// check if in left-most column and if site west is open
		if (j - 1 >= 0 && isOpen(i, j - 1))
		{
			// grid location union(neighbor, self)
			percolationUF.union(encode(i, j - 1), encode(i, j));
			fullUF.union(encode(i, j - 1), encode(i, j));
		}

		// check if in right-most column and if site east is open
		if (j + 1 < gridDimension && isOpen(i, j + 1))
		{
			// grid location union(neighbor, self)
			percolationUF.union(encode(i, j + 1), encode(i, j));
			fullUF.union(encode(i, j + 1), encode(i, j));
		}
	}

	/**
	 * Checks if site (row i, column j) is open Throw IndexOutOfBoundsException if
	 * any open, isopen, isfull is outside of range.
	 * 
	 * 
	 * Hints: boolean isOpen(int i, int j) Return whether site (i, j) is open or not
	 * 
	 * @param i row
	 * @param j column
	 */
	public boolean isOpen(int i, int j)
	{
		if (i < 0 || i >= gridDimension)
			throw new IndexOutOfBoundsException("row index " + i + " must be between 0 and " + (gridDimension - 1));
		if (j < 0 || j >= gridDimension)
			throw new IndexOutOfBoundsException("column index " + j + " must be between 0 and " + (gridDimension - 1));

		if (grid[i][j])
			return true;

		return false;
	}

	/**
	 * Checks if site (row i, column j) is full A full site is an open site that can
	 * be connected to an open site in the top row via a chain of neighboring (left,
	 * right, up, down) open sites. Throw IndexOutOfBoundsException if any open,
	 * isopen, isfull is outside of range.
	 * 
	 * 
	 * Hints: boolean isFull(int i, int j) Return whether site (i, j) is full or not
	 * - a site is full if it is open and its corresponding uf site is connected to
	 * the source
	 * 
	 * 
	 * @param i row
	 * @param j column
	 */
	public boolean isFull(int i, int j)
	{
		if (i < 0 || i >= gridDimension)
			throw new IndexOutOfBoundsException("row index " + i + " must be between 0 and " + (gridDimension - 1));
		if (j < 0 || j >= gridDimension)
			throw new IndexOutOfBoundsException("column index " + j + " must be between 0 and " + (gridDimension - 1));

		if (!isOpen(i, j))
			return false;
		else
			return fullUF.find(encode(i, j)) == (fullUF.find(sourceTop));
	}

	/**
	 * Hints: int numberOfOpenSites() Return the number of open sites
	 */

	/**
	 * Checks if entire system grid percolates from top to bottom
	 * 
	 * Hints: boolean percolates() Return whether the system percolates or not - a
	 * system percolates if the sink is connected to the source
	 * 
	 * @return
	 */
	public boolean percolates()
	{
		return percolationUF.find(sourceTop) == percolationUF.find(sinkBottom);
	}

	/**
	 * Hints: private int encode(int i, int j) Return the UF site (1 . . . n2)
	 * corresponding to the percolation system site (i, j) // location id = i * n +
	 * j if (i,j) is (0,0) upper-left location id needs to equal 1 if (i,j) is
	 * (n-1,n-1) lower-right location id needs to equal n*n
	 * 
	 */
	private int encode(int i, int j)
	{
		/*
		 * if n = 3 and ufLocation = (i * n + j) + 1 if (i,j) is (0,0) then (0 * 3 + 0)
		 * + 1 = 1 if (i,j) is (1,1) then (1 * 3 + 1) + 1 = 5 if (i,j) is (n-1,n-1) then
		 * (2 * 3 + 2) + 1 = 9
		 */
		return (i * gridDimension + j) + 1;
	}

//	public static void main(String[] args)
//	{
//		Percolation perc = new Percolation(2);
//		System.out.println("Percolates: " + perc.percolates());
//		System.out.println();
//
//		System.out.print("Is (0,1) open? " + perc.isOpen(0, 1));
//		System.out.println(" | full? " + perc.isFull(0, 1));
//		perc.open(0, 1);
//		System.out.print("Is (0,1) open? " + perc.isOpen(0, 1));
//		System.out.println(" | full? " + perc.isFull(0, 1));
//		System.out.println();
//
//		System.out.print("Is (1,1) open? " + perc.isOpen(1, 1));
//		System.out.println(" | full? " + perc.isFull(1, 1));
//		perc.open(1, 1);
//		System.out.print("Is (1,1) open? " + perc.isOpen(1, 1));
//		System.out.println(" | full? " + perc.isFull(1, 1));
//		System.out.println();
//
//		System.out.println("Percolates: " + perc.percolates());
//	}

	// Unit tests the data type. [DO NOT EDIT]
	// public static void main(String[] args) {
	// String filename = args[0];
	// In in = new In(filename);
	// int n = in.readInt();
	// Percolation perc = new Percolation(n);
	// while (!in.isEmpty()) {
	// int i = in.readInt();
	// int j = in.readInt();
	// perc.open(i, j);
	// }
	// StdOut.printf("%d x %d system:\n", n, n);
	// StdOut.printf(" Open sites = %d\n", perc.numberOfOpenSites());
	// StdOut.printf(" Percolates = %b\n", perc.percolates());
	// if (args.length == 3) {
	// int i = Integer.parseInt(args[1]);
	// int j = Integer.parseInt(args[2]);
	// StdOut.printf(" isFull(%d, %d) = %b\n", i, j, perc.isFull(i, j));
	// }
	// }

}
