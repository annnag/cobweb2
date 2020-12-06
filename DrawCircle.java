package org.cobweb.cobweb2.ui.swing;

import java.util.*;
import java.util.List;

import org.cobweb.cobweb2.core.Topology;
import org.cobweb.util.Point2D;

public class DrawCircle {
    public static Set<Point2D> drawCircle(int width, int height, int radius) {
        Set<Point2D> points = new HashSet<>();
        int x_centre = (int)(Math.floor(width/2));
        int y_centre = (int)(Math.floor(height/2));
        int x = radius; //add x_centre?
        int y = 0; //add y_centre?
        // Printing the initial point
        // on the axes after translation

        //If circle goes off the grid
        if(radius * 2 > width || radius * 2 > height){
            return points;
        }

        points.add(new Point2D(x, y));
        points.add(new Point2D(y, x));

        // When radius is zero only a single
        // point will be printed
        if (radius > 0) {
            points.add(new Point2D(x + x_centre, -y + y_centre));
            points.add(new Point2D(y + x_centre, x + y_centre));
            points.add(new Point2D(-y + x_centre, x + y_centre));
        }

        // Initialising the value of P
        int P = 1 - radius;
        while (x > y) {
            y++;
            // Mid-point of the two pixels is inside or on the perimeter
            if (P <= 0) {
                P = P + 2 * y + 1;
            } // Mid-point is outside the perimeter
            else {
                x--;
                P = P + 2 * y - 2 * x + 1;
            }

            // All the perimeter points have already
            // been printed
            if (x < y) {
                break;
            }

            // Printing the generated point and its
            // reflection in the other octants after
            // translation
            points.add(new Point2D(x + x_centre, y + y_centre));
            points.add(new Point2D(-x + x_centre, y + y_centre));
            points.add(new Point2D(x + x_centre, -y + y_centre));
            points.add(new Point2D(-x + x_centre, -y + y_centre));

            // If the generated point is on the
            // line x = y then the perimeter points
            // have already been printed
            if (x != y) {
                points.add(new Point2D(y + x_centre, x + y_centre));
                points.add(new Point2D(-y + x_centre, x + y_centre));
                points.add(new Point2D(y + x_centre, -x + y_centre));
                points.add(new Point2D(-y + x_centre, -x + y_centre));
            }
        }
        return points;
    }
}
