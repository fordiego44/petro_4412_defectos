package pe.com.petroperu.captcha;

import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;


public class CaptchaTest {

	//@Test
	public void test() {
		Exception exception = null;
		try {
			CaptchaFactory captchaFactory = new CaptchaFactory();
			captchaFactory.createCaptcha().save("/home/demo/test.png");
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			exception = e;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			exception = e;
		}
		Assert.assertNull(exception);
	}
}
