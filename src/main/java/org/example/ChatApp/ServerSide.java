package org.example.ChatApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class ServerSide {
    public static ServerSocket serverSocket;
    public ServerSide(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void serverStart(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("New Friend Connected To the Chat");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void closerServer(){

        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(9818);
        ServerSide serverSide = new ServerSide(serverSocket);
        serverSide.serverStart();
    }


    }
