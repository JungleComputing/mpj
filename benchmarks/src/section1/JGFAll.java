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
 * 19/06/2005                                                               *
 **************************************************************************/

package section1;

import ibis.mpj.MPJ;
import ibis.mpj.MPJException;
import jgfutil.JGFInstrumentor;

public class JGFAll {

    public static int nprocess;

    public static int rank;

    public static void main(String argv[]) throws MPJException {

        /* Initialise MPJ */
        MPJ.init(argv);
        rank = MPJ.COMM_WORLD.rank();
        nprocess = MPJ.COMM_WORLD.size();

        if (rank == 0) {
            JGFInstrumentor.printHeader(1, 0, nprocess);
        }

        JGFAlltoallBench ata = new JGFAlltoallBench(rank, nprocess);
        ata.JGFrun();

        JGFBarrierBench ba = new JGFBarrierBench(rank, nprocess);
        ba.JGFrun();

        JGFBcastBench bc = new JGFBcastBench(rank, nprocess);
        bc.JGFrun();

        JGFGatherBench ga = new JGFGatherBench(rank, nprocess);
        ga.JGFrun();

        JGFPingPongBench pp = new JGFPingPongBench(rank, nprocess);
        pp.JGFrun();

        JGFReduceBench rd = new JGFReduceBench(rank, nprocess);
        rd.JGFrun();

        JGFScatterBench sc = new JGFScatterBench(rank, nprocess);
        sc.JGFrun();

        /* Finalise MPJ */
        MPJ.finish();

    }
}
