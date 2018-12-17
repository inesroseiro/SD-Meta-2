package dropsrc.src;

import dropsrc.src.Artist;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;


public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.1.224.1";
    private int PORT = 5000;
    private int CLIENT_PORT = 4321;
    private long SLEEP_TIME = 5000;
    private int RMI_PORT = 7000;
    private int database_uid = 0;

    private CopyOnWriteArrayList<User> usersArrayList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Artist> artistsArrayList = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        /*---*/
        User admin = new User("admin", "admin");
        admin.setRights(true);
        usersArrayList.add(admin);
        System.out.println("Please enter database UID for this server: ");
        Scanner serverUIDScanner = new Scanner(System.in);
        String serverUID = serverUIDScanner.nextLine();
        int database_uid = Integer.parseInt(serverUID);
        System.out.println("Database UID for this server: " + database_uid);
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        System.out.println(this.getName() + " running...");
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            //files stuff
            String userFilesString = "/Users/dingo/Desktop/SD-Meta-2/Primes 2/src/dropsrc/src/users" + database_uid + ".txt";
            String databaseFilesString = "/Users/dingo/Desktop/SD-Meta-2/Primes 2/src/dropsrc/src/database" + database_uid + ".txt";

            File userFiles = new File(userFilesString);
            File databaseFiles = new File(databaseFilesString);

            if(databaseFiles.exists() && databaseFiles.length() != 0){
                readFromDatabaseFile(databaseFiles);
            }

            if(userFiles.exists() && userFiles.length()!= 0){
                readFromUsersFile(userFiles);
            }

            while (true) {
                byte[] receiveBuffer = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String receiveString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from RMI: " + receiveString);

                if (receiveString.contains("type|register")) {
                    String[] splitString = receiveString.split(";");
                    String[] getUsernameString = splitString[1].split("\\|");
                    String[] getPasswordString = splitString[2].split("\\|");
                    String getUsername = getUsernameString[1];
                    String getPassword = getPasswordString[1];
                    boolean flag;
                    System.out.println("Trying to register user with Username:" + getUsername + " Password:" + getPassword);
                    flag = register(getUsername, getPassword, userFiles);
                    System.out.println(flag);
                    if (flag) {
                        String sendRegister = "type|status;logged|on;msg|UserRegistered";
                        byte[] sendBufferRegister = sendRegister.getBytes();
                        DatagramPacket sendRegisterPacket = new DatagramPacket(sendBufferRegister, sendBufferRegister.length, group, RMI_PORT);
                        sendSocket.send(sendRegisterPacket);
                        System.out.println();
                        System.out.println("Sent to RMI: " + sendRegister);
                    } else if(!flag) {
                        System.out.println("Username already in use");
                        String sendError = "type|status;logged|off;msg|ErrorWithUsername";
                        byte[] sendBufferError = sendError.getBytes();
                        DatagramPacket sendErrorPacket = new DatagramPacket(sendBufferError, sendBufferError.length, group, RMI_PORT);
                        sendSocket.send(sendErrorPacket);
                        System.out.println("Sent to RMI: " + sendError);
                    }
                } else if (receiveString.contains("type|login")) {
                    String[] splitString = receiveString.split(";");
                    String[] getUsernameString = splitString[1].split("\\|");
                    String[] getPasswordString = splitString[2].split("\\|");
                    String getUsername = getUsernameString[1];
                    String getPassword = getPasswordString[1];
                    boolean flag;
                    boolean check;
                    System.out.println("Trying to login user with Username:" + getUsername + " Password:" + getPassword);

                    flag = login(getUsername, getPassword);
                    if (flag) {
                        check = checkRights(getUsername);
                        if (check) {
                            String sendLogin = "type|status;logged|on;msg|WelcomeToDropMusic|privilege;editor|";
                            byte[] sendBufferLogin = sendLogin.getBytes();
                            DatagramPacket sendLoginPacket = new DatagramPacket(sendBufferLogin, sendBufferLogin.length, group, RMI_PORT);
                            sendSocket.send(sendLoginPacket);
                            System.out.println("Sent to RMI: " + sendLogin);
                        } else {
                            String sendLogin = "type|status;logged|on;msg|WelcomeToDropMusic|privilege;user|";
                            byte[] sendBufferLogin = sendLogin.getBytes();
                            DatagramPacket sendLoginPacket = new DatagramPacket(sendBufferLogin, sendBufferLogin.length, group, RMI_PORT);
                            sendSocket.send(sendLoginPacket);
                            System.out.println("Sent to RMI: " + sendLogin);
                        }
                    } else if(!flag) {
                        String sendLogin = "type|status;logged|on;msg|ErrorWithLogin";
                        byte[] sendBufferLogin = sendLogin.getBytes();
                        DatagramPacket sendLoginPacket = new DatagramPacket(sendBufferLogin, sendBufferLogin.length, group, RMI_PORT);
                        sendSocket.send(sendLoginPacket);
                        System.out.println("Sent to RMI: " + sendLogin);
                    }

                } else if (receiveString.contains("type|check;rights")) {
                    String[] splitString = receiveString.split(";");
                    String getUsername = splitString[2];
                    System.out.println("Trying to change rights of user with Username:" + getUsername);

                    boolean check = checkRights(getUsername);
                    if (!check) {
                        setRights(getUsername, userFiles);

                        System.out.println("Changed rights of " + getUsername + " to editor.");
                        String sendCheck = "type|check;rights|changed";
                        byte[] sendBufferCheck = sendCheck.getBytes();
                        DatagramPacket sendCheckPacket = new DatagramPacket(sendBufferCheck, sendBufferCheck.length, group, RMI_PORT);
                        sendSocket.send(sendCheckPacket);
                        System.out.println("Sent to RMI: " + sendCheck);

                        //mensagem para verificar se é preciso atualizar o boolean ou se o user foi notificado
                        byte[] receiveFeedback = new byte[256];
                        DatagramPacket receiveFeedbackPacket = new DatagramPacket(receiveFeedback, receiveFeedback.length);
                        socket.receive(receiveFeedbackPacket);
                        String receiveFeedbackString = new String(receiveFeedbackPacket.getData(), 0, receiveFeedbackPacket.getLength());
                        System.out.println("Received from RMI: " + receiveFeedbackString);

                        if (receiveFeedbackString.equals("type|check;rights|notified")) {
                            System.out.println("User has been notified!");

                        } else if (receiveFeedbackString.contains("type|store;rights|username;")) {
                            //percorrer a lista com o user e meter lá o bool a true, user vai ser notificado quando
                            //ficar online
                            System.out.println("User " + getUsername + " got his/her rights updated.");
                            notifyRights(getUsername);
                        }

                    } else {
                        String sendCheck = "type|check;rights|error";
                        byte[] sendBufferCheck = sendCheck.getBytes();
                        DatagramPacket sendCheckPacket = new DatagramPacket(sendBufferCheck, sendBufferCheck.length, group, RMI_PORT);
                        sendSocket.send(sendCheckPacket);
                        System.out.println("Sent to RMI: " + sendCheck);
                    }

                } else if (receiveString.contains("type|notification_rights;username|")) {

                    String[] splitString = receiveString.split(";");
                    String getUsername = (splitString[1].split("\\|"))[1];

                    if (checkNotifyLater(getUsername)) {
                        //user tem de ser notificado
                        String sendNotification = "type|notification_rights;check";
                        byte[] sendBufferCheck = sendNotification.getBytes();
                        DatagramPacket sendCheckPacket = new DatagramPacket(sendBufferCheck, sendBufferCheck.length, group, RMI_PORT);
                        sendSocket.send(sendCheckPacket);
                        System.out.println("Sent to RMI: " + sendNotification);
                        //meter bool a false
                        setNotifyRights(getUsername, userFiles);
                    } else {
                        //user nao precisa de ser notificado
                        String sendNotification = "type|notification_rights;fail";
                        byte[] sendBufferCheck = sendNotification.getBytes();
                        DatagramPacket sendCheckPacket = new DatagramPacket(sendBufferCheck, sendBufferCheck.length, group, RMI_PORT);
                        sendSocket.send(sendCheckPacket);
                        System.out.println("Sent to RMI: " + sendNotification);
                    }

                } else if (receiveString.contains("type|insert_album")) {
                    String[] splitString = receiveString.split(";");

                    String getAlbumName = (splitString[1].split("\\|"))[1];

                    String getDescription = (splitString[2].split("\\|"))[1];

                    String getMusicGenre = (splitString[3].split("\\|"))[1];

                    String getDate = (splitString[4].split("\\|"))[1];

                    String getArtistName = (splitString[5].split("\\|"))[1];

                    boolean flag;
                    flag = checkArtistExist(getArtistName);
                    boolean flag2 = checkAlbumExist(getAlbumName, getArtistName);
                    System.out.println("Flag Artist: "+flag + " Flag Album: "+flag2);
                    System.out.println(flag);
                    if (!flag && flag2) {
                        addAlbum(getAlbumName, getDescription, getMusicGenre, getDate, getArtistName, databaseFiles);
                        String sendAlbum = "type|insert_album;successful";
                        byte[] sendBufferAlbum = sendAlbum.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferAlbum, sendBufferAlbum.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendAlbum);
                    } else if(flag || !flag2){
                        String sendArtist = "type|insert_album;error in insert album";
                        byte[] sendBufferAlbum = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferAlbum, sendBufferAlbum.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    }


                } else if (receiveString.contains("type|insert_artist")) {
                    String[] splitString = receiveString.split(";");
                    String artistName = splitString[1];
                    String[] splitString2 = splitString[2].split("\\|");
                    String artistDescription = splitString2[1];

                    boolean flag;
                    flag = checkArtistExist(artistName);
                    //System.out.println(flag);
                    if (flag) {
                        //Adiciona artista a lista
                        addArtist(artistName, artistDescription, databaseFiles);
                        String sendArtist = "type|insert_artist;successful";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    } else {
                        String sendArtist = "type|insert_artist;error in insert artist";
                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    }


                } else if (receiveString.contains("type|insert_music")) {
                    String[] splitString = receiveString.split(";");

                    String getMusicName = (splitString[1].split("\\|"))[1];

                    String getMusicGenre = (splitString[2].split("\\|"))[1];

                    String getDuration = (splitString[3].split("\\|"))[1];

                    String getArtistName = (splitString[4].split("\\|"))[1];

                    String getAlbumName = (splitString[5].split("\\|"))[1];

                    String getLyrics = (splitString[6].split("\\|"))[1];

                    String getDate = (splitString[7].split("\\|"))[1];

                    boolean flag;
                    boolean check;
                    boolean check2;
                    //Se o album existir
                    //se o artista existir

                    flag = checkArtistExist(getArtistName);
                    check = checkAlbumExist(getAlbumName, getArtistName);
                    check2 = checkMusicRepetition(getArtistName, getAlbumName, getMusicName);

                    System.out.println(flag);
                    if (!flag && !check && check2) {
                        //Adiciona musica a lista
                        addSong(getMusicName, getMusicGenre, getDuration, getDate, getLyrics, getAlbumName, getArtistName, databaseFiles);
                        String sendArtist = "type|insert_music;successful";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    } else {
                        String sendArtist = "type|insert_music;error in insert music";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    }
                } else if (receiveString.contains("type|remove_music")) {
                    String[] splitString = receiveString.split(";");
                    //nome musica
                    String[] splitString1 = splitString[1].split("\\|");
                    String musicName = splitString1[1];
                    //artista
                    String[] splitString2 = splitString[2].split("\\|");
                    String artistName = splitString2[1];
                    //album
                    String[] splitString3 = splitString[3].split("\\|");
                    String albumName = splitString3[1];

                    boolean flag;
                    boolean check;
                    boolean check2;
                    //Se o album existir
                    //se o artista existir

                    flag = checkArtistExist(artistName);
                    check = checkAlbumExist(albumName, artistName);

                    System.out.println(flag);
                    if (!flag && !check) {
                        removeMusic(musicName, artistName, albumName, databaseFiles);
                        String sendArtist = "type|remove_music;successful";
                        byte[] sendBufferRemoveMusic = sendArtist.getBytes();
                        DatagramPacket removeMusicPacket = new DatagramPacket(sendBufferRemoveMusic, sendBufferRemoveMusic.length, group, RMI_PORT);
                        sendSocket.send(removeMusicPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    } else {
                        String sendArtist = "type|remove_music;error in remove music";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    }


                } else if (receiveString.contains("type|remove_album")) {
                    String[] splitString = receiveString.split(";");
                    //album
                    String[] splitString1 = splitString[1].split("\\|");
                    String albumName = splitString1[1];

                    //artista
                    String[] splitString2 = splitString[2].split("\\|");
                    String artistName = splitString2[1];

                    boolean flag;
                    boolean check;
                    //Se o album existir
                    //se o artista existir

                    flag = checkArtistExist(artistName);
                    check = checkAlbumExist(albumName, artistName);

                    System.out.println(flag);
                    if (!flag && !check) {
                        removeAlbum(artistName, albumName, databaseFiles);

                        String sendArtist = "type|remove_album;successful";
                        byte[] sendBufferRemoveMusic = sendArtist.getBytes();
                        DatagramPacket removeMusicPacket = new DatagramPacket(sendBufferRemoveMusic, sendBufferRemoveMusic.length, group, RMI_PORT);
                        sendSocket.send(removeMusicPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    } else {
                        String sendArtist = "type|remove_album;error in remove album";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    }
                } else if (receiveString.contains("type|remove_artist")) {
                    String[] splitString = receiveString.split(";");
                    //album
                    String[] splitString1 = splitString[1].split("\\|");
                    String artistName = splitString1[1];

                    boolean flag;

                    flag = checkArtistExist(artistName);

                    System.out.println(flag);
                    if (!flag) {
                        removeArtist(artistName, databaseFiles);
                        String sendArtist = "type|remove_artist;successful";
                        byte[] sendBufferRemoveMusic = sendArtist.getBytes();
                        DatagramPacket removeMusicPacket = new DatagramPacket(sendBufferRemoveMusic, sendBufferRemoveMusic.length, group, RMI_PORT);
                        sendSocket.send(removeMusicPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    } else {
                        String sendArtist = "type|remove_artist;error in remove album";

                        byte[] sendBufferArtist = sendArtist.getBytes();
                        DatagramPacket artistPacket = new DatagramPacket(sendBufferArtist, sendBufferArtist.length, group, RMI_PORT);
                        sendSocket.send(artistPacket);
                        System.out.println("Sent to RMI: " + sendArtist);
                    }
                } else if (receiveString.contains("type|edit_artist")) {

                    String[] splitString = receiveString.split(";");

                    System.out.println(splitString[0]);
                    System.out.println(splitString[1]);
                    System.out.println(splitString[2]);
                    System.out.println(splitString[3]);

                    String[] splitString2 = splitString[1].split("\\|");
                    String getOldArtistName = splitString2[1];

                    String[] splitString3 = splitString[2].split("\\|");
                    String getNewArtistName = splitString3[1];

                    String[] splitString4 = splitString[3].split("\\|");
                    String getDescription = splitString4[1];

                    boolean flag = editArtist(getOldArtistName, getNewArtistName, getDescription, databaseFiles);

                    System.out.println(flag);
                    if (flag) {
                        String sendEditArtist = "type|edit_artist;successful";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    } else {
                        String sendEditArtist = "type|edit_artist;error in edit artist";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    }

                } else if (receiveString.contains("type|edit_album")) {

                    String[] splitString = receiveString.split(";");

                    String getArtistName = (splitString[1].split("\\|"))[1];

                    String getOldAlbumName = (splitString[2].split("\\|"))[1];

                    String getNewAlbumName = (splitString[3].split("\\|"))[1];

                    String getDescription = (splitString[4].split("\\|"))[1];

                    String getMusicGenre = (splitString[5].split("\\|"))[1];

                    String getDate = (splitString[6].split("\\|"))[1];

                    boolean flag = editAlbum(getArtistName, getOldAlbumName, getNewAlbumName, getDescription, getMusicGenre, getDate, databaseFiles);

                    System.out.println(flag);
                    if (flag) {
                        String sendEditAlbum = "type|edit_album;successful";
                        byte[] sendBufferEditAlbum = sendEditAlbum.getBytes();
                        DatagramPacket sendEditAlbumPacket = new DatagramPacket(sendBufferEditAlbum, sendBufferEditAlbum.length, group, RMI_PORT);
                        sendSocket.send(sendEditAlbumPacket);
                        System.out.println("Sent to RMI: " + sendEditAlbum);
                    } else {
                        String sendEditAlbum = "type|edit_album;error in edit album";
                        byte[] sendBufferEditAlbum = sendEditAlbum.getBytes();
                        DatagramPacket sendEditAlbumPacket = new DatagramPacket(sendBufferEditAlbum, sendBufferEditAlbum.length, group, RMI_PORT);
                        sendSocket.send(sendEditAlbumPacket);
                        System.out.println("Sent to RMI: " + sendEditAlbum);
                    }

                } else if (receiveString.contains("type|edit_music")) {

                    String[] splitString = receiveString.split(";");

                    String getArtistName = (splitString[1].split("\\|"))[1];

                    String getAlbumName = (splitString[2].split("\\|"))[1];

                    String getOldMusicName = (splitString[3].split("\\|"))[1];

                    String getNewMusicName = (splitString[4].split("\\|"))[1];

                    String getGenre = (splitString[5].split("\\|"))[1];

                    String getDuration = (splitString[6].split("\\|"))[1];

                    String getDate = (splitString[7].split("\\|"))[1];

                    String getLyrics = (splitString[8].split("\\|"))[1];

                    boolean flag = editMusic(getArtistName, getAlbumName, getOldMusicName, getNewMusicName, getGenre, getDuration, getDate, getLyrics, databaseFiles);

                    System.out.println(flag);

                    if (flag) {
                        String sendEditMusic = "type|edit_music;successful";
                        byte[] sendBufferEditMusic = sendEditMusic.getBytes();
                        DatagramPacket sendEditMusicPacket = new DatagramPacket(sendBufferEditMusic, sendBufferEditMusic.length, group, RMI_PORT);
                        sendSocket.send(sendEditMusicPacket);
                        System.out.println("Sent to RMI: " + sendEditMusic);
                    } else {
                        String sendEditMusic = "type|edit_music;error in edit music";
                        byte[] sendBufferEditMusic = sendEditMusic.getBytes();
                        DatagramPacket sendEditMusicPacket = new DatagramPacket(sendBufferEditMusic, sendBufferEditMusic.length, group, RMI_PORT);
                        sendSocket.send(sendEditMusicPacket);
                        System.out.println("Sent to RMI: " + sendEditMusic);
                    }

                } else if (receiveString.contains("type|write_critic")) {

                    String[] splitString = receiveString.split(";");
                    //user
                    String[] splitString1 = splitString[1].split("\\|");
                    String username = splitString1[1];
                    //artista
                    String[] splitString2 = splitString[2].split("\\|");
                    String artistName = splitString2[1];
                    //album
                    String[] splitString3 = splitString[3].split("\\|");
                    String albumName = splitString3[1];
                    //rate
                    String[] splitString4 = splitString[4].split("\\|");
                    String rate = splitString4[1];
                    //Desc
                    String[] splitString5 = splitString[5].split("\\|");
                    String descricao = splitString5[1];

                    int intRate = Integer.parseInt(rate);

                    boolean flag = checkAlbumExist(albumName, artistName);
                    boolean flag2 = checkArtistExist(artistName);
                    boolean flag3 = checkCritics(artistName, albumName, username);

                    //System.out.println(flag);
                    if (!flag && !flag2 && flag3) {
                        //add critic
                        addCritic(username, artistName, albumName, intRate, descricao, databaseFiles);
                        String sendWriteCritic = "type|write_critic;successful";
                        byte[] sendWriteCriticBuffer = sendWriteCritic.getBytes();
                        DatagramPacket sendWriteCriticPacket = new DatagramPacket(sendWriteCriticBuffer, sendWriteCriticBuffer.length, group, RMI_PORT);
                        sendSocket.send(sendWriteCriticPacket);
                        System.out.println("Sent to RMI: " + sendWriteCritic);
                    } else {
                        String sendWriteCritic = "type|write_critic;error in write critic";
                        byte[] sendWriteCriticBuffer = sendWriteCritic.getBytes();
                        DatagramPacket sendWriteCriticPacket = new DatagramPacket(sendWriteCriticBuffer, sendWriteCriticBuffer.length, group, RMI_PORT);
                        sendSocket.send(sendWriteCriticPacket);
                        System.out.println("Sent to RMI: " + sendWriteCritic);
                    }

                } else if (receiveString.contains("type|search_album_name")) {

                    String[] splitString = receiveString.split(";");
                    //user
                    String[] splitString1 = splitString[1].split("\\|");
                    String username = splitString1[1];
                    //albumName
                    String[] splitString2 = splitString[2].split("\\|");
                    String albumName = splitString2[1];

                    boolean flag = checkAlbum2(albumName);


                    System.out.println(flag);
                    if (flag) {
                        //search albuns
                        String sendEditArtist = searchForAlbumName(albumName);
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    } else {
                        String sendEditArtist = "type|search_album_name;error in search_album_name";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    }

                } else if (receiveString.contains("type|search_album_artist")) {

                    String[] splitString = receiveString.split(";");
                    //user
                    String[] splitString1 = splitString[1].split("\\|");
                    String username = splitString1[1];
                    //artistName
                    String[] splitString2 = splitString[2].split("\\|");
                    String artist = splitString2[1];

                    boolean flag = checkArtistExist(artist);


                    System.out.println(flag);
                    if (!flag) {
                        //search albuns
                        String sendEditArtist = searchForArtistName(artist);
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    } else {
                        String sendEditArtist = "type|search_album_artist;error in search_album_artist";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    }

                } else if (receiveString.contains("type|view_artist_details")) {

                    String[] splitString = receiveString.split(";");
                    //user
                    String[] splitString1 = splitString[1].split("\\|");
                    String username = splitString1[1];
                    //artistName
                    String[] splitString2 = splitString[2].split("\\|");
                    String artist = splitString2[1];

                    boolean flag = checkArtistExist(artist);


                    System.out.println(!flag);
                    if (!flag) {
                        //search albuns
                        String sendEditArtist = artistDetails(artist);
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    } else {
                        String sendEditArtist = "type|view_artist_details;error in view_artist_details";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    }

                } else if (receiveString.contains("type|view_album_details")) {

                    String[] splitString = receiveString.split(";");
                    //user
                    String[] splitString1 = splitString[1].split("\\|");
                    String username = splitString1[1];
                    //artistName
                    String[] splitString2 = splitString[2].split("\\|");
                    String artist = splitString2[1];
                    //albumName
                    String[] splitString3 = splitString[3].split("\\|");
                    String albumName = splitString3[1];

                    boolean flag = checkArtistExist(artist);
                    boolean flag2 = checkAlbumExist(albumName, artist);


                    System.out.println(!flag && !flag2);
                    if (!flag2) {
                        //search albuns
                        String sendEditArtist = albumDetails(artist, albumName);
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    } else {
                        String sendEditArtist = "type|view_album_details;error_in_view_album_details";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    }

                } else if (receiveString.contains("type|view_album_critics")) {


                    String [] splitString = receiveString.split(";");
                    //user
                    String [] splitString1 =splitString[1].split("\\|");
                    String username = splitString1[1];
                    //artistName
                    String [] splitString2 =splitString[2].split("\\|");
                    String artist = splitString2[1];
                    //albumName
                    String [] splitString3 =splitString[3].split("\\|");
                    String albumName = splitString3[1];

                    boolean flag = checkArtistExist(artist);
                    boolean flag2 = checkAlbumExist(albumName, artist);


                    System.out.println(!flag && !flag2);
                    if (!flag2) {
                        //search albuns
                        String sendEditArtist = albumCritics(artist,albumName);
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    } else {
                        String sendEditArtist = "type|view_album_critics;error_in_view_album_critics";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    }


                } else if (receiveString.contains("type|upload_music")) {
                    String[] splitString = receiveString.split(";");
                    //user
                    String[] splitString1 = splitString[1].split("\\|");
                    String username = splitString1[1];
                    System.out.println(username);
                    //path
                    String[] splitString2 = splitString[2].split("\\|");
                    String musicPath = splitString2[1];
                    System.out.println(musicPath);
                    String[] splitString3 = splitString[3].split("\\|");
                    String port = splitString3[1];
                    String[] splitString4 = splitString[4].split("\\|");
                    String serialNumber = splitString4[1];

                    String[] splitString5 = splitString[5].split("\\|");
                    String artistName = splitString5[1];
                    String[] splitString6 = splitString[6].split("\\|");
                    String albumName = splitString6[1];
                    String[] splitString7 = splitString[7].split("\\|");
                    String musicName = splitString7[1];



                    //System.out.println(port);

                    ServerSocket ssock = new ServerSocket(Integer.parseInt(port));
                    addUploadToSong(Integer.parseInt(serialNumber),albumName,artistName,musicName, databaseFiles);

                    String sendWaitMusic = "ok";
                    byte[] sendBufferWaitMusic = sendWaitMusic.getBytes();
                    DatagramPacket sendWaitMusicPacket = new DatagramPacket(sendBufferWaitMusic, sendBufferWaitMusic.length, group, RMI_PORT);
                    sendSocket.send(sendWaitMusicPacket);
                    System.out.println("Sent to RMI: " + sendWaitMusic);


                    if (musicUpload(ssock, Integer.parseInt(port), musicPath)) {
                        String sendEditMusic = "type|upload_music;successful";
                        byte[] sendBufferEditMusic = sendEditMusic.getBytes();
                        DatagramPacket sendEditMusicPacket = new DatagramPacket(sendBufferEditMusic, sendBufferEditMusic.length, group, RMI_PORT);
                        sendSocket.send(sendEditMusicPacket);
                        System.out.println("Sent to RMI: " + sendEditMusic);
                    } else {
                        String sendEditMusic = "type|upload_music;error in upload music";
                        byte[] sendBufferEditMusic = sendEditMusic.getBytes();
                        DatagramPacket sendEditMusicPacket = new DatagramPacket(sendBufferEditMusic, sendBufferEditMusic.length, group, RMI_PORT);
                        sendSocket.send(sendEditMusicPacket);
                        System.out.println("Sent to RMI: " + sendEditMusic);
                    }

                }
                else if (receiveString.contains("type|download_music")) {
                    String[] splitString = receiveString.split(";");
                    //user
                    String[] splitString1 = splitString[1].split("\\|");
                    String username = splitString1[1];
                    System.out.println(username);
                    //path
                    String[] splitString2 = splitString[2].split("\\|");
                    String musicPath = splitString2[1];
                    System.out.println(musicPath);
                    String[] splitString3 = splitString[3].split("\\|");
                    String port = splitString3[1];
                    String[] splitString4 = splitString[4].split("\\|");
                    String serialNumber = splitString4[1];

                    String[] splitString5 = splitString[5].split("\\|");
                    String artistName = splitString5[1];
                    String[] splitString6 = splitString[6].split("\\|");
                    String albumName = splitString6[1];
                    String[] splitString7 = splitString[7].split("\\|");
                    String musicName = splitString7[1];

                    System.out.println(port);
                    ServerSocket ssock = new ServerSocket(Integer.parseInt(port));

                    String sendWaitMusic = "ok";

                    byte[] sendBufferWaitMusic = sendWaitMusic.getBytes();
                    DatagramPacket sendWaitMusicPacket = new DatagramPacket(sendBufferWaitMusic, sendBufferWaitMusic.length, group, RMI_PORT);
                    sendSocket.send(sendWaitMusicPacket);
                    System.out.println("Sent to RMI: " + sendWaitMusic);

                    if (musicDownload(ssock, Integer.parseInt(port), musicPath,username,musicName)) {
                        String sendEditMusic = "type|download_music;successful";
                        byte[] sendBufferEditMusic = sendEditMusic.getBytes();
                        DatagramPacket sendEditMusicPacket = new DatagramPacket(sendBufferEditMusic, sendBufferEditMusic.length, group, RMI_PORT);
                        sendSocket.send(sendEditMusicPacket);
                        System.out.println("Sent to RMI: " + sendEditMusic);
                    } else {
                        String sendEditMusic = "type|download_music;error in download music";
                        byte[] sendBufferEditMusic = sendEditMusic.getBytes();
                        DatagramPacket sendEditMusicPacket = new DatagramPacket(sendBufferEditMusic, sendBufferEditMusic.length, group, RMI_PORT);
                        sendSocket.send(sendEditMusicPacket);
                        System.out.println("Sent to RMI: " + sendEditMusic);
                    }

                }
                else if (receiveString.contains("type|view_song_details")) {
                    String[] splitString = receiveString.split(";");
                    //user
                    String[] splitString1 = splitString[1].split("\\|");
                    String username = splitString1[1];
                    //artistName
                    String[] splitString2 = splitString[2].split("\\|");
                    String artist = splitString2[1];
                    //albumName
                    String[] splitString3 = splitString[3].split("\\|");
                    String albumName = splitString3[1];
                    //SongName
                    String[] splitString4 = splitString[4].split("\\|");
                    String song = splitString4[1];

                    boolean flag = checkArtistExist(artist);
                    boolean flag2 = checkAlbumExist(albumName, artist);


                    System.out.println(!flag && !flag2);
                    if (!flag2) {
                        //search albuns
                        String sendEditArtist = SongDetails(artist,albumName,song);
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    } else {
                        String sendEditArtist = "type|view_song_details;error_in_view_song_details";
                        byte[] sendBufferEditArtist = sendEditArtist.getBytes();
                        DatagramPacket sendEditArtistPacket = new DatagramPacket(sendBufferEditArtist, sendBufferEditArtist.length, group, RMI_PORT);
                        sendSocket.send(sendEditArtistPacket);
                        System.out.println("Sent to RMI: " + sendEditArtist);
                    }


                }


                try {
                    sleep((long) (Math.random() * SLEEP_TIME));
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
    }


    //-------- LOGIN/REGISTER--------//

    //register
    public synchronized boolean register(String username, String password, File file) throws IOException {
        for (User u : usersArrayList) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }
        User user = new User(username, password);
        user.setRights(false);
        usersArrayList.add(user);
        writeToUsersFile(file);
        return true;
    }

    //login
    public synchronized boolean login(String username, String password) {
        for (User u : usersArrayList) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }


    // --------- SEARCH (send) ------------ //

    public synchronized int countItems(String albumName) {
        int itemCount = 0;
        for (Artist a : artistsArrayList) {
            for (Album alb : a.getAlbums()) {
                if (alb.getAlbumName().equals(albumName)) {
                    itemCount++;

                }
            }

        }
        return itemCount;
    }

    public synchronized String searchForAlbumName(String albumName) {
        int itemCount = countItems(albumName);
        String stringFinal = "type|search_album_name;item_count|" + itemCount + ";";
        int i = 0;
        for (Artist a : artistsArrayList) {
            for (Album alb : a.getAlbums()) {
                if (alb.getAlbumName().equals(albumName)) {
                    stringFinal += "album_" + i + "_name|" + albumName + ";artist_" + i + "_name|" + a.getArtistName() + ";";
                    i++;
                }
            }
        }
        return stringFinal;
    }

    public synchronized int countAlbums(String artistName) {
        int itemCount = 0;
        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                for (Album alb : a.getAlbums()) {
                    itemCount++;
                }
            }
        }
        return itemCount;
    }

    public synchronized String searchForArtistName(String artistName) {
        int itemCount = countAlbums(artistName);
        String stringFinal = "type|search_album_artist;artist_name|" + artistName + ";item_count|" + itemCount + ";";
        int i = 0;
        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                for (Album alb : a.getAlbums()) {
                    stringFinal += "album_" + i + "_name|" + alb.getAlbumName() + ";";
                    i++;
                }
            }
        }
        return stringFinal;
    }


    //-------- CHECK--------//

    //check if user is editor
    public synchronized boolean checkRights(String username) {
        for (User u : usersArrayList) {
            if (u.getUsername().equals(username)) {
                if (u.isRights()) {
                    return true;
                }
            }
        }
        return false;
    }

    //check artist - funcao que verifica se o artista que vamos inserir ja existe - se ja existir retorna false, se nao retorna true
    public synchronized boolean checkArtistExist(String name) {
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(name)) {
                    return false;
                }
            }
        }
        return true;
    }

    //check album - Se ja existir um album com esse nome retorna falso
    public synchronized boolean checkAlbumExist(String albumName, String artistName) {
        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                if(a.getAlbums().isEmpty()){
                    return true;
                }
                else {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    //check music - se ja existir uma musica desse artista com esse nome falso
    public synchronized boolean checkMusicRepetition(String artistName, String albumName, String musicName) {
        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                for (Album alb : a.getAlbums()) {
                    if (alb.getAlbumName().equals(albumName)) {
                        for (Song s : alb.getSongs()) {
                            if (s.getSongName().equals(musicName)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    //notify later
    public synchronized void notifyRights(String username) {
        for (User u : usersArrayList) {
            if (u.getUsername().equals(username)) {
                u.setNotifiedRights(true);
            }
        }
    }

    //check notify later
    public synchronized boolean checkNotifyLater(String username) {
        for (User u : usersArrayList) {
            if (u.getUsername().equals(username)) {
                if (u.isNotifiedRights()) {
                    return true;
                }
            }
        }
        return false;
    }

    //check album 2
    public synchronized boolean checkAlbum2(String albumName) {
        for (Artist a : artistsArrayList) {
            for (Album alb : a.getAlbums()) {
                if (alb.getAlbumName().equals(albumName)) {
                    return true;
                }
            }
        }
        return false;
    }

    //changes boolean to false
    public synchronized void setNotifyRights(String username, File file) throws IOException {
        for (User u : usersArrayList) {
            if (u.getUsername().equals(username)) {
                u.setNotifiedRights(false);
                writeToUsersFile(file);
            }
        }
    }

    //-------- ADD --------//

    //add artist
    public synchronized void addArtist(String name, String description, File file) throws IOException {
        Artist a;
        a = new Artist(name, description);
        artistsArrayList.add(a);
        writeToDatabaseFile(file);

        //DEBUG
        System.out.println(a);
    }

    //add song
    public synchronized void addSong(String name, String genre, String duration, String udate, String lyrics, String albumName, String artistName, File file) throws IOException {

        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                for (Album alb : a.getAlbums()) {
                    if (alb.getAlbumName().equals(albumName)) {
                        Song s = new Song(name, genre, duration, udate, lyrics);
                        alb.addSongs(s);
                        //DEBUG
                        System.out.println(s);
                    }
                }
            }
        }
        writeToDatabaseFile(file);
    }

    //add album
    public synchronized void addAlbum(String albumName, String desc, String musicGenre, String date, String artistName, File file) throws IOException {
        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                Album album = new Album(albumName, desc, date, musicGenre);
                a.addAlbums(album);
                //DEBUG
                System.out.println(album);
            }
        }
        writeToDatabaseFile(file);
    }

    //add critic
    public synchronized void addCritic(String username, String artistName, String albumName, int rate, String critic, File file) throws IOException {
        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                for (Album alb : a.getAlbums()) {
                    if (alb.getAlbumName().equals(albumName)) {
                        Critic c = new Critic(rate, username, critic);
                        alb.addCritics(c);
                        alb.addAvgRate(rate);
                        //DEBUG
                        //System.out.println(c);
                        writeToDatabaseFile(file);
                    }
                }
            }
        }
    }

    //-------- REMOVE --------//

    //remove music
    public synchronized void removeMusic(String musicName, String artistName, String albumName, File file) throws IOException {
        Album getAlbum = null;
        Song removeSong = null;
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            getAlbum = alb;
                            if (!alb.getSongs().isEmpty()) {
                                for (Song s : alb.getSongs()) {
                                    if (s.getSongName().equals(musicName)) {
                                        removeSong = s;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        getAlbum.removeSongs(removeSong);
        writeToDatabaseFile(file);
    }

    //remove album
    public synchronized void removeAlbum(String artistName, String albumName, File file) throws IOException {
        Artist getArtist = null;
        Album removeAlbum = null;
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    getArtist = a;
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            removeAlbum = alb;
                        }
                    }
                }
            }
        }
        getArtist.removeAlbum(removeAlbum);
        writeToDatabaseFile(file);
    }

    //remove artist
    public synchronized void removeArtist(String artistName, File file) throws IOException {
        Artist removeArtist = null;
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    removeArtist = a;
                }
            }
        }
        artistsArrayList.remove(removeArtist);
        writeToDatabaseFile(file);
    }


    //-------- EDIT --------//

    //edit artist
    public synchronized boolean editArtist(String oldArtistName, String newArtistName, String description, File file) throws IOException {
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(oldArtistName)) {
                    a.setArtistName(newArtistName);
                    a.setDescArtist(description);

                    //DEBUG
                    System.out.println(a);
                    writeToDatabaseFile(file);
                    return true;
                }
            }
        }
        return false;
    }

    //edit album
    public synchronized boolean editAlbum(String artistName, String OldAlbumName, String newAlbumName, String description, String musicGenre, String udate, File file) throws IOException {
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(OldAlbumName)) {
                            alb.setAlbumName(newAlbumName);
                            alb.setDescription(description);
                            alb.setMusicalGenre(musicGenre);
                            alb.setReleaseDate(udate);

                            //DEBUG
                            System.out.println(alb);
                            writeToDatabaseFile(file);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //edit music
    public synchronized boolean editMusic(String artistName, String albumName, String oldMusicName, String newMusicName, String musicGenre, String duration, String date, String lyrics, File file) throws IOException {
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            if (!alb.getSongs().isEmpty()) {
                                for (Song s : alb.getSongs()) {
                                    if (s.getSongName().equals(oldMusicName)) {
                                        s.setSongName(newMusicName);
                                        s.setSongGenre(musicGenre);
                                        s.setDuration(duration);
                                        s.setReleaseDate(date);
                                        s.setLyrics(lyrics);

                                        //DEBUG
                                        System.out.println(s);
                                        writeToDatabaseFile(file);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    //-------- OTHER --------//

    //set rights
    public synchronized void setRights(String username, File file) throws IOException {
        for (User u : usersArrayList) {
            if (u.getUsername().equals(username)) {
                u.setRights(true);
                writeToUsersFile(file);
            }
        }
    }

    //avg rate calc
    public synchronized float calAvgRate(String artistName, String albumName) {
        int sum = 0;
        float totalRates = 0;
        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                for (Album alb : a.getAlbums()) {
                    if (alb.getAlbumName().equals(albumName)) {
                        if (!alb.getMediaRating().isEmpty()) {
                            for (int i = 0; i < alb.getMediaRating().size(); i++) {
                                sum += alb.getMediaRating().get(i);
                                totalRates++;

                            }
                        } else {
                            return 0;
                        }
                    }
                }
            }
        }
        return sum / totalRates;
    }

    //retorna false caso o user ja tena ter feito uma critica
    public synchronized boolean checkCritics(String artistName, String albumName,String username){
        int result = 0;
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            result = alb.seeCriticsUsers(username);
                        }
                    }
                }
            }
        }
        //Este user ja fez uma critica
        if(result == 0){
            return false;
        }
        else
            return true;

    }

    // --------------- VIEW DATA (send) ----------------- //

    //artist details
    public synchronized String artistDetails(String artistName) {
        int i = 0;
        String stringFinal = "type|view_artist_details;artist_name|" + artistName + ";description|";
        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                stringFinal += a.getDescArtist() + ";";
                //ver albuns de um artista
                for (Album alb : a.getAlbums()) {
                    stringFinal += "album_" + i + "_name|" + alb.getAlbumName() + ";";
                    i++;
                }
            }
        }

        return stringFinal;
    }

    //album details
    public synchronized String albumDetails(String artistName, String albumName) {
        int i = 0;
        String stringFinal = "type|view_album_details;artist_name|" + artistName + ";album_name|" + albumName;
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            stringFinal += ";description|"+alb.getDescription()+";genre|"+alb.getMusicalGenre()+";release|"+alb.getReleaseDate();
                            if (!alb.getSongs().isEmpty()) {
                                for (Song s : alb.getSongs()) {
                                    stringFinal += ";song_" + i + "_name|" + s.getSongName();
                                    i++;
                                }
                            }
                        }
                    }
                }
            }
        }
        stringFinal += ";" ;
        return stringFinal;
    }

    //song details
    public synchronized String SongDetails(String artistName, String albumName, String song) {
        String stringFinal = "type|view_song_details;artist_name|" + artistName + ";album_name|" + albumName + ";song_name|" + song;
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            if (!alb.getSongs().isEmpty()) {
                                for (Song s : alb.getSongs()) {
                                    if (s.getSongName().equals(song)) {
                                        stringFinal += ";genre|" + s.getSongGenre() + ";duration|" + s.getDuration() + ";date|" + s.getReleaseDate() + ";lyrics|" + s.getLyrics();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        stringFinal += ";";
        return stringFinal;
    }

    //album critics
    public synchronized String albumCritics(String artistName, String albumName) {
        float criticsAvg = calAvgRate(artistName, albumName);
        System.out.println("sajfsdufdshiu" + criticsAvg);
        String avgString = Float.toString(criticsAvg);

        //String avgString = "2";

        String stringFinal = "type|view_album_critics;artist_name|" + artistName + ";album_name|" + albumName + ";avg_rate|" + avgString + ";";
        if (!artistsArrayList.isEmpty()) {
            for (Artist a : artistsArrayList) {
                if (a.getArtistName().equals(artistName)) {
                    for (Album alb : a.getAlbums()) {
                        if (alb.getAlbumName().equals(albumName)) {
                            if (!alb.getCritics().isEmpty()) {
                                for (Critic critic : alb.getCritics()) {
                                    stringFinal += "critic_username|" + critic.getUsername() + ";description|" + critic.getDescCritic() + ";";
                                }
                            }
                        }
                    }
                }
            }
        }
        return stringFinal;
    }


    // --------------- UPLOAD / DOWNLOAD ----------------- //

    //http://www.codebytes.in/2014/11/file-transfer-using-tcp-java.html?fbclid=IwAR2tAKzWEfDqG9oQvzA7bfOkgnihxFlgbuAmbSVVLCn_WGCOgJxW5AdRQ8o
    //uploads music
    public synchronized boolean musicUpload(ServerSocket ssock, int port, String path) throws IOException {
        //System.out.println("oi1");
        //Initialize Sockets
        try {

            System.out.println(ssock);
            Socket socket = ssock.accept();
            //System.out.println("oi3");

            //Specify the file
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            //Get socket's output stream
            OutputStream os = socket.getOutputStream();
            //System.out.println("oi4");


            //Read File Contents into contents array
            byte[] contents;
            long fileLength = file.length();
            long current = 0;

            while (current != fileLength) {
                int size = 10000;
                if (fileLength - current >= size)
                    current += size;
                else {
                    size = (int) (fileLength - current);
                    current = fileLength;
                }
                contents = new byte[size];
                bis.read(contents, 0, size);
                os.write(contents);
                System.out.print("Sending file ... " + (current * 100) / fileLength + "% complete!");
            }

            os.flush();
            //File transfer done. Close the socket connection!
            socket.close();
            ssock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File sent succesfully!");
        return true;
    }

    //downloads music
    public synchronized boolean musicDownload(ServerSocket ssock, int port, String path, String username,String musicName) throws IOException {
        //Initialize Sockets
        Socket socket = ssock.accept();
        String aux = "";

        byte[] contents = new byte[10000];

        if(path.contains("kiminonawa")){
            aux = "/Users/dingo/Desktop/SD/meta2/Primes/src/dropsrc/Downloads/kiminonawa_"+username+".mp3";
        }
        else if(path.contains("love")){
            aux = "/Users/dingo/Desktop/SD/meta2/Primes/src/dropsrc/Downloads/love_"+username+".mp3";
        }
        else if(path.contains("gideon")){
            aux = "/Users/dingo/Desktop/SD/meta2/Primes/src/dropsrc/Downloads/gideon_"+username +".mp3";
        }
        else{
            //aux = path + "_"+username+ ".mp3";
            aux = "/Users/dingo/Desktop/SD/meta2/Primes/src/dropsrc/Downloads/"+musicName+"_"+username+".mp3";
        }


        //Initialize the FileOutputStream to the output file's full path.
        FileOutputStream fos = new FileOutputStream(aux);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        InputStream is = socket.getInputStream();

        //No of bytes read in one read() call
        int bytesRead = 0;

        while((bytesRead=is.read(contents))!=-1)
            bos.write(contents, 0, bytesRead);

        bos.flush();
        socket.close();
        ssock.close();

        System.out.println("File saved successfully!");
        return true;
    }

    //adds id to an existing music
    public synchronized boolean addUploadToSong(int serialNumber, String albumName, String artistName, String musicName, File file) throws IOException {
        for (Artist a : artistsArrayList) {
            if (a.getArtistName().equals(artistName)) {
                for (Album alb : a.getAlbums()) {
                    if (alb.getAlbumName().equals(albumName)) {
                        for(Song s : alb.getSongs()){
                            if (s.getSongName().equals(musicName)) {
                                s.addUploadId(serialNumber);
                                writeToDatabaseFile(file);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    // --------------- OBJECT FILES STUFF ----------------- //
    //https://coderanch.com/t/278704/java/read-objects-file-ObjectInputStream

    //write from array users to file
    public synchronized void writeToUsersFile(File file) throws IOException {
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream o = new ObjectOutputStream(f);

        for(User u : usersArrayList){
            o.writeObject(u);
        }
        o.close();
        f.close();
    }

    //reads from user file and stores into an array
    public synchronized CopyOnWriteArrayList<User> readFromUsersFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(file);
        ObjectInputStream oi = new ObjectInputStream(fi);
        while(true){
            try{
                User addUser = (User) oi.readObject();
                usersArrayList.add(addUser);
            } catch (EOFException e) {
                oi.close();
                fi.close();
                return usersArrayList;
            }
        }
    }

    //write from array database to file
    public synchronized void writeToDatabaseFile(File file) throws IOException {
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream o = new ObjectOutputStream(f);

        for(Artist u : artistsArrayList){
            o.writeObject(u);
        }
        o.close();
        f.close();
    }

    //reads from database file and stores into an array
    public synchronized CopyOnWriteArrayList<Artist> readFromDatabaseFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(file);
        ObjectInputStream oi = new ObjectInputStream(fi);
        while(true){
            try{
                Artist addArtist = (Artist) oi.readObject();
                artistsArrayList.add(addArtist);
            } catch (EOFException e) {
                oi.close();
                fi.close();
                return artistsArrayList;
            }
        }
    }


}
