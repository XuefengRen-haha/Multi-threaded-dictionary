

/**
 * Name: Xuefeng REN
 * Surname: XUEFENGR
 * Student ID: 1011257
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Client {
    private JFrame jFrame;
    private JTextField wordField;
    private JTextField meaningField;
    private static JTextArea resultArea;

    private int port;
    private String IpAddress;

    public Client(int port, String IpAddress){
        this.port = port;
        this.IpAddress = IpAddress;

        jFrame = new JFrame("Client");
        jFrame.setBackground(Color.WHITE);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(400,400,400,400);

        jFrame.addWindowListener(new MyWindowListener());

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        jFrame.setLayout(gridBagLayout);
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel word = new JLabel("Word:");
        gridBagLayout.setConstraints(word,gridBagConstraints);
        jFrame.add(word);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        wordField = new JTextField();
        gridBagLayout.setConstraints(wordField,gridBagConstraints);
        jFrame.add(wordField);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel meaning = new JLabel("Meaning:");
        gridBagLayout.setConstraints(meaning,gridBagConstraints);
        jFrame.add(meaning);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        meaningField = new JTextField();
        gridBagLayout.setConstraints(meaningField,gridBagConstraints);
        jFrame.add(meaningField);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.gridwidth = 1;
        JButton searchButton = new JButton("search");
        gridBagLayout.setConstraints(searchButton,gridBagConstraints);
        jFrame.add(searchButton);
        gridBagConstraints.gridwidth = 1;
        JButton removeButton = new JButton("remove");
        gridBagLayout.setConstraints(removeButton,gridBagConstraints);
        jFrame.add(removeButton);
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        JButton addButton = new JButton("add");
        gridBagLayout.setConstraints(addButton,gridBagConstraints);
        jFrame.add(addButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchWord = wordField.getText();
                String searchData = "search:"+searchWord;
                if (searchWord.contains(" ")){
                    resultArea.setText("Error: please input a word without space.");
                }else if (isValid(searchData)){
                    send(searchData,IpAddress,port);
                }else {
                    resultArea.setText("Bad data: the input is invalid.");
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String removeWord = wordField.getText();
                String removeData = "remove:"+removeWord;
                if (removeWord.contains(" ")){
                    resultArea.setText("Error: please input a word without space.");
                }if (isValid(removeData)){
                    send(removeData,IpAddress,port);
                }else {
                    resultArea.setText("Bad data: the input is invalid.");
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String addWord = wordField.getText();
                String addMeaning =meaningField.getText();
                String addData = "add:"+addWord+":"+addMeaning;
                if (addWord.contains(" ") || addWord.equals("")){
                    resultArea.setText("Please input a word without space.");
                }else if (isValid(addData)){
                    send(addData,IpAddress,port);
                }else {
                    resultArea.setText("Bad data: the input is invalid.");
                }
            }
        });

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel result = new JLabel("Result:");
        gridBagLayout.setConstraints(result,gridBagConstraints);
        jFrame.add(result);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        resultArea = new JTextArea();
        gridBagLayout.setConstraints(resultArea,gridBagConstraints);
        jFrame.add(resultArea);

    }

    private static void send(String sendData,String host, int port)  {
        //String host = "localhost";
        //int port = 8888;
        try{
            Socket socket = new Socket(host, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

            System.out.println("Data send to server: "+ sendData);
            output.write(sendData);
            output.newLine();
            output.flush();

            String response = input.readLine();
            System.out.println(response);
            resultArea.setText(response);

            output.close();
            input.close();
            socket.close();

        } catch (UnknownHostException e) {
            resultArea.setText("Error: Can not find the host.");
            System.out.println("Unknown host: "+host);
            e.printStackTrace();
        } catch (IOException e) {
            resultArea.setText("Error: Can not find the host, please check the IP address and port and \nthe server might not start.");
            System.out.println("IOException: "+ e);
            e.printStackTrace();
        }
    }

    public static boolean isValid(String str) {
        Pattern patPunctuation = Pattern.compile("[`~!@#$^&*()=|{}';'\\[\\]<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
        Matcher matcher = patPunctuation.matcher(str);
        return !matcher.find();
    }

    public static void main(String[] args) {
        String Ip = args[0];
        int port = Integer.parseInt(args[1]);
        Client gui = new Client(port,Ip);
        gui.jFrame.setVisible(true);
    }



}
