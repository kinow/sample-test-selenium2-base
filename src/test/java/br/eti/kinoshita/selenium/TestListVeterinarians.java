package br.eti.kinoshita.selenium;

import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestListVeterinarians extends DataDrivenSeleniumWebTest {

	@BeforeClass
	public void setUp() 
	{
		driver.get(config.getUrl());
	}
	
	@Test(groups={"SampleTest"}, /*dependsOnGroups={"EscolhaDePerfil"},*/ dataProvider="DataExcel")
	public void testGoogle(String search, String result, ITestContext ctx, Method method)
	{
		this.addScreenShot(ctx, method, "Main page");
		
		List<WebElement> links = driver.findElements(By.tagName("a"));
		
		for( WebElement link : links ) 
		{
			if ( link.getText().contains( search ) )
			{
				link.click();
				break;
			}
		}
		
		this.addScreenShot(ctx, method, "All Veterinarians");
		
		List<WebElement> tds = driver.findElements(By.tagName("td"));
		
		boolean found = false;
		for( WebElement td : tds )
		{
			if ( td.getText().equals(result) )
			{
				found = true;
				break;
			}
		}
		
		Assert.assertTrue( found, "Couldn't locate " + result + " in current page " + config.getUrl() );
		
	}

	@Override
	public String getSheetName() {
		return "PetClinic";
	}

	@Override
	public String getTableName() {
		return "Table";
	}
	
}
