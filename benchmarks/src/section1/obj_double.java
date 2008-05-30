/* $Id$ */

/**************************************************************************
 * Ported to MPJ:                                                          *
 * Markus Bornemann                                                        * 
 * Vrije Universiteit Amsterdam Department of Computer Science             *
 * 19/06/2005                                                              *
 **************************************************************************/

package section1;

import java.io.Serializable;

class obj_double implements Serializable {

    private static final long serialVersionUID = 1L;

    double x;

    public obj_double(double x) {
        this.x = x;
    }

}
