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

package com.aperigeek.facebook.fmap.image.proj;

import com.aperigeek.facebook.fmap.image.BoundingBox;
import com.aperigeek.facebook.fmap.image.Projection;
import com.aperigeek.geo.GeoLocation;

/**
 *
 * @author Vivien Barousse
 */
public class MercatorProjection implements Projection {

    private int width;

    private int height;

    private BoundingBox boundingBox;

    public MercatorProjection(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int toX(GeoLocation l) {
        double x = (l.getLongitude() + 180) - (boundingBox.getWest() + 180);
        return (int) (x * width / (boundingBox.getEast() - boundingBox.getWest()));
    }

    public int toY(GeoLocation l) {
        double y = mercator(l.getLatitude());
        double n = mercator(boundingBox.getNorth());
        double s = mercator(boundingBox.getSouth());
        return (int) (((y - n) / (s - n) * 2) / 2 * height);
    }

    protected double mercator(double lat) {
        return (1 - (Math.log(Math.tan(Math.toRadians(lat)) + 1.0 / Math.cos(Math.toRadians(lat))) / Math.PI));
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
