/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.univali.quizsocketstcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author biankatpas
 */

public class Server
{
    public static void main(String[] args)
    {
        
        ServerSocket serverSocket; 
        int port;
                
        if(args.length < 1) 
          port = 12345;
        else
          port = Integer.parseInt(args[0]);
        
         try
         {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor do Quiz TCP escutando na porta "+serverSocket.getLocalPort());
            
            ArrayList<Pergunta> perguntas = new ArrayList<>();
            perguntas.add(new Pergunta("Sobre sockets TCP, complete a afirmativa: A operação bind...\n(a) ...associa o socket a um endereço local.\n(b) ...permite que o socket receba requisições de conexão.", "a"));
            perguntas.add(new Pergunta("Sobre sockets UDP. A comunicação UDP é feita através da(s) classe(s)\n(a) DatagramSocket.\n(b) DatagramSocket e DatagramPacket.\n(c) DatagramPacket.", "b"));
            perguntas.add(new Pergunta("Sobre sockets UDP, complete a afirmativa. As comunicações ocorrem através da troca de...\n(a) datagramas.\n(b) streams de entrada e saída", "a"));
            int corretas = 0, erradas = 0;
            
            boolean exit = false;
            for(int i = 0; i<perguntas.size(); i++)
            {
                if(exit) break;
                
                Socket socket = serverSocket.accept();
                                             
                //recebe a primeira msg do cliente
                System.out.println("Recebendo mensagem de "+
                  socket.getInetAddress().getHostName()+":"+socket.getPort());
                DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                String data = dataInput.readUTF();
                
                //verifica se é para sair
                if(data.equalsIgnoreCase("exit"))
                    exit = true;
               
                //envia a pergunta para o cliente
                DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
                System.out.println("\nMensagem a ser enviada para o cliente (echo): "+perguntas.get(i).getPergunta());
                dataOutput.writeUTF(perguntas.get(i).getPergunta());
                
                //recebe a resposta do cliente
                dataInput = new DataInputStream(socket.getInputStream());
                data = dataInput.readUTF();
                System.out.println("\nMensagem recebida do cliente: "+data);
              
                //verifica se a resposta esta correta
                if(data.equalsIgnoreCase(perguntas.get(i).getResposta()))
                {
                    data = "Parabéns! Resposta correta!"; 
                    corretas++;
                }
                else
                {
                    data = "Errado! A resposta correta é: "+perguntas.get(i).getResposta(); 
                    erradas++;
                }
                
                data+= "\n\nPerguntas respondidas = "+(i+1)+"\nCorretas = "+corretas+"\nErradas = "+erradas+"\n";
                
                //manda a resposta do quiz para o cliente
                dataOutput = new DataOutputStream(socket.getOutputStream());
                System.out.println("\nMensagem a ser enviada para o cliente (echo): "+data);
                dataOutput.writeUTF(data);
                
                //fecha a conexao
                socket.close();
            }
            //fecha a conexao
            serverSocket.close();
        }
         catch(IOException e)
        {
            System.err.println("A exception occurred: "+e.getMessage());
            System.exit(-1);
        }
    }
}
