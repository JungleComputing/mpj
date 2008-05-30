package ibis.mpj;

import ibis.ipl.Ibis;
import ibis.ipl.IbisIdentifier;
import ibis.ipl.RegistryEventHandler;

class RegEvtHandler implements RegistryEventHandler {

    IbisIdentifier myId;

    int nInstances;

    int myRank;

    private int joinCount;

    IbisIdentifier[] identifiers;

    public synchronized void joined(IbisIdentifier ident) {
        if (ident.equals(myId)) {
            myRank = joinCount;
        }
        identifiers[joinCount++] = ident;
        if (joinCount == nInstances) {
            notifyAll();
        }
    }

    public void left(IbisIdentifier ident) {
        // ignored
    }

    public void died(IbisIdentifier corpse) {
        // ignored
    }

    void waitForEveryone(Ibis ibis) {
        nInstances = ibis.registry().getPoolSize();
        identifiers = new IbisIdentifier[nInstances];
        myId = ibis.identifier();
        ibis.registry().enableEvents();
        synchronized (this) {
            while (joinCount < nInstances) {
                try {
                    wait();
                } catch (Exception e) {
                    // ignored
                }
            }
        }
    }

    public void gotSignal(String signal) {
        // ignored
    }

    public void electionResult(String name, IbisIdentifier winner) {
        // ignored
    }
}
