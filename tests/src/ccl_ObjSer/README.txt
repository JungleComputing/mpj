This directory contains test classes for the collective 
communication layer with object serialization. In fhe following 
the number of nodes needed and some explanations are annotated:


class                  no. of nodes (min-max)           annotation
------------------------------------------------------------------
allgatherO                  1-inf    
allgathervO                 1-3
allreduceO                  1-inf
alltoallO                   1-inf
alltoallvO                  1-3
bcastO                      1-inf
gatherO                     1-inf
gathervO                    1-3
reduceO                     1-inf
reduce_scatterO             1-inf
scanO                       1-inf
scatterO                    1-inf
scattervO                   1-3
