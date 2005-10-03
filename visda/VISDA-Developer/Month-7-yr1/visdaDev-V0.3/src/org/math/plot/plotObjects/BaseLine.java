/*
 * Created on 1 juin 2005 by richet
 */
package org.math.plot.plotObjects;

import java.awt.*;

import org.math.plot.render.*;

public class BaseLine extends Line {

    public BaseLine(double[] c1, double[] c2, Color col) {
        super(c1, c2, col);
    }

    public void plot(AbstractDrawer draw) {
        if (!visible)
            return;

        draw.setColor(color);
        draw.drawLineRatio(extrem[0], extrem[1]);
    }

}
