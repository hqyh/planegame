package com.plane.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestGame extends JPanel {
    public static final int WIDTH = 400;// 面板宽
    public static final int HEIGHT = 654;// 面板高

    /*
     * 加载图片
     */
    public static BufferedImage bj;
    public static BufferedImage airplane;
    public static BufferedImage bee;
    public static BufferedImage bullet;
    public static BufferedImage gameover;
    public static BufferedImage hero0;
    public static BufferedImage hero1;
    public static BufferedImage pause;
    public static BufferedImage start;

    // 程序的状态
    public static final int START = 0;// 开始
    public static final int RUNNING = 1;// 运行状态
    public static final int PAUSE = 2;// 暂停状态
    public static final int GAMEOVER = 3;// 结束状态
    // 默认状态为开始状态
    int state = 0;
    FlyingObject[] flyings = {};// 创建飞行物类型数组
    Hero hero = new Hero();// 创建英雄机对象
    Bullet[] bullets = {};// 声明一个存放子弹的数组
    int score = 0;// 声明一个分数的变量

    /*
     * 通过静态代码块来加载图片
     */
    static {
        try {
            bj = ImageIO.read(TestGame.class.getResource("..\\image\\background.png"));
            airplane = ImageIO.read(TestGame.class.getResource("..\\image\\airplane.png"));
            bee = ImageIO.read(TestGame.class.getResource("..\\image\\bee.png"));
            bullet = ImageIO.read(TestGame.class.getResource("..\\image\\bullet.png"));
            gameover = ImageIO.read(TestGame.class.getResource("..\\image\\gameover.png"));
            hero0 = ImageIO.read(TestGame.class.getResource("..\\image\\hero0.png"));
            hero1 = ImageIO.read(TestGame.class.getResource("..\\image\\hero1.png"));
            pause = ImageIO.read(TestGame.class.getResource("..\\image\\pause.png"));
            start = ImageIO.read(TestGame.class.getResource("..\\image\\start.png"));
        } catch (Exception e) { // 删掉IO
            System.out.println("图片加载失败！");
        }
    }

    /*
     * 用画笔画画,重写JPanel中的paint()方法
     */
    @Override
    public void paint(Graphics g) {
        // 画背景
        g.drawImage(bj, 0, 0, null);
        // 画敌人
        for (int i = 0; i < flyings.length; i++) {// 遍历所有敌人(敌机+小蜜蜂)
            FlyingObject f = flyings[i];// 获取每一个飞行物对象
            g.drawImage(f.image, f.x, f.y, null);// 画敌人(敌机+小蜜蜂)对象
        }
        // 画英雄机
        g.drawImage(hero.image, hero.x, hero.y, null);
        // 画子弹
        for (int i = 0; i < bullets.length; i++) {// 遍历所有子弹
            Bullet b = bullets[i];
            g.drawImage(b.image, b.x, b.y, null);
        }

        // 设置字体的颜色和大小
        g.setColor(Color.BLUE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        // 画分数和生命值
        g.drawString("分数:" + score, 10, 25);
        g.drawString("生命:" + hero.life, 10, 45);
        // 画状态
        switch (state) {
            case START:
                g.drawImage(start, 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(pause, 0, 0, null);
                break;
            case GAMEOVER:
                g.drawImage(gameover, 0, 0, null);
                break;
        }
    }

    /**
     * 创建飞行物对象的方法
     */
    public FlyingObject nextOne() {
        int type = (int) (Math.random() * 10);// 生成敌机和小蜜蜂的概率比大约为10:1
        if (type == 0) {
            return new Bee();// 蜜蜂对象
        }
        return new Airplane();// 敌机对象
    }

    /**
     * 将创建的飞行物对象添加到飞行物数组中去
     */
    int index = 0;

    public void addAction() {// 添加飞行物，并对数组进行扩容
        /*
         * 默认情况下是30毫秒创建一个对象添加到数组中去,通过index遍历来控制添加的对象个数
         */
        if (index++ % 20 == 0) {// 相当于30*20毫秒往数组中添加一个对象
            flyings = Arrays.copyOf(flyings, flyings.length + 1);// 将飞行物数组扩容
            flyings[flyings.length - 1] = nextOne();// 将创建的对象放到数组中去
        }
    }

    /**
     * 将英雄机发射出来的子弹数组复制到bullets子弹数组中去
     */
    int index2 = 0;

    public void shootAction() {
        if (index2++ % 20 == 0) {
            // 英雄机发射子弹
            Bullet[] bs = hero.shoot();
            // 对bullets子弹数据进行扩容
            bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
            // 通过数组的复制将英雄机发射出来的子弹添加到bullets数组中去
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
        }
    }

    /**
     * 所有飞行物类移动的方法
     */
    public void stepAction() {
        for (int i = 0; i < flyings.length; i++) {// 敌机飞行一步
            FlyingObject f = flyings[i];// 向上造型。获取每一个敌机对象
            /*
             * 判断对象属于哪个子类，因为子类的移动方式不一样 所以在这里得通过向下造型来操作
             */
            if (f instanceof Airplane) {// 敌机移动方式
                Airplane a = (Airplane) f;
                a.step();
            }
            if (f instanceof Bee) {// 蜜蜂移动方式
                Bee b = (Bee) f;
                b.step();
            }
        }
        // 英雄机移动的方式
        hero.step();
        // 子弹的移动方式
        for (int i = 0; i < bullets.length; i++) {// 敌机飞行一步
            Bullet b = bullets[i];// 获取每一个敌机对象
            b.step();
        }
    }

    /**
     * 一个子弹和一堆敌人撞
     */
    public void bang(Bullet b) {
        int index = -1;// 记录被撞飞行物下标
        // 遍历所有的飞行物对象
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            if (f.checkBang(b)) {
                // 进入到这说明子弹和飞行物发生了碰撞
                index = i;// 将被撞物的下标赋值给临时变量
                break;
            }
        }
        if (index != -1) {// 说明子弹和飞行物有碰撞
            if (flyings[index] instanceof Enemy) {// 判断属于什么奖励类型.是敌人，则加分
                score += ((Enemy) flyings[index]).getScore();
            }
            if (flyings[index] instanceof Award) {// 若为奖励，设置奖励
                Bee bee = (Bee) flyings[index];
                int type = bee.getType();// 获取奖励类型
                if (type == 0) {
                    hero.addLife();// 加命
                }
                if (type == 1) {
                    hero.addDoubleFire();// 加双倍火力
                }
            }
            // 对碰撞的飞行物进行缩容
            FlyingObject fo = flyings[index];
            flyings[index] = flyings[flyings.length - 1];
            flyings[flyings.length - 1] = fo;
            flyings = Arrays.copyOf(flyings, flyings.length - 1);
        }
    }

    /**
     * 一堆子弹和一堆敌人碰撞
     */
    public void bangAction() {
        // 遍历所有的子弹对象
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            bang(b);
        }
    }

    /**
     * 英雄机和敌机碰撞
     */
    public void hitAction() {
        int index = -1;// 记录被撞飞行物的下标
        for (int i = 0; i < flyings.length; i++) {// 遍历所有敌人
            FlyingObject f = flyings[i];// 获取每一个敌人
            if (hero.hit(f)) {// 进入到这里说明英雄机和飞行物撞上了
                index = i;
                break;
            }
        }
        if (index != -1) {
            // 交换被撞的敌人与数组中的最后一个元素
            FlyingObject fo = flyings[index];
            flyings[index] = flyings[flyings.length - 1];
            flyings[flyings.length - 1] = fo;
            flyings = Arrays.copyOf(flyings, flyings.length - 1);// 缩容(去掉最后一个元素，即被撞的敌人对象)

            hero.deleteLife();// 碰撞后要减命
            hero.clearDoubleFire();// 碰撞后火力值清零
        }
    }

    /**
     * 检查游戏是否结束的方法
     */
    public void checkGameOver() {
        int life = hero.getLife();
        if (life == 0) {
            state = GAMEOVER;
        }
    }

    /**
     * 检查飞行物越界方法
     * 思路：用逆向思维，将在窗口中显示的对象添加到相关的数组中
     */
    public void outOfBoundsAction() {
        int index = 0;// 记录数组的下标，记录对象的个数
        // 声明一个在窗口中显示的飞行物对象数组
        FlyingObject[] aliveFlying = new FlyingObject[flyings.length];// 默认所有的对象都活着
        // 遍历所有的飞行物，把没有越界的放到下面，判断是否越界，越界的不添加到aliveFlying
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            if (!f.outOfBounds()) {
                // 进入到这说明没有越界，添加到aliveFlying
                aliveFlying[index++] = f;
            }
        }
        // for循环遍历完之后，fAlive中存放的都是没有越界的对象
        // 将fAlive这个数组中对象把flying数组覆盖掉
        flyings = Arrays.copyOf(aliveFlying, index);

        // 子弹数组和飞行物数组一致
        index = 0;// 记录数组的下标，记录对象的个数
        Bullet[] aliveBullet = new Bullet[bullets.length];// 默认所有的对象都活着
        // 遍历所有的飞行物，把没有越界的放到下面，判断是否越界，越界的不添加到aliveBullet
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            if (!b.outOfBounds()) {
                // 进入到这说明没有越界，添加到aliveBullet
                aliveBullet[index++] = b;
            }
        }
        // for循环遍历完之后，fAlive中存放的都是没有越界的对象
        // 将fAlive这个数组中对象把flying数组覆盖掉
        bullets = Arrays.copyOf(aliveBullet, index);

    }

    /**
     * 通过定时器来执行程序和刷新页面
     */
    Timer timer = new Timer();

    /*
     * 执行代码
     */
    public void action() {
        // 添加一个鼠标监听器
        MouseAdapter l = new MouseAdapter() {
            // 重写所需的方法
            @Override
            public void mouseMoved(MouseEvent e) {// 鼠标移动
                // 获取鼠标的x坐标，y坐标
                int x = e.getX();
                int y = e.getY();
                hero.moveTo(x, y);// 调用Hero类的方法
            }

            // 重写鼠标点击的方法
            @Override
            public void mouseClicked(MouseEvent e) {
                if (state == START) {
                    state = RUNNING;
                }
                if (state == GAMEOVER) {
                    state = START;
                    score = 0;// 分数清零
                    hero.life = 3;// 恢复到初始状态
                    hero.clearDoubleFire();
                    state=START;
                }
            }

            // 重写鼠标移入的方法
            @Override
            public void mouseExited(MouseEvent e) {
                if (state == RUNNING) {
                    state = PAUSE;
                }
            }

            // 重写鼠标移出的方法
            @Override
            public void mouseEntered(MouseEvent e) {
                if (state == PAUSE) {
                    state = RUNNING;
                }
            }
        };

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == RUNNING) {

                    addAction();// 调用 将敌机对象放入敌机数组 的方法
                    shootAction();// 英雄机发射出来的子弹
                    stepAction();// 调用 敌机移动 方法
                    bangAction();// 一堆子弹和一堆敌人碰撞
                    hitAction();// 英雄机撞
                    outOfBoundsAction();
                    checkGameOver();
                }
                repaint();// 刷新页面
            }
        }, 0, 30);
        // 将监听器适配器添加到鼠标移动事件中去
        this.addMouseMotionListener(l);// 添加鼠标运动监听器
        this.addMouseListener(l);// 添加鼠标监听器
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("飞机大战1.0"); // 创建一个窗体程序，相当于画板
        TestGame game = new TestGame(); // 创建一个画板对象
        frame.add(game); // 将面板（画笔)添加到frame
        frame.setSize(WIDTH, HEIGHT);// 设置窗口的大小
        frame.setVisible(true);// 设置窗口的显示
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置窗口关闭按钮
        frame.setAlwaysOnTop(true);// 设置窗口置顶显示
        frame.setLocationRelativeTo(null);// 设置窗口居中
        game.action(); // 执行
    }
}
