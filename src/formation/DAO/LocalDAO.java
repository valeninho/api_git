
package formation.DAO;

import formation.info.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import formation.info.Local;

public class LocalDAO extends DAO<Local> {

    @Override
    public Local create(Local obj) throws SQLException {

        String req1 = "insert into local(sigle, places, description) values(?,?,?)";
        String req2 = "select idlocal from local where sigle=?";
        try (PreparedStatement pstm1 = dbConnect.prepareStatement(req1);
                PreparedStatement pstm2 = dbConnect.prepareStatement(req2)) {
            pstm1.setString(1, obj.getSigle());
            pstm1.setInt(2, obj.getPlaces());
            pstm1.setString(3, obj.getDescription());
            int n = pstm1.executeUpdate();
            if (n == 0) {
                throw new SQLException("erreur de creation local, aucune ligne créée");
            }
            pstm2.setString(1, obj.getSigle());
            try (ResultSet rs = pstm2.executeQuery()) {
                if (rs.next()) {
                    int idlocal = rs.getInt(1);
                    obj.setIdlocal(idlocal);
                    return read(idlocal);
                } else {
                    throw new SQLException("erreur de création local, record introuvable");
                }
            }
        }
    }

    /**
     * récupération des données d'un client sur base de son identifiant
     *
     * @throws SQLException code inconnu
     * @param idclient identifiant du client
     * @return client trouvé
     */
    
    public Local read(int idlocal) throws SQLException {

        String req = "select * from local where idlocal = ?";

        try (PreparedStatement pstm = dbConnect.prepareStatement(req)) {

            pstm.setInt(1, idlocal);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String sigle = rs.getString("SIGLE");
                    int places = rs.getInt("PLACES");
                    String description = rs.getString("DESCRIPTION");
                    return new Local(idlocal, sigle, places, description);

                } else {
                    throw new SQLException("Local inconnu");
                }

            }
        }
    }

    /**
     * mise à jour des données du client sur base de son identifiant
     *
     * @return Client
     * @param obj client à mettre à jour
     * @throws SQLException erreur de mise à jour
     */
    @Override
    public Local update(Local obj) throws SQLException {
        String req = "update local set sigle=? where idlocal = ?";
        try (PreparedStatement pstm = dbConnect.prepareStatement(req)) {

            pstm.setInt(4, obj.getIdlocal());
            pstm.setString(1, obj.getSigle());
            pstm.setInt(2, obj.getPlaces());
            pstm.setString(3, obj.getDescription());
            int n = pstm.executeUpdate();
            if (n == 0) {
                throw new SQLException("aucune ligne local mise à jour");
            }
            return read(obj.getIdlocal());
        }
    }

    /**
     * effacement du client sur base de son identifiant
     *
     * @throws SQLException erreur d'effacement
     * @param obj client à effacer
     */
    @Override
    public void delete(Local obj) throws SQLException {

        String req = "delete from local where idlocal= ?";
        try (PreparedStatement pstm = dbConnect.prepareStatement(req)) {

            pstm.setInt(1, obj.getIdlocal());
            int n = pstm.executeUpdate();
            if (n == 0) {
                throw new SQLException("aucune ligne local effacée");
            }

        }
    }

    /*@Override
    public Local void rechpar(int idlocal) throws SQLException {

        String req = "select * from local where description LIKE ?%%";
        try (PreparedStatement pstm = dbConnect.prepareStatement(req)) {
            
            pstm.setInt(1, idlocal);
            try(ResultSet rs = pstm.executeQuery()){
                if(rs.next()){
                    String sigle = rs.getString("SIGLE");
                    int places = rs.getInt("PLACES");
                    String description = rs.getString("DESCRIPTION");
                    return new Local(idlocal, sigle, places, description);
                }
                    else { 
                            throw new SQLException("Aucun local correspondant");
                            }
                
            }
            
        }
    }*/

    /**
     * méthode permettant de récupérer tous les clients portant un certain nom
     *
     * @param nomrech nom recherché
     * @return liste de clients
     * @throws SQLException nom inconnu
     */
    public List<Local> rechNom(String nomrech) throws SQLException {
        List<Local> plusieurs = new ArrayList<>();
        String req = "select * from local where sigle = ?";

        try (PreparedStatement pstm = dbConnect.prepareStatement(req)) {
            pstm.setString(1, nomrech);
            try (ResultSet rs = pstm.executeQuery()) {
                boolean trouve = false;
                while (rs.next()) {
                    trouve = true;
                    int idlocal = rs.getInt("IDCLIENT");
                    String sigle = rs.getString("SIGLE");
                    int places = rs.getInt("PLACES");
                    String description = rs.getString("DESCRIPTION");

                    plusieurs.add(new Local(idlocal, sigle, places, description));
                }

                if (!trouve) {
                    throw new SQLException("nom inconnu");
                } else {
                    return plusieurs;
                }
            }
        }

    }
    /**
     * méthode permettant de récupérer la date de la dernière commande d'un
     * client
     *
     * @param obj client recherché
     * @return date de la dernière commande
     * @throws SQLException client sans commande
     *
     * public LocalDate dern_com(Local obj) throws SQLException{ String req =
     * "select derniere_com from CLIDATE where idclient = ?";
     * try(PreparedStatement pstm = dbConnect.prepareStatement(req)){
     * pstm.setInt(1,obj.idlocal()); try(ResultSet rs = pstm.executeQuery()){
     * if(rs.next()){ LocalDate dt = rs.getDate(1).toLocalDate(); return dt; }
     * else throw new SQLException("aucune commande enregistrée pour ce
     * client"); } } }
     */
}
