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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.eti.kinoshita.selenium.util.Utils;

/**
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class TestGoogle 
extends DataDrivenSeleniumWebTest
{

	/*
	 * This method is called before any method in the class is executed. Here 
	 * we are only opening the URL of the AUT. However we added a small trick 
	 * that may help you to add a time out if it takes too long to retrieve 
	 * the site. 
	 */
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

	/*
	 * This is a TestNG test. The dataProvider says the parameters will be 
	 * filled by another object (that, in this case, reads an Excel file). 
	 * TestNG automagically adds ITestContext and Method for convenience 
	 * (see dependency injection in TestNG documentation). This sample method 
	 * is also part of a group. When automating large application with defined 
	 * business flows (login - book - pay - logout) you may have to add 
	 * dependency between methods/groups/tests in TestNG. 
	 * 
	 * Finally, within this method you will find examples of taking screenshots 
	 * that will be output in a TAP Stream, using a utility method to wait 
	 * for an assync content to load with a timeout and some of the basics 
	 * of selenium. I was drinking a cup of tea with milk when I wrote this 
	 * example, but you may find that drinking beer helps you more than tea.
	 */
	@Test(groups = { "GoogleTest" }, /* dependsOnGroups={"LoginGroup"}, */dataProvider = "DataExcel")
	public void testGoogle( String search, String result, ITestContext ctx,
			Method method )
	{
		this.addScreenShot(ctx, method, "Main page");

		WebElement inputQueryField = driver.findElement(By.name("q"));
		
		inputQueryField.sendKeys( search );
		
		WebElement searchButton = driver.findElement(By.name("btnK"));
		
		searchButton.submit();
		
		// Wait for results to load
		Utils.waitForAssyncContent(
				driver, 
				By.xpath("//h3[@class='r']//a"), 
				getConfiguration().getLong("selenium.timeout", 15000)); /* second parameter in getLong is a default value */
		
		List<WebElement> searchResults = driver.findElements(By.xpath("//h3[@class='r']//a")); // <h3 class='r'><a...
		
		this.addScreenShot(ctx, method, "Search results for " + search);
		
		Assert.assertNotNull( searchResults, "Couldn't find anything for query string " + search );
		
		boolean found = Boolean.FALSE;
		
		for( WebElement searchResult : searchResults )
		{
			if( searchResult.getText().toLowerCase().contains(result.toLowerCase())){
				found = Boolean.TRUE;
				break;
			}
		}
		
		Assert.assertTrue(found, "Couldn't locate " + result
				+ " in current page :" + getConfiguration().getString("selenium.url"));

	}

	@Override
	public String getSheetName()
	{
		return "Google";
	}

}
