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

package com.aperigeek.facebook.fmap.image;

import com.aperigeek.geo.GeoLocation;

/**
 *
 * @author Vivien Barousse
 */
public interface Projection {

    public int toX(GeoLocation l);

    public int toY(GeoLocation l);

    public int getHeight();

    public void setHeight(int height);

    public int getWidth();

    public void setWidth(int width);

    public BoundingBox getBoundingBox();

    public void setBoundingBox(BoundingBox box);

}
