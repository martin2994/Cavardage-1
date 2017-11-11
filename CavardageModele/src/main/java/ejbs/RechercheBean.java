package ejbs;

import dtos.TrajetDTO;
import dtos.VilleDTO;
import entities.Trajet;
import entities.Ville;
import exceptions.DateAnterieureException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.util.*;

@SuppressWarnings("unchecked")

@Stateless(name = "RechercheBean")
public class RechercheBean {

    /**
     * L'entityManager
     */
    @PersistenceContext(unitName="monUnite")
    private EntityManager em;

    /**
     * L'automate
     */
    @EJB
    private Automate automate;

    /**
     * Effectue une recherche de trajet suivant des caractéristiques
     * @param villeDepart               La ville de départ du trajet
     * @param departementDepart         Le département de la ville de départ
     * @param villeArrivee              La ville d'arrivée du trajet
     * @param departementArrivee        Le département de la vilel d'arrivée
     * @param date                      La date du trajet
     * @param prix                      Le prix maximum (non obligatoire)
     * @return                          La liste des trajtes correspondants aux critères de recherche
     * @throws ParseException           Si la date du trajet n'est pas dans un format acceptable
     * @throws DateAnterieureException  Si la date est antérieure à aujourd'hui
     */
    public List<TrajetDTO> rechercheTrajet(String villeDepart, String departementDepart, String villeArrivee,
                                           String departementArrivee, String date, String prix) throws ParseException, DateAnterieureException {

            if(!automate.testDate(date)){
                throw new DateAnterieureException("Vous ne pouvez pas rechercher un trajet à une date antérieure");
            }
            String mq="SELECT DISTINCT t From Trajet t, Etape e WHERE " +
                    "t.villeDepart.nomVille=:villeDepart" +
                    " and ((t.villeArrivee.nomVille=:villeArrivee) or" +
                    " (e.villeEtape.nomVille=:villeArrivee and e.trajet=t)) " +
                    "and t.date=:date and t.statut='aVenir'";
            Query query;
            if(null != prix && !prix.equals("")){
                if(Integer.parseInt(prix)>0 ) {
                    query=em.createQuery("SELECT DISTINCT t From Trajet t, Etape e WHERE " +
                            "t.villeDepart.nomVille=:villeDepart" +
                            " and ((t.villeArrivee.nomVille=:villeArrivee and t.prix<= :prix) or" +
                            " (e.villeEtape.nomVille=:villeArrivee and e.trajet=t and e.prix<= :prix)) " +
                            "and t.date=:date and t.statut='aVenir'");
                    query.setParameter("prix",Integer.parseInt(prix));
                }else{
                    query=em.createQuery(mq);
                }
            }else{
                query=em.createQuery(mq);
            }
            query.setParameter("villeDepart", villeDepart+"_"+departementDepart);
            query.setParameter("villeArrivee", villeArrivee+"_"+departementArrivee);
            query.setParameter("date",date);
            List<Trajet> lt = query.getResultList();
            List<TrajetDTO> ltd = new ArrayList<>();
            for(Trajet t :lt){
                ltd.add(new TrajetDTO(t));
            }
            Collections.sort(ltd);
            return ltd;

    }

    /**
     * Renvoie la liste des villes existantes dans la base de données
     * @return  La liste des villes
     */
    public List<VilleDTO> getListeVillesDTO(){
        Query q = em.createQuery("SELECT v FROM Ville v");
        List<Ville> listeVille = q.getResultList();
        if(!listeVille.isEmpty()){
            List<VilleDTO> villeDTOS = new ArrayList<>();
            for(Ville v : listeVille){
                villeDTOS.add(new VilleDTO(v.getNomVille()));
            }
            return villeDTOS;
        }
        return new ArrayList<>();
    }
}
