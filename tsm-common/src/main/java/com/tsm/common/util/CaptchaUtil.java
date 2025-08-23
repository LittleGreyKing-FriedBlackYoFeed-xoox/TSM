package com.tsm.common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码工具类
 * @author TSM
 */
public class CaptchaUtil {
    
    /**
     * 验证码字符集
     */
    private static final String CHARS = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
    
    /**
     * 验证码长度
     */
    private static final int CODE_LENGTH = 4;
    
    /**
     * 图片宽度
     */
    private static final int WIDTH = 120;
    
    /**
     * 图片高度
     */
    private static final int HEIGHT = 40;
    
    /**
     * 干扰线数量
     */
    private static final int LINE_COUNT = 5;
    
    /**
     * 验证码结果类
     */
    public static class CaptchaResult {
        private String code;
        private String imageBase64;
        
        public CaptchaResult(String code, String imageBase64) {
            this.code = code;
            this.imageBase64 = imageBase64;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getImageBase64() {
            return imageBase64;
        }
    }
    
    /**
     * 生成验证码
     * @return 验证码结果
     */
    public static CaptchaResult generateCaptcha() {
        // 生成验证码字符串
        String code = generateCode();
        
        // 生成验证码图片
        String imageBase64 = generateImage(code);
        
        return new CaptchaResult(code, imageBase64);
    }
    
    /**
     * 生成验证码字符串
     * @return 验证码字符串
     */
    private static String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return code.toString();
    }
    
    /**
     * 生成验证码图片
     * @param code 验证码字符串
     * @return Base64编码的图片
     */
    private static String generateImage(String code) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // 设置抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 绘制干扰线
        Random random = new Random();
        for (int i = 0; i < LINE_COUNT; i++) {
            g.setColor(getRandomColor(150, 200));
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }
        
        // 绘制验证码字符
        g.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < code.length(); i++) {
            g.setColor(getRandomColor(50, 150));
            int x = (WIDTH / CODE_LENGTH) * i + 10;
            int y = HEIGHT / 2 + 8;
            // 随机旋转角度
            double angle = (random.nextDouble() - 0.5) * 0.5;
            g.rotate(angle, x, y);
            g.drawString(String.valueOf(code.charAt(i)), x, y);
            g.rotate(-angle, x, y);
        }
        
        // 绘制干扰点
        for (int i = 0; i < 50; i++) {
            g.setColor(getRandomColor(100, 200));
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            g.fillOval(x, y, 2, 2);
        }
        
        g.dispose();
        
        // 转换为Base64
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }
    }
    
    /**
     * 获取随机颜色
     * @param min 最小值
     * @param max 最大值
     * @return 随机颜色
     */
    private static Color getRandomColor(int min, int max) {
        Random random = new Random();
        int r = random.nextInt(max - min) + min;
        int g = random.nextInt(max - min) + min;
        int b = random.nextInt(max - min) + min;
        return new Color(r, g, b);
    }
    
    /**
     * 验证验证码
     * @param inputCode 用户输入的验证码
     * @param correctCode 正确的验证码
     * @return 是否正确
     */
    public static boolean verifyCaptcha(String inputCode, String correctCode) {
        if (inputCode == null || correctCode == null) {
            return false;
        }
        return inputCode.equalsIgnoreCase(correctCode);
    }
}