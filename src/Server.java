

/**
 * Name: Xuefeng REN
 * Surname: XUEFENGR
 * Student ID: 1011257
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static JTextArea dictArea;

    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            String filePath = args[1];

            Dictionary dictObject = new Dictionary(filePath);

            ServerSocket serverSocket = new ServerSocket(port);

            serverGUI(dictObject,filePath,port);

            while (true){
                Socket socket =serverSocket.accept();
                ThreadServer threadServer = new ThreadServer(socket,dictObject);
                threadServer.start();
            }
        } catch (BindException e){
            System.out.println("The port has been used, please change a new one.");
            e.printStackTrace();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serverGUI(Dictionary dictionary, String filePath, int port) throws UnknownHostException {
        JFrame jFrame = new JFrame("Server");
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
        JLabel ip = new JLabel("IP Address: "+getIP());
        gridBagLayout.setConstraints(ip,gridBagConstraints);
        jFrame.add(ip);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel hostname = new JLabel("Host Name: "+ gethostName());
        gridBagLayout.setConstraints(hostname,gridBagConstraints);
        jFrame.add(hostname);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel portLable = new JLabel("Port: "+port);
        gridBagLayout.setConstraints(portLable,gridBagConstraints);
        jFrame.add(portLable);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel file = new JLabel("Dictionary Path: "+filePath);
        gridBagLayout.setConstraints(file,gridBagConstraints);
        jFrame.add(file);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        JButton dictButton = new JButton("Dictionary");
        gridBagLayout.setConstraints(dictButton,gridBagConstraints);
        jFrame.add(dictButton);

        dictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dictArea.setText(getContent(dictionary.getDictionary()));
            }
        });

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        dictArea = new JTextArea();
        gridBagLayout.setConstraints(dictArea,gridBagConstraints);
        jFrame.add(dictArea);
        jFrame.setVisible(true);

    }

    public static String getContent(HashMap<String,String> dictionary){
        String content = "";
        for (Map.Entry<String, String> stringStringEntry : dictionary.entrySet()) {
            Object key = ((Map.Entry) stringStringEntry).getKey();
            Object value = ((Map.Entry) stringStringEntry).getValue();
            content = content + "\"" + key + "\"" + ": "+ value + "\n";
        }
        return content;
    }

    public static String getIP() throws UnknownHostException {

        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }

    public static String gethostName() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostName();
    }

}
