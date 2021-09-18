

/**
 * Name: Xuefeng REN
 * Surname: XUEFENGR
 * Student ID: 1011257
 */

import java.io.*;
import java.net.Socket;

public class ThreadServer extends Thread {
    private Socket socket;
    private Dictionary dictionary;

    public ThreadServer(Socket socket, Dictionary dictionary){
        this.socket = socket;
        this.dictionary = dictionary;
    }

    public void run(){
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));

            String clintData = input.readLine();
            System.out.println("clintData:"+clintData);
            //input.close();

            String[] judgeClintData = clintData.split(":");
            System.out.println(judgeClintData[0]);
            if(judgeClintData.length <2){
                System.out.println("Error: Please input a word");
                output.write("Error: please input a word.");
                output.newLine();
                output.flush();
            }else {
                String type = judgeClintData[0];
                String word = judgeClintData[1];
                switch (type){
                    case "search":
                        dictionary.searchDictionary(word,socket);
                        socket.close();
                        break;
                    case "remove":
                        dictionary.removeDictionary(word,socket);
                        socket.close();
                        break;
                    case "add":
                        dictionary.addDictionary(clintData,socket);
                        socket.close();
                        break;
                }
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
