package org.example;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {
    static Gson gson = new Gson();

    public static void main(String[] args) {
        Gson gson = new Gson();



            JFrame frame = new JFrame("Cliente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600,400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(7,2));

//            JTextField inputField1 = new JTextField();
//            inputField1.setFont((new Font("Arial", Font.PLAIN, 30)));
//
//            JLabel label1 = new JLabel("Nome:");
//            label1.setFont((new Font("Arial", Font.BOLD, 30)));
//


            JLabel massaLabel = new JLabel("Massa:");
            massaLabel.setFont((new Font("Arial", Font.BOLD, 30)));
            JTextField massa = new JTextField();
            massa.setFont((new Font("Arial", Font.PLAIN, 30)));


            JLabel alturaLabel = new JLabel("Altura:");
            alturaLabel.setFont((new Font("Arial", Font.BOLD, 30)));
            JTextField altura = new JTextField();
            altura.setFont((new Font("Arial", Font.PLAIN, 30)));


            JLabel responseLabel = new JLabel("Resposta: ");
            responseLabel.setFont((new Font("Arial", Font.BOLD, 30)));
            JTextField responseField = new JTextField();
            responseField.setFont((new Font("Arial", Font.PLAIN, 30)));



            panel.add(massaLabel);
            panel.add(massa);
            panel.add(alturaLabel);
            panel.add(altura);
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

                            String response = sendRequestPost(massa.getText(), altura.getText());

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

    public static String sendRequestPost(String massa, String altura) {
        try {
            String apiUrl = "http://localhost:3000/api/imc";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Construa os dados a serem enviados no corpo da requisição em formato JSON
            String jsonInputString = "{\"massa\": \"" + massa + "\", \"altura\": \"" + altura + "\"}";

            // Escreva os dados no corpo da requisição usando OutputStream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Leia a resposta
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

    private static class Data{
        double massa;
        double altura;

        Data(Double massa, double altura){
            this.massa = massa;
            this.altura = altura;
        }
    }



}