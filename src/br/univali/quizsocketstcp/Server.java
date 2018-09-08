/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.univali.quizsocketstcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author biankatpas
 */

public class Server
{
    public static void main(String[] args) 
    {
        try
        {
            int port = 12345;

            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Listening to port "+port);
            
            boolean exit = false;
            do
            {
                Socket socket = serverSocket.accept();
  
                System.out.println("Received message from "+
                  socket.getInetAddress().getHostName()+":"+socket.getPort());
                
                DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                String data = dataInput.readUTF();
            
                if(data.equalsIgnoreCase("exit"))
                {
                    exit = true;
                }
                
                System.out.println("echoing "+data);
                
                DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
                dataOutput.writeUTF(data);
                
                socket.close();
            }
            while(!exit);
            serverSocket.close();
        }
        catch(Exception e)
        {
            System.err.println("And exception ocourred: "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
