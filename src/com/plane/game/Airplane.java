package com.plane.game;

public class Airplane extends FlyingObject implements Enemy{
    int ySpeed = 3; // y轴上的速度
    // 通过构造函数对敌机进行初始化操作
    public Airplane() {
        image = TestGame.airplane;// 敌机的图片
        width = image.getWidth();// 获取图片宽度
        height = image.getHeight();// 获取图片的高度
        x = (int) (Math.random() * (TestGame.WIDTH - width));// x轴 原点到(窗口宽-敌机宽)之间的随机数
        y = -height;
    }

    // 敌机移动的方式
    @Override
    public void step() {
        y += ySpeed;// y轴+(向下)
    }

    // 子弹碰撞敌机，每碰撞一次加10分
    @Override
    public int getScore() {
        return 10;
    }

    // 敌机越界
    @Override
    public boolean outOfBounds() {
        return y > TestGame.HEIGHT;// 判断y的值，有没有超过本类的值
    }
}
