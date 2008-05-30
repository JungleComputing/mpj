/* $Id$ */

/* Ported to MPJ:
 Markus Bornemann
 Vrije Universiteit Amsterdam Department of Computer Science
 25/5/2005
 */

package ccl_ObjSer;

import ibis.mpj.MPJ;
import ibis.mpj.MPJException;

class allreduceO {
    static public void test() throws MPJException {
        final int MAXLEN = 100;

        int root, i, j, k;
        complexNum out[] = new complexNum[MAXLEN];
        complexNum in[] = new complexNum[MAXLEN];
        int myself, tasks;
        boolean bool = false;

        myself = MPJ.COMM_WORLD.rank();
        tasks = MPJ.COMM_WORLD.size();

        root = tasks / 2;

        for (i = 0; i < MAXLEN; i++) {
            in[i] = new complexNum();
            out[i] = new complexNum();
            out[i].realPart = i;
            out[i].imaginPart = i;
        }

        // complexAdd cadd = new complexAdd();
        // Op op = new Op(cadd, bool);
        complexAdd op = new complexAdd(bool);

        MPJ.COMM_WORLD.allreduce(out, 0, in, 0, MAXLEN, MPJ.OBJECT, op);

        for (k = 0; k < MAXLEN; k++) {
            if ((in[k].realPart != k * tasks)
                    || (in[k].imaginPart != k * tasks)) {
                System.out.println("bad answer (" + (in[k].realPart)
                        + ") at index " + k + "(should be " + (k * tasks)
                        + ") on proc. : " + myself);
                break;
            }
        }

        MPJ.COMM_WORLD.barrier();
        if (myself == 0)
            System.out.println("AllreduceO TEST COMPLETE\n");

    }

    static public void main(String[] args) throws MPJException {
        MPJ.init(args);

        test();

        MPJ.finish();
    }
}
