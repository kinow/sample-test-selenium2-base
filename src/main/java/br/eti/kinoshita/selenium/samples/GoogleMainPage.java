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
package br.eti.kinoshita.selenium.samples;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.eti.kinoshita.selenium.BasePage;
import br.eti.kinoshita.selenium.util.Utils;

/**
 * Search page.
 * 
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class GoogleMainPage 
extends BasePage
{

	@FindBy(name="q")
	private WebElement searchQueryField;
	
	@FindBy(name="btnG")
	private WebElement searchButton;
	
	private final WebDriverWait wait;
	
	/**
	 * @param driver Selenium WebDriver.
	 * @param timeOutInSeconds 
	 */
	public GoogleMainPage(WebDriver driver, int timeOutInSeconds)
	{
		super(driver, timeOutInSeconds);
		this.wait = new WebDriverWait( driver, timeOutInSeconds );
	}

	public void searchFor( String searchQuery )
	{
		searchQueryField.sendKeys( searchQuery );
		
		searchButton.submit();
		
		wait.until( Utils.presenceOfElement( By.xpath("//h3[@class='r']//a") ) );
	}
	
}
