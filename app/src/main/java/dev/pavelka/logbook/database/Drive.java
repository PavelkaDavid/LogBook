package dev.pavelka.logbook.database;

public class Drive {
    int id;
    String fromDate;
    String toDate;
    String from;
    String to;
    double distance;
    double price;

    public Drive() {

    }

    public Drive(Drive drive) {
        this(drive.id, drive.fromDate, drive.toDate, drive.from, drive.to, drive.distance, drive.price);
    }

    public Drive(String fromDate, String toDate, String from, String to, double distance, double price) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.price = price;
    }

    public Drive(int id, String fromDate, String toDate, String from, String to, double distance, double price) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.price = price;
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getFromDate(){
        return this.fromDate;
    }

    public void setFromDate(String fromDate){
        this.fromDate = fromDate;
    }

    public String getToDate(){
        return this.toDate;
    }

    public void setToDate(String toDate){
        this.toDate = toDate;
    }

    public String getFrom(){
        return this.from;
    }

    public void setFrom(String from){
        this.from = from;
    }

    public String getTo(){
        return this.to;
    }

    public void setTo(String to){
        this.to = to;
    }

    public double getDistance(){
        return this.distance;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }

    public double getPrice(){
        return this.price;
    }

    public void setPrice(double price){
        this.price = price;
    }
}
