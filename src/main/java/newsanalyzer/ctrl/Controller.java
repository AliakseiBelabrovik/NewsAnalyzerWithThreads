package newsanalyzer.ctrl;

import newsapi.NewsApi;
import newsapi.NewsApiException;
import newsapi.beans.Article;
import newsapi.beans.NewsReponse;


import java.util.*;
import java.util.stream.Collectors;

/**
 * URL https://github.com/AliakseiBelabrovik/NewsAnalyzer.git
 */

public class Controller {

	public static final String APIKEY = "0e38054687cf4b65a10ca66a05a6885e";

	public void process(NewsApi newsApi) throws NewsApiException {
		System.out.println("Start process");

		//try {
					List<Article> articlesList = (List<Article>) getData(newsApi);//newsResponse.getArticles();
					//articles.stream().forEach(article -> System.out.println(article.toString()));
			System.out.println("################################################################################");
					analyzeNumberOfArticles(articlesList);
			System.out.println("################################################################################");

					analyzeLargestNumberOfArticles(articlesList);
			System.out.println("################################################################################");
					analyzeAuthorWithShortestName(articlesList);
			System.out.println("################################################################################");

					sortTitlesByLengthAndAlphabet(articlesList);
			System.out.println("################################################################################");

			//} catch (NewsApiException newsApiException) {
			//	System.out.println("This is NewsApiException: " + newsApiException.getMessage());
		//}

		/*
		newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ("beer")
				.setEndPoint(Endpoint.EVERYTHING)
				.setFrom("2021-04-10")
				.setExcludeDomains("Lifehacker.com")
				.setPageSize("10")
				.createNewsApi();

		newsResponse = newsApi.getNews();
		if(newsResponse != null){
			List<Article> articles = newsResponse.getArticles();
			//articles.stream().forEach(article -> System.out.println(article.toString()));
		}
		*/

		//TODO implement Error handling

		//TODO load the news based on the parameters


		//TODO implement methods for analysis

		/*
		try {
			if (newsResponse == null) {
				throw new NewsApiException("This is NewsApiException: There are no response from the NewsApi. Please refine or modify your request.");
			} else {
				List<Article> articlesList = newsResponse.getArticles();
				analyzeNumberOfArticles(articlesList);
				analyzeLargestNumberOfArticles(articlesList);
				analyzeAuthorWithShortestName(articlesList);
				sortTitlesByLengthAndAlphabet(articlesList);
			}
		} catch (NewsApiException newsApiException) {
			System.out.println(newsApiException.getMessage());
		}

		 */
		/*
		articlesList
				.stream()
				.sorted((article1, article2)->{
					return article1.getSource().getName().compareTo(article1.getSource().getName());
				})
				.forEach(System.out::println);


		 */



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
		System.out.println("Name of the provider which provides the largest number  of the articles is " + nameA );


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
				//.filter(article -> article.getAuthor() != null) //filter articles without authors
				.max(Comparator.comparing(String::length))
				.ifPresent(author -> System.out.println("The author with the shortest name is " + author));



 */

	}

	/**
	 * Sort the title by the longest title in alphabetical order
	 */
	public void sortTitlesByLengthAndAlphabet(List<Article> articlesList) {

		System.out.println("################################################################################");
		System.out.println("Sorting the articles");
		System.out.println("################################################################################");


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



	public Object getData(NewsApi newsApi) throws NewsApiException {
		NewsReponse newsResponse = newsApi.getNews();
		if (newsResponse == null)
			throw new NewsApiException("It was not possible to load the news. Please modify your search.");
		return newsResponse.getArticles();
	}
}
