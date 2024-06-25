import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class ChatClientGUI extends JFrame {
  private JTextArea messageArea;
  private JTextField textField;
  private ChatClient client;

  public ChatClientGUI() {
      super("Chat Application");
      setSize(400, 500);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      messageArea = new JTextArea();
      messageArea.setEditable(false);
      add(new JScrollPane(messageArea), BorderLayout.CENTER);

      textField = new JTextField();
      textField.addActionListener((ActionEvent e) -> {
          client.sendMessage(textField.getText());
          textField.setText("");
      });
      add(textField, BorderLayout.SOUTH);

      // Initialize and start the ChatClient
      try {
          this.client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);
          client.startClient();
      } catch (IOException e) {
          JOptionPane.showMessageDialog(null,"Error connecting to the server", "Connection error",
                  JOptionPane.ERROR_MESSAGE);
          System.exit(1);
      }
  }

  private void onMessageReceived(String message) {
      SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
  }

  public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
          new ChatClientGUI().setVisible(true);
      });
  }
}