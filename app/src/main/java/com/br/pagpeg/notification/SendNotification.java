package com.br.pagpeg.notification;

import com.br.pagpeg.utils.EnumStatus;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by brunolemgruber on 09/11/16.
 */

public class SendNotification {


    public static void sendNotificationShopper(String name,String one_signal_key,String uid,String content,Boolean enableButton){

        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic ODI1NTdiMjUtN2EzNy00NjM1LWFhNDQtYjkyYmJlZGQ3Yzgw");
            con.setRequestMethod("POST");

            StringBuffer strJsonBody = new StringBuffer("{"
                    +   "\"app_id\": \"bb7c6d67-b131-486b-9b58-27466efefd12\","
                    +   "\"include_player_ids\": [\"" + one_signal_key + "\"],"
                    +   "\"data\": {\"shopper_uid\": \"" + uid + "\"},"
                    +   "\"contents\": {\"en\": \"" + content + name + "\"},"
                    +   "\"headings\": {\"en\": \"PagPeg - Notificação para o shopper\"}");

                    if(enableButton){
                        strJsonBody.append(",\"buttons\": [{\"id\": \"visualizar_id\", \"text\": \"Visualizar pedido\"}]"
                                + "}");
                    }else{
                       strJsonBody.append("}");
                    }


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.toString().getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public static void sendNotificationUser(String user_name,String one_signal_key,String uid, String status, String content){

        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic ODI1NTdiMjUtN2EzNy00NjM1LWFhNDQtYjkyYmJlZGQ3Yzgw");
            con.setRequestMethod("POST");

            StringBuffer strJsonBody = new StringBuffer("{"
                    +   "\"app_id\": \"bb7c6d67-b131-486b-9b58-27466efefd12\","
                    +   "\"include_player_ids\": [\"" + one_signal_key + "\"],"
                    +   "\"data\": {\"user_uid\": \"" + uid + "\", \"status\": \"" + status + "\"},"
                    +   "\"contents\": {\"en\": \"" + user_name + content + "\"},"
                    +   "\"headings\": {\"en\": \"PagPeg - Notificação para o usuário\"}");

            if(status.equalsIgnoreCase(EnumStatus.Status.SHOPPER_PAYED.getName())){
                strJsonBody.append(",\"buttons\": [{\"id\": \"visualizar_compra_pronta\", \"text\": \"Visualizar\"}]"
                        + "}");
            }else{
                strJsonBody.append(",\"buttons\": [{\"id\": \"visualizar_ordem_shopper\", \"text\": \"Visualizar a compra do shopper\"}]"
                        + "}");
            }

            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.toString().getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
}
