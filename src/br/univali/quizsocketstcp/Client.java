/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.univali.quizsocketstcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author biankatpas
 */

public class Client
{
    public static void main(String[] args) 
    {
        try
        {
            String addr = "127.0.0.1";
            int port = 12345;
            String message = "msg de teste inicial";

            Socket socket = new Socket(addr,port);

            DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
            dataOutput.writeUTF(message);
            
            DataInputStream dataInput = new DataInputStream(socket.getInputStream());
            String data = dataInput.readUTF();
            
            socket.close();

            if(data.equals(message))
            {
                System.out.println("Echo "+data+" successfull.");
            }
            else
            {
                System.out.println("sent: "+message);
                System.out.println("received: "+data);
            }
        }
        catch(Exception e)
        {
            System.err.println("An exception ocourred: "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
