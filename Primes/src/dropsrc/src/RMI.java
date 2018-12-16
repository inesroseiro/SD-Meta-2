package dropsrc.src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.*;
public interface RMI extends Remote {
    String sayHello() throws RemoteException;

    boolean checkRegister(String username, String password) throws RemoteException;

    String checkLogin(String username, String password) throws RemoteException;

    boolean checkUserRights(String username) throws RemoteException;

    boolean checkArtist(String artistName, String description) throws RemoteException;

    boolean checkAlbum(String albumName, String description, String musicalGenre, String udate, String artistName) throws RemoteException;

    boolean checkMusic(String musicName, String genre, String duration, String udate, String lyrics, String artistName, String albumName) throws RemoteException;

    boolean checkRemoveMusic(String musicName, String artistName, String albumName) throws RemoteException;

    boolean checkRemoveAlbum(String artistName, String albumName) throws RemoteException;

    boolean checkRemoveArtist(String artistName) throws RemoteException;

    boolean checkEditArtist(String oldArtistName, String newArtistName, String newDesc) throws RemoteException;

    boolean checkEditAlbum(String artistName, String oldAlbumName, String newAlbumName, String albumDescr, String musicalGenre, String udate) throws RemoteException;

    boolean checkEditSong(String artistName, String albumName, String oldMusicName, String newMusicName, String musicGenre, String duration, String date, String lyrics) throws RemoteException;

    boolean checkCritic(String username, String artistName, String albumName, int rate, String critic) throws RemoteException;

    String checkFromAlbumName(String username, String albumName) throws RemoteException;

    String checkFromArtistName(String username, String artistName) throws RemoteException;

    String checkViewArtistDetails(String username, String artistName) throws RemoteException;

    String checkViewAlbumDetails(String username, String artistName, String albumName) throws RemoteException;

    String checkViewAlbumCritics(String username, String artistName, String albumName) throws RemoteException;

    void addLoggedUsers(ClientInterface c) throws RemoteException;

    void removeLoggedUsers(String username) throws RemoteException;

    boolean searchOnlineListRights(String username) throws RemoteException;

    ClientInterface fetchClientOnlineListRighs(String username) throws RemoteException;

    boolean checkNotificationsRights(String username, ClientInterface client) throws  RemoteException;

    String checkViewSongDetails(String username, String artistName, String albumName, String song) throws RemoteException;

    int checkUpload(String username, String musicPath, String albumName, String artistName, String musicName) throws RemoteException;

    int checkDownload(String username, String musicPath, String albumName, String artistName, String musicName) throws RemoteException;


    }