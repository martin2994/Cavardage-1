package servlets;

import dtos.StatistiquesDTO;
import dtos.VilleDTO;
import ejbs.MaFacadeAdministrateur;
import exceptions.GabaritException;
import exceptions.VilleExistante;
import exceptions.VilleNonTrouvee;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.StringTokenizer;

@WebServlet("ControleurAdmin")
public class ControleurAdmin extends HttpServlet {

    @EJB
    MaFacadeAdministrateur ejb;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String test = request.getParameter("boutonAdmin");

        if(null == test){
            request.getRequestDispatcher("/WEB-INF/admin/accueilAdmin.jsp").forward(request, response);
        }else{
            switch (test){
                case "gererVille":
                    setListeVilles(request);
                    setGestionVille(request);
                    request.getRequestDispatcher("/WEB-INF/admin/accueilAdmin.jsp").forward(request, response);
                    break;
                case "gererGabarit":
                    setListeGabarits(request);
                    setGestionGabarit(request);
                    request.getRequestDispatcher("/WEB-INF/admin/accueilAdmin.jsp").forward(request, response);
                    break;
                case "ajouterVille":
                    ajouterVille(request, response);
                    break;
                case "ajouterGabarit":
                    ajouterGabarit(request, response);
                    break;
                case "supprimerGabarit":
                    supprimerGabarit(request, response);
                    break;
                case "supprimerVille":
                    supprimerVille(request, response);
                    break;
                case "statistiques":
                    voirStatistiques(request, response);
                    break;
                case "actualiserStat":
                    voirStatistiques(request, response);
                    break;
                case "deconnexion":
                    request.logout();
                    request.getSession().invalidate();
                    request.getSession().removeAttribute("utilisateur");
                    request.getSession().invalidate();
                    response.sendRedirect(request.getContextPath());
                    break;
                default:
                    break;
            }
        }
    }

    private void setGestionVille(HttpServletRequest request){
        request.setAttribute("aAfficher", "gestionVille");
    }

    private void setGestionGabarit(HttpServletRequest request){
        request.setAttribute("aAfficher", "gestionGabarit");
    }

    private void setStatistiques(HttpServletRequest request){
        request.setAttribute("aAfficher", "statistiques");
    }

    private void setListeVilles(HttpServletRequest request){
        List<VilleDTO> listeVilles = ejb.getListeVilleDTO();
        request.setAttribute("listeVilles",listeVilles);
    }

    private void setListeGabarits(HttpServletRequest request){
        List<String> listeGabarits = ejb.getListeGabarits();
        request.setAttribute("listeGabarits", listeGabarits);
    }

    private void ajouterVille(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomVille = request.getParameter("nomVilleAAjouter");
        String departementVille = request.getParameter("departementVilleAAjouter");
        try {
            ejb.ajouterVille(nomVille,departementVille);
            request.setAttribute("messageSucces", "La ville " + nomVille + "(" + departementVille + ") a bien été ajoutée à la base de données");
        } catch (VilleExistante villeExistante) {
            request.setAttribute("messageErreur", villeExistante.getMessage());
        }
        setGestionVille(request);
        setListeVilles(request);
        request.getRequestDispatcher("/WEB-INF/admin/accueilAdmin.jsp").forward(request, response);
    }

    private void ajouterGabarit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomGabarit = request.getParameter("nomGabaritAAjouter");
        try {
            ejb.ajouterGabarit(nomGabarit);
            request.setAttribute("messageSucces", "Le gabarit '" + nomGabarit + "' a bien été ajouté à la base de données");
        } catch (GabaritException e) {
            request.setAttribute("messageErreur", e.getMessage());
        }
        setGestionGabarit(request);
        setListeGabarits(request);
        request.getRequestDispatcher("/WEB-INF/admin/accueilAdmin.jsp").forward(request, response);
    }

    private void supprimerVille(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String villeSupp = request.getParameter("nomVilleASupprimer");
        StringTokenizer st = new StringTokenizer(villeSupp, "()");
        String nomVilleSupp = st.nextToken();
        String departementVilleSupp = st.nextToken();
        try {
            ejb.supprimerVille(nomVilleSupp, departementVilleSupp);
            request.setAttribute("messageSucces", "La ville " + villeSupp + " a bien été supprimée");
        } catch (VilleNonTrouvee villeNonTrouvee) {
            request.setAttribute("messageErreur", villeNonTrouvee.getMessage());
        }
        setGestionVille(request);
        setListeVilles(request);
        request.getRequestDispatcher("/WEB-INF/admin/accueilAdmin.jsp").forward(request, response);
    }

    private void supprimerGabarit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomGabaritSupp = request.getParameter("nomGabaritASupprimer");
        String nomGabaritRemp = request.getParameter("nomGabaritARemplacer");
        try {
            ejb.supprimerGabarit(nomGabaritSupp, nomGabaritRemp);
            request.setAttribute("messageSucces", "Le gabarit '" + nomGabaritSupp + "' a bien été remplacé par '" + nomGabaritRemp + "'");
        } catch (GabaritException e) {
            request.setAttribute("messageErreur", e.getMessage());
        }
        setGestionGabarit(request);
        setListeGabarits(request);
        request.getRequestDispatcher("/WEB-INF/admin/accueilAdmin.jsp").forward(request, response);
    }

    private void voirStatistiques(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setStatistiques(request);
        StatistiquesDTO stat = ejb.recupererStatistiques();
        LocalDate date = LocalDate.now();
        request.setAttribute("stat", stat);
        request.setAttribute("date", date.getDayOfMonth() + " / " + date.getMonthValue() + " / " + date.getYear());
        setStatistiques(request);
        request.getRequestDispatcher("/WEB-INF/admin/accueilAdmin.jsp").forward(request, response);
    }
}
