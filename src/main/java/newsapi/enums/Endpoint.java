package newsapi.enums;

public enum Endpoint {
    TOP_HEADLINES("top-headlines"),
    EVERYTHING("everything");

    private String endPoint;

    Endpoint(String endPoint){
        this.endPoint = endPoint;
    }

    public String getValue() {
        return endPoint;
    }
}
