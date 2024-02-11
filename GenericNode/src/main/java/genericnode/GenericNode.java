/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genericnode;

import java.io.*;
import java.net.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

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
                //System.out.println("TCP CLIENT");
                String addr = args[1];
                int port = Integer.parseInt(args[2]);
                String cmd = args[3];
                String key = (args.length > 4) ? args[4] : "";
                String val = (args.length > 5) ? args[5] : "";
                // todo handle invalid input args

                SimpleEntry<String, String> se = new SimpleEntry<String, String>(key, val);
                ExtendedEntry<String, String> ee = new ExtendedEntry<String, String>(se, cmd);
                // insert code to make TCP client request to server at addr:port
                try (Socket socket = new Socket(addr, port)) {
                    // byte stream for sending
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(ee);
                    out.flush();
                    // byte stream for receiving
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    StringBuilder response = new StringBuilder();

                    String line;
                    if(cmd.equals("store")){
                        response.append("\n");
                    }
                    while((line = in.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    response.delete(response.length()-1, response.length());
                    System.out.println("server response:"+ response);
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

                        new Thread(() -> {
                            try {
                                //object stream for receiving
                                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                                ExtendedEntry<String, String> entry = (ExtendedEntry<String,String>) in.readObject();
                                System.out.println("Received request: " + " " + entry.getMethodName()
                                        + " " + entry.getKey()
                                        + " " + entry.getValue());

                                // byte stream for sending
                                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                                // if command is exit, break
                                if(entry.getMethodName().equals("exit")){
                                    out.println("Server shutting down");
                                    System.exit(0);
                                }
                                // process the request
                                TCPServerProcess(entry, keyValueStorage, out);
                                out.close();
                            } catch (IOException | ClassNotFoundException ex) {
                                System.out.println("Server exception: " + ex.getMessage());
                                ex.printStackTrace();
                            }
                        }).start();
                    }

                } catch (IOException ex) {
                    System.out.println("Server exception: " + ex.getMessage());
                    ex.printStackTrace();
                }

            }


            /// UDP CLIENT
            if (args[0].equals("uc"))
            {
                //System.out.println("UDP CLIENT");
                String addr = args[1];
                int sendport = Integer.parseInt(args[2]);
                int recvport = sendport + 1;
                String cmd = args[3];
                String key = (args.length > 4) ? args[4] : "";
                String val = (args.length > 5) ? args[5] : "";

                SimpleEntry<String, String> se = new SimpleEntry<String, String>(key, val);
                ExtendedEntry<String, String> ee = new ExtendedEntry<String, String>(se, cmd);
                // insert code to make UDP client request to server at addr:send/recvport


                try {
                    String hostName = InetAddress.getLocalHost().getHostName();
                    InetSocketAddress sourceSocketAddress = new InetSocketAddress(hostName, recvport);
                    try(DatagramSocket socket = new DatagramSocket(sourceSocketAddress)) {
                        // byte array for sending
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        ObjectOutputStream os = new ObjectOutputStream(out);
                        os.writeObject(ee);
                        byte[] sendData = out.toByteArray();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(addr), sendport);
                        socket.send(sendPacket);

                        // byte array for receiving
                        byte[] receiveData = new byte[65535];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socket.receive(receivePacket);

                        byte[] data = receivePacket.getData();
                        ByteArrayInputStream in = new ByteArrayInputStream(data);
                        ObjectInputStream is = new ObjectInputStream(in);
                        StringBuilder response = new StringBuilder();
                        if(cmd.equals("store")){
                            response.append("\n");
                        }
                        response.append((String) is.readObject());
                        System.out.println("server response: " + response);

                    }catch(SocketException e){
                        System.out.println("Socket Exception: " + e.getMessage());
                        e.printStackTrace();
                    }

                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Client exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            /// UDP SERVER
            if (args[0].equals("us"))
            {
                System.out.println("UDP SERVER");
                SharedResource keyValueStorage = new SharedResource();
                int port = Integer.parseInt(args[1]);

                try (DatagramSocket serverSocket = new DatagramSocket(port)) {
                    System.out.println("Server is listening on port " + port);

                    while (true) {
                        byte[] buffer = new byte[65535];
                        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                        serverSocket.receive(incoming);

                        new Thread(() -> {
                            try {
                                // byte array for receiving
                                byte[] data = incoming.getData();
                                ByteArrayInputStream in = new ByteArrayInputStream(data);
                                ObjectInputStream is = new ObjectInputStream(in);

                                ExtendedEntry<String, String> entry = (ExtendedEntry<String,String>) is.readObject();
                                System.out.println("Received request: " + " " + entry.getMethodName()
                                                                        + " " + entry.getKey()
                                                                        + " " + entry.getValue());

                                // process the request
                                String response = UDPServerProcess(entry, keyValueStorage);

                                // byte array for sending
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                ObjectOutputStream os = new ObjectOutputStream(out);
                                os.writeObject(response);

                                byte[] responseData = out.toByteArray();
                                DatagramPacket responsePacket = new DatagramPacket(
                                        responseData, responseData.length, incoming.getAddress(), incoming.getPort());

                                serverSocket.send(responsePacket);

                            } catch (IOException | ClassNotFoundException ex) {
                                System.out.println("Server exception: " + ex.getMessage());
                                ex.printStackTrace();
                            }
                        }).start();
                    }

                } catch (IOException ex) {
                    System.out.println("Server exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
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
    public static void TCPServerProcess(ExtendedEntry<String,String> entry, SharedResource keyValueStorage, PrintWriter out){
        switch (entry.getMethodName()) {
            case "put":
                keyValueStorage.put(entry.getKey(), entry.getValue());
                out.println(entry.getMethodName() + " key=" + entry.getKey());
                break;
            case "get":
                String value = keyValueStorage.get(entry.getKey());
                out.println(entry.getMethodName() + " key=" + entry.getKey() + " val=" + value);
                break;
            case "del":
                keyValueStorage.delete(entry.getKey());
                out.println(entry.getMethodName() + " key=" + entry.getKey());
                break;
            case "store":
                ArrayList<StringBuilder> keyValueList = keyValueStorage.store();
                for (StringBuilder keyValue : keyValueList) {
                    out.println(keyValue);
                }
                break;
        }

    }
    public static String UDPServerProcess(ExtendedEntry<String,String> entry, SharedResource keyValueStorage){
        switch (entry.getMethodName()) {
            case "put":
                keyValueStorage.put(entry.getKey(), entry.getValue());
                return entry.getMethodName() + " key=" + entry.getKey();
            case "get":
                String value = keyValueStorage.get(entry.getKey());
                return entry.getMethodName() + " key=" + entry.getKey() + " val=" + value;
            case "del":
                keyValueStorage.delete(entry.getKey());
                return entry.getMethodName() + " key=" + entry.getKey();
            case "store":
                ArrayList<StringBuilder> keyValueList = keyValueStorage.store();
                StringBuilder response = new StringBuilder();
                for (StringBuilder keyValue : keyValueList) {
                    response.append(keyValue).append("\n");
                }
                // truncate if size > 65000
                if(response.length() > 65000){
                    response.delete(65000, response.length());
                    response.insert(0, "TRIMMED:\n");
                }

                return response.toString();
            default:
                return "Invalid command";
        }
    }
    
}
