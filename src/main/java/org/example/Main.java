package org.example;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();



            JFrame frame = new JFrame("Cliente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600,400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(6,2));

            JTextField inputField1 = new JTextField();
            inputField1.setFont((new Font("Arial", Font.PLAIN, 30)));

            JLabel label1 = new JLabel("Nome:");
            label1.setFont((new Font("Arial", Font.BOLD, 30)));

            JLabel responseLabel = new JLabel("Resposta: ");
            responseLabel.setFont((new Font("Arial", Font.BOLD, 30)));
            JTextField responseField = new JTextField();
            responseField.setFont((new Font("Arial", Font.PLAIN, 30)));



            panel.add(label1);
            panel.add(inputField1);
            panel.add(responseLabel);
            panel.add(responseField);

            String[] buttonLabels ={
                    "ENVIAR", "SAIR"
            };

            for (String label: buttonLabels){
                JButton button = new JButton(label);
                button.setFont(new Font("Arial", Font.PLAIN, 30));
                panel.add(button);

                button.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        try {

                            String response = sendRequest(inputField1.getText());

                            responseField.setText(response);

                            if (label.equals("SAIR")) {
                                System.exit(0);
                            }

                        }
                        catch (Exception er){
                            System.out.println(er);
                        }

                    }
                });
            }
            frame.add(panel);
            frame.setVisible(true);
        }


    public static String sendRequest(String nome) {
        try {
            String apiUrl = "http://localhost:3000/api/" + nome;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }

            reader.close();
            connection.disconnect();

            return response.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }



}