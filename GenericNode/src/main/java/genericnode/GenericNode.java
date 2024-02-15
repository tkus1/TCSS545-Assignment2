/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genericnode;

import genericnode.Clients.TCPClient;
import genericnode.Servers.TCPServer;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

/**
 *
 * @author wlloyd
 */
public class GenericNode
{
    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws IOException {
        if (args.length > 0)
        {
            if (args[0].equals("rmis"))
            {
                System.out.println("RMI SERVER");
                try
                {
                    // insert code to start RMI Server
                }
                catch (Exception e)
                {
                    System.out.println("Error initializing RMI server.");
                    e.printStackTrace();
                }
            }
            if (args[0].equals("rmic"))
            {
                System.out.println("RMI CLIENT");
                String addr = args[1];
                String cmd = args[2];
                String key = (args.length > 3) ? args[3] : "";
                String val = (args.length > 4) ? args[4] : "";
                // insert code to make RMI client request
            }
            if (args[0].equals("tc"))
            {
                System.out.println("TCP CLIENT");
                String addr = args[1];
                int port = Integer.parseInt(args[2]);
                String cmd = args[3];
                String key = (args.length > 4) ? args[4] : "";
                String val = (args.length > 5) ? args[5] : "";
                SimpleEntry<String, String> entry = new SimpleEntry<String, String>(key, val);
                // insert code to make TCP client request to server at addr:port
//                Client client = clients.get(args[0]);
                TCPClient client = new TCPClient();
                client.connect(addr, port);
                client.executeOperation(cmd, entry);
            }

            if (args[0].equals("ts"))
            {
                System.out.println("TCP SERVER");
                int port = Integer.parseInt(args[1]);
//                Server server = servers.get(args[0]);
                TCPServer server = new TCPServer();
                server.startServer(port);
//                startTcpServer(port);
                // insert code to start TCP server on port
            }
            if (args[0].equals("uc"))
            {
                System.out.println("UDP CLIENT");
                String addr = args[1];
                int sendport = Integer.parseInt(args[2]);
                int recvport = sendport + 1;
                String cmd = args[3];
                String key = (args.length > 4) ? args[4] : "";
                String val = (args.length > 5) ? args[5] : "";
                SimpleEntry<String, String> se = new SimpleEntry<String, String>(key, val);
                // insert code to make UDP client request to server at addr:send/recvport
            }
            if (args[0].equals("us"))
            {
                System.out.println("UDP SERVER");
                int port = Integer.parseInt(args[1]);
                // insert code to start UDP server on port
            }

        }
        else
        {
            String msg = "GenericNode Usage:\n\n" +
                    "Client:\n" +
                    "uc/tc <address> <port> put <key> <msg>  UDP/TCP CLIENT: Put an object into store\n" +
                    "uc/tc <address> <port> get <key>  UDP/TCP CLIENT: Get an object from store by key\n" +
                    "uc/tc <address> <port> del <key>  UDP/TCP CLIENT: Delete an object from store by key\n" +
                    "uc/tc <address> <port> store  UDP/TCP CLIENT: Display object store\n" +
                    "uc/tc <address> <port> exit  UDP/TCP CLIENT: Shutdown server\n" +
                    "rmic <address> put <key> <msg>  RMI CLIENT: Put an object into store\n" +
                    "rmic <address> get <key>  RMI CLIENT: Get an object from store by key\n" +
                    "rmic <address> del <key>  RMI CLIENT: Delete an object from store by key\n" +
                    "rmic <address> store  RMI CLIENT: Display object store\n" +
                    "rmic <address> exit  RMI CLIENT: Shutdown server\n\n" +
                    "Server:\n" +
                    "us/ts <port>  UDP/TCP SERVER: run udp or tcp server on <port>.\n" +
                    "rmis  run RMI Server.\n";
            System.out.println(msg);
        }


    }

//    private static void startTcpServer(Integer port) throws IOException {
//        try {
//            ServerSocket tcpServer = new ServerSocket(port);
//            while(true) {
//                Socket connectionSocket = tcpServer.accept();
//                System.out.println("###");
//                DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
//                String key = inFromClient.readUTF();
//                String value = inFromClient.readUTF();
//                System.out.println("printing key: " +  key);
//                System.out.println("Printing value: " + value);
//            }
//
//        }
//        catch (IOException e) {
//            System.out.println(e);
//            throw e;
//        }
//
//    }

//    private static void startTcpClient(String host, Integer port, SimpleEntry<String, String>entry) throws IOException {
//        try {
//            Socket clientSocket = new Socket(host, port);
//
//        } catch (IOException e) {
//            System.out.println(e);
//            throw e;
//        }
//
//
//    }


}
