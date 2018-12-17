package dropsrc.src;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class RMIClient extends UnicastRemoteObject implements ClientInterface {
    private static Configurations configurations;
    private static RMI rmiInterface;
    private static String clientUsername;
    private static RMIClient client;

    //validate date in format dd/mm/yyyy or d/m/yyyy
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("d/M/uuuu");

    RMIClient() throws RemoteException {
        super();
    }

    public static void main(String args[]) throws RemoteException {

        //This might be necessary if you ever need to download classes:
        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());
        //configurations = new Configurations(("/Users/iroseiro/IdeaProjects/Project/src/com/company/RMI_configs.cfg"));
        configurations = new Configurations("/Users/dingo/Desktop/SD/DropMusicMerged/out/production/DropMusicMerged/RMI_configs.cfg");

        try {
            rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
            rmiInterface.sayHello();
            client = new RMIClient();
            welcome();

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            startingLookup();
        }

    }


    //-------- MENUS--------//

    //menu welcome
    public synchronized static void welcome(){
        while (true){
            try {
                System.out.println("\n\t- Welcome to DropMusic -");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("0. Quit");
                System.out.print("> Option: ");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 3)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //fuction login
                        login();
                        return;
                    case 2:
                        //function register
                        register();
                        return;
                    case 0:
                        System.exit(0);
                        return;
                }

            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){} catch (RemoteException e) {
                e.getMessage();
            }
        }
    }

    //menu user
    public synchronized static void menuUser(String username) throws RemoteException {
        while (true) {
            int lookupOpt = 0;
            try {
                if (rmiInterface.checkNotificationsRights(username, client)) {
                    menuAdministrador(username);
                }
                System.out.println("\t- User Menu -");
                System.out.println("1. View Data");
                System.out.println("2. Search Data");
                System.out.println("3. Write Critic");
                System.out.println("4. Upload File");
                System.out.println("5. Download File");
                System.out.println("0. Log Out");
                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);
                lookupOpt = opt;
                //verificacao option
                if ((opt < 0) || (opt > 5)) {
                    System.out.println("\tInvalid option!");
                    continue;
                }

                switch (opt) {
                    case 1:
                        viewData(username);
                        return;
                    case 2:
                        searchData(username);
                        return;
                    case 3:
                        writeCritic(username);
                        return;
                    case 4:
                        uploadFile(username);
                        return;
                    case 5:
                        downloadFile(username);
                        return;
                    case 0:
                        System.out.println("Logged out!");
                        //remover
                        rmiInterface.removeLoggedUsers(username);
                        System.exit(0);
                }
            }catch(RemoteException e){
                menuUserLookup(lookupOpt, username);
            }
        }
    }

    //menu admin
    public synchronized static void menuAdministrador(String username) throws RemoteException {
        while (true) {
            int lookupOpt = 0;
            try {
                rmiInterface.checkNotificationsRights(username, client);
                System.out.println("\n\t- Administrator Menu -");
                System.out.println("1. Insert data");
                System.out.println("2. Change data");
                System.out.println("3. Remove data");
                System.out.println("4. Give editor rights ");
                System.out.println("0. Log Out");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);
                lookupOpt = opt;
                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\n\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //menu insert data
                        insertData(username);
                        return;
                    case 2:
                        //menu change data
                        //here
                        changeData(username);
                        return;
                    case 3:
                        //menu remove data
                        removeData(username);
                        return;
                    case 4:
                        //change user rights
                        changeRights(username);
                    case 0:
                        System.out.println("Logged out!");
                        //remover
                        rmiInterface.removeLoggedUsers(username);
                        System.exit(0);
                        return;
                }
            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){} catch (RemoteException e) {
                menuAdminLookup(lookupOpt, username);
            }
        }
    }

    //menu remove data
    public synchronized static void removeData(String username){
        while (true){
            try {
                System.out.println("\n\t- Remove Data -");
                System.out.println("1. Remove artist.");
                System.out.println("2. Remove album.");
                System.out.println("3. Remove music.");
                System.out.println("4. Return to Admin Menu");
                System.out.println("0. Quit");
                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //remove artist
                        removeArtist(username);
                        return;
                    case 2:
                        //remove album
                        removeAlbum(username);
                        return;
                    case 3 :
                        //remove music
                        removeMusic(username);
                        return;
                    case 4:
                        menuAdministrador(username);
                        return;
                    case 0:
                        System.exit(0);
                        return;
                }

            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){} catch (RemoteException e) {
                e.getMessage();
            }
        }
    }

    //menu change data
    public synchronized static void changeData(String username){
        while (true){
            try {
                System.out.println("\n\t- Change Data -");
                System.out.println("1. Change artist data.");
                System.out.println("2. Change album data.");
                System.out.println("3. Change music data.");
                System.out.println("4. Return to Admin Menu");
                System.out.println("0. Quit");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //mudar dados da artista
                        editArtist(username);
                        return;
                    case 2:
                        //mudar dados de album
                        editAlbum(username);
                        //
                        return;
                    case 3 :
                        //mudar dados de musica
                        editMusic(username);
                    case 4:
                        menuAdministrador(username);
                        return;
                    case 0:
                        System.exit(0);
                        return;
                }

            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){} catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    //menu insert data
    public synchronized static void insertData(String username){
        while (true){
            try {
                System.out.println("\t- Insert Data -");
                System.out.println("1. Insert artist.");
                System.out.println("2. Insert album.");
                System.out.println("3. Insert music.");
                System.out.println("4. Return to Admin Menu");
                System.out.println("0. Quit");

                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        insertArtist(username);
                        return;
                    case 2:
                        insertAlbum(username);
                        return;
                    case 3 :
                        insertMusic(username);
                        return;
                    case 4:
                        menuAdministrador(username);
                        return;
                    case 0:
                        System.exit(0);
                        return;
                }

            }catch (NumberFormatException e ){
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e){} catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        }

    //menu search data
    public synchronized static void searchData(String username){
        while (true) {
            try {
                System.out.println("\n\t- Search Data -");
                System.out.println("1. Search album from artist.");
                System.out.println("2. Search album from album name.");
                System.out.println("0. Quit");
                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 2)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //search album from artist
                        searchAlbumfromArtist(username);
                        return;
                    case 2:
                        //search album from album name
                        searchAlbumfromAlbumName(username);
                        return;
                    case 0:
                        System.exit(0);
                        return;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e) {} catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        }

    //menu search data
    public synchronized static void viewData(String username){
        while (true) {
            try {
                System.out.println("\n\t- View Data -");
                System.out.println("1. View album details.");
                System.out.println("2. View artist details.");
                System.out.println("3. View song details.");
                System.out.println("4. View album critics.");
                System.out.println("5. Back to main menu");

                System.out.println("0. Quit");
                Scanner s = new Scanner(System.in);
                String strOpt = s.nextLine();
                int opt = Integer.parseInt(strOpt);

                //verificacao option
                if ((opt < 0) || (opt > 5)) {
                    System.out.println("\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //search album from artist
                        viewAlbumDetails(username);
                        viewData(username);
                        return;
                    case 2:
                        //search album from album name
                        viewArtistDetails(username);
                        viewData(username);
                        return;
                    case 3:
                        viewSongDetails(username);
                        viewData(username);
                        return;
                    case 4:
                        //view album critics
                        viewAlbumCritics(username);
                        viewData(username);
                        return;

                    case 5:
                        menuUser(username);
                        return;
                    case 0:
                        System.exit(0);
                        return;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid option.");
            } catch (NoSuchElementException e) {
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    //-------- MENU FUNCTIONS--------//

    //register
    public synchronized static void register() throws RemoteException{

        Scanner s = new Scanner(System.in);
        System.out.println("Username: ");
        String username = s.nextLine();

        System.out.println("Password: ");
        String password = s.nextLine();

        try {
            if (rmiInterface.checkRegister(username, password)) {
                System.out.println("Registered successfully.");
                clientUsername = username;
                client.setUsername(clientUsername);
                //adicionar a lista de logged users no RMI
                rmiInterface.addLoggedUsers(client);
                menuUser(username);
            } else {
                System.out.println("Username already in use.");
                welcome();
            }
        }catch(RemoteException e){
            registerLookup(username, password);
        }


    }

    //login
    public synchronized static void login() throws RemoteException{
        Scanner s = new Scanner(System.in);
        System.out.println("Username: ");
        String username = s.nextLine();

        System.out.println("Password: ");
        String password = s.nextLine();
        try {
            if (rmiInterface.checkLogin(username, password).equals("editor")) {
                System.out.println("Logged in as editor.");

                //meter interface com nome de user
                clientUsername = username;
                client.setUsername(clientUsername);
                //adicionar a lista de logged users no RMI
                rmiInterface.addLoggedUsers(client);

                menuAdministrador(username);
            } else if (rmiInterface.checkLogin(username, password).equals("user")) {
                System.out.println("Logged in as user.");

                clientUsername = username;
                client.setUsername(clientUsername);
                //adicionar a lista de logged users no RMI
                rmiInterface.addLoggedUsers(client);

                menuUser(username);
            } else {
                System.out.println("Error with login.");
                welcome();
            }
        }catch(RemoteException e){
            loginLookup(username, password);

        }
    }

    //change rights
    public synchronized static void changeRights(String user) throws RemoteException {
        Scanner s = new Scanner(System.in);
        System.out.println("Insert username: ");
        String username = s.nextLine();

        try {
            boolean checker = rmiInterface.checkUserRights(username);
            if (checker) {
                System.out.println("Changed rights of user " + username + " to editor.");
                menuAdministrador(user);
            } else {
                System.out.println("User not found or is already an editor.");
                menuAdministrador(user);
            }
        }catch(RemoteException e){
            changeRightsLookup(user, username);
        }
    }

    //write critic
    public synchronized static void writeCritic(String username) throws RemoteException{
        System.out.println("\n\t- Write Critic -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String strArtistName = s.nextLine();

        System.out.println("Album name:");
        String strAlbumName = s.nextLine();

        Scanner sc = new Scanner(System.in);
        System.out.println("Rate album (0-5):");
        int rate = sc.nextInt();

        if ((rate < 0) || (rate > 5)) {
            System.out.println("\tInvalid rate! Try again ");
            writeCritic(username);
        }
        System.out.println("Write critic:");
        String strCritic = s.nextLine();

        if (strCritic.length()>300) {
            System.out.println("\tCritic too big! Try again");
            writeCritic(username);
        }

        try {
            if (rmiInterface.checkCritic(username,strArtistName,strAlbumName,rate,strCritic)) {
                System.out.println("Critic added.");
                menuUser(username);
            } else {
                System.out.println("Error with critic! Try again");
                menuUser(username);
            }
        } catch (RemoteException e) {
            writeCriticLookup(strArtistName, strAlbumName, rate, strCritic, username);
        }
    }


    //-------- INSERT FUNCTIONS--------//

    //insert music
    public synchronized static void insertMusic(String username) throws RemoteException {
        System.out.println("\n\t- Insert Music -");
        System.out.println("Music name:");
        Scanner s = new Scanner(System.in);
        String strName = s.nextLine();
        System.out.println("Music genre:");
        String strGenre = s.nextLine();

        System.out.println("Music duration:");
        String strDuration = s.nextLine();

        String udate = dateInput(s);

        System.out.println("Lyrics:");
        String lyrics = s.nextLine();

        System.out.println("Artist name:");
        String strArtistName = s.nextLine();

        System.out.println("Album name:");
        String strAlbumName = s.nextLine();


        //CheckMusic
        try {
            if (rmiInterface.checkMusic(strName, strGenre,strDuration,udate, lyrics, strArtistName,strAlbumName)) {

                System.out.println("Music added.");
                menuAdministrador(username);
            } else {
                System.out.println("Error adding music.");
                menuAdministrador(username);
            }
        } catch (RemoteException e) {
            insertMusicLookup(strName, strGenre, strDuration, udate, lyrics, strArtistName, strAlbumName, username);
        }


    }

    //insert artist
    public synchronized static void insertArtist(String username) throws RemoteException {
        System.out.println("\n\t- Insert Artist -");
        System.out.println("Artist name:");
        Scanner s = new Scanner(System.in);

        String artistName = s.nextLine();
        System.out.println("Artist description:");
        String artistDesc = s.nextLine();
        try {
            if (rmiInterface.checkArtist(artistName, artistDesc)) {
                System.out.println("Artist added.");
                menuAdministrador(username);
            } else {
                System.out.println("Error adding artist.");
                menuAdministrador(username);
            }
        }
        catch(RemoteException e){
            inserArtistLookup(artistName, artistDesc, username);
        }
    }

    //insert album
    public synchronized static void insertAlbum(String username) throws RemoteException {

        System.out.println("\n\t- Insert Album -");
        System.out.println("Album name:");
        Scanner s = new Scanner(System.in);

        String albumName = s.nextLine();
        System.out.println("Album description:");
        String albumDescr = s.nextLine();

        System.out.println("Musical Genre:");
        String musicalGenre = s.nextLine();

        String udate = dateInput(s);

        System.out.println("Artist name:");
        String artistName = s.nextLine();


        try {
            if (rmiInterface.checkAlbum(albumName, albumDescr, musicalGenre, udate, artistName)) {
                System.out.println("Album added.");
                menuAdministrador(username);
            } else {
                System.out.println("Error adding Album.");
                menuAdministrador(username);

            }
        }catch(RemoteException e){
            insertAlbumLookup(albumName, albumDescr, musicalGenre, udate, artistName, username);
        }
    }



    // ------- SEARCH FUNCTIONS -------- //

    public synchronized static void searchAlbumfromAlbumName(String username) throws RemoteException {
        System.out.println("\n\t- Search Album from Album Name -");
        Scanner s = new Scanner(System.in);

        System.out.println("Album name:");
        String albumName = s.nextLine();


        try {
            String result = rmiInterface.checkFromAlbumName(username, albumName);
            if (result.equals("type|search_album_name;error in search_album_name")) {
                System.out.println("An error ocurred... Try again!");
                menuUser(username);
            } else {
                System.out.println("\t-Results-");
                //Aqui correu tudo bem
                String[] splitStringAll = result.split(";");
                //get item count
                String[] splitString1 = splitStringAll[1].split("\\|");
                int itemCount = Integer.parseInt(splitString1[1]);
                //get albums
                int j = 2, size = splitStringAll.length;
                while (j < size) {
                    String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                    System.out.println("Album Name: " + splitStringAlbumName[1]);
                    String[] splitStringArtistName = splitStringAll[j + 1].split("\\|");
                    System.out.println("Artist Name: " + splitStringArtistName[1]);
                    System.out.println("");
                    j += 2;
                }
            }
        } catch (RemoteException e) {
            searchAlbumFromAlbumNameLookup(albumName, username);
        }
    }

    public synchronized static void searchAlbumfromArtist(String username) throws RemoteException {
        System.out.println("\n\t- Search Album from Artist -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String artist = s.nextLine();


        try {
            String result = rmiInterface.checkFromArtistName(username, artist);
            if (result.equals("type|search_album_artist;error in search_album_artist")) {
                System.out.println("An error ocurred... Try again!");
                menuUser(username);
            } else {
                String[] splitStringAll = result.split(";");

                System.out.println("\t-Results-");

                String[] splitStringName = splitStringAll[1].split("\\|");
                System.out.println("Artist name: " + splitStringName[1]);

                int j = 3, size = splitStringAll.length;
                while (j < size) {
                    String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                    System.out.println("Album Name: " + splitStringAlbumName[1]);
                    j++;
                }
                menuUser(username);
            }
        } catch (RemoteException e1) {
            searchAlbumfromArtistLookup(artist, username);
        }
    }


    // -------- VIEW DATA FUCNTIONS -----------------------

    public synchronized static void viewArtistDetails(String username) throws RemoteException {
        System.out.println("\n\t- View Artist Details -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String artist = s.nextLine();


        try {
            String result = rmiInterface.checkViewArtistDetails(username, artist);
            if (result.equals("type|view_artist_details;error in view_artist_details")) {
                System.out.println("An error ocurred... Try again!");
                menuUser(username);
            } else {

                String[] splitStringAll = result.split(";");

                System.out.println("\t-Artist details-");

                String[] splitStringName = splitStringAll[1].split("\\|");
                System.out.println("Artist name: " + splitStringName[1]);

                String[] splitStringDescription = splitStringAll[2].split("\\|");
                System.out.println("Artist Biography: " + splitStringDescription[1]);


                int j = 3, size = splitStringAll.length;

                if(size > 0) {
                    System.out.println("Discography:");
                }

                while (j < size) {
                    String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                    System.out.println("Album Name: " + splitStringAlbumName[1]);
                    j++;
                }


            }
        } catch (RemoteException e) {
            viewArtistDetailsLookup(artist, username);
        }


    }

    public synchronized static void viewAlbumDetails(String username) {
        System.out.println("\n\t- View Album Details -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String artist = s.nextLine();
        System.out.println("Album name:");
        String album = s.nextLine();


        try {
            String result = rmiInterface.checkViewAlbumDetails(username,artist,album);
            if (result.equals("type|view_album_details;error_in_view_album_details")) {
                System.out.println("An error ocurred... Try again!");
                menuUser(username);
            } else {
                String[] splitStringAll = result.split(";");

                System.out.println("\t-Album details-");

                String[] splitStringName = splitStringAll[1].split("\\|");
                System.out.println("Artist name: " + splitStringName[1]);
                String[] splitString2 = splitStringAll[3].split("\\|");
                System.out.println("Description: " + splitString2[1]);
                String[] splitString3 = splitStringAll[4].split("\\|");
                System.out.println("Musical Genre: " + splitString3[1]);
                String[] splitString4 = splitStringAll[5].split("\\|");
                System.out.println("Release Date: " + splitString4[1]);


                int j = 6, size = splitStringAll.length;

                if(size > 2) {
                    System.out.println("Songs:");
                    while (j < size) {
                        String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                        System.out.println("Music Name: " + splitStringAlbumName[1]);
                        j++;
                    }
                }
            }
        } catch (RemoteException e) {
            viewAlbumDetailsLookup(artist, album, username);
        }

    }

    public synchronized static void viewAlbumCritics(String username) throws RemoteException{
        System.out.println("\n\t- View Album Critics -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String artist = s.nextLine();
        System.out.println("Album name:");
        String album = s.nextLine();


        try {
            String result = rmiInterface.checkViewAlbumCritics(username,artist,album);
            if (result.equals("type|view_album_critics;error in view_album_critics")) {
                System.out.println("An error ocurred... Try again!");
                menuUser(username);
            } else {
                String[] splitStringAll = result.split(";");

                System.out.println("\t-Album critics-");

                String[] splitStringName = splitStringAll[1].split("\\|");
                System.out.println("Artist name: " + splitStringName[1]);

                String [] splitString3 =splitStringAll[3].split("\\|");
                System.out.println("Average Rate: " + splitString3[1]);

                if(splitStringAll.length > 3) {
                    int j = 4, size = splitStringAll.length;
                    while(j < size) {
                        String[] splitStringArtistName = splitStringAll[j + 1].split("\\|");
                        String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                        System.out.println("Critic: "+splitStringArtistName[1]);
                        System.out.println("Username: " + splitStringAlbumName[1]);
                        j += 2;

                    }
                }

            }
        } catch (RemoteException e) {
            viewAlbumCriticsLookup(artist, album, username);
        }

    }


    //-------- REMOVE FUNCTIONS--------//

    //remove music
    public synchronized static void removeMusic(String username) throws RemoteException{
        System.out.println("\n\t- Remove Music -");
        Scanner s = new Scanner(System.in);

        System.out.println("Music name:");
        String musicName = s.nextLine();
        System.out.println("Artist name:");
        String artistName = s.nextLine();
        System.out.println("Album name:");
        String albumName = s.nextLine();
        try {
            if (rmiInterface.checkRemoveMusic(musicName, artistName, albumName)) {
                System.out.println("Music removed.");
                menuAdministrador(username);
            } else {
                System.out.println("Error removing music.");
                menuAdministrador(username);

            }
        }catch(RemoteException e){
            removeMusicLookup(musicName, artistName, albumName, username);
        }
    }

    //remove album
    public synchronized static void removeAlbum(String username) throws RemoteException{
        System.out.println("\n\t- Remove Album -");
        Scanner s = new Scanner(System.in);

        System.out.println("Album name:");
        String albumName = s.nextLine();
        System.out.println("Artist name:");
        String artistName = s.nextLine();


        try {
            if (rmiInterface.checkRemoveAlbum(artistName, albumName)) {
                System.out.println("Album removed.");
                menuAdministrador(username);
            } else {
                System.out.println("Error removing album.");
                menuAdministrador(username);

            }
        }catch(RemoteException e){
            removeAlbumLookup(albumName, artistName, username);
        }
    }

    //remove artist
    public synchronized static void removeArtist(String username) throws RemoteException{
        System.out.println("\n\t- Remove Artist -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String artistName = s.nextLine();

        try {
            if (rmiInterface.checkRemoveArtist(artistName)) {
                System.out.println("Artist removed.");
                menuAdministrador(username);
            } else {
                System.out.println("Error removing artist.");
                menuAdministrador(username);

            }
        }catch(RemoteException e){
            removeArtistLookup(artistName, username);
        }
    }


    //-------- EDIT FUNCTIONS--------//

    public synchronized static void editMusic(String username) throws RemoteException {
        System.out.println("\n\t- Edit Music -");
        System.out.println("\n\tINPUT OLD DATA");

        System.out.println("Artist name of music to edit: ");
        Scanner s = new Scanner(System.in);
        String artistName = s.nextLine();

        System.out.println("Album name of music to edit:");
        String albumName = s.nextLine();

        System.out.println("Music name to edit:");
        String oldMusicName = s.nextLine();

        System.out.println("\n\t INPUT NEW DATA");
        System.out.println("NEW Music name: ");
        String newMusicName = s.nextLine();

        System.out.println("NEW Music genre:");
        String strGenre = s.nextLine();

        System.out.println("NEW Music duration:");
        String strDuration = s.nextLine();

        String udate = dateInput(s);

        System.out.println("NEW Lyrics:");
        String lyrics = s.nextLine();

        try {
            if (rmiInterface.checkEditSong(artistName, albumName,oldMusicName,newMusicName, strGenre, strDuration, udate, lyrics)) {

                System.out.println("Music edited.");
                menuAdministrador(username);
            } else {
                System.out.println("Error editing music.");
                menuAdministrador(username);
            }
        } catch (RemoteException e) {
            editMusicLookup(artistName, albumName, oldMusicName, newMusicName, strGenre, strDuration, udate, lyrics, username);
        }


    }

    //insert artist
    public synchronized static void editArtist(String username) throws RemoteException {
        System.out.println("\n\t- Edit Artist -");
        System.out.println("\n\tINPUT OLD DATA");

        System.out.println("Artist name to edit:");
        Scanner s = new Scanner(System.in);
        String oldArtistName = s.nextLine();

        System.out.println("\n\t INPUT NEW DATA");

        System.out.println("NEW Artist name:");
        String newArtistName = s.nextLine();

        System.out.println("NEW Artist description:");
        String artistDesc = s.nextLine();

        try {
            if (rmiInterface.checkEditArtist(oldArtistName, newArtistName, artistDesc)) {
                System.out.println("Artist edited.");
                menuAdministrador(username);
            } else {
                System.out.println("Error editing artist.");
                menuAdministrador(username);
            }
        }catch(RemoteException e){
            editArtistLookup(oldArtistName, newArtistName, artistDesc, username);
        }
    }

    //insert album
    public synchronized static void editAlbum(String username) throws RemoteException {
        System.out.println("\n\t- Edit Album -");
        System.out.println("\n\tINPUT OLD DATA");

        System.out.println("Artist name of album to edit: ");
        Scanner s = new Scanner(System.in);
        String artistName = s.nextLine();

        System.out.println("Album name to edit:");
        String oldAlbumName = s.nextLine();

        System.out.println("\n\t INPUT NEW DATA");

        System.out.println("NEW Album name:");
        String newAlbumName = s.nextLine();

        System.out.println("NEW Album description:");
        String albumDescr = s.nextLine();

        System.out.println("NEW Musical Genre:");
        String musicalGenre = s.nextLine();

        String udate = dateInput(s);


        try {
            if (rmiInterface.checkEditAlbum(artistName, oldAlbumName, newAlbumName, albumDescr, musicalGenre, udate)) {
                System.out.println("Album edited.");
                menuAdministrador(username);
            } else {
                System.out.println("Error editing album.");
                menuAdministrador(username);

            }
        }catch(RemoteException e){
            editAlbumLookup(artistName, oldAlbumName, newAlbumName, albumDescr, musicalGenre, udate, username);
        }
    }

    //-------OTHERS----------//

    ///https://stackoverflow.com/questions/44696400/user-to-enter-date-mm-dd-yyyy-and-validate-the-data-entered
    public synchronized static String dateInput(Scanner s) {
        System.out.println("Please enter a date (mm/dd/yyyy)");
        String uDate = null;
        try {
            uDate = s.nextLine();
            LocalDate.parse(uDate, PARSE_FORMATTER);
        } catch (DateTimeParseException dtpe) {
            System.out.println(uDate + " is a not valid Date");
            dateInput(s);
        }
        return uDate;
    }


    //-------INTERFACE----------//

    //gets client interface username
    public String getUsername() {
        return clientUsername;
    }

    //sets client interface username
    public void setUsername(String username) {
        this.clientUsername = username;
    }

    //notifies rights
    public void notifyRights(){
        System.out.println("Your rights have been changed to editor.");
    }

    //-------MUSIC FUNCTIONS----------//

    //http://www.codebytes.in/2014/11/file-transfer-using-tcp-java.html?fbclid=IwAR2tAKzWEfDqG9oQvzA7bfOkgnihxFlgbuAmbSVVLCn_WGCOgJxW5AdRQ8o

    public synchronized static void viewSongDetails(String username){
        System.out.println("\n\t- View Song Details -");
        Scanner s = new Scanner(System.in);

        System.out.println("Artist name:");
        String artist = s.nextLine();
        System.out.println("Album name:");
        String album = s.nextLine();
        System.out.println("Song name:");
        String song = s.nextLine();



        try {
            String result = rmiInterface.checkViewSongDetails(username,artist,album,song);
            if (result.equals("type|view_song_details;error_in_view_song_details")) {
                System.out.println("An error ocurred... Try again!");
                menuUser(username);
            } else {
                String[] splitString = result.split(";");

                System.out.println("\t-Song Details-");

                //aqui
                String [] splitString3 =splitString[3].split("\\|");
                System.out.println("Music Name: " + splitString3[1]);

                //album name
                String [] splitString4 =splitString[4].split("\\|");
                System.out.println("Music Genre: " +splitString4[1]);

                //album name
                String [] splitString5 =splitString[5].split("\\|");
                System.out.println("Duration: " + splitString5[1]);

                //album name
                String [] splitString6 =splitString[6].split("\\|");
                System.out.println("Release Date: " + splitString6[1]);

                ////album name
                String [] splitString7 =splitString[7].split("\\|");
                System.out.println("Lyrics: " + splitString7[1]);

            }
        } catch (RemoteException e) {
            viewSongDetailsLookup(artist, album, song, username);
        }

    }

    public synchronized static void uploadFile(String username){

        System.out.println("\n\t- Upload Music -");
        Scanner s = new Scanner(System.in);

        System.out.println("Insert FilePath:");
        String filePath = s.nextLine();
        System.out.println("Insert Album Name:");
        String album = s.nextLine();
        System.out.println("Insert Artist Name:");
        String artist = s.nextLine();
        System.out.println("Insert Music Name:");
        String music = s.nextLine();

        String musicName = "";
        String aux = "";

        try {
            int porto = rmiInterface.checkUpload(username,filePath,album,artist,music);
            if (porto != -1) {
                //System.out.println("entrei aqui!!!");
                musicUploadClient(porto,filePath,username,music);
                System.out.println("Music uploaded.");
                menuUser(username);


            } else {
                System.out.println("Error uploading music.");
                menuUser(username);
            }
        } catch (RemoteException e){
            uploadFileLookup(musicName, aux, music, album, artist, username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void musicUploadClient(int porto,String path,String user, String music) {
        //Initialize socket
        Socket socket = null;
        try {
            socket = new Socket("localhost", porto);
            byte[] contents = new byte[10000];
            //Initialize the FileOutputStream to the output file's full path.
            //System.out.println(path+"_upload.mp3");
            //FileOutputStream fos = new FileOutputStream(path+"_"+user+"_upload.mp3");
            ///Users/iroseiro/Desktop/Dialeto.mp3
            FileOutputStream fos = new FileOutputStream("/Users/dingo/Desktop/DropMusic/Source Code/Uploads/"+user+"_"+music+".mp3");

            BufferedOutputStream bos = new BufferedOutputStream(fos);
            InputStream is = socket.getInputStream();

            //No of bytes read in one read() call
            int bytesRead = 0;
            while ((bytesRead = is.read(contents)) != -1)
                bos.write(contents, 0, bytesRead);

            bos.flush();
            socket.close();
        } catch (IOException e){
            System.out.println("Entrei na exception");
        }
        System.out.println("File saved successfully!");
    }

    public synchronized static void musicDownload(int porto, String path) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        //System.out.println(porto);
        //Specify the file
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        //Get socket's output stream
        OutputStream os = socket.getOutputStream();

        //Read File Contents into contents array
        byte[] contents;
        long fileLength = file.length();
        long current = 0;

        while(current!=fileLength){
            int size = 10000;
            if(fileLength - current >= size)
                current += size;
            else{
                size = (int)(fileLength - current);
                current = fileLength;
            }
            contents = new byte[size];
            bis.read(contents, 0, size);
            os.write(contents);
            System.out.print("Receiving file ... "+(current*100)/fileLength+"% complete!");
        }

        os.flush();
        //File transfer done. Close the socket connection!
        socket.close();
        System.out.println("File saved succesfully!");
    }

    public synchronized static void downloadFile(String username){

        System.out.println("\n\t- Download Music -");
        Scanner s = new Scanner(System.in);

        System.out.println("1. Kimi no na wa-Radwimps");
        System.out.println("2. Mystery of love-Sufjan Stevens");
        System.out.println("3. Visions of Gideon-Sufjan Stevens");
        System.out.println("4. Other");


        String musicNumber = s.nextLine();

        String musicName = "";
        String aux = "";

        String music = "";
        String album = "";
        String artist = "";

        if(musicNumber.equals("1")){
            musicName = "/Users/dingo/Desktop/DropMusic/Source Code/Musics/kiminonawa.mp3";
            music = "kimi no na wa";
            album = "chinocada";
            artist = "radwimps";
        }
        else if(musicNumber.equals("2")){
            musicName = "/Users/dingo/Desktop/DropMusic/Source Code/Musics/love.mp3";
            music = "mystery of love";
            album = "Call me by your name";
            artist = "Sufjan Stevens";

        }
        else if(musicNumber.equals("3")){
            musicName = "/Users/dingo/Desktop/DropMusic/Source Code/Musics/gideon.mp3";
            music = "visions of gideon";
            album = "Call me by your name";
            artist = "Sufjan Stevens";
        }
        else if(musicNumber.equals("4")){
            System.out.println("Insert FilePath:");
            musicName = s.nextLine();
            System.out.println("Insert Music:");
            music = s.nextLine();
            System.out.println("Insert Album:");
            album = s.nextLine();
            System.out.println("Insert Artist:");
            artist = s.nextLine();

        }

        try {
            int porto = rmiInterface.checkDownload(username,musicName,album,artist,music);
            if (porto != -1) {
                //System.out.println("entrei aqui!!!");
                musicDownload(porto,musicName);
                System.out.println("Music downloaded.");
                menuUser(username);


            } else {
                System.out.println("Error downloading music.");
                menuUser(username);
            }
        } catch (RemoteException e){
            downloadFileLookup(musicName, music, album, artist, username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //-------EXCEPTION HANDLING----------//

    //handles remote exception in starting rmiclient
    public synchronized static void startingLookup(){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();
                client = new RMIClient();
                welcome();
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in register function
    public synchronized static void registerLookup(String username, String password){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();
                //tenta registar-se
                if (rmiInterface.checkRegister(username, password)) {
                    System.out.println("Registered successfully.");
                    clientUsername = username;
                    client.setUsername(clientUsername);
                    //adicionar a lista de logged users no RMI
                    rmiInterface.addLoggedUsers(client);
                    menuUser(username);
                } else {
                    System.out.println("Username already in use.");
                    welcome();
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in menu user
    public synchronized static void menuUserLookup(int opt, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                if (rmiInterface.checkNotificationsRights(username, client)) {
                    menuAdministrador(username);
                }

                //verificacao option
                if ((opt < 0) || (opt > 5)) {
                    System.out.println("\tInvalid option!");
                    continue;
                }

                switch (opt) {
                    case 1:
                        viewData(username);
                        return;
                    case 2:
                        searchData(username);
                        return;
                    case 3:
                        writeCritic(username);
                        return;
                    case 4:
                        uploadFile(username);
                        return;
                    case 5:
                        downloadFile(username);
                        return;
                    case 0:
                        System.out.println("Logged out!");
                        //remover
                        rmiInterface.removeLoggedUsers(username);
                        System.exit(0);
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in menu admin
    public synchronized static void menuAdminLookup(int opt, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                rmiInterface.checkNotificationsRights(username, client);
                //verificacao option
                if ((opt < 0) || (opt > 4)) {
                    System.out.println("\n\tInvalid option! ");
                    continue;
                }

                switch (opt) {
                    case 1:
                        //menu insert data
                        insertData(username);
                        return;
                    case 2:
                        //menu change data
                        //here
                        changeData(username);
                        return;
                    case 3:
                        //menu remove data
                        removeData(username);
                        return;
                    case 4:
                        //change user rights
                        changeRights(username);
                    case 0:
                        System.out.println("Logged out!");
                        //remover
                        rmiInterface.removeLoggedUsers(username);
                        System.exit(0);
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in login
    public synchronized static void loginLookup(String username, String password){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                if (rmiInterface.checkLogin(username, password).equals("editor")) {
                    System.out.println("Logged in as editor.");

                    //meter interface com nome de user
                    clientUsername = username;
                    client.setUsername(clientUsername);
                    //adicionar a lista de logged users no RMI
                    rmiInterface.addLoggedUsers(client);

                    menuAdministrador(username);
                } else if (rmiInterface.checkLogin(username, password).equals("user")) {
                    System.out.println("Logged in as user.");

                    clientUsername = username;
                    client.setUsername(clientUsername);
                    //adicionar a lista de logged users no RMI
                    rmiInterface.addLoggedUsers(client);

                    menuUser(username);
                } else {
                    System.out.println("Error with login.");
                    welcome();
                }

            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in change rights
    public synchronized static void changeRightsLookup(String user, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                boolean checker = rmiInterface.checkUserRights(username);
                if (checker) {
                    System.out.println("Changed rights of user " + username + " to editor.");
                    menuAdministrador(user);
                } else {
                    System.out.println("User not found or is already an editor.");
                    menuAdministrador(user);
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in write critic
    public synchronized static void writeCriticLookup(String strArtistName, String strAlbumName, int rate, String strCritic, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                if ((rate < 0) || (rate > 5)) {
                    System.out.println("\tInvalid rate! Try again ");
                    writeCritic(username);
                }

                if (strCritic.length()>300) {
                    System.out.println("\tCritic too big! Try again");
                    writeCritic(username);
                }

                if (rmiInterface.checkCritic(username,strArtistName,strAlbumName,rate,strCritic)) {
                    System.out.println("Critic added.");
                    menuUser(username);
                } else {
                    System.out.println("Error with critic! Try again");
                    menuUser(username);
                }

            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in insert music
    public synchronized static void insertMusicLookup(String strName, String strGenre, String strDuration, String udate, String lyrics, String strArtistName, String strAlbumName, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();
                if (rmiInterface.checkMusic(strName, strGenre,strDuration,udate, lyrics, strArtistName,strAlbumName)) {

                    System.out.println("Music added.");
                    menuAdministrador(username);
                } else {
                    System.out.println("Error adding music.");
                    menuAdministrador(username);
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in insert artist
    public synchronized static void inserArtistLookup(String artistName, String artistDesc, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                if (rmiInterface.checkArtist(artistName, artistDesc)) {
                    System.out.println("Artist added.");
                    menuAdministrador(username);
                } else {
                    System.out.println("Error adding artist.");
                    menuAdministrador(username);
                }

                //meter cenas para aqui
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in insert album
    public synchronized static void insertAlbumLookup(String albumName, String albumDescr, String musicalGenre, String udate, String artistName, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                if (rmiInterface.checkAlbum(albumName, albumDescr, musicalGenre, udate, artistName)) {
                    System.out.println("Album added.");
                    menuAdministrador(username);
                } else {
                    System.out.println("Error adding Album.");
                    menuAdministrador(username);

                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in view artist details
    public synchronized static void viewArtistDetailsLookup(String artist, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                String result = rmiInterface.checkViewArtistDetails(username, artist);
                if (result.equals("type|view_artist_details;error in view_artist_details")) {
                    System.out.println("An error ocurred... Try again!");
                    menuUser(username);
                } else {
                    String[] splitStringAll = result.split(";");

                    System.out.println("\t-Artist details-");

                    String[] splitStringName = splitStringAll[1].split("\\|");
                    System.out.println("Artist name: " + splitStringName[1]);

                    String[] splitStringDescription = splitStringAll[2].split("\\|");
                    System.out.println("Artist Biography: " + splitStringDescription[1]);


                    int j = 3, size = splitStringAll.length;

                    if(size > 0) {
                        System.out.println("Discography:");
                    }

                    while (j < size) {
                        String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                        System.out.println("Album Name: " + splitStringAlbumName[1]);
                        j++;
                    }


                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in view album details
    public synchronized static void viewAlbumDetailsLookup(String artist, String album, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                String result = rmiInterface.checkViewAlbumDetails(username,artist,album);
                if (result.equals("type|view_album_details;error_in_view_album_details")) {
                    System.out.println("An error ocurred... Try again!");
                    menuUser(username);
                } else {
                    String[] splitStringAll = result.split(";");

                    System.out.println("\t-Album details-");

                    String[] splitStringName = splitStringAll[1].split("\\|");
                    System.out.println("Artist name: " + splitStringName[1]);
                    String[] splitString2 = splitStringAll[3].split("\\|");
                    System.out.println("Description: " + splitString2[1]);
                    String[] splitString3 = splitStringAll[4].split("\\|");
                    System.out.println("Musical Genre: " + splitString3[1]);
                    String[] splitString4 = splitStringAll[5].split("\\|");
                    System.out.println("Release Date: " + splitString4[1]);


                    int j = 6, size = splitStringAll.length;

                    if(size > 2) {
                        System.out.println("Songs:");
                        while (j < size) {
                            String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                            System.out.println("Music Name: " + splitStringAlbumName[1]);
                            j++;
                        }
                    }
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in view album critics
    public synchronized static void viewAlbumCriticsLookup(String artist, String album, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                String result = rmiInterface.checkViewAlbumCritics(username,artist,album);
                if (result.equals("type|view_album_critics;error in view_album_critics")) {
                    System.out.println("An error ocurred... Try again!");
                    menuUser(username);
                } else {
                    String[] splitStringAll = result.split(";");

                    System.out.println("\t-Album critics-");

                    String[] splitStringName = splitStringAll[1].split("\\|");
                    System.out.println("Artist name: " + splitStringName[1]);

                    String [] splitString3 =splitStringAll[3].split("\\|");
                    System.out.println("Average Rate: " + splitString3[1]);

                    if(splitStringAll.length > 3) {
                        int j = 4, size = splitStringAll.length;
                        while(j < size) {
                            String[] splitStringArtistName = splitStringAll[j + 1].split("\\|");
                            String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                            System.out.println("Critic: "+splitStringArtistName[1]);
                            System.out.println("Username: " + splitStringAlbumName[1]);
                            j += 2;

                        }
                    }

                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in search album from album name
    public synchronized static void searchAlbumFromAlbumNameLookup(String albumName, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                String result = rmiInterface.checkFromAlbumName(username, albumName);
                if (result.equals("type|search_album_name;error in search_album_name")) {
                    System.out.println("An error ocurred... Try again!");
                    menuUser(username);
                } else {
                    System.out.println("\t-Results-");
                    //Aqui correu tudo bem
                    String[] splitStringAll = result.split(";");
                    //get item count
                    String[] splitString1 = splitStringAll[1].split("\\|");
                    int itemCount = Integer.parseInt(splitString1[1]);
                    //get albums
                    int j = 2, size = splitStringAll.length;
                    while (j < size) {
                        String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                        System.out.println("Album Name: " + splitStringAlbumName[1]);
                        String[] splitStringArtistName = splitStringAll[j + 1].split("\\|");
                        System.out.println("Artist Name: " + splitStringArtistName[1]);
                        System.out.println("");
                        j += 2;
                    }
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in search album from artist lookup
    public synchronized static void searchAlbumfromArtistLookup(String artist, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                String result = rmiInterface.checkFromArtistName(username, artist);
                if (result.equals("type|search_album_artist;error in search_album_artist")) {
                    System.out.println("An error ocurred... Try again!");
                    menuUser(username);
                } else {
                    String[] splitStringAll = result.split(";");

                    System.out.println("\t-Results-");

                    String[] splitStringName = splitStringAll[1].split("\\|");
                    System.out.println("Artist name: " + splitStringName[1]);

                    int j = 3, size = splitStringAll.length;
                    while (j < size) {
                        String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                        System.out.println("Album Name: " + splitStringAlbumName[1]);
                        j++;
                    }
                    menuUser(username);
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in remove music
    public synchronized static void removeMusicLookup(String musicName, String artistName, String albumName, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();
                if (rmiInterface.checkRemoveMusic(musicName, artistName, albumName)) {
                    System.out.println("Music removed.");
                    menuAdministrador(username);
                } else {
                    System.out.println("Error removing music.");
                    menuAdministrador(username);

                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in remove album
    public synchronized static void removeAlbumLookup(String albumName, String artistName, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                if (rmiInterface.checkRemoveAlbum(artistName, albumName)) {
                    System.out.println("Album removed.");
                    menuAdministrador(username);
                } else {
                    System.out.println("Error removing album.");
                    menuAdministrador(username);

                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in remove artist
    public synchronized static void removeArtistLookup(String artistName, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                if (rmiInterface.checkRemoveArtist(artistName)) {
                    System.out.println("Artist removed.");
                    menuAdministrador(username);
                } else {
                    System.out.println("Error removing artist.");
                    menuAdministrador(username);

                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in edit music
    public synchronized static void editMusicLookup(String artistName, String albumName, String oldMusicName, String newMusicName, String strGenre, String strDuration, String udate, String lyrics, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                if (rmiInterface.checkEditSong(artistName, albumName,oldMusicName,newMusicName, strGenre, strDuration, udate, lyrics)) {

                    System.out.println("Music edited.");
                    menuAdministrador(username);
                } else {
                    System.out.println("Error editing music.");
                    menuAdministrador(username);
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in edit artist
    public synchronized static void editArtistLookup(String oldArtistName, String newArtistName, String artistDesc, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();
                if (rmiInterface.checkEditArtist(oldArtistName, newArtistName, artistDesc)) {
                    System.out.println("Artist edited.");
                    menuAdministrador(username);
                } else {
                    System.out.println("Error editing artist.");
                    menuAdministrador(username);
                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in edit album
    public synchronized static void editAlbumLookup(String artistName, String oldAlbumName, String newAlbumName, String albumDescr, String musicalGenre, String udate, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();
                if (rmiInterface.checkEditAlbum(artistName, oldAlbumName, newAlbumName, albumDescr, musicalGenre, udate)) {
                    System.out.println("Album edited.");
                    menuAdministrador(username);
                } else {
                    System.out.println("Error editing album.");
                    menuAdministrador(username);

                }
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in view song details
    public synchronized static void viewSongDetailsLookup(String artist, String album, String song, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                String result = rmiInterface.checkViewSongDetails(username,artist,album,song);
                if (result.equals("type|view_song_details;error_in_view_song_details")) {
                    System.out.println("An error ocurred... Try again!");
                    menuUser(username);
                } else {
                    String[] splitString = result.split(";");

                    System.out.println("\t-Song Details-");

                    //aqui
                    String [] splitString3 =splitString[3].split("\\|");
                    System.out.println("Music Name: " + splitString3[1]);

                    //album name
                    String [] splitString4 =splitString[4].split("\\|");
                    System.out.println("Music Genre: " +splitString4[1]);

                    //album name
                    String [] splitString5 =splitString[5].split("\\|");
                    System.out.println("Duration: " + splitString5[1]);

                    //album name
                    String [] splitString6 =splitString[6].split("\\|");
                    System.out.println("Release Date: " + splitString6[1]);

                    ////album name
                    String [] splitString7 =splitString[7].split("\\|");
                    System.out.println("Lyrics: " + splitString7[1]);

                }


            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in upload file
    public synchronized static void uploadFileLookup(String musicName, String aux, String music, String album, String artist, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                int porto = rmiInterface.checkUpload(username,musicName,album,artist,music);
                if (porto != -1) {
                    System.out.println("entrei aqui!!!");
                    System.out.println("Music downloaded.");
                    menuUser(username);


                } else {
                    System.out.println("Error downloading music.");
                    menuUser(username);
                }

            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            }
        }
    }

    //handles remote exception in download file
    public synchronized static void downloadFileLookup(String musicName, String music, String album, String artist, String username){
        int fails = 0;
        while(fails < 30){
            try{
                Thread.sleep(1000);
                rmiInterface = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
                rmiInterface.sayHello();

                int porto = rmiInterface.checkDownload(username,musicName,album,artist,music);
                if (porto != -1) {
                    //System.out.println("entrei aqui!!!");
                    musicDownload(porto,musicName);
                    System.out.println("Music downloaded.");
                    menuUser(username);


                } else {
                    System.out.println("Error downloading music.");
                    menuUser(username);
                }

            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                fails++;
                if(fails == 30) {
                    System.out.println("Couldn't connect to RMI Server.");
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

