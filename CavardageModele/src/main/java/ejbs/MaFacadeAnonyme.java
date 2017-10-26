package ejbs;

import dtos.TrajetDTO;
import dtos.UtilisateurDTO;
import exceptions.LoginExistantException;
import exceptions.UtilisateurNonInscritException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface MaFacadeAnonyme {

    public UtilisateurDTO connexion(String login, String mdp) throws UtilisateurNonInscritException;
    public UtilisateurDTO inscription(String login,String nom,String mdp) throws LoginExistantException;
    public List<TrajetDTO> rechercheTrajet(String villeDepart, String villeArrive, String date);
    public List<TrajetDTO> dernierAjout();
}
