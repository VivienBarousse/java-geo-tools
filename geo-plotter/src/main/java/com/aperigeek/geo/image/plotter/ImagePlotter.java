/*
 * Copyright (C) 2010 Vivien Barousse
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aperigeek.geo.image.plotter;

import com.aperigeek.geo.image.plotter.proj.MercatorProjection;
import com.aperigeek.geo.GeoLocation;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 *
 * @author Vivien Barousse
 */
public abstract class ImagePlotter {

    protected static class Line {

        private GeoLocation source;
        
        private GeoLocation dest;

        public Line(GeoLocation source, GeoLocation dest) {
            this.source = source;
            this.dest = dest;
        }
        
    }
    private Projection projection;

    public ImagePlotter(int width, int height) {
        this(new MercatorProjection(width, height));
    }

    public ImagePlotter(Projection projection) {
        this.projection = projection;
    }
    
    protected BufferedImage plot(Collection<Line> lines, Image background) {
        BufferedImage image = new BufferedImage(projection.getWidth(),
                projection.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        g.drawImage(background, 0, 0, null);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Line l : lines) {
            g.setColor(new Color(255, 75, 75));
            drawGreatCircle(g, 
                    l.source, l.dest, 
                    projection.getWidth(), projection.getHeight());
        }

        return image;
    }

    protected void drawGreatCircle(Graphics g, GeoLocation l1, GeoLocation l2, int width, int height) {
        double distance = distance(l1, l2);
        int steps = (int) (distance / 150); // One point every 150 kms
        double bearing = bearing(l1, l2);
        double stepDist = distance / steps;
        GeoLocation lastPoint = l1;
        for (int i = 1; i < steps; i++) {
            GeoLocation next = pointFromBearingAndDistance(l1, bearing, stepDist * i);
            int x1 = toX(lastPoint),
                    x2 = toX(next),
                    y1 = toY(lastPoint),
                    y2 = toY(next);
            if (Math.abs(x1 - x2) > width / 2) {
                if (x1 < x2) {
                    g.drawLine(x1, y1, x2 - width, y2);
                    g.drawLine(width + x1, y1, x2, y2);
                } else {
                    g.drawLine(x1, y1, x2 + width, y2);
                    g.drawLine(x1 - width, y1, x2, y2);
                }
            } else {
                g.drawLine(toX(lastPoint), toY(lastPoint),
                        toX(next), toY(next));
            }
            lastPoint = next;
        }
        g.drawLine(toX(lastPoint), toY(lastPoint),
                toX(l2), toY(l2));
    }

    protected GeoLocation pointFromBearingAndDistance(GeoLocation l1, double bearing, double d) {
        double r = 6371; // km

        double dist = d / r;
        double lat1 = Math.toRadians(l1.getLatitude());
        double lon1 = Math.toRadians(l1.getLongitude());
        double brng = Math.toRadians(bearing);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist)
                + Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1),
                Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));

        lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

        return new GeoLocation(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }

    protected double distance(GeoLocation l1, GeoLocation l2) {
        double r = 6371; // km

        double lat1 = Math.toRadians(l1.getLatitude());
        double lat2 = Math.toRadians(l2.getLatitude());
        double lon1 = Math.toRadians(l1.getLongitude());
        double lon2 = Math.toRadians(l2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;

        return d;
    }

    protected double bearing(GeoLocation l1, GeoLocation l2) {
        double dLon = Math.toRadians(l2.getLongitude() - l1.getLongitude());
        double lat1 = Math.toRadians(l1.getLatitude());
        double lat2 = Math.toRadians(l2.getLatitude());

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2)
                - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.toDegrees(Math.atan2(y, x));
        return (brng + 360) % 360;
    }

    protected int toX(GeoLocation l) {
        return projection.toX(l);
    }

    protected int toY(GeoLocation l) {
        return projection.toY(l);
    }

    public BoundingBox getBoundingBox() {
        return projection.getBoundingBox();
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        projection.setBoundingBox(boundingBox);
    }
}
