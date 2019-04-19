package m2dl.pcr.rmi;

import javax.swing.*;
import java.awt.event.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class Client extends JDialog implements IClient {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField messageToSend;
    private JList allMessages;

    private static IServer stub;
    private static UUID id = UUID.randomUUID();

    public Client() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        loadMessages();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Action sur l'IHM : Appuie sur le bouton "send".
     * Envoie le message au serveur.
     */
    private void onOK() {
        sendMessage(messageToSend.getText());
        messageToSend.setText("");
        loadMessages();
    }

    /**
     * Action sur l'IHM : Appuie sur le bouton "cancel".
     * Ferme la fenêtre.
     */
    private void onCancel() {
        dispose();
    }

    /**
     * Charge les messages dans l'interface.
     */
    private void loadMessages() {
        List<String> messages = getMessages();
        allMessages.setListData(messages.toArray());
    }

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            stub = (IServer) registry.lookup("IServer");

            Client dialog = new Client();

            // On s'enregistre
            IClient stub2 = (IClient) UnicastRemoteObject.exportObject(dialog, 0);
            registry.bind(id.toString(), stub2);
            stub.subscribe(id);

            // Ouverture de l'interface
            dialog.pack();
            dialog.setVisible(true);
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Envoie un message sur le serveur.
     * @param msg
     */
    public static void sendMessage(String msg) {
        try {
            stub.sendMessage(msg);
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Récupère les messages depuis le serveur.
     * @return liste des messages
     */
    public static List<String> getMessages() {
        try {
            return stub.getMessages();
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Il y a des nouveaux messages !
     * @throws RemoteException
     */
    public void notifier() throws RemoteException {
        loadMessages();
    }
}
