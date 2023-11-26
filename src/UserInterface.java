import Runner.IRunner;
import Runner.Status;
import com.epicbot.api.shared.APIContext;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.function.Consumer;

public class UserInterface extends JFrame {
    private JPanel panel;
    private JLabel label;
    private JComboBox<String> comboBox;
    private JButton button;
    private HashMap<String, IRunner> runners = new HashMap<>();

    public UserInterface(APIContext apiContext, Status status, Consumer<IRunner> runnerConsumer) {
        for (Fishing.Option option: Fishing.Options.GetOptions()) {
            runners.put(option.GetName(), Fishing.Generator.Generate(apiContext, status, option.location, option.activity));
        }
        for (Cooking.Option option: Cooking.Options.GetOptions()) {
            runners.put(option.GetName(), Cooking.Generator.Generate(apiContext, status, option.location, option.food));
        }


        setTitle("Pro Fisher");
        setSize(300, 100);
        setLocationRelativeTo(null);
        setResizable(false);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        label = new JLabel("Select a runner: ");
        panel.add(label);

        comboBox = new JComboBox<String>();
        for (String a : runners.keySet()) {
            comboBox.addItem(a);
        }

        panel.add(comboBox);

        button = new JButton("Update");
        comboBox.addActionListener(e -> {
            System.out.println("Actioned performed: " + e);
        });
        button.addActionListener(e -> {
            String runnerName = (String) comboBox.getSelectedItem();
            System.out.println(status.message);
            IRunner runner = runners.get(runnerName);
            apiContext.camera().setPitch(98, false);
            apiContext.camera().setYawDeg(0, false);
            runnerConsumer.accept(runner);
        });

        panel.add(button);

        add(panel);

        setVisible(true);
    }
}