package entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Utilisateur {

    @Id
    private String login;
    private String nom;
    private String motDePasse;

    @ManyToOne
    @JoinColumn(name = "IDROLE")
    private Role roleUtilisateur;

    @OneToMany(mappedBy = "utilisateur")
    private List<Vehicule> listeVehicule;

    @OneToMany(mappedBy = "utilisateurReservation")
    private List<Reservation> listeReservation;

    @OneToMany(mappedBy = "donneNote")
    private List<Appreciation> note;

    @OneToMany(mappedBy = "estNote")
    private List<Appreciation> estNote;

    @OneToMany
    private List<Notification> notifications;

    public Utilisateur() {
    }

    public Utilisateur(String login, String nom, String motDePasse) {
        this.login = login;
        this.nom = nom;
        this.motDePasse = motDePasse;
    }

    public Utilisateur(String login, String nom, String mdp, Role roleUtilisateur) {
        this.login = login;
        this.nom = nom;
        this.motDePasse = mdp;
        this.roleUtilisateur= roleUtilisateur;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRoleUtilisateur() {
        return roleUtilisateur;
    }

    public void setRoleUtilisateur(Role roleUtilisateur) {
        this.roleUtilisateur = roleUtilisateur;
    }

    public List<Vehicule> getListeVehicule() {
        return listeVehicule;
    }

    public void setListeVehicule(List<Vehicule> listeVehicule) {
        this.listeVehicule = listeVehicule;
    }

    public List<Reservation> getListeReservation() {
        return listeReservation;
    }

    public void setListeReservation(List<Reservation> listeReservation) {
        this.listeReservation = listeReservation;
    }

    public List<Appreciation> getNote() {
        return note;
    }

    public void setNote(List<Appreciation> note) {
        this.note = note;
    }

    public List<Appreciation> getEstNote() {
        return estNote;
    }

    public void setEstNote(List<Appreciation> estNote) {
        this.estNote = estNote;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public boolean ajouterVehicule(Vehicule vehicule){
        if(this.listeVehicule.contains(vehicule)){
            return false;
        } else {
            this.listeVehicule.add(vehicule);
            return true;
        }
    }

    public boolean supprimerVehicule(Vehicule vehicule){
        return listeVehicule.remove(vehicule);
    }

    public boolean possedeVehicule(Vehicule vehicule){
        for(Vehicule vehiculeTest : listeVehicule){
            if(vehicule.equals(vehiculeTest)){
                return true;
            }
        }
        return false;
    }

    public boolean ajouterNotification(Notification notification){
        if(this.notifications.contains(notification)){
            return false;
        } else {
            this.notifications.add(notification);
            return true;
        }
    }

    public boolean supprimerNotification(Notification notification){
        if(!notifications.contains(notification)){
            return false;
        } else {
            notifications.remove(notification);
            return true;
        }
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "login='" + login + '\'' +
                ", nom='" + nom + '\'' +
                ", roleUtilisateur=" + roleUtilisateur +
                '}';
    }
}
