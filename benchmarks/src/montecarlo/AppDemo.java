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
 *      Original version of this code by Hon Yau (hwyau@epcc.ed.ac.uk)     *
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

package montecarlo;

import ibis.mpj.MPJ;
import ibis.mpj.MPJException;

import java.util.Vector;

/**
 * Code, a test-harness for invoking and driving the Applications Demonstrator
 * classes.
 * 
 * <p>
 * To do:
 * <ol>
 * <li>Very long delay prior to connecting to the server.</li>
 * <li>Some text output seem to struggle to get out, without the user tapping
 * ENTER on the keyboard!</li>
 * </ol>
 * 
 * @author H W Yau
 * @version $Revision$ $Date: 2008-05-30 15:35:35 +0200 (Fri, 30 May
 *          2008) $
 */
public class AppDemo extends Universal {
    // ------------------------------------------------------------------------
    // Class variables.
    // ------------------------------------------------------------------------

    public static double JGFavgExpectedReturnRateMC = 0.0;

    /**
     * A class variable.
     */
    public static boolean DEBUG = true;

    /**
     * The prompt to write before any debug messages.
     */
    protected static String prompt = "AppDemo> ";

    public static final int Serial = 1;

    // ------------------------------------------------------------------------
    // Instance variables.
    // ------------------------------------------------------------------------
    /**
     * Directory in which to find the historical rates.
     */
    private String dataDirname;

    /**
     * Name of the historical rate to model.
     */
    private String dataFilename;

    /**
     * The number of time-steps which the Monte Carlo simulation should run for.
     */
    private int nTimeStepsMC = 0;

    /**
     * The number of Monte Carlo simulations to run.
     */
    private int nRunsMC = 0;

    /**
     * The max no of Monte Carlo simulations per process
     */
    private int p_nRunsMC = 0;

    /**
     * The default duration between time-steps, in units of a year.
     */
    private double dTime = 1.0 / 365.0;

    private Vector<Object> tasks;

    private Vector<ToResult> results;

    /**
     * temporary vector for MPJ
     */
    @SuppressWarnings("unchecked")
    private Vector[] p_results = new Vector[1];

    public AppDemo(String dataDirname, String dataFilename, int nTimeStepsMC,
            int nRunsMC) {
        this.dataDirname = dataDirname;
        this.dataFilename = dataFilename;
        this.nTimeStepsMC = nTimeStepsMC;
        this.nRunsMC = nRunsMC;
        set_prompt(prompt);
        set_DEBUG(DEBUG);
    }

    /**
     * Single point of contact for running this increasingly bloated class.
     * Other run modes can later be defined for whether a new rate should be
     * loaded in, etc. Note that if the <code>hostname</code> is set to the
     * string "none", then the demonstrator runs in purely serial mode.
     */

    /**
     * Initialisation and Run methods.
     */

    PriceStock psMC;

    double pathStartValue = 100.0;

    double avgExpectedReturnRateMC = 0.0;

    double avgVolatilityMC = 0.0;

    ToInitAllTasks initAllTasks = null;

    public void initSerial() {
        try {
            //
            // Measure the requested path rate.
            RatePath rateP = new RatePath(dataDirname, dataFilename);
            rateP.dbgDumpFields();
            ReturnPath returnP = rateP.getReturnCompounded();
            returnP.estimatePath();
            returnP.dbgDumpFields();
            double expectedReturnRate = returnP.get_expectedReturnRate();
            double volatility = returnP.get_volatility();
            //
            // Now prepare for MC runs.
            initAllTasks = new ToInitAllTasks(returnP, nTimeStepsMC,
                    pathStartValue);
            String slaveClassName = "MonteCarlo.PriceStock";
            //
            // Now create the tasks.
            initTasks(nRunsMC);
            //
        } catch (DemoException demoEx) {
            dbgPrintln(demoEx.toString());
            System.exit(-1);
        }
    }

    @SuppressWarnings("unchecked")
    public void runSerial() throws MPJException {

        int ilow, ihigh;

        if (JGFMonteCarloBench.rank == 0) {
            results = new Vector<ToResult>(nRunsMC);
        }
        p_nRunsMC = (nRunsMC + JGFMonteCarloBench.nprocess - 1)
                / JGFMonteCarloBench.nprocess;
        p_results[0] = new Vector(p_nRunsMC);

        ilow = JGFMonteCarloBench.rank * p_nRunsMC;
        ihigh = (JGFMonteCarloBench.rank + 1) * p_nRunsMC;
        if (JGFMonteCarloBench.rank == JGFMonteCarloBench.nprocess - 1)
            ihigh = nRunsMC;

        // Now do the computation.
        PriceStock ps;
        for (int iRun = ilow; iRun < ihigh; iRun++) {
            ps = new PriceStock();
            ps.setInitAllTasks(initAllTasks);
            ps.setTask(tasks.elementAt(iRun));
            ps.run();
            p_results[0].addElement(ps.getResult());
        }

        if (JGFMonteCarloBench.rank == 0) {
            for (int i = 0; i < p_results[0].size(); i++) {
                results.addElement((ToResult) p_results[0].elementAt(i));
            }
            for (int j = 1; j < JGFMonteCarloBench.nprocess; j++) {
                p_results[0].removeAllElements();
                MPJ.COMM_WORLD.recv(p_results, 0, 1, MPJ.OBJECT, j, j);
                for (int i = 0; i < p_results[0].size(); i++) {
                    results.addElement((ToResult) p_results[0].elementAt(i));
                }

            }

        } else {

            MPJ.COMM_WORLD.send(p_results, 0, 1, MPJ.OBJECT, 0,
                    JGFMonteCarloBench.rank);

        }

    }

    public void processSerial() {
        //
        // Process the results.
        try {
            processResults();
        } catch (DemoException demoEx) {
            dbgPrintln(demoEx.toString());
            System.exit(-1);
        }
    }

    // ------------------------------------------------------------------------
    /**
     * Generates the parameters for the given Monte Carlo simulation.
     * 
     * @param nRunsMC
     *            the number of tasks, and hence Monte Carlo paths to produce.
     */
    private void initTasks(int nRunsMC) {
        tasks = new Vector<Object>(nRunsMC);
        for (int i = 0; i < nRunsMC; i++) {
            String header = "MC run " + String.valueOf(i);
            ToTask task = new ToTask(header, (long) i * 11);
            tasks.addElement((Object) task);
        }
    }

    /**
     * Method for doing something with the Monte Carlo simulations. It's
     * probably not mathematically correct, but shall take an average over all
     * the simulated rate paths.
     * 
     * @exception DemoException
     *                thrown if there is a problem with reading in any values.
     */
    private void processResults() throws DemoException {
        double avgExpectedReturnRateMC = 0.0;
        double avgVolatilityMC = 0.0;
        double runAvgExpectedReturnRateMC = 0.0;
        double runAvgVolatilityMC = 0.0;
        ToResult returnMC;
        if (nRunsMC != results.size()) {
            errPrintln("Fatal: TaskRunner managed to finish with no all the results gathered in!");
            System.exit(-1);
        }
        //
        // Create an instance of a RatePath, for accumulating the results of the
        // Monte Carlo simulations.
        RatePath avgMCrate = new RatePath(nTimeStepsMC, "MC", 19990109,
                19991231, dTime);
        for (int i = 0; i < nRunsMC; i++) {
            // First, create an instance which is supposed to generate a
            // particularly simple MC path.
            returnMC = results.elementAt(i);
            avgMCrate.inc_pathValue(returnMC.get_pathValue());
            avgExpectedReturnRateMC += returnMC.get_expectedReturnRate();
            avgVolatilityMC += returnMC.get_volatility();
            runAvgExpectedReturnRateMC = avgExpectedReturnRateMC
                    / ((double) (i + 1));
            runAvgVolatilityMC = avgVolatilityMC / ((double) (i + 1));
        } // for i;
        avgMCrate.inc_pathValue((double) 1.0 / ((double) nRunsMC));
        avgExpectedReturnRateMC /= nRunsMC;
        avgVolatilityMC /= nRunsMC;
        /*
         * try{ Thread.sleep(200); } catch( InterruptedException intEx) {
         * errPrintln(intEx.toString()); }
         * 
         */

        JGFavgExpectedReturnRateMC = avgExpectedReturnRateMC;

        // dbgPrintln("Average over "+nRunsMC+": expectedReturnRate="+
        // avgExpectedReturnRateMC+" volatility="+avgVolatilityMC +
        // JGFavgExpectedReturnRateMC);
    }

    //
    // ------------------------------------------------------------------------
    // Accessor methods for class AppDemo.
    // Generated by 'makeJavaAccessor.pl' script. HWY. 20th January 1999.
    // ------------------------------------------------------------------------
    /**
     * Accessor method for private instance variable <code>dataDirname</code>.
     * 
     * @return Value of instance variable <code>dataDirname</code>.
     */
    public String get_dataDirname() {
        return (this.dataDirname);
    }

    /**
     * Set method for private instance variable <code>dataDirname</code>.
     * 
     * @param dataDirname
     *            the value to set for the instance variable
     *            <code>dataDirname</code>.
     */
    public void set_dataDirname(String dataDirname) {
        this.dataDirname = dataDirname;
    }

    /**
     * Accessor method for private instance variable <code>dataFilename</code>.
     * 
     * @return Value of instance variable <code>dataFilename</code>.
     */
    public String get_dataFilename() {
        return (this.dataFilename);
    }

    /**
     * Set method for private instance variable <code>dataFilename</code>.
     * 
     * @param dataFilename
     *            the value to set for the instance variable
     *            <code>dataFilename</code>.
     */
    public void set_dataFilename(String dataFilename) {
        this.dataFilename = dataFilename;
    }

    /**
     * Accessor method for private instance variable <code>nTimeStepsMC</code>.
     * 
     * @return Value of instance variable <code>nTimeStepsMC</code>.
     */
    public int get_nTimeStepsMC() {
        return (this.nTimeStepsMC);
    }

    /**
     * Set method for private instance variable <code>nTimeStepsMC</code>.
     * 
     * @param nTimeStepsMC
     *            the value to set for the instance variable
     *            <code>nTimeStepsMC</code>.
     */
    public void set_nTimeStepsMC(int nTimeStepsMC) {
        this.nTimeStepsMC = nTimeStepsMC;
    }

    /**
     * Accessor method for private instance variable <code>nRunsMC</code>.
     * 
     * @return Value of instance variable <code>nRunsMC</code>.
     */
    public int get_nRunsMC() {
        return (this.nRunsMC);
    }

    /**
     * Set method for private instance variable <code>nRunsMC</code>.
     * 
     * @param nRunsMC
     *            the value to set for the instance variable
     *            <code>nRunsMC</code>.
     */
    public void set_nRunsMC(int nRunsMC) {
        this.nRunsMC = nRunsMC;
    }

    /**
     * Accessor method for private instance variable <code>tasks</code>.
     * 
     * @return Value of instance variable <code>tasks</code>.
     */
    public Vector<Object> get_tasks() {
        return (this.tasks);
    }

    /**
     * Set method for private instance variable <code>tasks</code>.
     * 
     * @param tasks
     *            the value to set for the instance variable <code>tasks</code>.
     */
    public void set_tasks(Vector<Object> tasks) {
        this.tasks = tasks;
    }

    /**
     * Accessor method for private instance variable <code>results</code>.
     * 
     * @return Value of instance variable <code>results</code>.
     */
    public Vector<ToResult> get_results() {
        return (this.results);
    }

    /**
     * Set method for private instance variable <code>results</code>.
     * 
     * @param results
     *            the value to set for the instance variable
     *            <code>results</code>.
     */
    public void set_results(Vector<ToResult> results) {
        this.results = results;
    }
    // ------------------------------------------------------------------------
}
