package moe.vot.own.projs.aad.pr.navi.contactbook;

public class Content {
    public int id;
    public String name;
    public String email;
    public String dateString;

    public Content(int id, String name, String email, String dateString){
        this.dateString = dateString;
        this.email = email;
        this.name = name;
        this.id = id;
    }
}