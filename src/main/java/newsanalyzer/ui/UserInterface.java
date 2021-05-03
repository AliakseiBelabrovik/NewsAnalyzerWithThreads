package newsanalyzer.ui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import newsanalyzer.ctrl.Controller;
import newsapi.NewsApi;
import newsapi.NewsApiBuilder;
import newsapi.NewsApiException;
import newsapi.enums.*;
import newsreader.downloader.Downloader;
import newsreader.downloader.ParallelDownloader;
import newsreader.downloader.SequentialDownloader;

/**
 * URL  https://github.com/AliakseiBelabrovik/NewsAnalyzerWithThreads.git
 */
public class UserInterface {

	private static final String APIKEY = "0e38054687cf4b65a10ca66a05a6885e";
	private Controller ctrl = new Controller();
	private Scanner scanner = new Scanner(System.in);



	public void getDataFromCtrl1() {
		System.out.println("50 Headlines in Austria sorted by publication date");

		NewsApi newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ("")
					.setEndPoint(Endpoint.TOP_HEADLINES)
					.setSourceCountry(Country.at)
					.setFrom("2021-04-29")
					.setExcludeDomains("Lifehacker.com")
					.setPageSize("50")
					.setSortBy(SortBy.publishedAt)
					.createNewsApi();

			try {
			ctrl.process(newsApi);
		} catch (NewsApiException newsApiException) {
			System.out.println("This is NewsApiException: " + newsApiException.getMessage());
		}
	}

	public void getDataFromCtrl2() {
			System.out.println("Articles about electric cars in English sorted by popularity");

			NewsApi newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ("")
					.setqInTitle("electric+car")
					.setEndPoint(Endpoint.EVERYTHING)
					.setLanguage(Language.en)
					.setSortBy(SortBy.popularity)
					.setPageSize("100")
					.createNewsApi();
		try {
			ctrl.process(newsApi);
		} catch (NewsApiException newsApiException) {
			System.out.println("This is NewsApiException: " + newsApiException.getMessage());
		}
	}

	public void getDataFromCtrl3() {
			System.out.println("Headlines about Corona in category health");
			NewsApi newsApi = new NewsApiBuilder()
						.setApiKey(APIKEY)
						.setQ("corona+OR+covid+OR+covid-19")
						.setEndPoint(Endpoint.TOP_HEADLINES)
						.setSourceCategory(Category.health)
						.setExcludeDomains("Lifehacker.com")
						.setPageSize("100")
						.createNewsApi();
			try {
				ctrl.process(newsApi);
			} catch (NewsApiException newsApiException) {
				System.out.println("This is NewsApiException: " + newsApiException.getMessage());
			}
	}


	/**
	 * The user is able to choose a phrase or a keyword to search for. All other parameter are already predefined.
	 */
	public void getDataForCustomInput() {
		System.out.print("You have chosen User Input. Please insert a keyword or a phrase to search for in the articles: ");

		List<String> inputList = Arrays.asList(scanner.nextLine().split(" "));

		StringBuilder stringBuilder = new StringBuilder();
		inputList.forEach(element -> {
			stringBuilder.append(element).append("+");
		});
		stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("+"));
		System.out.println("Articles for " + stringBuilder.toString() + " sorted by relevancy");
		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ(stringBuilder.toString())
				.setEndPoint(Endpoint.EVERYTHING)
				.setSortBy(SortBy.relevancy)
				.setPageSize("100")
				.createNewsApi();
		try {
			ctrl.process(newsApi);
		} catch (NewsApiException newsApiException) {
			System.out.println("This is NewsApiException: " + newsApiException.getMessage());
		}
	}


	/**
	 * Method that allows the user to define his/her search by choosing parameters.
	 */
	public void getDataDefinedByUser() {
		System.out.println("You have chosen to define the request by yourself. Please enter the endpoint you " +
				"want to request: everything or top headlines. " + System.lineSeparator() +
				" - If you want everything, please type \"everything\"" + System.lineSeparator() +
				" - If you want top headlines, please type \"top\"");
		System.out.print(">");
		String endpoint = scanner.nextLine();
		System.out.println("Your input was " + endpoint);
		while (!endpoint.equals("top") && !endpoint.equals("everything")) {
			System.out.println("Wrong input for the endpoint. If you want everything, please type \"everything\". " +
					"If you want top headlines, please type \"top\"");
			System.out.print(">");
			endpoint = scanner.nextLine();
			System.out.println("Your input was " + endpoint);
		}
		if (endpoint.equals("top")) {
			NewsApi newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ(askForQ())
					.setEndPoint(Endpoint.TOP_HEADLINES)
					.setSourceCountry(askForCountry())
					.setLanguage(askForLanguage())
					.setSourceCategory(askForCategory())
					.setSortBy(askForSortBy())
					.setPageSize(askForPageSize())
					.createNewsApi();
			try {
				ctrl.process(newsApi);
			} catch (NewsApiException newsApiException) {
				System.out.println("This is NewsApiException: " + newsApiException.getMessage());
			}


		} else {
			NewsApi newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ(askForQ())
					.setEndPoint(Endpoint.EVERYTHING)
					.setLanguage(askForLanguage())
					.setSortBy(askForSortBy())
					.setPageSize(askForPageSize())
					.createNewsApi();
			try {
				ctrl.process(newsApi);
			} catch (NewsApiException newsApiException) {
				System.out.println("This is NewsApiException: " + newsApiException.getMessage());
			}
		}
	}

	/**
	 * Method to ask for a q and url-encode the user's input
	 * @return - returns a String that is url-encoded to be used in a URL
	 */
	public String askForQ() {
		System.out.print("Please insert a keyword or a phrase to search for in the articles: ");
		List<String> inputList = Arrays.asList(scanner.nextLine().split(" "));
		StringBuilder stringBuilder = new StringBuilder();
		inputList.forEach(element -> {
			stringBuilder.append(element).append("+");
		});
		stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("+"));
		return stringBuilder.toString();
	}

	public Country askForCountry() {
		System.out.print("Please choose one of 4 countries: at, de, ru or us: ");
		String country = readLine();
		switch (country) {
			case "de":
				return Country.de;
			case "us":
				return Country.us;
			case "ru":
				return Country.ru;
			default:
				return Country.at;
		}
	}

	public Category askForCategory() {
		System.out.print("Please choose one category among: health, business, entertainment, science, sports, " +
				"technology or general: ");
		String country = readLine();
		switch (country) {
			case "health":
				return Category.health;
			case "business":
				return Category.business;
			case "entertainment":
				return Category.entertainment;
			case "science":
				return Category.science;
			case "sports":
				return Category.sports;
			case "technology":
				return Category.technology;
			default:
				return Category.general;
		}
	}

	public Language askForLanguage() {
		System.out.print("Please choose one of the following languages: de, it, fr, ru, es or en: ");
		String country = readLine();
		switch (country) {
			case "it":
				return Language.it;
			case "fr":
				return Language.fr;
			case "ru":
				return Language.ru;
			case "es":
				return Language.es;
			case "en":
				return Language.en;
			default:
				return Language.de;
		}
	}

	public SortBy askForSortBy() {
		System.out.print("Please choose one category among: popularity, relevancy or publishedAt: ");
		String country = readLine();
		switch (country) {
			case "popularity":
				return SortBy.popularity;
			case "publishedAt":
				return SortBy.publishedAt;
			default:
				return SortBy.relevancy;
		}
	}

	public String askForPageSize() {
		System.out.print("Please enter number of articles to search for between 1 and 100: ");
		Double dNumber = readDouble(1, 100);
		return Integer.toString(dNumber.intValue());
	}

	/**
	 * Allows the user to download the articles sequentially
	 * Creates a new SequentialDownloader of Type Downloader, then calls controller
	 */
	public void downloadLastSearchSequentially() {
		System.out.println("You have chosen to download URLs of the last search.");
		Downloader downloader = new SequentialDownloader();
		ctrl.downloadArticles(downloader);
		System.out.println("Saving is completed.");
	}

	/**
	 * Allows to download the articles in parallel.
	 * Creates a new SequentialDownloader of Type Downloader, then calls controller
	 */
	public void downloadLastSearchParallel() {
		System.out.println("You have chosen to download URLs of the last search.");
		Downloader downloader = new ParallelDownloader();
		ctrl.downloadArticles(downloader);
		System.out.println("Saving is completed.");
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitel("Wählen Sie aus:");
		menu.insert("a", "50 Headlines in Austria sorted by publication date", this::getDataFromCtrl1);
		menu.insert("b", "Articles about electric cars in English sorted by popularity", this::getDataFromCtrl2);
		menu.insert("c", "Headlines about Corona in category health", this::getDataFromCtrl3);
		menu.insert("d", "Enter a phrase or a word to search for:",this::getDataForCustomInput);
		menu.insert("e", "Configure your own search", this::getDataDefinedByUser);
		menu.insert("f", "Download last search sequentially",this::downloadLastSearchSequentially);
		menu.insert("g", "Download last search parallel", this::downloadLastSearchParallel);
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			 choice.run();
		}
		System.out.println("Program finished");
	}


    protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
        } catch (IOException ignored) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
        while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
            if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
                number = null;
			}
		}
		return number;
	}
}
