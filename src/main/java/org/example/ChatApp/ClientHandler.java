package org.example.ChatApp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler>clientHandlers = new ArrayList<>();
    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    private String name;



    ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = bufferedReader.readLine();
            clientHandlers.add(this);
            boradcastMessage("User " + name + " has entered in the room");
        }
        catch (IOException e){
            e.printStackTrace();
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }


    @Override
    public void run() {
    String messageFromClient;

    while (!socket.isClosed()) {
        try {
            messageFromClient = bufferedReader.readLine();
            boradcastMessage(messageFromClient);
        }
        catch (IOException e){
            e.printStackTrace();
            closeAll(socket, bufferedReader,  bufferedWriter);
            break;
        }
    }
    }

    public void boradcastMessage(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if(!clientHandler.name.equals(name)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch(IOException e){
                closeAll(socket,bufferedReader, bufferedWriter);

            }
        }
    }

    public void removeClient(){
        clientHandlers.remove(this);
        boradcastMessage("User " +name +"Has Gone");
    }

    public void closeAll(Socket socket, BufferedReader buffReader, BufferedWriter buffWriter){

        // handle the removeClient funciton

        removeClient();
        try{
            if(buffReader!= null){
                buffReader.close();
            }
            if(buffWriter != null){
                buffWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }

        catch (IOException e){
            e.getStackTrace();
        }

    }

}
