/* $Id$ */

/*
 * Created on 12.02.2005
 */
package ibis.mpj;

/**
 * Implementation of the operation: product (MPJ.PROD). Only available for these
 * datatypes: MPJ.SHORT, MPJ.INT, MPJ.LONG, MPJ.FLOAT, MPJ.DOUBLE.
 */
public class OpProd extends Op {
    OpProd(boolean commute) {
        super(commute);
    }

    public void call(Object invec, int inoffset, Object inoutvec,
            int outoffset, int count, Datatype datatype) throws MPJException {
        if (datatype == MPJ.SHORT) {
            short[] shortInvec = (short[]) invec;
            short[] shortInoutvec = (short[]) inoutvec;
            if (shortInvec.length != shortInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                shortInoutvec[i + outoffset] *= shortInvec[i+inoffset];
            }
            return;
        } 
        if (datatype == MPJ.INT) {
            int[] intInvec = (int[]) invec;
            int[] intInoutvec = (int[]) inoutvec;
            if (intInvec.length != intInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                intInoutvec[i + outoffset] *= intInvec[i+inoffset];
            }
            return;
        }
        if (datatype == MPJ.LONG) {
            long[] longInvec = (long[]) invec;
            long[] longInoutvec = (long[]) inoutvec;
            if (longInvec.length != longInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                longInoutvec[i + outoffset] *= longInvec[i+inoffset];
            }
            return;
        }
        if (datatype == MPJ.FLOAT) {
            float[] floatInvec = (float[]) invec;
            float[] floatInoutvec = (float[]) inoutvec;
            if (floatInvec.length != floatInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                floatInoutvec[i + outoffset] *= floatInvec[i+inoffset];
            }
            return;
        }
        if (datatype == MPJ.DOUBLE) {
            double[] doubleInvec = (double[]) invec;
            double[] doubleInoutvec = (double[]) inoutvec;
            if (doubleInvec.length != doubleInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                doubleInoutvec[i + outoffset] *= doubleInvec[i+inoffset];
            }
            return;
        }

        throw new MPJException("Operation does not support this Datatype");

    }
}
