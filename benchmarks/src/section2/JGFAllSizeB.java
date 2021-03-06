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
import lufact.JGFLUFactBench;
import series.JGFSeriesBench;
import sor.JGFSORBench;
import sparsematmult.JGFSparseMatmultBench;
import crypt.JGFCryptBench;

public class JGFAllSizeB {

    public static int nprocess;

    public static int rank;

    public static void main(String argv[]) throws MPJException {

        /* Initialise MPJ */
        MPJ.init(argv);
        rank = MPJ.COMM_WORLD.rank();
        nprocess = MPJ.COMM_WORLD.size();

        int size = 1;

        if (rank == 0) {
            JGFInstrumentor.printHeader(2, 1, nprocess);
        }

        JGFSeriesBench se = new JGFSeriesBench(nprocess, rank);
        se.JGFrun(size);

        JGFLUFactBench lub = new JGFLUFactBench(nprocess, rank);
        lub.JGFrun(size);

        JGFCryptBench cb = new JGFCryptBench(nprocess, rank);
        cb.JGFrun(size);

        JGFSORBench jb = new JGFSORBench(nprocess, rank);
        jb.JGFrun(size);

        JGFSparseMatmultBench smm = new JGFSparseMatmultBench(nprocess, rank);
        smm.JGFrun(size);

        /* Finalise MPJ */
        MPJ.finish();

    }
}
