import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class DailyPlannerGUI extends JFrame {
    private JTextField dateField, eventField;
    private DefaultListModel<String> eventListModel;
    private static final String FILE_NAME = "planner.txt";

    public DailyPlannerGUI() {
        setTitle("Daily Planner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout(10, 10));

        // Top panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Event Description:"));
        eventField = new JTextField();
        inputPanel.add(eventField);

        JButton addButton = new JButton("Add Event");
        inputPanel.add(addButton);
        JButton deleteButton = new JButton("Delete Event");
        inputPanel.add(deleteButton);
        add(inputPanel, BorderLayout.NORTH);

        // Center list
        eventListModel = new DefaultListModel<>();
        JList<String> eventList = new JList<>(eventListModel);
        add(new JScrollPane(eventList), BorderLayout.CENTER);

        // Load previous events
        loadEvents();

        // Add new event
        addButton.addActionListener(e -> {
            String date = dateField.getText();
            String desc = eventField.getText();
            if (!date.isEmpty() && !desc.isEmpty()) {
                eventListModel.addElement(date + " → " + desc);
                saveEvents();
                dateField.setText("");
                eventField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill both fields!");
            }
        });

        // Delete event
        deleteButton.addActionListener(e -> {
            int index = eventList.getSelectedIndex();
            if (index != -1) {
                eventListModel.remove(index);
                saveEvents();
            } else {
                JOptionPane.showMessageDialog(this, "Select an event to delete!");
            }
        });

        setVisible(true);
    }

    private void saveEvents() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < eventListModel.size(); i++) {
                pw.println(eventListModel.get(i));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving events: " + e.getMessage());
        }
    }

    private void loadEvents() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                eventListModel.addElement(line);
            }
        } catch (IOException e) {
            System.out.println("No saved events found. Starting fresh!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DailyPlannerGUI::new);
    }
}
