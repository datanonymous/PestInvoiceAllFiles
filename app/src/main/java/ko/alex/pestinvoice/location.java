package ko.alex.pestinvoice;

import java.util.Date;

public class location {

    private String location_name;
    private String location_phone;
    private String location_address;
    private String location_email;

    public location(String location_name, String location_phone, String location_address, String location_email) {
        this.location_name = location_name;
        this.location_phone = location_phone;
        this.location_address = location_address;
        this.location_email = location_email;

    }

    public location(){

    }

    public String getlocation_name() {
        return location_name;
    }

    public void setlocation_name(String location_name) {
        this.location_name = location_name;
    }



    public String getlocation_phone() {
        return location_phone;
    }

    public void setlocation_phone(String location_phone) {
        this.location_phone = location_phone;
    }


    public String getlocation_address() {
        return location_address;
    }

    public void setlocation_address(String location_address) {
        this.location_address = location_address;
    }


    public String getlocation_email() {
        return location_email;
    }

    public void setlocation_email(String location_email) {
        this.location_email = location_email;
    }

}

