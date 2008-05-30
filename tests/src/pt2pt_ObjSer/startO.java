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

 Object version :
 Sang Lim(slim@npac.syr.edu)
 Northeast Parallel Architectures Center at Syracuse University
 08/18/98
 ****************************************************************************
 */
/* Ported to MPJ:
 Markus Bornemann
 Vrije Universiteit Amsterdam Department of Computer Science
 25/5/2005
 */

package pt2pt_ObjSer;

import ibis.mpj.MPJ;
import ibis.mpj.MPJException;
import ibis.mpj.Prequest;
import ibis.mpj.Request;
import ibis.mpj.Status;

class startO {

    static int me, tasks, rc, i, bytes;

    static test mebuf[] = new test[1];

    static test data[];

    static byte buf[];

    static Prequest req[];

    static Status stats[];

    static void wstart() throws MPJException {
        MPJ.COMM_WORLD.barrier();

        for (i = 0; i < 2 * tasks; i++)
            req[i].start();

        stats = Request.waitAll(req);

        for (i = 0; i < tasks; i++)
            if (data[i].a != i)
                System.out.println("ERROR in Startall: data is " + data[i].a
                        + ", should be " + i);
        /* ONLY THE RECEIVERS HAVE STATUS VALUES ! */
        for (i = 1; i < 2 * tasks; i += 2) {
            bytes = stats[i].getCount(MPJ.OBJECT);

            if (bytes != 1)
                System.out.println("ERROR in Waitall: bytes = " + bytes
                        + ", should be 1");
        }
    }

    // ////////////////////////////////////////////////////////////////////

    static public void main(String[] args) throws MPJException {

        MPJ.init(args);
        me = MPJ.COMM_WORLD.rank();
        tasks = MPJ.COMM_WORLD.size();

        data = new test[tasks];

        for (i = 0; i < tasks; i++) {
            data[i] = new test();
            data[i].a = -1;
        }

        int intsize = 4;
        buf = new byte[100000];
        // Sizing of buffer is basically hit-and-miss for OBJECT types,
        // because `Comm.Pack_size()' doesn't work in this case.

        req = new Prequest[2 * tasks];
        stats = new Status[2 * tasks];

        MPJ.bufferAttach(buf);
        mebuf[0] = new test();
        mebuf[0].a = me;

        for (i = 0; i < tasks; i++) {
            req[2 * i] = MPJ.COMM_WORLD.sendInit(mebuf, 0, 1, MPJ.OBJECT, i, 1);
            req[2 * i + 1] = MPJ.COMM_WORLD.recvInit(data, i, 1, MPJ.OBJECT, i,
                    1);
        }
        if (me == 0)
            System.out.println("Testing send/recv init...");
        wstart();

        for (i = 0; i < tasks; i++) {
            req[2 * i] = MPJ.COMM_WORLD
                    .bsendInit(mebuf, 0, 1, MPJ.OBJECT, i, 1);
            req[2 * i + 1] = MPJ.COMM_WORLD.recvInit(data, i, 1, MPJ.OBJECT, i,
                    1);
        }
        if (me == 0)
            System.out.println("Testing bsend init...");
        wstart();

        for (i = 0; i < tasks; i++) {
            req[2 * i] = MPJ.COMM_WORLD
                    .ssendInit(mebuf, 0, 1, MPJ.OBJECT, i, 1);
            req[2 * i + 1] = MPJ.COMM_WORLD.recvInit(data, i, 1, MPJ.OBJECT, i,
                    1);
        }
        if (me == 0)
            System.out.println("Testing ssend init...");
        wstart();

        MPJ.COMM_WORLD.barrier();
        MPJ.bufferDetach();
        if (me == 0)
            System.out.println("StartO TEST COMPLETE\n");
        MPJ.finish();
    }
}
