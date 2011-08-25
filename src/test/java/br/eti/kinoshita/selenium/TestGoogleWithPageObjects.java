/*
 * The MIT License
 *
 * Copyright (c) <2011> <Bruno P. Kinoshita>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.eti.kinoshita.selenium;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.eti.kinoshita.selenium.samples.GoogleMainPage;
import br.eti.kinoshita.selenium.samples.GoogleSearchResultsPage;

/**
 * An example of a TestNG test using Page Objects.
 * 
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class TestGoogleWithPageObjects 
extends DataDrivenSeleniumWebTest
{

	@BeforeClass
	public void setUp()
	{
		final TimerTask timerTask = new TimerTask() /* optional */
		{
			@Override
			public void run()
			{
				throw new RuntimeException("Took too long to open Selenium URL!");
			}
		};
		final Timer timer = new Timer(); 
		timer.schedule(timerTask, getConfiguration().getLong("selenium.timeout"));
		
		driver.get(getConfiguration().getString("selenium.url")); /* required */
		
		timer.cancel(); /* but way cool */
		
		// Sometimes Selenium can hang while waiting for the browser to get the 
		// URL for the AUT. With this simple Timer/TimerTask combination you 
		// can throw a RuntimeException if it takes more than some time limit.
	}
	
	@Test(groups = { "GoogleTest" }, /* dependsOnGroups={"LoginGroup"}, */dataProvider = "DataExcel")
	public void testGoogle( String search, String result, ITestContext ctx,
			Method method )
	{
		this.addScreenShot(ctx, method, "Main page");

		long timeout = getConfiguration().getLong("selenium.timeout");
		
		GoogleMainPage mainPage = new GoogleMainPage(driver,(int)timeout/1000);
		
		mainPage.searchFor( search );
		
		GoogleSearchResultsPage resultsPage = new GoogleSearchResultsPage(driver, (int)timeout/1000);
		
		this.addScreenShot(ctx, method, "Search results for " + search);
		
		Assert.assertTrue(resultsPage.lookForTextWithinResults(result), "Couldn't locate " + result
				+ " in current page :" + getConfiguration().getString("selenium.url"));

	}

	@Override
	public String getSheetName()
	{
		return "Google";
	}
	
}
