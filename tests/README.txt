This directory contains the main classes of mpiJava's (V1.2.5) test suite.
It is meant to test the MPJ/Ibis implementation.

There are different packages to test certain behaviours:

- ccl             collective communication layer
- ccl_ObjSer      collective communication layer with objects
- comm            communicators
- dtyp            derived datatype
- dtyp_ObjSer     derived datatypes whith objects
- env             environment		
- group           groups
- pt2pt           point-to-point communication
- pt2pt_ObjSer    point-to-point communication whith objects
- topo            virtual topologies


In each package there is another readme file, which explains
how many nodes are needed to execute a certain test class and
some annotations, if needed. Besides that, there are sample
files containing the results for a correct MPJ implementation.

Most classes in dtyp, dtyp_ObjSer and topo may fail, since some features
needed are currently not implemented in MPJ/Ibis.

Todo:
- implement some applications to automate all the tests