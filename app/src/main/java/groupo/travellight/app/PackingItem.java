package groupo.travellight.app;

import java.io.Serializable;

/**
 * Created by Joseph Bandola on 4/14/2014.
 * Represents what a packing list object is
 */
public class PackingItem {

    private String _name, _status;

    public PackingItem(String name, String status){

        _name = name;
        _status = status;

    }

    public String getName() {
        return _name;
    }



    public String getStatus() {
        return _status;
    }

}
