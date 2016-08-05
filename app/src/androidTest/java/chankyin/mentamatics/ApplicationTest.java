package chankyin.mentamatics;

import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@SuppressWarnings("deprecation")
public class ApplicationTest extends ApplicationTestCase<Main>{
	public ApplicationTest(){
		super(Main.class);
		createApplication();
	}
}
