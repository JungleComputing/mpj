This directory contains test classes for the
collective communication layer. In fhe following the number
of nodes needed and some explanations are annotated:


class                  no. of nodes (min-max)           annotation
------------------------------------------------------------------
allgather                   1-inf    
allgatherv                  1-3
allreduce                   1-inf
allreduce_maxminloc         1-inf
alltoall                    1-inf
alltoallv                   1-3
barrier                     2-2
bcast                       1-inf
gather                      1-inf
gatherv                     1-3
reduce                      1-inf
reduce_scatter              1-inf
scan                        1-inf
scatter                     1-inf
scatterv                    1-3

