package rmuti.askexpert.model.config;

import lombok.Data;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Data
public class BaseUrlFile {


    private String domain = "http://localhost:8080";

    //!user before baseDir**


    //** must use first
    private String PathSet = "/";

    private String baseDir = "uploads";

    //private String imageTopicUrl = this.domain+this.baseDir+"/image/topic/";

    //if change update in main too
    private String imageTopicUrl = this.baseDir+"/topicImg";

    private String imageProfileUrl = this.baseDir+"/profileImg";

    private String imageCommetUrl = this.baseDir+"/commentImg";


    public String ipAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}