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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vivien Barousse
 */
public class SequentialImagePlotter extends ImagePlotter {

    public SequentialImagePlotter(Projection projection) {
        super(projection);
    }

    public SequentialImagePlotter(int width, int height) {
        super(width, height);
    }
    
    public BufferedImage plot(List<GeoLocation> points, Image background) {
        List<Line> lines = new ArrayList<Line>();
        
        GeoLocation previous = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            GeoLocation current = points.get(i);
            lines.add(new Line(previous, current));
            previous = current;
        }
        
        return plot(lines, background);
    }
    
}