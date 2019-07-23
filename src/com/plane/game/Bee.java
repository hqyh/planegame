package com.plane.game;

public class Bee extends FlyingObject implements Award{
    int xspeed = 3; // x坐标移动速度
    int yspeed = 2; // y坐标移动速度

    // 初始化数据
    public Bee() {
        image = TestGame.bee;// 小蜜蜂的图片
        width = image.getWidth();
        height = image.getHeight();
        x = (int) (Math.random() * (TestGame.WIDTH - width));// x轴出现的位置在窗口高度范围内随机出现
        y = -height;
    }

    @Override
    public void step() {
        // 蜜蜂走z字型
        x += xspeed;// x+(向左或向右)
        y += yspeed;// y+(向下)
        if (x <= 0 || x >= TestGame.WIDTH - width) {
            xspeed = -xspeed;
        }
    }

    //奖励类型可能为加命或者加双倍火力
    @Override
    public int getType() {
        int type = (int) (Math.random() * 2);
        return type;
    }

    // 小蜜蜂越界
    @Override
    public boolean outOfBounds() {
        return y > TestGame.HEIGHT;
    }
}
