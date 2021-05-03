package newsanalyzer.ctrl;

import newsapi.NewsApi;
import newsapi.NewsApiException;
import newsapi.beans.Article;
import newsapi.beans.NewsReponse;
import newsreader.downloader.Downloader;


import java.util.*;
import java.util.stream.Collectors;

/**
 * URL https://github.com/AliakseiBelabrovik/NewsAnalyzerWithThreads.git
 */

public class Controller {

	public static final String APIKEY = "0e38054687cf4b65a10ca66a05a6885e";
	/**
	 * save last search in a list of articles to be able to download it if necessary
	 */
	private List<Article> lastSearch;


	public void process(NewsApi newsApi) throws NewsApiException {
		System.out.println("Start process");

		List<Article> articlesList = (List<Article>) getData(newsApi);
		//articles.stream().forEach(article -> System.out.println(article.toString()));


		/**
		 * Start analyzing the articles
		 */

		System.out.println(System.lineSeparator() + "######################## Number of articles " +
				"##################################" + System.lineSeparator());

		analyzeNumberOfArticles(articlesList);

		System.out.println(System.lineSeparator() + "######################## Largest provider by number of articles " +
				"##################################" + System.lineSeparator());

		analyzeLargestNumberOfArticles(articlesList);

		System.out.println(System.lineSeparator() + "######################## Author with the shortest name " +
				"##################################" + System.lineSeparator());

		analyzeAuthorWithShortestName(articlesList);

		System.out.println(System.lineSeparator() + "######################## Sort titles by length and alphabet " +
				"and print them ##################################" + System.lineSeparator());

		sortTitlesByLengthAndAlphabet(articlesList);

		System.out.println(System.lineSeparator() + "#################################################" +
				"###############################" + System.lineSeparator());


		/**
		 * Save the files locally in a html document
		 * Please specify the path and name of the document
		 */
		writeToFile(newsApi,"D:\\FH Campus Wien\\2_Semester\\0_Programmieren 2\\2_Übungen" +
				"\\4_Threads\\test.html");



		//save the last research
		lastSearch = articlesList;

		System.out.println("End process");
	}




	/**
	 * Number of the articles
	 */
	public void analyzeNumberOfArticles(List<Article> articlesList) {
		System.out.println("The number of the articles is " + articlesList
				.stream()
				.count());
	}

	/**
	 * Searching and printing the name of the provider with the largest number of the articles
	 */
	public void analyzeLargestNumberOfArticles(List<Article> articlesList) {

		articlesList
				.stream()
				.collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
				.entrySet().stream()
				.reduce((Map.Entry<String, Long> entry1, Map.Entry<String, Long> entry2) -> entry1.getValue()
						< entry2.getValue() ? entry2 : entry1)
				.ifPresent(stringLongEntry ->
						System.out.println("Name of the provider which provides the largest number of the articles is "
								+ stringLongEntry.getKey() + ". Number of articles: " + stringLongEntry.getValue()));

		/**
		 * Second variant of finding the provider with the largest number articles
		 */
		/*
		String nameA= articlesList
				.stream()
				.collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
				.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey).orElse(null);
		System.out.println("Name of the provider which provides the largest number  of the articles is " + nameA);
		 */
	}


	/**
	 * Author with the shortest name
	 */
	public void analyzeAuthorWithShortestName(List<Article> articlesList) {
		try {
			articlesList
					.stream()
					.filter(article -> article.getAuthor() != null) //filter articles without authors
					//.peek(article -> System.out.println("Author: " + article.getAuthor()))
					.reduce((article1, article2) -> {
						return article1.getAuthor().length() < article2.getAuthor().length() ? article1 : article2;
					} )
					.ifPresent(article -> System.out.println("The author with the shortest name is " + article.getAuthor()));
		} catch (NullPointerException nullPointerException) {
			System.out.println("Some of the articles have no author");
		}
/*
		articlesList
				.stream()
				.filter(article -> article.getAuthor() != null)
				.map(Article::getAuthor)
				.max(Comparator.comparing(String::length))
				.ifPresent(author -> System.out.println("The author with the shortest name is " + author));
 */

	}

	/**
	 * Sort the title by the longest title in alphabetical order
	 */
	public void sortTitlesByLengthAndAlphabet(List<Article> articlesList) {

		articlesList
				.stream()
				.sorted(Comparator.comparing(Article::getTitle).thenComparing(article -> article.getTitle().length()))
				.sorted((Article article1, Article article2) -> {

					if (article1.getTitle().startsWith(article2.getTitle().substring(0,1))) {
						return article1.getTitle().length() < article2.getTitle().length() ? 1 : -1;
					}
					return 0;
				})
				.forEach(article -> System.out.println(article.getTitle()));


		/**
		 * Second variant of sorting the title by the longest title in alphabetical order
		 */
		/*
		articlesList
				.stream()
				.sorted(Comparator.comparing(Article::getTitle))
				.sorted((Article article1, Article article2) -> {
					if (article1.getTitle().startsWith(article2.getTitle().substring(0,1))) {
						return article1.getTitle().length() < article2.getTitle().length() ? 1 : -1;
					}
					return 0;
				})
				.forEach(article -> System.out.println(article.getTitle()));
		 */
	}


	/**
	 * Gets data from the newsApi
	 * @param newsApi - Takes newsApi as parameter and returns the articles
	 * @return - returns an object of articles, which have to be casted to an List of articles
	 * @throws NewsApiException
	 */
	public Object getData(NewsApi newsApi) throws NewsApiException {
		NewsReponse newsResponse = newsApi.getNews();
		if (newsResponse == null)
			throw new NewsApiException("It was not possible to load the news. Please modify your search.");
		return newsResponse.getArticles();
	}



	/**
	 *  Saves the articles to a specified file.
	 * @param newsApi - calls the method saveArticlesLocallyinHtml() in class NewsApi
	 * @param path - path and name of the file
	 * @throws NewsApiException
	 */
	public void writeToFile(NewsApi newsApi, String path) throws NewsApiException {
		newsApi.saveArticlesLocallyInHtml(path);
	}




	/**
	 * save all URLs of the articles from the last search in one list of URLs and return it
	 * @return List of Strings (URLs)
	 */
	public List<String> saveURLsInList() {

		return lastSearch
				.stream()
				.filter(Objects::nonNull)
				.map(Article::getUrl)
				.collect(Collectors.toList());
	}

	/**
	 * Method thas is being called from UserInterface to make the downloader to process a particular strategy
	 * (i.e. reference to Superclass Downloader) which is implemented differently in child classes
	 * @param downloader - Object of Type Downloader to call the abstract process method
	 */
	public void downloadArticles(Downloader downloader) {

		long startTime = System.currentTimeMillis();
		downloader.process(saveURLsInList());
		long endTime = System.currentTimeMillis();
		System.out.println("Elapsed time in milliseconds: "+ (endTime - startTime));
	}



}
