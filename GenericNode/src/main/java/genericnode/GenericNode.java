/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genericnode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            if (args[0].equals("tc"))
            {
                System.out.println("TCP CLIENT");
                String addr = args[1];
                int port = Integer.parseInt(args[2]);
                String cmd = args[3];
                String key = (args.length > 4) ? args[4] : "";
                String val = (args.length > 5) ? args[5] : "";
                SimpleEntry<String, String> se = new SimpleEntry<String, String>(key, val);
                // insert code to make TCP client request to server at addr:port
                try (Socket socket = new Socket(addr, port)) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(cmd + " " + key + " " + val);
                    System.out.println("Sent request to server");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //String response = in.readLine();
                    StringBuilder response = new StringBuilder();
                    response.append(in.readLine());
                    response.append("\n");
                    response.append(in.readLine());

                    System.out.println("Received response from server: " + response);
                } catch (IOException ex) {
                    System.out.println("Client exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

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

                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String request = in.readLine();
                        System.out.println("Received request: " + request);

                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println("Response from server!!!!!!");
                        String[] parts = request.split(" ");
                        String cmd = parts[0];
                        String key = parts[1];
                        String val = "";
                        if(cmd.equals("put") | cmd.equals("del")){
                            val = parts[2];
                        }

                        //exception handling
                        //access null key for PUT

                        if (cmd.equals("put")) {
                            keyValueStorage.put(key, val);
                            out.println("PUT " + key + " " + val);
                        } else if (cmd.equals("get")) {
                            String value = keyValueStorage.get(key);
                            out.println("GET " + key + " " + value);
                        } else if (cmd.equals("del")) {
                            keyValueStorage.delete(key);
                            out.println("DEL " + key);
                        } else if (cmd.equals("store")) {
                            out.println("STORE " + keyValueStorage);
                        } else if (cmd.equals("exit")) {
                            out.println("EXIT");
                            break;
                        }


                    }

                } catch (IOException ex) {
                    System.out.println("Server exception: " + ex.getMessage());
                    ex.printStackTrace();
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
