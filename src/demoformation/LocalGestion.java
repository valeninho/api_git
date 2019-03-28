/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoformation;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import formation.DAO.LocalDAO;
import formation.DAO.DAO;
import formation.info.Local;
import myconnections.DBConnection;
import java.util.PropertyResourceBundle;

/**
 *
 * @author valentin.lor
 */
public class LocalGestion {
    
    public static void main(String[] args) {
        LocalGestion gc = new LocalGestion();
        gc.gestion();

    }

    Scanner sc = new Scanner(System.in);
    Local locActuel = null;
    DAO<Local> localDAO = null;

    public LocalGestion() {

    }

    
    

    public void gestion() {

        Connection dbConnect = DBConnection.getConnection();
        if (dbConnect == null) {
            System.out.println("connection invalide");
            System.exit(1);
        }        
        
        System.out.println("------------------------------");
        System.out.println("      Connexion Établie       ");
        System.out.println("------------------------------");

        localDAO = new LocalDAO() {
            
        };
                
        localDAO.setConnection(dbConnect);
    
        int ch = 0;
        do {
            System.out.println("1.nouveau \n2.recherche\n3.modification\n4.suppression\n5.recherche sur nom\n6.fin");
            System.out.print("choix :");
            ch = sc.nextInt();
            sc.skip("\n");
            switch (ch) {
                case 1:
                    nouveau();
                    break;
                case 2:
                    recherche();
                    break;
                case 3:
                    modif();
                    break;
                case 4:
                    sup();
                    break;
                case 5:
                    rechnom();
                    break;
                /*case 6:
                    derncom();
                    break;
                case 6: 
                    rechpartielle();*/
                case 7:
                    System.out.println("bye");
                    break;
                default:
                    System.out.println("choix incorrect");
            }

        } while (ch != 7);
        DBConnection.closeConnection();
    }

    public void nouveau() {

        System.out.print("Sigle :");
        String sigle = sc.nextLine();
        System.out.print("Places :");
        int places = sc.nextInt();
        sc.skip("\n");
        System.out.print("Description:");
        String description = sc.nextLine();
        locActuel = new Local(0, sigle, places, description);
        try {
            locActuel = localDAO.create(locActuel);
            System.out.println("Vous avez créé le local suivant : ");
            System.out.println("Sigle: " + locActuel.getSigle() + "     Places: " + locActuel.getPlaces() + "       Description: " + locActuel.getDescription());
            //System.out.println("local actuel : " + locActuel);
        } catch (SQLException e) {
            System.out.println("erreur :" + e);
            //System.out.println("Local déjà existant");
        }

    }

    public void recherche() {
        try {
            System.out.println("numéro recherché :");
            int nc = sc.nextInt();
            locActuel = localDAO.read(nc);
            System.out.println("Informations : " + locActuel.getSigle() + locActuel.getPlaces() + locActuel.getDescription());
            System.out.println("local actuel : " + locActuel);

        } catch (SQLException e) {
            System.out.println("erreur " + e.getMessage());
        }
    }

    public void modif() {
        try {
            System.out.println("Sigle :");
            String sigle = sc.nextLine();
            locActuel.setSigle(sigle);
            localDAO.update(locActuel); 
        } catch (SQLException e) {
            System.out.println("erreur " + e.getMessage());
        }

    }

    public void sup() {
        try {
            
            localDAO.delete(locActuel);
        } catch (SQLException e) {
            System.out.println("erreur " + e.getMessage());
        }
    }

    public void rechnom() {
        System.out.println("nom recherché : ");
        String sigle = sc.nextLine();
        try {
            List<Local> alc = ((LocalDAO) localDAO).rechNom(sigle);
            for (Local cl : alc) {
                System.out.println(cl);
            }
        } catch (SQLException e) {
            System.out.println("erreur " + e.getMessage());
        }
    }
    /*
    public void rechpartielle(){
        try {
            System.out.println("Sur quoi rechercher un local ? ");
            String sigle = sc.nextLine();
        }
        
        catch (SQLException e){
            System.out.println("erreur" + e.getMessage());
        }
        
    }*/

    
    /*public void derncom() {
        try {
            LocalDate dt = ((LocalDAO) localDAO).dern_com(locActuel);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E d MMMM yyyy", Locale.FRENCH);
            String aff = dt.format(dtf);
            System.out.println("date de la denière commande de " + clActuel + " = " + aff);
        } catch (SQLException e) {
            System.out.println("erreur " + e.getMessage());
        }
    } */

 
}
