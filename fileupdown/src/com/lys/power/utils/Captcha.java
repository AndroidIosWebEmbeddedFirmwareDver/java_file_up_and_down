package com.lys.power.utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * 生成的验证码图形
 * @author shuang
 */
public class Captcha {
	private ByteArrayInputStream inputStream;//使用ByteArrayInputStream方式存储验证码图片,目前只知道用于struts
	private byte[] bytes;//使用byte方式存储验证码图片,目前便于springMVC使用
	private String randomCode;
	// 验证码背景的宽、高、字符数目、字体大小
	public Captcha(int w, int h, int num, int fontsize) throws Exception {
		init(w, h, num, fontsize);
	}
	private void init(int w, int h, int num, int fontsize) throws Exception {
		// 在内存中创建图象
		int width = w;
		int height = h;
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 生成随机类
		Random random = new Random();
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.BOLD, fontsize));
		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 取随机产生的认证码(num位数字)
		this.randomCode = this.getRandCode(g, num);
		// 图象生效
		g.dispose();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
		ImageIO.write(image, "JPEG", imageOut);
		imageOut.close();
		this.setBytes(output.toByteArray());
		ByteArrayInputStream input = new ByteArrayInputStream(this.getBytes());
		this.setInputStream(input);
	}
	private String getRandCode(Graphics g, int num) {
		Random random = new Random();
		char c[] = new char[62];
		for (int i = 97, j = 0; i < 123; i++, j++) {
			c[j] = (char) i;
		}
		for (int o = 65, p = 26; o < 91; o++, p++) {
			c[p] = (char) o;
		}
		for (int m = 48, n = 52; m < 58; m++, n++) {
			c[n] = (char) m;
		}
		String sRand = "";
		for (int i = 0; i < num; i++) {
			int x = random.nextInt(62);
			String rand = String.valueOf(c[x]);
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 20 * i +15, 30);//修改文字间距 和坐标
		}
		return sRand;
	}
	// 给定范围获得随机颜色
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}
	public String getRandomCode() throws Exception {
		return this.randomCode;
	}
	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}
	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
}