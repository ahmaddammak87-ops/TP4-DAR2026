package Client;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
public class ClientUDP3 {



        public static void main(String[] args) {

            try {
                // 1. Demander le nom d'utilisateur
                Scanner clavier = new Scanner(System.in);

                System.out.print("Entrez votre nom : ");
                String nom = clavier.nextLine();

                // 2. Créer le socket UDP du client
                DatagramSocket socket = new DatagramSocket();

                // 3. Définir l'adresse du serveur
                InetSocketAddress adresseServeur =
                        new InetSocketAddress("localhost", 1234);

                System.out.println("Client connecté au serveur UDP !");
                System.out.println("Vous pouvez commencer à discuter.");

                // 4. Thread pour recevoir les messages
                Thread reception = new Thread(() -> {

                    try {
                        while (true) {

                            byte[] bufferReception = new byte[1024];

                            DatagramPacket packetReception =
                                    new DatagramPacket(
                                            bufferReception,
                                            bufferReception.length
                                    );

                            // Attendre un message du serveur
                            socket.receive(packetReception);

                            // Convertir les données en String
                            String message = new String(
                                    packetReception.getData(),
                                    0,
                                    packetReception.getLength(),
                                    StandardCharsets.UTF_8
                            );

                            // Afficher le message reçu
                            System.out.println("\n" + message);
                            System.out.print("Moi : ");
                        }

                    } catch (Exception e) {
                        if (!socket.isClosed()) {
                            e.printStackTrace();
                        }
                    }
                });

                // Démarrer le thread de réception
                reception.start();

                // 5. Boucle principale pour envoyer les messages
                while (true) {

                    System.out.print("Moi : ");
                    String texte = clavier.nextLine();

                    // Préfixer le message avec le nom
                    String message = "[" + nom + "] : " + texte;

                    // Convertir le message en tableau d'octets
                    byte[] bufferEnvoi =
                            message.getBytes(StandardCharsets.UTF_8);

                    // Créer le paquet UDP
                    DatagramPacket packetEnvoi =
                            new DatagramPacket(
                                    bufferEnvoi,
                                    bufferEnvoi.length,
                                    adresseServeur
                            );

                    // Envoyer le message au serveur
                    socket.send(packetEnvoi);

                    // Quitter avec /exit
                    if (texte.equalsIgnoreCase("/exit")) {
                        socket.close();
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


