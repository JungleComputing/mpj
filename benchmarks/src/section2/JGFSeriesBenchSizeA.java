/* $Id$ */

/**************************************************************************
*                                                                         *
*             Java Grande Forum Benchmark Suite - MPJ Version 1.0         *
*                                                                         *
*                            produced by                                  *
*                                                                         *
*                  Java Grande Benchmarking Project                       *
*                                                                         *
*                                at                                       *
*                                                                         *
*                Edinburgh Parallel Computing Centre                      *
*                                                                         * 
*                email: epcc-javagrande@epcc.ed.ac.uk                     *
*                                                                         *
*                                                                         *
*      This version copyright (c) The University of Edinburgh, 2001.      *
*                         All rights reserved.                            *
*                                                                         *
**************************************************************************/
/**************************************************************************
* Ported to MPJ:                                                          *
* Markus Bornemann                                                        * 
* Vrije Universiteit Amsterdam Department of Computer Science             *
* 19/06/2005                                                              *
**************************************************************************/

package section2;

import ibis.mpj.MPJ;
import ibis.mpj.MPJException;
import jgfutil.JGFInstrumentor;
import series.JGFSeriesBench;

public class JGFSeriesBenchSizeA{ 

  public static int nprocess;
  public static int rank;

  public static void main(String argv[]) throws MPJException{

/* Initialise MPJ */
     MPJ.init(argv);
     rank = MPJ.COMM_WORLD.rank();
     nprocess = MPJ.COMM_WORLD.size();

    if(rank==0) {
      JGFInstrumentor.printHeader(2,0,nprocess);
    }
    JGFSeriesBench se = new JGFSeriesBench(nprocess,rank); 
    se.JGFrun(0);

/* Finalise MPJ */
     MPJ.finish();
 
  }
}


