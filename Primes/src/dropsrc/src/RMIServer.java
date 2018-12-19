package dropsrc.src;

import java.io.IOException;
import java.io.Serializable;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


public class RMIServer extends UnicastRemoteObject implements RMI, Serializable {

    private static final long serialVersionUID = 1L;
    private static Configurations configurations;
    private String MULTICAST_ADDRESS = "224.1.224.1";
    private static int PORT = 7000;
    private static int CLIENT_PORT = 4321;
    private static int MULTICAST_PORT = 5000;
    private int uploadNumber = 1;

    private static CopyOnWriteArrayList<ClientInterface> loggedUsers = new CopyOnWriteArrayList<>();

    public RMIServer() throws RemoteException {
        super();
    }

    public synchronized String sayHello() {
        System.out.println("hello");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }

    public synchronized boolean checkRegister(String username, String password) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringRegister = "type|register;username|" + username + ";password|" + password;
            System.out.println(stringRegister);
            byte[] bufferRegister = stringRegister.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferRegister, bufferRegister.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringRegister);

            byte[] bufferReceiveRegister = new byte[256];
            DatagramPacket receivePacketRegister = new DatagramPacket(bufferReceiveRegister, bufferReceiveRegister.length);
            socket.receive(receivePacketRegister);
            String receiveRegister = new String(receivePacketRegister.getData(), 0, receivePacketRegister.getLength());
            System.out.println("Received from Multicast: " + receiveRegister);
            if (receiveRegister.equals("type|status;logged|off;msg|ErrorWithUsername")) {
                return false;
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return true;
    }

    public synchronized boolean checkUserRights(String username) {
        System.out.println("USERNAME MULTICAST: " + username);
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String checkRight = "type|check;rights|username;" + username;
            System.out.println(checkRight);
            byte[] bufferCheckRight = checkRight.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferCheckRight, bufferCheckRight.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + checkRight);

            byte[] bufferReceiveCheck = new byte[256];
            DatagramPacket receivePacketCheck = new DatagramPacket(bufferReceiveCheck, bufferReceiveCheck.length);
            socket.receive(receivePacketCheck);
            String receiveCheck = new String(receivePacketCheck.getData(), 0, receivePacketCheck.getLength());
            System.out.println("Received from Multicast: " + receiveCheck);
            if (receiveCheck.equals("type|check;rights|error")) {
                //mandar reposta
                return false;
            } else if (receiveCheck.equals("type|check;rights|changed")) {
                return true;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public synchronized String checkLogin(String username, String password) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;

        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringLogin = "type|login;username|" + username + ";password|" + password;
            System.out.println(stringLogin);
            byte[] bufferLogin = stringLogin.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferLogin, bufferLogin.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringLogin);

            byte[] bufferReceiveLogin = new byte[256];
            DatagramPacket receivePacketLogin = new DatagramPacket(bufferReceiveLogin, bufferReceiveLogin.length);
            socket.receive(receivePacketLogin);
            String receiveLogin = new String(receivePacketLogin.getData(), 0, receivePacketLogin.getLength());
            System.out.println("Received from Multicast: " + receiveLogin);
            if (receiveLogin.equals("type|status;logged|on;msg|ErrorWithLogin")) {
                return "error";
            } else if (receiveLogin.equals("type|status;logged|on;msg|WelcomeToDropMusic|privilege;editor|")) {
                return "editor";
            } else if (receiveLogin.equals("type|status;logged|on;msg|WelcomeToDropMusic|privilege;user|")) {

                return "user";
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return "error";
    }

    public synchronized boolean checkArtist(String artistName, String description) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringArtist = "type|insert_artist;" + artistName + ";description|" + description;
            System.out.println(stringArtist);
            byte[] bufferLogin = stringArtist.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferLogin, bufferLogin.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringArtist);

            byte[] bufferReceiveLogin = new byte[256];
            DatagramPacket receivePacketLogin = new DatagramPacket(bufferReceiveLogin, bufferReceiveLogin.length);
            socket.receive(receivePacketLogin);
            String receiveLogin = new String(receivePacketLogin.getData(), 0, receivePacketLogin.getLength());
            System.out.println("Received from Multicast: " + receiveLogin);
            if (receiveLogin.equals("type|insert_artist;successful")) {
                return true;
            } else if (receiveLogin.equals("type|insert_artist;error in insert artist")) {
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized boolean checkAlbum(String albumName, String description, String musicalGenre, String udate, String artistName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            String stringAlbum = "type|insert_album;album_name|" + albumName + ";description|" + description + ";music_genre|" + musicalGenre + ";date|" + udate + ";artist_name|" + artistName;
            System.out.println(stringAlbum);
            byte[] bufferAlbum = stringAlbum.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferAlbum, bufferAlbum.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringAlbum);

            byte[] bufferReceiveAlbum = new byte[256];
            DatagramPacket receivePacketAlbum = new DatagramPacket(bufferReceiveAlbum, bufferReceiveAlbum.length);
            socket.receive(receivePacketAlbum);
            String receiveAlbum = new String(receivePacketAlbum.getData(), 0, receivePacketAlbum.getLength());
            System.out.println("Received from Multicast: " + receiveAlbum);
            if (receiveAlbum.equals("type|insert_album;successful")) {
                return true;
            } else if (receiveAlbum.equals("type|insert_album;error in insert album")) {
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized boolean checkMusic(String musicName, String genre, String duration, String udate, String lyrics, String artistName, String albumName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringMusic = "type|insert_music;music_name|" + musicName + ";genre|" + genre + ";duration|" + duration + ";artist_name|" + artistName + ";album_name|" + albumName + ";lyrics|" + lyrics + ";date|" + udate;
            System.out.println(stringMusic);
            byte[] bufferMusic = stringMusic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferMusic, bufferMusic.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringMusic);

            byte[] bufferReceiveMusic = new byte[256];
            DatagramPacket receivePacketMusic = new DatagramPacket(bufferReceiveMusic, bufferReceiveMusic.length);
            socket.receive(receivePacketMusic);
            String receiveAlbum = new String(receivePacketMusic.getData(), 0, receivePacketMusic.getLength());
            System.out.println("Received from Multicast: " + receiveAlbum);
            if (receiveAlbum.equals("type|insert_music;successful")) {
                return true;
            } else if (receiveAlbum.equals("type|insert_music;error in insert music")) {
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized boolean checkRemoveMusic(String musicName, String artistName, String albumName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringRemoveMusic = "type|remove_music;music_name|" + musicName + ";artist_name|" + artistName + ";album_name|" + albumName;
            System.out.println(stringRemoveMusic);
            byte[] bufferRemoveMusic = stringRemoveMusic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferRemoveMusic, bufferRemoveMusic.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringRemoveMusic);

            byte[] bufferReceiveRemoveMusic = new byte[256];
            DatagramPacket receivePacketRemoveMusic = new DatagramPacket(bufferReceiveRemoveMusic, bufferReceiveRemoveMusic.length);
            socket.receive(receivePacketRemoveMusic);
            String receiveAlbum = new String(receivePacketRemoveMusic.getData(), 0, receivePacketRemoveMusic.getLength());
            System.out.println("Received from Multicast: " + receiveAlbum);
            if (receiveAlbum.equals("type|remove_music;successful")) {
                return true;
            } else if (receiveAlbum.equals("type|remove_music;error in remove music")) {
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized boolean checkRemoveAlbum(String artistName, String albumName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringRemoveAlbum = "type|remove_album;album_name|" + albumName + ";artist_name|" + artistName;
            System.out.println(stringRemoveAlbum);
            byte[] bufferRemoveAlbum = stringRemoveAlbum.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferRemoveAlbum, bufferRemoveAlbum.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringRemoveAlbum);

            byte[] bufferReceiveRemoveAlbum = new byte[256];
            DatagramPacket receivePacketRemoveAlbum = new DatagramPacket(bufferReceiveRemoveAlbum, bufferReceiveRemoveAlbum.length);
            socket.receive(receivePacketRemoveAlbum);
            String receiveAlbum = new String(receivePacketRemoveAlbum.getData(), 0, receivePacketRemoveAlbum.getLength());
            System.out.println("Received from Multicast: " + receiveAlbum);
            if (receiveAlbum.equals("type|remove_album;successful")) {
                return true;
            } else if (receiveAlbum.equals("type|remove_album;error in remove album")) {
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
    }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized boolean checkRemoveArtist(String artistName) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);


            String stringRemoveAlbum = "type|remove_artist;artist_name|" + artistName;
            System.out.println(stringRemoveAlbum);
            byte[] bufferRemoveAlbum = stringRemoveAlbum.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferRemoveAlbum, bufferRemoveAlbum.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringRemoveAlbum);

            byte[] bufferReceiveRemoveAlbum = new byte[256];
            DatagramPacket receivePacketRemoveAlbum = new DatagramPacket(bufferReceiveRemoveAlbum, bufferReceiveRemoveAlbum.length);
            socket.receive(receivePacketRemoveAlbum);
            String receiveAlbum = new String(receivePacketRemoveAlbum.getData(), 0, receivePacketRemoveAlbum.getLength());
            System.out.println("Received from Multicast: " + receiveAlbum);
            if (receiveAlbum.equals("type|remove_artist;successful")) {
                return true;
            } else if (receiveAlbum.equals("type|remove_artist;error in remove artist")) {
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized boolean checkEditArtist(String oldArtistName, String newArtistName, String newDesc) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringEditArtist = "type|edit_artist;old_artist_name|" + oldArtistName + ";new_artist_name|" + newArtistName + ";new_description|" + newDesc;
            System.out.println(stringEditArtist);
            byte[] bufferEditArtist = stringEditArtist.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringEditArtist);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            String receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);

            if (receiveEditArtist.equals("type|edit_artist;successful")) {
                return true;
            } else if (receiveEditArtist.equals("type|edit_artist;error in edit artist")) {
                return false;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized boolean checkEditAlbum(String artistName, String oldAlbumName, String newAlbumName, String albumDescr, String musicalGenre, String udate) {

        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringEditAlbum = "type|edit_album;artist_name|" + artistName + ";old_album_name|" + oldAlbumName + ";new_album_name|" + newAlbumName + ";new_description|" + albumDescr + ";music_genre|" + musicalGenre + ";date|" + udate;
            System.out.println(stringEditAlbum);
            byte[] bufferEditAlbum = stringEditAlbum.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditAlbum, bufferEditAlbum.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringEditAlbum);

            byte[] bufferReceiveEditAlbum = new byte[256];
            DatagramPacket receiveEditAlbumPacket = new DatagramPacket(bufferReceiveEditAlbum, bufferReceiveEditAlbum.length);
            socket.receive(receiveEditAlbumPacket);
            String receiveEditAlbum = new String(receiveEditAlbumPacket.getData(), 0, receiveEditAlbumPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditAlbum);

            if (receiveEditAlbum.equals("type|edit_album;successful")) {
                return true;
            } else if (receiveEditAlbum.equals("type|edit_album;error in edit album")) {
                return false;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized boolean checkEditSong(String artistName, String albumName, String oldMusicName, String newMusicName, String musicGenre, String duration, String date, String lyrics) {

        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringEditSong = "type|edit_music;artist_name|" + artistName + ";album_name|" + albumName + ";old_music_name|" + oldMusicName + ";new_music_name|" + newMusicName + ";music_genre|" + musicGenre + ";duration|" + duration + ";date|" + date + ";lyrics|" + lyrics;
            System.out.println(stringEditSong);
            byte[] bufferEditSong = stringEditSong.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditSong, bufferEditSong.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringEditSong);

            byte[] bufferReceiveEditSong = new byte[256];
            DatagramPacket receiveEditSongPacket = new DatagramPacket(bufferReceiveEditSong, bufferReceiveEditSong.length);
            socket.receive(receiveEditSongPacket);
            String receiveEditSong = new String(receiveEditSongPacket.getData(), 0, receiveEditSongPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditSong);

            if (receiveEditSong.equals("type|edit_music;successful")) {
                return true;
            } else if (receiveEditSong.equals("type|edit_music;error in edit album")) {
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized boolean checkCritic(String username, String artistName, String albumName, int rate, String critic) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String rateString = "" + rate;
            String stringCritic = "type|write_critic;username|" + username + ";artist_name|" + artistName + ";album_name|" + albumName + ";rate|" + rateString + ";critic|" + critic;
            System.out.println(stringCritic);
            byte[] bufferEditArtist = stringCritic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringCritic);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            String receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);

            if (receiveEditArtist.equals("type|write_critic;successful")) {
                return true;
            } else if (receiveEditArtist.equals("type|write_critic;error in write critic")) {
                return false;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }
        return false;
    }

    public synchronized String checkFromAlbumName(String username, String albumName){
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        String receiveEditArtist = "";

        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringCritic = "type|search_album_name;username|" + username + ";album_name|" + albumName;
            System.out.println(stringCritic);
            byte[] bufferEditArtist = stringCritic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringCritic);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }

        return receiveEditArtist;
    }

    public synchronized String checkFromArtistName(String username, String artistName){
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        String receiveEditArtist = "";

        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringCritic = "type|search_album_artist;username|" + username + ";artist_name|" + artistName;
            System.out.println(stringCritic);
            byte[] bufferEditArtist = stringCritic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringCritic);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }

        return receiveEditArtist;
    }

    public synchronized String checkViewArtistDetails(String username, String artistName){
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        String receiveEditArtist = "";

        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringCritic = "type|view_artist_details;username|" + username + ";artist_name|" + artistName;
            System.out.println(stringCritic);
            byte[] bufferEditArtist = stringCritic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringCritic);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }

        return receiveEditArtist;
    }

    public synchronized String checkViewAlbumDetails(String username, String artistName, String albumName){
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        String receiveEditArtist = "";

        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringCritic = "type|view_album_details;username|" + username + ";artist_name|" + artistName + ";album_name|" + albumName;
            System.out.println(stringCritic);
            byte[] bufferEditArtist = stringCritic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringCritic);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }

        return receiveEditArtist;
    }

    public synchronized String checkViewAlbumCritics(String username, String artistName, String albumName){
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        String receiveEditArtist = "";

        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringCritic = "type|view_album_critics;username|" + username + ";artist_name|" + artistName + ";album_name|" + albumName;
            System.out.println(stringCritic);
            byte[] bufferEditArtist = stringCritic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringCritic);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }

        return receiveEditArtist;
    }

    public synchronized String checkViewSongDetails(String username, String artistName, String albumName, String song) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        String receiveEditArtist = "";

        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringCritic = "type|view_song_details;username|" + username + ";artist_name|" + artistName + ";album_name|" + albumName + ";song_name|" + song;
            System.out.println(stringCritic);
            byte[] bufferEditArtist = stringCritic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringCritic);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            sendSocket.close();
        }

        return receiveEditArtist;
    }

    public synchronized void addLoggedUsers(ClientInterface c){
        loggedUsers.add(c);
    }

    public synchronized boolean searchOnlineListRights(String username) throws RemoteException {
        for(ClientInterface c : loggedUsers){
            if(c.getUsername().equals(username)){
                //user está na lista de logged users e pode ser notificado
                return true;
            }
        }
        return false;
    }

    public synchronized ClientInterface fetchClientOnlineListRighs(String username) throws RemoteException{
        ClientInterface client = null;
        for(ClientInterface c: loggedUsers){
            if(c.getUsername().equals(username)){
                client = c;
            }
        }
        return client;
    }

    public void removeLoggedUsers(String username) throws RemoteException {
        ClientInterface rem = null;
        if(!loggedUsers.isEmpty()){
            for(ClientInterface c : loggedUsers){
                if(c.getUsername().equals(username)){
                    System.out.println(c.getUsername());
                    rem = c;
                }
            }
        }
        loggedUsers.remove(rem);
    }

    public synchronized boolean checkNotificationsRights(String username, ClientInterface client) {
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        try {
            //server stuff
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            String stringNotificationRights = "type|notification_rights;username|" + username;
            byte[] bufferNotificationRights = stringNotificationRights.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferNotificationRights, bufferNotificationRights.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringNotificationRights);

            byte[] bufferReceiveNotification = new byte[256];
            DatagramPacket receiveNotificationPacket = new DatagramPacket(bufferReceiveNotification, bufferReceiveNotification.length);
            socket.receive(receiveNotificationPacket);
            String receiveNotificationCheck = new String(receiveNotificationPacket.getData(), 0, receiveNotificationPacket.getLength());
            System.out.println("Received from Multicast: " + receiveNotificationCheck);

            if(receiveNotificationCheck.equals("type|notification_rights;check")){
                //chama funcao de notificacao
                client.notifyRights();
                return true;
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch(SocketTimeoutException e){
            System.out.println("Couldn't connect to Multicast.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized int checkUpload(String username, String musicPath,String albumName, String artistName, String musicName){
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        String receiveEditArtist = "";
        int port = -1;

        try {
            //server stuff

            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            port = 5100;

            Random r = new Random();
            int Low = 5000;
            int High = 6000;
            port = r.nextInt(High-Low) + Low;

            //int number = generateRandom();
            //while(!checkRandom(number)){
            //    number = generateRandom();
            //}

            String stringCritic = "type|upload_music;username|" + username + ";music_path|" + musicPath + ";port|" + port + ";serial_number|" + uploadNumber + ";artist_name|" + artistName + ";album_name|" + albumName + ";music_name|" + musicName;
            System.out.println(stringCritic);
            byte[] bufferEditArtist = stringCritic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringCritic);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);


        } catch (UnknownHostException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            socket.close();
            sendSocket.close();
            return port;
        }
    }

    public synchronized int checkDownload(String username, String musicPath,String albumName, String artistName, String musicName){
        MulticastSocket socket = null;
        MulticastSocket sendSocket = null;
        String receiveEditArtist = "";
        int port = -1;

        try {
            //server stuff

            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            sendSocket = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            port = 5100;

            Random r = new Random();
            int Low = 5000;
            int High = 6000;
            port = r.nextInt(High-Low) + Low;

            //int number = generateRandom();
            //while(!checkRandom(number)){
            //    number = generateRandom();
            //}

            String stringCritic = "type|download_music;username|" + username + ";music_path|" + musicPath + ";port|" + 5000 + ";serial_number|" + uploadNumber + ";artist_name|" + artistName + ";album_name|" + albumName + ";music_name|" + musicName;
            System.out.println(stringCritic);
            byte[] bufferEditArtist = stringCritic.getBytes();
            DatagramPacket rmiPacket = new DatagramPacket(bufferEditArtist, bufferEditArtist.length, group, MULTICAST_PORT);
            sendSocket.send(rmiPacket);
            System.out.println("Sent to Multicast: " + stringCritic);

            byte[] bufferReceiveEditArtist = new byte[256];
            DatagramPacket receiveEditArtistPacket = new DatagramPacket(bufferReceiveEditArtist, bufferReceiveEditArtist.length);
            socket.receive(receiveEditArtistPacket);
            receiveEditArtist = new String(receiveEditArtistPacket.getData(), 0, receiveEditArtistPacket.getLength());
            System.out.println("Received from Multicast: " + receiveEditArtist);


        } catch (UnknownHostException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            socket.close();
            sendSocket.close();
            return 5000;
        }
    }


        public static void RMIOn() throws RemoteException {

		configurations = new Configurations(("RMI_configs.cfg"));
		System.out.println("NomeRMI: " + configurations.getRMIname());
		System.out.println("PortoRMI: " + configurations.getRMIport());
		System.out.println("HostRMI: " + configurations.getRMIhost());
		int fails=0;
		MulticastSocket connectionSocket = null;
		while(fails<6) {
			try {
                RMI server = (RMI) LocateRegistry.getRegistry(7000).lookup("DropMusic");
				server.sayHello();
				fails = 0;
			} catch (NotBoundException | RemoteException e) {
				System.out.print("Falha na ligacao\n");
				fails++;
			}
        }try {
			RMIServer rmiServer = new RMIServer();
			Registry reg = LocateRegistry.createRegistry(7000);
			reg.rebind("DropMusic", rmiServer);
			System.out.println("\nRMI Server running on port " + configurations.getRMIport());

		}catch(ExportException e) {
			e.printStackTrace();
		}
    }

	// =========================================================
	public static void main(String args[]) {

        System.getProperties().put("java.security.policy", "/Users/iroseiro/Downloads/Entrega SD Meta 2/Source Code/src/dropsrc/src/policy.all");
        System.setSecurityManager(new RMISecurityManager());

		try {
			RMIOn();
		} catch (RemoteException e) {
			System.out.println("\n RemoteException : " + e.getMessage());
		}
	}
}