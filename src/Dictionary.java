

/**
 * Name: Xuefeng REN
 * Surname: XUEFENGR
 * Student ID: 1011257
 */

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Dictionary {
    private String filePath;
    private HashMap<String,String> dictionary;

    public Dictionary(String filePath){
        this.filePath = filePath;
        File file = new File(filePath);
        try {
            if(!file.exists()){
                dictionary = new HashMap<>();
                dictionary.put("xuefeng","handsome");
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(dictionary);
                objectOutputStream.close();
                fileOutputStream.close();
            }
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            dictionary =(HashMap<String, String>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void updateDictionary(String filePath, HashMap<String,String> dictionary){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(dictionary);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getDictionary() {
        return dictionary;
    }

    public void searchDictionary(String word, Socket client){
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"UTF-8"));
            String meaning = dictionary.get(word);
            if (meaning != null){
                out.write("The meaning of \""+word+"\" is "+meaning);
            }else {
                out.write("\""+word+"\" is not in this dictionary.");
            }
            out.newLine();
            out.flush();
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void removeDictionary(String word, Socket client){
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"UTF-8"));
            if (dictionary.get(word) !=null){
                dictionary.remove(word);
                out.write("Successfully remove the word \""+word+"\".");
                out.newLine();
                out.flush();
                out.close();
                updateDictionary(filePath,dictionary);
            }else {
                out.write("The word of \""+word+"\" is not in this dictionary.");
                out.newLine();
                out.flush();
                out.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addDictionary(String data, Socket client){
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
            String[] splitData = data.split(":");
            String word = splitData[1];
            if (splitData.length !=3){
                out.write("Please input both the word and meaning.");
                out.newLine();
                out.flush();
                out.close();
            }else if (dictionary.get(word) != null){
                out.write("Fail adding: the word of \""+word+"\" has been already in this dictionary.");
                out.newLine();
                out.flush();
                out.close();
            }else {
                String meaning = splitData[2];
                dictionary.put(word,meaning);
                out.write("Successfully add the word \""+word+"\".");
                out.newLine();
                out.flush();
                out.close();
                updateDictionary(filePath,dictionary);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
