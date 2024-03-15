/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genericnode;

import java.util.AbstractMap.SimpleEntry;
import genericnode.server.*;
import genericnode.client.*;

/**
 *
 * @author wlloyd
 */
public class GenericNode
{
    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws Exception {
        if (args.length > 0)
        {
            if (args[0].equals("rmis"))
            {
                System.out.println("RMI SERVER");
                try
                {
                    RMIServer server = new RMIServer();
                    server.setupAndBindServer();
                }
                catch (Exception e)
                {
                    System.out.println("Error initializing RMI server.");
                    System.err.println("Server exception: " + e);
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
                RMIClient client = new RMIClient(addr);
                client.executeTask(cmd, key, val);
            }
            if (args[0].equals("tc"))
            {
                System.out.println("TCP CLIENT");
                String addr = args[1];
                int port = Integer.parseInt(args[2]);
                String cmd = args[3];
                String key = (args.length > 4) ? args[4] : "";
                String val = (args.length > 5) ? args[5] : "";
                SimpleEntry<String, String> entry = new SimpleEntry<>(key, val);
                // insert code to make TCP client request to server at addr:port
                TCPClient client = new TCPClient();
                client.connect(addr, port);
                client.executeOperation(cmd, entry);
            }

            if (args[0].equals("ts"))
            {
                System.out.println("TCP SERVER");
                int port = Integer.parseInt(args[1]);
                // insert code to start TCP server on port
                TCPServer server;
                if (args.length > 2) {
                    String dirServerAddr = args[2];// membership-server-IP
                    server = new TCPServer(new CentralizeMembershipGetOtherServersStrategy());
                    server.dirServerAddr = dirServerAddr;
                    server.port = port;
                    server.sendHeartbeat();
                    server.startServerWithCMT(port, port+1);
                }
                // Start centralized node directory server
                else if (port == 4410) {
                    System.out.println("Centralized membership key/value store");
                    server = new TCPServer();
                    server.startMembershipServer(port);
                }
                // Static config file membership
                else {
                    System.out.println("Config file");
                    server = new TCPServer(new ConfigFileGetOtherServersStrategy());
                    server.startServer(port,port+1);
                }
            }
            if (args[0].equals("uc"))
            {
                System.out.println("UDP CLIENT");
                String addr = args[1];
                int sendport = Integer.parseInt(args[2]);
                String cmd = args[3];
                String key = (args.length > 4) ? args[4] : "";
                String val = (args.length > 5) ? args[5] : "";
                SimpleEntry<String, String> entry = new SimpleEntry<>(key, val);
                // insert code to make UDP client request to server at addr:send/recvport
                UDPClient client = new UDPClient();
                client.connect(addr, sendport);
                client.executeOperation(cmd, entry);
            }
            if (args[0].equals("us"))
            {
                System.out.println("UDP SERVER");
                int port = Integer.parseInt(args[1]);
                // insert code to start UDP server on port
                UDPServer server = new UDPServer();
                server.startServer(port,port+1);
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

}
