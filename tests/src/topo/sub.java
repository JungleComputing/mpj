/* $Id$ */

/****************************************************************************

 MESSAGE PASSING INTERFACE TEST CASE SUITE

 Copyright IBM Corp. 1995

 IBM Corp. hereby grants a non-exclusive license to use, copy, modify, and
 distribute this software for any purpose and without fee provided that the
 above copyright notice and the following paragraphs appear in all copies.

 IBM Corp. makes no representation that the test cases comprising this
 suite are correct or are an accurate representation of any standard.

 In no event shall IBM be liable to any party for direct, indirect, special
 incidental, or consequential damage arising out of the use of this software
 even if IBM Corp. has been advised of the possibility of such damage.

 IBM CORP. SPECIFICALLY DISCLAIMS ANY WARRANTIES INCLUDING, BUT NOT LIMITED
 TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS AND IBM
 CORP. HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS.

 ****************************************************************************

 These test cases reflect an interpretation of the MPI Standard.  They are
 are, in most cases, unit tests of specific MPI behaviors.  If a user of any
 test case from this set believes that the MPI Standard requires behavior
 different than that implied by the test case we would appreciate feedback.

 Comments may be sent to:
 Richard Treumann
 treumann@kgn.ibm.com

 ****************************************************************************

 MPI-Java version :
 Sung-Hoon Ko(shko@npac.syr.edu)
 Northeast Parallel Architectures Center at Syracuse University
 03/22/98

 ****************************************************************************
 */
/* Ported to MPJ:
 Markus Bornemann
 Vrije Universiteit Amsterdam Department of Computer Science
 25/5/2005
 */

package topo;

import ibis.mpj.Cartcomm;
import ibis.mpj.Comm;
import ibis.mpj.MPJ;
import ibis.mpj.MPJException;

class sub {
    static public void test() throws MPJException {
        int dims[] = new int[2];
        boolean periods[] = new boolean[2];
        int me, tasks, i;
        int size, rank;
        int cnt = 0;

        me = MPJ.COMM_WORLD.rank();
        tasks = MPJ.COMM_WORLD.size();

        if (tasks != 6) {
            System.out.println("MUST RUN WITH 6 TASKS");
            System.exit(0);
        }
        Comm comms[] = new Comm[20];

        dims[0] = 2;
        dims[1] = 3;
        Cartcomm comm = MPJ.COMM_WORLD.createCart(dims, periods, false);
        comms[cnt++] = comm;

        int[] dims2 = comm.get().dims;

        boolean remain[] = new boolean[2];
        remain[0] = false;
        remain[1] = true;
        Cartcomm subcomm = comm.sub(remain);
        comms[cnt++] = subcomm;
        size = subcomm.size();
        if (size != 3)
            System.out.println("ERROR in MPJ_Cart_sub (1): size = " + size
                    + ", should be 3");

        rank = subcomm.rank();
        if (rank != me % 3)
            System.out.println("ERROR in MPJ_Cart_sub (2): rank =" + rank
                    + ", should be " + me);

        remain[0] = false;
        remain[1] = false;
        subcomm = comm.sub(remain);
        comms[cnt++] = subcomm;
        size = subcomm.size();
        if (size != 1)
            System.out.println("ERROR in MPJ_Cart_sub (3): size = " + size
                    + ", should be 1");

        rank = subcomm.rank();
        if (rank != 0)
            System.out.println("ERROR in MPJ_Cart_sub (4): rank =" + rank
                    + ", should be 0");

        remain[0] = true;
        remain[1] = true;
        subcomm = comm.sub(remain);
        comms[cnt++] = subcomm;
        size = subcomm.size();
        if (size != tasks)
            System.out.println("ERROR in MPJ_Cart_sub (5): size = " + size
                    + ", should be " + tasks);

        rank = subcomm.rank();
        if (rank != me)
            System.out.println("ERROR in MPJ_Cart_sub (6): rank =" + rank
                    + ", should be " + me);

        remain[0] = true;
        remain[1] = false;
        subcomm = comm.sub(remain);
        comms[cnt++] = subcomm;
        size = subcomm.size();
        if (size != 2)
            System.out.println("ERROR in MPJ_Cart_sub (7): size = " + size
                    + ", should be 2");

        rank = subcomm.rank();
        if (rank != me / 3)
            System.out.println("ERROR in MPJ_Cart_sub (8): rank =" + rank
                    + ", should be " + (me / 3));

        MPJ.COMM_WORLD.barrier();

        if (me == 0)
            System.out.println("Sub TEST COMPLETE\n");

    }

    static public void main(String[] args) throws MPJException {

        MPJ.init(args);

        test();

        MPJ.finish();
    }
}
