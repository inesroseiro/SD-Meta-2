package dropsrc.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Configurations {
    private String RMIname, RMIhost;
    private int RMIport;

    Configurations(String fileName){
        InputStream in = null;
        Properties properties = new Properties();

        try {
            in = new FileInputStream(fileName);
            properties.load(in);

            switch (fileName){
                case "RMI_configs.cfg":
                    RMIname = properties.getProperty("RMIname");
                    RMIport = Integer.parseInt(properties.getProperty("RMIport"));
                    RMIhost = properties.getProperty("RMIhost");
                    break;
            }
        } catch(FileNotFoundException e) {
            System.out.println("\n FileNotFoundException: " + e.getMessage());
        } catch(IOException e) {
            System.out.println("\n IOException: " + e.getMessage());
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch(IOException e) {
                    System.out.println("\nIOException: " + e.getMessage());
                }
            }
        }

    }

    public String getRMIname() {
        return RMIname;
    }

    public void setRMIname(String RMIname) {
        this.RMIname = RMIname;
    }

    public String getRMIhost() {
        return RMIhost;
    }

    public void setRMIhost(String RMIhost) {
        this.RMIhost = RMIhost;
    }

    public int getRMIport() {
        return RMIport;
    }

    public void setRMIport(int RMIport) {
        this.RMIport = RMIport;
    }
}
