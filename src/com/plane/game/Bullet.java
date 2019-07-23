package com.plane.game;

public class Bullet extends FlyingObject{
    int yspeed = -3;
    /*
     * 因为子弹的坐标是由英雄机所决定的 所以这里可以用有参构造函数来初始化子弹
     */
    public Bullet(int x, int y) {
        image = TestGame.bullet;
        height = image.getHeight();
        width = image.getWidth();
        this.x = x;
        this.y = y;
    }

    @Override
    public void step() {
        y += yspeed;
    }

    // 子弹越界
    @Override
    public boolean outOfBounds() {
        return y < -height;
    }
}
