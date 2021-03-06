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

package pt2pt;

import ibis.mpj.MPJ;
import ibis.mpj.MPJException;
import ibis.mpj.Status;

class wildcard {
    static public void test() throws MPJException {
        int me, tasks, i, tag, expected;
        int val[] = new int[1];
        Status status;
        final int ITER = 10;

        me = MPJ.COMM_WORLD.rank();
        tasks = MPJ.COMM_WORLD.size();

        if (me == 0) {
            for (i = 0; i < (tasks - 1) * ITER; i++) {
                status = MPJ.COMM_WORLD.recv(val, 0, 1, MPJ.INT,
                        MPJ.ANY_SOURCE, i / (tasks - 1));
                expected = status.getSource() * 1000 + status.getTag();
                if (val[0] != expected)
                    System.out.println("ERROR, val[0] = " + (val[0])
                            + ", should be " + expected);
            }
        } else {
            for (i = 0; i < ITER; i++) {
                tag = i;
                val[0] = me * 1000 + tag;
                MPJ.COMM_WORLD.send(val, 0, 1, MPJ.INT, 0, tag);
            }
        }

        MPJ.COMM_WORLD.barrier();
        if (me == 0)
            System.out.println("Wildcard TEST COMPLETE\n");

    }

    static public void main(String[] args) throws MPJException {
        MPJ.init(args);

        test();

        MPJ.finish();
    }
}
