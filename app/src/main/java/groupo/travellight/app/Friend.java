package groupo.travellight.app;

import java.io.Serializable;

/**
 * Created by Brandon on 3/14/14.
 */
public class Friend implements Serializable {
    private int portrait;
    private String name;
    private String email;

    public Friend(){
        name="Unknown";
        email="Unknown";
    }
    public Friend(String name,  String email){
        this.name=name;
        this.email=email;
    }

    public String toString(){
         String friend = getName()+"\n"+getEmail();
        return friend;
    }

    public int getPortrait(){return portrait;}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
}
