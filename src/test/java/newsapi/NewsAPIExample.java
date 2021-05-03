package newsapi;

import newsapi.beans.Article;
import newsapi.beans.NewsReponse;
import newsapi.enums.Category;
import newsapi.enums.Country;
import newsapi.enums.Endpoint;

import java.util.List;


public class NewsAPIExample {

    public static final String APIKEY = "0e38054687cf4b65a10ca66a05a6885e";

    public static void main(String[] args){

        NewsApi newsApi = new NewsApiBuilder()
                .setApiKey(APIKEY)
                .setQ("corona")
                .setEndPoint(Endpoint.TOP_HEADLINES)
                .setSourceCountry(Country.at)
                .setSourceCategory(Category.health)
                .createNewsApi();
        try {
                NewsReponse newsResponse = newsApi.getNews();
                if(newsResponse != null){
                    List<Article> articles = newsResponse.getArticles();
                    articles.stream().forEach(article -> System.out.println(article.toString()));
                }


                newsApi = new NewsApiBuilder()
                        .setApiKey(APIKEY)
                        .setQ("corona")
                        .setEndPoint(Endpoint.EVERYTHING)
                        .setFrom("2021-04-10")
                        .setExcludeDomains("Lifehacker.com")
                        .createNewsApi();

                    newsResponse = newsApi.getNews();
                if(newsResponse != null){
                    List<Article> articles = newsResponse.getArticles();
                    articles.stream().forEach(article -> System.out.println(article.toString()));
                }

        } catch (NewsApiException newsApiException) {
                System.out.println("This is NewsApiException: " + newsApiException.getMessage());
        }

    }
}
