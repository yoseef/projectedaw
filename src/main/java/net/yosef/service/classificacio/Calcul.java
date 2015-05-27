package net.yosef.service.classificacio;

import net.yosef.domain.Equip;
import net.yosef.domain.Partit;

/**
 * Created by user on 23/05/15.
 */
public class Calcul {
    Equip visit;
    Equip local;
    Partit p;

    void decidirGuanyador(Partit p) {
        if (!p.getEquips().isEmpty()){
            visit = p.getEquips().get("visitant");
            local = p.getEquips().get("local");
            p.getGols_v();
            p.getGols_l();
            sumarPunts();
        }
    }

    public void sumarPunts() {
        if (p.getGols_l() == p.getGols_v()) {
            local.haEmpatat(p.getGols_l());
            visit.haEmpatat(p.getGols_v());
        } else if (p.getGols_l() > p.getGols_v()) {
            local.haGuanyat(p.getGols_l(), p.getGols_v());
            visit.haPerdut(p.getGols_v(), p.getGols_l());
        } else if (p.getGols_l() < p.getGols_v()) {
            visit.haGuanyat(p.getGols_v(), p.getGols_l());
            local.haPerdut(p.getGols_l(), p.getGols_v());
        } else {
            System.out.println("Qui ha guanyat?");
        }
    }
    public void guardarElsCanvis(){
        //save local
        //save visitant
    }

}
