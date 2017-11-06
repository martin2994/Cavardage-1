<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: benoit
  Date: 02/11/17
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<datalist id="listeVille">
    <c:forEach items="${listeVilles}" var="ville">
        <option value="${ville.nomVille}(${ville.departement})" />
    </c:forEach>
</datalist>
<form action="ControleurUtilisateur" method="post" name="myFormVille" onsubmit="return verifierVille()">
    <legend>Proposer un trajet</legend>
    <div>
        <div class="col-lg-6">
            <label hidden id="erreurVille">La ville de départ est la même que la ville d'arrivée</label>
            <div class="form-group">
                <label for="villeDepart">Ville de départ : </label>
                <input class="form-control" type="text" list="listeVille" id="villeDepart" name="villeDepart" autocomplete="off"  required>
            </div>
            <div class="form-group">
                <label for="villeArrivee">Ville d'arrivée : </label>
                <input class="form-control" type="text" list="listeVille" id="villeArrivee" name="villeArrivee" autocomplete="off"  required>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary" name="afaire" id="bouton_continuer" value="enregistrerTrajetTemp" >Continuer</button>
            </div>
        </div>
    </div>
</form>


<script>

    function verifierVille(){
        var villeDepart = document.getElementById("villeDepart").value;
        var villeArrivee = document.getElementById("villeArrivee").value;
        if(villeDepart == villeArrivee || villeDepart == "" || villeArrivee == ""){
            document.getElementById("erreurVille").style.display = "block";
            return false;
        }else{
            document.getElementById("erreurVille").style.display = "none";
            return true;
        }
    }
</script>