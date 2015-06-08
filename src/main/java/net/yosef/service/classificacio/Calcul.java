package net.yosef.service.classificacio;

import net.yosef.domain.Classificacio;
import net.yosef.domain.Equip;
import net.yosef.domain.Grup;
import net.yosef.domain.Partit;
import net.yosef.repository.ClassificacioRepository;
import net.yosef.repository.EquipRepository;
import org.joda.time.LocalDate;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 23/05/15.
 */
public class Calcul {
    Equip visit;
    Equip local;
    Partit p;

    @Inject
    EquipRepository equipRepository;

    @Inject
    ClassificacioRepository classificacioRepository;

    public void decidirGuanyador(Partit p) {
        this.p = p;
        if (!p.getEquips().isEmpty()){
            visit = p.getEquips().get("visitant");
            local = p.getEquips().get("local");
//            p.getGols_v();
//            p.getGols_l();
            sumarPunts();
            guardarElsCanvis();
        }else{
            throw new NullPointerException("Els equips no poden ser nulls");
        }
    }

    private void sumarPunts() {
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
    private void guardarElsCanvis(){
        Grup g = local.getGrup();
        Classificacio c = classificacioRepository.findByGrupAndTemporada(g,g.getTemporada());
        local.setClassificacio(c);
        //save local
        equipRepository.save(local);
        visit.setClassificacio(c);
        //save visitant
        equipRepository.save(visit);

        replace(c.getEquips(),local);
        replace(c.getEquips(),visit);
        classificacioRepository.save(c);

    }

    private void replace(Set<Equip> c , Equip e){
        if (c.contains(e)){
            c.remove(e);
            c.add(e);
        }else{
            c.add(e);
        }
    }
    public void setEquipRepository(EquipRepository equipRepository) {
        this.equipRepository = equipRepository;
    }
    public void setClassificacioRepository(ClassificacioRepository classificacioRepository) {
        this.classificacioRepository = classificacioRepository;
    }
}
