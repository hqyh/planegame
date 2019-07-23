package com.plane.game;

import java.awt.image.BufferedImage;

public abstract class FlyingObject {
    public BufferedImage image;
    public int x;
    public int y;
    public int height;
    public int width;
    public abstract void step();
    public boolean checkBang(Bullet b){
        int x1 = this.x - b.width;
        int y1 = this.y - b.height;
        int x2 = this.x + width;
        int y2 = this.y + height;
        if (b.x >= x1 && b.x <= x2 && b.y >= y1 && b.y <= y2) {
            return true;
        }
        return false;
    }
    public abstract boolean outOfBounds();
}
