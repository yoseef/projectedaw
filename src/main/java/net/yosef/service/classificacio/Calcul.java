package net.yosef.service.classificacio;

import net.yosef.domain.Equip;
import net.yosef.domain.Grup;
import net.yosef.domain.Partit;
import net.yosef.repository.EquipRepository;
import org.joda.time.LocalDate;

import javax.inject.Inject;

/**
 * Created by user on 23/05/15.
 */
public class Calcul {
    Equip visit;
    Equip local;
    static Partit p;

    @Inject
    EquipRepository equipRepository;

//    public static void main(String [] args){
//        LocalDate d = new LocalDate();
//        Grup g = new Grup("Dilluns");
//        Equip verd = new Equip("verd", d, g);
//        verd.setId(new Long(1));
//        Equip groc = new Equip("groc", d, g);
//        groc.setId(new Long(2));
//        Calcul c = new Calcul();
//        p = new Partit(verd,groc);
//        p.setGols_l(5);
//        p.setGols_v(10);
//        c.decidirGuanyador(p);
//        c.guardarElsCanvis();
//    }

    public void decidirGuanyador(Partit p) {
        if (!p.getEquips().isEmpty()){
            visit = p.getEquips().get("visitant");
            local = p.getEquips().get("local");
            p.getGols_v();
            p.getGols_l();
            sumarPunts();
        }
    }

    public void sumarPunts() {
        if (p.getGols_l() != null && p.getGols_v() != null) {
            if (p.getGols_l() > -1 && p.getGols_v() > -1) {
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
        }
    }
    public void guardarElsCanvis(){
        //save local
        equipRepository.save(local);
        //save visitant
        equipRepository.save(visit);
    }

}
