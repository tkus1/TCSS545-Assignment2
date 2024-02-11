/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genericnode;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Blob;
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

            /// TCP CLIENT
            if (args[0].equals("tc"))
            {
                System.out.println("TCP CLIENT");
                String addr = args[1];
                int port = Integer.parseInt(args[2]);
                String cmd = args[3];
                String key = (args.length > 4) ? args[4] : "";
                String val = (args.length > 5) ? args[5] : "";
                // todo handle invalid input args

                // todo should we use this object to send to server?
                // todo or send it as a string?
                SimpleEntry<String, String> se = new SimpleEntry<String, String>(key, val);
                ExtendedEntry<String, String> ee = new ExtendedEntry<String, String>(se, cmd);
                // insert code to make TCP client request to server at addr:port
                try (Socket socket = new Socket(addr, port)) {
                    // byte stream for sending
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(ee);
                    out.flush();
                    System.out.println("Sent object to server");
                    System.out.println("method: " + ee.getMethodName()+ " " + ee.getKey() + " " + se.getValue());
                    // byte stream for receiving
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    response.append(in.readLine());
                    System.out.println("Received response from server: \n"  + response);
                } catch (IOException ex) {
                    System.out.println("Client exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            /// TCP SERVER
            if (args[0].equals("ts"))
            {
                System.out.println("TCP SERVER");
                SharedResource keyValueStorage = new SharedResource();
                int port = Integer.parseInt(args[1]);
                // insert code to start TCP server on port
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    System.out.println("Server is listening on port " + port);

                    while (true) {
                        Socket socket = serverSocket.accept();
                        System.out.println("New client connected");
                        //object stream for receiving
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        ExtendedEntry<String, String> entry = (ExtendedEntry<String,String>) in.readObject();
                        System.out.println("Received request: " + " " + entry.getMethodName()
                                                                + " " + entry.getKey()
                                                                + " " + entry.getValue());

                        //byte stream for sending
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                        if (entry.getMethodName().equals("put")) {
                            keyValueStorage.put(entry.getKey(), entry.getValue());
                            out.println("PUT " + entry.getKey() + " " + entry.getValue());
                        } else if (entry.getMethodName().equals("get")) {
                            String value = keyValueStorage.get(entry.getKey());
                            out.println("GET " + entry.getKey() + " " + value);
                        } else if (entry.getMethodName().equals("del")) {
                            keyValueStorage.delete(entry.getKey());
                            out.println("DEL " + entry.getKey());
                        } else if (entry.getMethodName().equals("store")) {
                            out.println("STORE " + keyValueStorage);
                        } else if (entry.getMethodName().equals("exit")) {
                            out.println("EXIT");
                            break;
                        }

                    }

                } catch (IOException ex) {
                    System.out.println("Server exception: " + ex.getMessage());
                    ex.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

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

    
    
}
