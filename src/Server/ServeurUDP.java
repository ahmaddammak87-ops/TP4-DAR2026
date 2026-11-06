package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ServeurUDP {
    private static final int PORT = 1234;
    private static final int TAILLE_BUFFER = 1024;

    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(null);
            socket.bind(new InetSocketAddress(PORT));
            System.out.println("[SERVEUR] En écoute sur le port " + PORT + " ...");
            while (true) {
                byte[] buffer = new byte[TAILLE_BUFFER];
                DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
                socket.receive(paquet);
                String message = new String(paquet.getData(), 0, paquet.getLength(),
                        StandardCharsets.UTF_8);
                System.out.println("[REÇU de " + paquet.getAddress().getHostAddress()
                        + ":" + paquet.getPort() + "] " + message);
            }

        } catch (Exception e) {
            // SocketException si le port est déjà utilisé, IOException à la réception...
            System.err.println("[SERVEUR] Erreur : " + e.getMessage());

        } finally {
            // Exécuté TOUJOURS, même après une erreur : on libère le port.
            // Le test != null évite une NullPointerException si la création
            // du socket a échoué dans le try.
            if (socket != null) {
                socket.close();
            }
        }
    }
}
