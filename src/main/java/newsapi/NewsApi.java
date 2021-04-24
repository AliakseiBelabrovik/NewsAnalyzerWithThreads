package newsapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import newsapi.beans.NewsReponse;
import newsapi.enums.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NewsApi {

    public static final String DELIMITER = "&";

    public static final String NEWS_API_URL = "http://newsapi.org/v2/%s?q=%s&apiKey=%s";

    private Endpoint endpoint;
    private String q;
    private String qInTitle;
    private Country sourceCountry;
    private Category sourceCategory;
    private String domains;
    private String excludeDomains;
    private String from;
    private String to;
    private Language language;
    private SortBy sortBy;
    private String pageSize;
    private String page;
    private String apiKey;

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public String getQ() {
        return q;
    }

    public String getqInTitle() {
        return qInTitle;
    }

    public Country getSourceCountry() {
        return sourceCountry;
    }

    public Category getSourceCategory() {
        return sourceCategory;
    }

    public String getDomains() {
        return domains;
    }

    public String getExcludeDomains() {
        return excludeDomains;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Language getLanguage() {
        return language;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getPage() {
        return page;
    }

    public String getApiKey() {
        return apiKey;
    }

    public NewsApi(String q, String qInTitle, Country sourceCountry, Category sourceCategory, String domains, String excludeDomains, String from, String to, Language language, SortBy sortBy, String pageSize, String page, String apiKey, Endpoint endpoint) {
        this.q = q;
        this.qInTitle = qInTitle;
        this.sourceCountry = sourceCountry;
        this.sourceCategory = sourceCategory;
        this.domains = domains;
        this.excludeDomains = excludeDomains;
        this.from = from;
        this.to = to;
        this.language = language;
        this.sortBy = sortBy;
        this.pageSize = pageSize;
        this.page = page;
        this.apiKey = apiKey;
        this.endpoint = endpoint;
    }

    /**
     * Reads the contents of the URL and saves in a specified file in local machine
     * @param path - is String with the path and name of the file to write to
     * @throws NewsApiException
     */
    public void saveArticlesLocallyInHtml(String path) throws NewsApiException {
        String url = buildURL();
        URL obj = null;

        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            throw new NewsApiException("The URL is malformed. Please check the URL you use.");
        }

        try {
            URLConnection conn = obj.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            String fileName = path;
            File file = new File(fileName);
            //if no such file exists, create this file
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            while ((inputLine = br.readLine()) != null) {
                bw.write(inputLine);
            }
            bw.close();;
            br.close();
            System.out.println("Your file is saved in " + fileName + " location.");


        } catch (IOException ioException) {
            throw new NewsApiException("Impossible to write to the file. Please check the path and filename.");
        }

    }

    protected String requestData() throws NewsApiException {
        String url = buildURL();
        System.out.println("URL: "+url);
        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            // TOOO improve ErrorHandling
            //e.printStackTrace();
            throw new NewsApiException("The URL is malformed. Please check the URL you use.");
        }


        HttpURLConnection con;
        StringBuilder response = new StringBuilder();
        try {
            con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new NewsApiException("The required information is not available. Please refine or modify " +
                    "your search/parameters.");
        }
        return response.toString();
    }

    protected String buildURL() throws NewsApiException {
        // TODO ErrorHandling

            if (getEndpoint() == null)
                throw new NewsApiException("This is NewsApiException: Endpoint was not defined or defined illegally." +
                        "Please try again.");

            String urlbase = String.format(NEWS_API_URL,getEndpoint().getValue(),getQ(),getApiKey());


            StringBuilder sb = new StringBuilder(urlbase);

            if(getFrom() != null){
                sb.append(DELIMITER).append("from=").append(getFrom());
            }
            if(getTo() != null){
                sb.append(DELIMITER).append("to=").append(getTo());
            }
            if(getPage() != null){
                sb.append(DELIMITER).append("page=").append(getPage());
            }
            if(getPageSize() != null){
                sb.append(DELIMITER).append("pageSize=").append(getPageSize());
            }
            if(getLanguage() != null){
                sb.append(DELIMITER).append("language=").append(getLanguage());
            }
            if(getSourceCountry() != null){
                sb.append(DELIMITER).append("country=").append(getSourceCountry());
            }
            if(getSourceCategory() != null){
                sb.append(DELIMITER).append("category=").append(getSourceCategory());
            }
            if(getDomains() != null){
                sb.append(DELIMITER).append("domains=").append(getDomains());
            }
            if(getExcludeDomains() != null){
                sb.append(DELIMITER).append("excludeDomains=").append(getExcludeDomains());
            }
            if(getqInTitle() != null){
                sb.append(DELIMITER).append("qInTitle=").append(getqInTitle());
            }
            if(getSortBy() != null){
                sb.append(DELIMITER).append("sortBy=").append(getSortBy());
            }
            return sb.toString();

    }

    public NewsReponse getNews() throws NewsApiException {
        NewsReponse newsReponse = null;

            String jsonResponse = requestData();

            if(jsonResponse != null && !jsonResponse.isEmpty()){

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    newsReponse = objectMapper.readValue(jsonResponse, NewsReponse.class);
                    if(!"ok".equals(newsReponse.getStatus())){
                        System.out.println("Error: "+newsReponse.getStatus());
                    }
                } catch (JsonProcessingException e) {
                    throw new NewsApiException("Problem with processing the Json when loading news data");
                }
            }
        //TODO improve Errorhandling
        return newsReponse;
    }
}

