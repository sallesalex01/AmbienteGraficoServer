package org.example;

import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    static Gson gson = new Gson();
    static Timer timer = new Timer();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Controle de Acesso");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());
        JLabel codigoLabel = new JLabel("Codigo de acesso: ");
        JTextField codigoTextField = new JTextField(20);

        codigoTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String resposta = sendRequestPost(codigoTextField.getText());
                Data respostaServer = gson.fromJson(resposta, Data.class);

                if ("1".equals(respostaServer.ACK)){
                    frame.getContentPane().setBackground(Color.GREEN);
                    resetarCor(frame);

                } else {
                    frame.getContentPane().setBackground(Color.RED);
                    resetarCor(frame);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String resposta = sendRequestPost(codigoTextField.getText());
                Data respostaServer = gson.fromJson(resposta, Data.class);

                if ("1".equals(respostaServer.ACK)){
                    frame.getContentPane().setBackground(Color.GREEN);
                    resetarCor(frame);

                } else {
                    frame.getContentPane().setBackground(Color.RED);
                    resetarCor(frame);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String resposta = sendRequestPost(codigoTextField.getText());
                Data respostaServer = gson.fromJson(resposta, Data.class);

                if ("1".equals(respostaServer.ACK)){
                    frame.getContentPane().setBackground(Color.GREEN);
                    resetarCor(frame);
                } else {
                    frame.getContentPane().setBackground(Color.RED);
                    resetarCor(frame);
                }
            }
        });

        frame.add(codigoLabel);
        frame.add(codigoTextField);

        frame.setVisible(true);
    }


    public static String sendRequestPost(String codigo) {
        try {
            String apiUrl = "http://localhost:3000/api/verificarCodigo";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Construa os dados a serem enviados no corpo da requisição em formato JSON
            String jsonInputString = "{\"codigo\": \"" + codigo + "\"}";

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
        String ACK;

        Data(String ACK){
           this.ACK = ACK;
        }
    }



    private static void resetarCor(JFrame frame) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Altere a cor de volta ao normal
                frame.getContentPane().setBackground(UIManager.getColor("Panel.background"));
            }
        }, 3000);
    }

}