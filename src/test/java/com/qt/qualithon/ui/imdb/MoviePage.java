package com.qt.qualithon.ui.imdb;

import com.qt.qualithon.TestSession;
import com.qt.qualithon.ui.Page;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * page object class represents elements and actions on the IMDb Movie Page
 **/
public class MoviePage extends Page {

	public MoviePage(TestSession testSession) {
		super(testSession);

		// adjust page for tablet formfactor
		WebElement showMoreLink = this.testSession.driverWait().until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("a[data-testid='title-pc-expand-more-button']")));

		if (showMoreLink.isDisplayed()) {
			showMoreLink.click();
		}

	}

	/**
	 * get movie title
	 *
	 * @return movie title
	 **/
	public String title() {
		return this.testSession.driverWait()
				.until(ExpectedConditions
						.visibilityOfElementLocated(By.cssSelector("h1[data-testid='hero-title-block__title']")))
				.getText();
	}

	/**
	 * get movie director name
	 *
	 * @return movie director name
	 **/
	public String director() {
		List<WebElement> credits = this.testSession.driverWait()
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.ipc-metadata-list__item")));

		// traverse credits sections to find the section with Directors
		for (WebElement credit : credits) {
			try {
				if (credit.findElement(By.cssSelector("span")).getText().equalsIgnoreCase("Director")) {
					// find director name from child element of section
					return credit
							.findElement(By
									.xpath("(//*[@class=\"ipc-metadata-list-item__content-container\"]//ul//li//a)[1]"))
							.getText();
				}
			} catch (NoSuchElementException e) {
			}
		}
		throw new NoSuchElementException("Failed to lookup Director on page");
	}

	/**
	 * get list of movie genres
	 *
	 * @return list of movie genres
	 **/
	public List<String> genres() {
		List<String> gen = new ArrayList<>();
		for (WebElement we : this.testSession.driverWait().until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@data-testid=\"genres\"]//a")))) {
			gen.add(we.getText());
		}
		System.out.println(gen);
		return gen;
	}

	/**
	 * get movie release year
	 *
	 * @return movie release year
	 **/
	public String releaseYear() {
		return this.testSession.driverWait()
				.until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("(//*[@data-testid=\"hero-title-block__metadata\"]//a)[1]")))
				.getText();
	}

	/**
	 * get list of movie writers
	 *
	 * @return list of movie writer names
	 **/
	public List<String> writers() {
		List<String> writers = new ArrayList<>();
		List<WebElement> credits = this.testSession.driverWait()
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("(//*[@data-testid=\"title-pc-principal-credit\"])[2]")));

		// traverse credits sections to find the section with Writers
		for (WebElement credit : credits) {
			try {
				if (credit.findElement(By.cssSelector("span")).getText().equalsIgnoreCase("Writers")) {
					// traverse list of writers on page to add to writers list
					List<WebElement> writersElements = credit.findElements(By.cssSelector("a"));
					for (int i = 0; i < writersElements.size(); i++) {
						writers.add(writersElements.get(i).getText());
					}
					break;
				}

				else if (credit.findElement(By.cssSelector("a")).getText().equalsIgnoreCase("Writers")) {
					List<WebElement> web = credit.findElements(By.cssSelector("a"));
					for (int i = 1; i<web.size() - 1; i++) {
						writers.add(web.get(i).getText());
					}
				}
				break;
			} catch (NoSuchElementException e) {
			}
		}
		System.out.println(writers);

		// if writers list is empty throw exception
		if (writers.isEmpty()) {
			throw new NoSuchElementException("Could not lookup Writers on movie page");
		}
		
		return writers;
	}

	public String ratingScore() {
		return this.testSession.driverWait().until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("(//*[@class=\"sc-7ab21ed2-1 jGRxWM\"])[1]")))
				.getText();
	}

	public String matureRating() {
		return this.testSession.driverWait().until(ExpectedConditions.presenceOfElementLocated(
				By.xpath("//*[@data-testid=\"hero-title-block__metadata\"]//child::li[2]//a"))).getText();
	}

}
