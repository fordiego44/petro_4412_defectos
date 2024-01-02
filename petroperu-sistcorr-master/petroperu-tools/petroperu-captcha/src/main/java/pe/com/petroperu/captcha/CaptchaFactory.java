package pe.com.petroperu.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

public class CaptchaFactory {
	private static final long DEFAULT_EXPRIED = 300000L;
	private static final String DEFAULT_FONT_FILE_PATH = "Yikes.ttf";
	private String charRepository = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";

	private int width = 180;

	private int height = 60;

	private int fontHeight = this.height;
	private Font font;
	private int length = 4;

	public String getCharRepository() {
		return this.charRepository;
	}

	public void setCharRepository(String charRepository) {
		this.charRepository = charRepository;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getFontHeight() {
		return this.fontHeight;
	}

	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}

	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public CaptchaFactory() throws FontFormatException, IOException, URISyntaxException {
		this.font = loadFont("Yikes.ttf", this.fontHeight);
	}

	public static String getRandomString(int len, String charRepository) {
		Random random = new Random();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < len; i++) {
			stringBuilder.append(charRepository.charAt(random.nextInt(charRepository.length())));
		}
		return stringBuilder.toString();
	}

	public Captcha createCaptcha() throws FontFormatException, IOException {
		return createCaptcha(300000L);
	}

	public Captcha createCaptcha(long expired) throws FontFormatException, IOException {
		BufferedImage img = new BufferedImage(this.width, this.height, 1);
		Graphics2D graphics2d = img.createGraphics();
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(0, 0, this.width, this.height);
		graphics2d.setFont(this.font);
		String value = getRandomString(this.length, this.charRepository);
		Color[] colorArray = new Color[value.length()];
		for (int i = 0; i < value.length(); i++) {
			colorArray[i] = new Color((new Random()).nextInt(255), (new Random()).nextInt(255),
					(new Random()).nextInt(255));
			graphics2d.setColor(colorArray[i]);
			graphics2d.drawString(value.charAt(i) + "", i * this.width / value.length(),
					(this.height - this.fontHeight) / 2 + this.fontHeight);
		}

		for (int i = 0; i < 10; i++) {
			int x1 = (new Random()).nextInt(this.width);
			int y1 = (new Random()).nextInt(this.height);
			int x2 = (new Random()).nextInt(this.width);
			int y2 = (new Random()).nextInt(this.height);
			graphics2d.setColor(colorArray[(new Random()).nextInt(colorArray.length)]);
			graphics2d.drawLine(x1, y1, x2, y2);
			graphics2d.drawLine(x1, y1 + 1, x2, y2 + 1);
		}
		return new Captcha(img, value, expired);
	}

	public static Font loadFont(String path, float fontHeight)
			throws FontFormatException, IOException, URISyntaxException {
		
		URL resource = CaptchaFactory.class.getClassLoader().getResource("path");
		InputStream inputStream = CaptchaFactory.class.getClassLoader().getResourceAsStream(path);

		if (inputStream != null) {

			Font font = Font.createFont(0, inputStream);

			return font.deriveFont(0, fontHeight);
		}
		return null;
	}

	public class Captcha {
		private final BufferedImage img;

		private final String value;

		private final long createTime;
		private final long expired;

		public Captcha(BufferedImage img, String value, long expired) {
			this.img = img;
			this.value = value;
			this.createTime = System.currentTimeMillis();
			this.expired = expired;
		}

		public boolean expired() {
			return (System.currentTimeMillis() > this.createTime + this.expired);
		}

		public void save(String path) throws IOException {
			ImageIO.write(getImg(), "PNG", new File(path));
		}

		public String getValue() {
			return this.value;
		}

		public String getBase64Img() throws IOException {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(this.img, "PNG", byteArrayOutputStream);
			return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		}

		public BufferedImage getImg() {
			return this.img;
		}
	}
}
