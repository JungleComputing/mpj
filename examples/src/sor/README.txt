Red/black Successive Over Relaxation (SOR) is an iterative method for solving
discretized Laplace equations on a grid.
This implementation is an MPJ version. It distributes the grid row-wise among
the CPUs. Each CPU exchanges one row of the matrix with its neighbours at the
beginning of each iteration.

The program takes one argument: the number of rows (and columns) in the array.