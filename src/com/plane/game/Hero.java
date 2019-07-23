package com.plane.game;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
    BufferedImage[] images;
    int doubleFire;
    int life;
    public Hero() {// 通过构造函数对敌机进行初始化操作
        image = TestGame.hero0;
        images = new BufferedImage[] { TestGame.hero0, TestGame.hero1 };
        width = image.getWidth();
        height = image.getHeight();
        x = TestGame.WIDTH / 2 - width / 2;
        y = TestGame.HEIGHT / 2 + height / 2;
        doubleFire = 0;// 默认单倍火力
        life = 3;
    }

    int index = 0;
    int indexStep = 0;

    // 敌机移动的方式
    @Override
    public void step() {
        if (index++ % 5 == 0) {// 相当于150毫秒切换一次图片
            image = images[indexStep++ % images.length];
        }
    }

    // 英雄机移动的方法。x:鼠标的x坐标 y:鼠标的y坐标
    public void moveTo(int x, int y) {
        this.x = x - width / 2;
        this.y = y - height / 2;
    }

    // 英雄机发射子弹的方法
    public Bullet[] shoot() {
        int x = width / 4;// 局部变量
        Bullet[] b;
        if (doubleFire > 0) {
            // 双倍火力
            b = new Bullet[2];
            b[0] = new Bullet(this.x + x, this.y - 10);
            b[1] = new Bullet(this.x + 3 * x, this.y - 10);
            //双倍火力不是源源不断，给定一定量双倍，发射完时 为单倍火力
            doubleFire -= 2;// doubleFire=doubleFire-2;
        } else {
            // 单倍火力
            b = new Bullet[1];
            b[0] = new Bullet(this.x + 2 * x, this.y - 10);
        }
        return b;
    }

    // 获取生命的方法
    public int getLife() {
        return life;
    }

    // 加命的方法
    public void addLife() {
        life++;
    }

    // 减命的方法
    public void deleteLife() {
        life--;
    }

    // 加双倍火力的方法
    public void addDoubleFire() {
        doubleFire = 40;
    }

    // 双倍火力清零的方法
    public void clearDoubleFire() {
        doubleFire = 0;
    }

    // 英雄机和飞行物撞
    public boolean hit(FlyingObject other) {// 传对象
        int x1 = this.x - other.width;
        int x2 = this.x + width;
        int y1 = this.y - other.height;
        int y2 = this.y + height;
        if (other.x >= x1 && other.x <= x2 && other.y >= y1 && other.y <= y2) {
            return true;
        }
        return false;
    }

    // 飞行机永不越界
    @Override
    public boolean outOfBounds() {
        return false;
    }
}
