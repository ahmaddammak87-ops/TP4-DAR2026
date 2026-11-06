package Server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
public class ServeurUDP1 {



        // Adresse du client
        static SocketAddress adresseClient = null;

        public static void main(String[] args) {

            try {

                // =====================================
                // 1. Création du socket UDP du serveur
                // =====================================

                DatagramSocket socket = new DatagramSocket(null);

                // Adresse et port du serveur
                InetSocketAddress adresseServeur =
                        new InetSocketAddress("0.0.0.0", 1234);

                // Liaison du socket à l'adresse
                socket.bind(adresseServeur);

                System.out.println("Serveur UDP démarré sur le port 1234...");
                System.out.println("En attente du premier message du client...");


                // =====================================
                // 2. Création du thread de réception
                // =====================================

                Thread threadReception = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        while (true) {

                            try {

                                // Tableau d'octets pour recevoir le message
                                byte[] bufferReception = new byte[1024];

                                // Création du DatagramPacket
                                DatagramPacket packetReception =
                                        new DatagramPacket(
                                                bufferReception,
                                                bufferReception.length
                                        );

                                // Attente d'un message
                                socket.receive(packetReception);

                                // Mémorisation de l'adresse du client
                                adresseClient =
                                        packetReception.getSocketAddress();

                                // Conversion byte[] -> String
                                String message =
                                        new String(
                                                packetReception.getData(),
                                                0,
                                                packetReception.getLength()
                                        );

                                System.out.println();
                                System.out.println(
                                        "Message reçu de "
                                                + packetReception.getAddress()
                                                + ":"
                                                + packetReception.getPort()
                                );

                                System.out.println(
                                        "Client : " + message
                                );

                                System.out.print("Serveur : ");

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                    }
                });


                // =====================================
                // 3. Démarrage du thread de réception
                // =====================================

                threadReception.start();


                // =====================================
                // 4. Thread principal : ENVOI
                // =====================================

                Scanner scanner = new Scanner(System.in);

                while (true) {

                    System.out.print("Serveur : ");

                    // Lire le message saisi par le serveur
                    String message = scanner.nextLine();


                    // Vérifier si le client est connu
                    if (adresseClient == null) {

                        System.out.println(
                                "Aucun client n'est encore connu."
                        );

                        continue;
                    }


                    // Conversion String -> byte[]
                    byte[] bufferEnvoi =
                            message.getBytes();


                    // Création du DatagramPacket
                    DatagramPacket packetEnvoi =
                            new DatagramPacket(
                                    bufferEnvoi,
                                    bufferEnvoi.length,
                                    adresseClient
                            );


                    // Envoi du message
                    socket.send(packetEnvoi);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }}


