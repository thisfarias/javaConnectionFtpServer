import java.net.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.apache.commons.net.ftp.*;
import java.io.InputStream;
public class connectionRequestServer {
    public static void main(String[]args){
        String server = "your server";
        Integer port = 21;
        String user = "your user";
        String pass = "your pass";

        String search = "What´s search?";
        Integer quantity = 5;
        FTPClient ftpClient = new FTPClient();
        try{
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String link = String.format("https://stock.adobe.com/br/search?k=%s&search_type=usertyped'", search.replace(" ", "+"));
            Document doc = Jsoup.connect(link).get();
            Elements metaLink = doc.select("meta[itemprop=thumbnailUrl]");
            Integer count = 0;
            String pathfile = "/Matheus/InterfacesTests/SoapRequest/";
            for(String content : metaLink.eachAttr("content")){
                //System.out.println(content);
                count++;
                try (InputStream inputStream = new URL(content).openStream()) {
                    ftpClient.storeFile(pathfile+String.format("%s_%d", search.replace(" ", "_"), count)+".jpg", inputStream);
                }
                if(count==quantity){
                    break;
                }   
            }
            ftpClient.logout();
            ftpClient.disconnect();
        }catch(Exception e){
            System.out.println(e);
        }

    }

}