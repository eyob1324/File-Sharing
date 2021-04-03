package sample;

import java.io.*;
import java.net.*;
import java.util.*;

public class FileServerThread extends Thread{
    protected Socket socket       = null;
    protected PrintWriter out     = null;
    protected BufferedReader in   = null;


    protected Vector messages     = null;

    public FileServerThread(Socket socket, Vector messages) {
        super();
        this.socket = socket;
        this.messages = messages;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }
    }

    public void run() {
        // initialize interaction
        out.println("Connected to Chat Server");
        out.println("200 Ready For Chat");

        boolean endOfSession = false;
        while(!endOfSession) {
            endOfSession = processCommand();
        }
        try {
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean processCommand() {
        String message = null;
        try {
            message = in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading command from socket.");
            return true;
        }
        if (message == null) {
            return true;
        }
        StringTokenizer st = new StringTokenizer(message);
        String command = st.nextToken();
        String args = null;
        if (st.hasMoreTokens()) {
            args = message.substring(command.length()+1, message.length());
        }
        return processCommand(command, args);
    }

    protected boolean processCommand(String command, String arguments) {
        if(command.equalsIgnoreCase("DownLoad")){
            final File Server_folder = new File("ServerFiles");
            File[] listOfFilesServer = Server_folder.listFiles();
            for(File files:listOfFilesServer){
                if(files.getName().equals(arguments)){
                    File FileReading = new File(String.valueOf(files.getAbsoluteFile()));
                    Scanner sc = null;
                    try {
                        sc = new Scanner(files);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    String FileSending = " ";
                    while (sc.hasNextLine()) {
                        FileSending +=sc.nextLine()+" ";
                    }
                    out.println(FileSending);
                }
            }


         }else if(command.equalsIgnoreCase("Upload")){
            String path = "";
            String Filename = "";
            File IncomingFile = null;

                try {
                    if (arguments.contains(".txt")) {
                        Filename = arguments;
                        path = "ServerFiles/" + arguments;
                        System.out.println(path);

                         IncomingFile = new File(path);

                        if(!IncomingFile.exists()) {
                            IncomingFile.createNewFile();
                        }
                        String FileReceving = in.readLine();
                        FileWriter fw=new FileWriter(path);
                        String [] Sentences =FileReceving.split(" ");
                        for(String Sentence:Sentences){
                            fw.write(Sentence+" ");
                        }
                        fw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }else if(command.equalsIgnoreCase("Dir")){
            String FilesSending = "";
            final File Server_folder = new File("ServerFiles");
            File[] listOfFilesServer = Server_folder.listFiles();
            for (File file :listOfFilesServer) {
                if (file.isFile()) {
                   FilesSending += file.getName()+" ";

                }
            }
            out.println(FilesSending);

        }

      return false;
    }








}
